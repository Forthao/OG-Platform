/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.marketdata.snapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.threeten.bp.Instant;

import com.opengamma.core.marketdatasnapshot.MarketDataSnapshotSource;
import com.opengamma.core.marketdatasnapshot.MarketDataValueSpecification;
import com.opengamma.core.marketdatasnapshot.MarketDataValueType;
import com.opengamma.core.marketdatasnapshot.SnapshotDataBundle;
import com.opengamma.core.marketdatasnapshot.StructuredMarketDataKey;
import com.opengamma.core.marketdatasnapshot.StructuredMarketDataSnapshot;
import com.opengamma.core.marketdatasnapshot.UnstructuredMarketDataSnapshot;
import com.opengamma.core.marketdatasnapshot.ValueSnapshot;
import com.opengamma.core.marketdatasnapshot.VolatilityCubeData;
import com.opengamma.core.marketdatasnapshot.VolatilityCubeKey;
import com.opengamma.core.marketdatasnapshot.VolatilityCubeSnapshot;
import com.opengamma.core.marketdatasnapshot.VolatilityPoint;
import com.opengamma.core.marketdatasnapshot.VolatilitySurfaceData;
import com.opengamma.core.marketdatasnapshot.VolatilitySurfaceKey;
import com.opengamma.core.marketdatasnapshot.VolatilitySurfaceSnapshot;
import com.opengamma.core.marketdatasnapshot.YieldCurveKey;
import com.opengamma.core.marketdatasnapshot.YieldCurveSnapshot;
import com.opengamma.core.value.MarketDataRequirementNames;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.marketdata.AbstractMarketDataSnapshot;
import com.opengamma.engine.marketdata.ExternalIdBundleLookup;
import com.opengamma.engine.marketdata.MarketDataUtils;
import com.opengamma.engine.target.ComputationTargetReference;
import com.opengamma.engine.target.ComputationTargetSpecificationResolver;
import com.opengamma.engine.target.DefaultComputationTargetSpecificationResolver;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Tenor;
import com.opengamma.util.tuple.Pair;

// REVIEW jonathan 2011-06-29 -- The user market data provider classes, including this, no longer need to be in the
// engine and they simply introduce dependencies on the MarketDataSnapshotSource and specific StructuredMarketDataKeys.
// They are a perfect example of adding a custom market data source and should be moved elsewhere.
/**
 * Represents a market data snapshot from a {@link MarketDataSnapshotSource}.
 */
public class UserMarketDataSnapshot extends AbstractMarketDataSnapshot implements StructuredMarketDataKey.Visitor<Object> {

  private static final String INSTRUMENT_TYPE_PROPERTY = "InstrumentType";
  private static final String SURFACE_QUOTE_TYPE_PROPERTY = "SurfaceQuoteType";
  private static final String SURFACE_QUOTE_UNITS_PROPERTY = "SurfaceUnits";
  private static final Map<String, StructuredMarketDataKeyFactory> s_structuredKeyFactories = new HashMap<String, StructuredMarketDataKeyFactory>();

  // TODO: We should probably be storing value specifications in the snapshot. The logic for resolving identifiers can then be shifted to a more
  // central location - e.g. graph construction when all forms of the identifier are known.
  private static final ComputationTargetSpecificationResolver.AtVersionCorrection s_targetSpecificationResolver = new DefaultComputationTargetSpecificationResolver()
      .atVersionCorrection(VersionCorrection.LATEST);

  private final ExternalIdBundleLookup _identifierLookup;
  private final StructuredMarketDataSnapshot _snapshot;

  /**
   * Factory for {@link StructuredMarketDataKey} instances.
   */
  private abstract static class StructuredMarketDataKeyFactory {

    /**
     * Gets the {@link StructuredMarketDataKey} and {@link ValueSpecification} corresponding to a value requirement.
     * 
     * @param valueRequirement the value requirement, not null
     * @return the structured market data key, null if the value requirement does not correspond to a key
     */
    public abstract Pair<? extends StructuredMarketDataKey, ValueSpecification> fromRequirement(ValueRequirement valueRequirement, UserMarketDataSnapshot snapshot);

    protected ComputationTargetSpecification resolve(final ComputationTargetReference reference) {
      return s_targetSpecificationResolver.getTargetSpecification(reference);
    }

    protected Currency getCurrencyTarget(final ComputationTargetSpecification target) {
      final UniqueId targetId = target.getUniqueId();
      if (targetId == null) {
        return null;
      }
      if (!Currency.OBJECT_SCHEME.equals(targetId.getScheme())) {
        return null;
      }
      final Currency currency = Currency.of(targetId.getValue());
      return currency;
    }

  }

  static {
    registerStructuredKeyFactory(ValueRequirementNames.YIELD_CURVE_MARKET_DATA, new StructuredMarketDataKeyFactory() {

      @Override
      public Pair<? extends StructuredMarketDataKey, ValueSpecification> fromRequirement(final ValueRequirement valueRequirement, final UserMarketDataSnapshot snapshot) {
        final ComputationTargetSpecification targetSpec = resolve(valueRequirement.getTargetReference());
        if (targetSpec == null) {
          return null;
        }
        final Currency target = getCurrencyTarget(targetSpec);
        if (target == null) {
          return null;
        }
        final ValueProperties constraints = valueRequirement.getConstraints();
        final Set<String> names = constraints.getValues(ValuePropertyNames.CURVE);
        YieldCurveKey key = null;
        if ((names != null) && (names.size() == 1)) {
          key = new YieldCurveKey(target, names.iterator().next());
        } else {
          if (snapshot.getSnapshot().getYieldCurves() != null) {
            for (final YieldCurveKey curve : snapshot.getSnapshot().getYieldCurves().keySet()) {
              if (!target.equals(curve.getCurrency())) {
                continue;
              }
              if ((names != null) && !names.isEmpty() && !names.contains(curve.getName())) {
                continue;
              }
              key = curve;
              break;
            }
          }
          if (key == null) {
            return null;
          }
        }
        final ValueProperties properties = ValueProperties.with(ValuePropertyNames.CURVE, key.getName()).get();
        if (!constraints.isSatisfiedBy(properties)) {
          return null;
        }
        return Pair.of(key, MarketDataUtils.createMarketDataValue(ValueRequirementNames.YIELD_CURVE_MARKET_DATA, targetSpec, properties));
      }

    });
    registerStructuredKeyFactory(ValueRequirementNames.VOLATILITY_SURFACE_DATA, new StructuredMarketDataKeyFactory() {

      @Override
      public Pair<? extends StructuredMarketDataKey, ValueSpecification> fromRequirement(final ValueRequirement valueRequirement, final UserMarketDataSnapshot snapshot) {
        final ComputationTargetSpecification targetSpec = resolve(valueRequirement.getTargetReference());
        if (targetSpec == null) {
          return null;
        }
        final UniqueId target = targetSpec.getUniqueId();
        if (target == null) {
          return null;
        }
        final ValueProperties constraints = valueRequirement.getConstraints();
        final Set<String> names = constraints.getValues(ValuePropertyNames.SURFACE);
        final Set<String> instrumentTypes = constraints.getValues(INSTRUMENT_TYPE_PROPERTY);
        final Set<String> quoteTypes = constraints.getValues(SURFACE_QUOTE_TYPE_PROPERTY);
        final Set<String> quoteUnits = constraints.getValues(SURFACE_QUOTE_UNITS_PROPERTY);
        VolatilitySurfaceKey key = null;
        if ((names != null) && (instrumentTypes != null) && (quoteTypes != null) && (quoteUnits != null) && (names.size() == 1) && (instrumentTypes.size() == 1) && (quoteTypes.size() == 1) &&
            (quoteUnits.size() == 1)) {
          key = new VolatilitySurfaceKey(target, names.iterator().next(), instrumentTypes.iterator().next(), quoteTypes.iterator().next(), quoteUnits.iterator().next());
        } else {
          if (snapshot.getSnapshot().getVolatilitySurfaces() != null) {
            for (final VolatilitySurfaceKey surface : snapshot.getSnapshot().getVolatilitySurfaces().keySet()) {
              if (!target.equals(surface.getTarget())) {
                continue;
              }
              if ((names != null) && !names.isEmpty() && !names.contains(surface.getName())) {
                continue;
              }
              if ((instrumentTypes != null) && !instrumentTypes.isEmpty() && !instrumentTypes.contains(surface.getInstrumentType())) {
                continue;
              }
              if ((quoteTypes != null) && !quoteTypes.isEmpty() && !quoteTypes.contains(surface.getQuoteType())) {
                continue;
              }
              if ((quoteUnits != null) && !quoteUnits.isEmpty() && !quoteUnits.contains(surface.getQuoteUnits())) {
                continue;
              }
              key = surface;
              break;
            }
          }
          if (key == null) {
            return null;
          }
        }
        final ValueProperties properties = ValueProperties.with(ValuePropertyNames.SURFACE,
            key.getName()).with(INSTRUMENT_TYPE_PROPERTY, key.getInstrumentType()).with(SURFACE_QUOTE_TYPE_PROPERTY, key.getQuoteType())
            .with(SURFACE_QUOTE_UNITS_PROPERTY, key.getQuoteUnits()).get();
        if (!constraints.isSatisfiedBy(properties)) {
          return null;
        }
        return Pair.of(key, MarketDataUtils.createMarketDataValue(ValueRequirementNames.VOLATILITY_SURFACE_DATA, targetSpec, properties));
      }

    });
    registerStructuredKeyFactory(ValueRequirementNames.VOLATILITY_CUBE_MARKET_DATA, new StructuredMarketDataKeyFactory() {

      @Override
      public Pair<? extends StructuredMarketDataKey, ValueSpecification> fromRequirement(final ValueRequirement valueRequirement, final UserMarketDataSnapshot snapshot) {
        final ComputationTargetSpecification targetSpec = resolve(valueRequirement.getTargetReference());
        if (targetSpec == null) {
          return null;
        }
        final Currency target = getCurrencyTarget(targetSpec);
        if (target == null) {
          return null;
        }
        final ValueProperties constraints = valueRequirement.getConstraints();
        final Set<String> names = constraints.getValues(ValuePropertyNames.CUBE);
        VolatilityCubeKey key = null;
        if ((names != null) && (names.size() == 1)) {
          key = new VolatilityCubeKey(target, names.iterator().next());
        } else {
          if (snapshot.getSnapshot().getVolatilityCubes() != null) {
            for (final VolatilityCubeKey cube : snapshot.getSnapshot().getVolatilityCubes().keySet()) {
              if (!target.equals(cube.getCurrency())) {
                continue;
              }
              if ((names != null) && !names.isEmpty() && !names.contains(cube.getName())) {
                continue;
              }
              key = cube;
              break;
            }
          }
          if (key == null) {
            return null;
          }
        }
        final ValueProperties properties = ValueProperties.with(ValuePropertyNames.CUBE, key.getName()).get();
        if (!constraints.isSatisfiedBy(properties)) {
          return null;
        }
        return Pair.of(key, MarketDataUtils.createMarketDataValue(ValueRequirementNames.VOLATILITY_CUBE_MARKET_DATA, targetSpec, properties));
      }

    });
  }

  public UserMarketDataSnapshot(StructuredMarketDataSnapshot snapshot, final ExternalIdBundleLookup identifierLookup) {
    _snapshot = snapshot;
    _identifierLookup = identifierLookup;
  }

  //-------------------------------------------------------------------------

  @Override
  public UniqueId getUniqueId() {
    return _snapshot.getUniqueId();
  }

  @Override
  public Instant getSnapshotTimeIndication() {
    return getSnapshotTime();
  }

  @Override
  public void init() {
    // No-op
  }

  @Override
  public void init(final Set<ValueRequirement> valuesRequired, final long timeout, final TimeUnit unit) {
    // No-op
  }
  
  @Override
  public boolean isInitialized() {
    return true;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public Instant getSnapshotTime() {
    // TODO [PLAT-1393] should explicitly store a snapshot time, which the user might choose to customise
    Instant latestTimestamp = null;
    final Map<YieldCurveKey, YieldCurveSnapshot> yieldCurves = getSnapshot().getYieldCurves();
    if (yieldCurves != null) {
      for (final YieldCurveSnapshot yieldCurveSnapshot : yieldCurves.values()) {
        if (latestTimestamp == null || latestTimestamp.isBefore(yieldCurveSnapshot.getValuationTime())) {
          latestTimestamp = yieldCurveSnapshot.getValuationTime();
        }
      }
    }
    if (latestTimestamp == null) {
      // What else can we do until one is guaranteed to be stored with the snapshot?
      latestTimestamp = Instant.now();
    }
    return latestTimestamp;
  }

  @Override
  public ComputedValue query(final ValueRequirement requirement) {
    final StructuredMarketDataKeyFactory factory = s_structuredKeyFactories.get(requirement.getValueName());
    if (factory != null) {
      final Pair<? extends StructuredMarketDataKey, ValueSpecification> key = factory.fromRequirement(requirement, this);
      if (key != null) {
        final Object value = key.getFirst().accept(this);
        if (value == null) {
          return null;
        } else {
          return new ComputedValue(key.getSecond(), value);
        }
      } else {
        // TODO: Or call queryUnstructured?
        return null;
      }
    }
    return queryUnstructured(requirement);
  }

  //-------------------------------------------------------------------------
  private static void registerStructuredKeyFactory(final String valueRequirementName, final StructuredMarketDataKeyFactory factory) {
    s_structuredKeyFactories.put(valueRequirementName, factory);
  }

  private ComputedValue queryUnstructured(final ValueRequirement requirement) {
    final UnstructuredMarketDataSnapshot globalValues = getSnapshot().getGlobalValues();
    if (globalValues == null) {
      return null;
    }
    final MarketDataValueType type = MarketDataUtils.getMarketDataValueType(requirement.getTargetReference().getType());
    if (type == null) {
      return null;
    }
    final ExternalIdBundle identifiers = _identifierLookup.getExternalIds(requirement.getTargetReference());
    for (final ExternalId identifier : identifiers) {
      final MarketDataValueSpecification marketDataValueSpecification = new MarketDataValueSpecification(type, identifier);
      final Map<String, ValueSnapshot> map = globalValues.getValues().get(marketDataValueSpecification);
      if (map != null) {
        final ValueSnapshot valueSnapshot = map.get(requirement.getValueName());
        final Object value = query(valueSnapshot);
        if (value != null) {
          return new ComputedValue(MarketDataUtils.createMarketDataValue(requirement, identifier), value);
        }
      }
    }
    return null;
  }

  @Override
  public Object visitYieldCurveKey(final YieldCurveKey marketDataKey) {
    final YieldCurveSnapshot yieldCurveSnapshot = getYieldCurveSnapshot(marketDataKey);
    if (yieldCurveSnapshot == null) {
      return null;
    }
    return buildSnapshot(yieldCurveSnapshot);
  }

  @Override
  public Object visitVolatilitySurfaceKey(final VolatilitySurfaceKey marketDataKey) {
    final VolatilitySurfaceSnapshot volSurfaceSnapshot = getVolSurfaceSnapshot(marketDataKey);
    if (volSurfaceSnapshot == null) {
      return null;
    }
    return buildVolatilitySurfaceData(volSurfaceSnapshot, marketDataKey);
  }

  @Override
  public Object visitVolatilityCubeKey(final VolatilityCubeKey marketDataKey) {
    final VolatilityCubeSnapshot volCubeSnapshot = getVolCubeSnapshot(marketDataKey);
    if (volCubeSnapshot == null) {
      return null;
    }
    return buildVolatilityCubeData(volCubeSnapshot);
  }

  private YieldCurveSnapshot getYieldCurveSnapshot(final YieldCurveKey yieldcurveKey) {
    if (getSnapshot().getYieldCurves() == null) {
      return null;
    }
    return getSnapshot().getYieldCurves().get(yieldcurveKey);
  }

  private VolatilityCubeSnapshot getVolCubeSnapshot(final VolatilityCubeKey volCubeKey) {
    if (getSnapshot().getVolatilityCubes() == null) {
      return null;
    }
    return getSnapshot().getVolatilityCubes().get(volCubeKey);
  }

  private VolatilitySurfaceSnapshot getVolSurfaceSnapshot(final VolatilitySurfaceKey volSurfaceKey) {
    if (getSnapshot().getVolatilitySurfaces() == null) {
      return null;
    }
    return getSnapshot().getVolatilitySurfaces().get(volSurfaceKey);
  }

  private StructuredMarketDataSnapshot getSnapshot() {
    return _snapshot;
  }
  
  private Double query(final ValueSnapshot valueSnapshot) {
    if (valueSnapshot == null) {
      return null;
    }
    //TODO configure which value to use
    if (valueSnapshot.getOverrideValue() != null) {
      return valueSnapshot.getOverrideValue();
    }
    return valueSnapshot.getMarketValue();
  }

  private SnapshotDataBundle buildSnapshot(final YieldCurveSnapshot yieldCurveSnapshot) {
    final UnstructuredMarketDataSnapshot values = yieldCurveSnapshot.getValues();
    return buildBundle(values);
  }

  private SnapshotDataBundle buildBundle(final UnstructuredMarketDataSnapshot values) {
    final SnapshotDataBundle ret = new SnapshotDataBundle();
    final HashMap<ExternalId, Double> points = new HashMap<ExternalId, Double>();
    for (final Entry<MarketDataValueSpecification, Map<String, ValueSnapshot>> entry : values.getValues().entrySet()) {
      final Double value = query(entry.getValue().get(MarketDataRequirementNames.MARKET_VALUE));
      points.put(entry.getKey().getIdentifier(), value);
    }
    ret.setDataPoints(points);
    return ret;
  }

  private VolatilityCubeData buildVolatilityCubeData(final VolatilityCubeSnapshot volCubeSnapshot) {
    final Map<VolatilityPoint, ValueSnapshot> values = volCubeSnapshot.getValues();
    final HashMap<VolatilityPoint, Double> dataPoints = buildVolValues(values);
    final HashMap<Pair<Tenor, Tenor>, Double> strikes = buildVolStrikes(volCubeSnapshot.getStrikes());
    final SnapshotDataBundle otherData = buildBundle(volCubeSnapshot.getOtherValues());

    final VolatilityCubeData ret = new VolatilityCubeData();
    ret.setDataPoints(dataPoints);
    ret.setOtherData(otherData);
    ret.setATMStrikes(strikes);

    return ret;
  }

  private HashMap<Pair<Tenor, Tenor>, Double> buildVolStrikes(final Map<Pair<Tenor, Tenor>, ValueSnapshot> strikes) {
    final HashMap<Pair<Tenor, Tenor>, Double> dataPoints = new HashMap<Pair<Tenor, Tenor>, Double>();
    for (final Entry<Pair<Tenor, Tenor>, ValueSnapshot> entry : strikes.entrySet()) {
      final ValueSnapshot value = entry.getValue();
      final Double query = query(value);
      if (query != null) {
        dataPoints.put(entry.getKey(), query);
      }
    }
    return dataPoints;
  }

  private HashMap<VolatilityPoint, Double> buildVolValues(final Map<VolatilityPoint, ValueSnapshot> values) {
    final HashMap<VolatilityPoint, Double> dataPoints = new HashMap<VolatilityPoint, Double>();
    for (final Entry<VolatilityPoint, ValueSnapshot> entry : values.entrySet()) {
      final ValueSnapshot value = entry.getValue();
      final Double query = query(value);
      if (query != null) {
        dataPoints.put(entry.getKey(), query);
      }
    }
    return dataPoints;
  }

  private VolatilitySurfaceData<Object, Object> buildVolatilitySurfaceData(final VolatilitySurfaceSnapshot volCubeSnapshot, final VolatilitySurfaceKey marketDataKey) {
    final Set<Object> xs = new HashSet<Object>();
    final Set<Object> ys = new HashSet<Object>();
    final Map<Pair<Object, Object>, Double> values = new HashMap<Pair<Object, Object>, Double>();
    final Map<Pair<Object, Object>, ValueSnapshot> snapValues = volCubeSnapshot.getValues();
    for (final Entry<Pair<Object, Object>, ValueSnapshot> entry : snapValues.entrySet()) {
      values.put(entry.getKey(), query(entry.getValue()));
      xs.add(entry.getKey().getFirst());
      ys.add(entry.getKey().getSecond());
    }
    return new VolatilitySurfaceData<Object, Object>(marketDataKey.getName(), "UNKNOWN", marketDataKey.getTarget(),
        xs.toArray(), ys.toArray(), values);
  }

}
