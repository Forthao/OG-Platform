/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.instrument.InstrumentDefinitionWithData;
import com.opengamma.analytics.financial.instrument.future.FederalFundsFutureTransactionDefinition;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeries;
import com.opengamma.financial.analytics.ircurve.strips.CurveNode;
import com.opengamma.financial.analytics.ircurve.strips.CurveNodeWithIdentifier;
import com.opengamma.financial.analytics.ircurve.strips.DeliverableSwapFutureNode;
import com.opengamma.financial.analytics.ircurve.strips.RateFutureNode;
import com.opengamma.financial.analytics.ircurve.strips.ZeroCouponInflationNode;
import com.opengamma.financial.analytics.timeseries.HistoricalTimeSeriesBundle;
import com.opengamma.id.ExternalId;
import com.opengamma.timeseries.DoubleTimeSeries;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleEntryIterator;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ImmutableZonedDateTimeDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ZonedDateTimeDoubleTimeSeries;
import com.opengamma.timeseries.precise.zdt.ZonedDateTimeDoubleTimeSeriesBuilder;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public class CurveNodeConverter {

  /**
   * Given an {@link InstrumentDefinition} (the time-independent form used in the analytics library) and a valuation time, converts to the
   * time-dependent {@link InstrumentDerivative} form.
   * @param node The curve node, not null
   * @param definition The definition, not null
   * @param now The valuation time, not null
   * @param timeSeries A fixing time series, not null if required
   * @return A derivative instrument
   */
  @SuppressWarnings("unchecked")
  public InstrumentDerivative getDerivative(final CurveNodeWithIdentifier node, final InstrumentDefinition<?> definition, final ZonedDateTime now,
      final HistoricalTimeSeriesBundle timeSeries) {
    ArgumentChecker.notNull(node, "node");
    ArgumentChecker.notNull(definition, "definition");
    ArgumentChecker.notNull(now, "now");
    if (definition instanceof InstrumentDefinitionWithData<?, ?> && requiresFixingSeries(node.getCurveNode())) {
      if (node.getCurveNode() instanceof ZeroCouponInflationNode) {
        ArgumentChecker.notNull(timeSeries, "time series");
        final ExternalId id = node.getIdentifier();
        final HistoricalTimeSeries historicalTimeSeries = timeSeries.get(node.getDataField(), id);
        if (historicalTimeSeries == null) {
          throw new OpenGammaRuntimeException("Could not get price time series for " + id);
        }
        final DoubleTimeSeries<?> ts = historicalTimeSeries.getTimeSeries();
        if (ts == null) {
          throw new OpenGammaRuntimeException("Could not get price time series for " + id);
        }
        final int length = ts.size();
        if (length == 0) {
          throw new OpenGammaRuntimeException("Price time series for " + id + " was empty");
        }
        return ((InstrumentDefinitionWithData<?, DoubleTimeSeries<ZonedDateTime>>) definition).toDerivative(now, (DoubleTimeSeries<ZonedDateTime>) ts.multiply(100));
      }
      if (node.getCurveNode() instanceof RateFutureNode || node.getCurveNode() instanceof DeliverableSwapFutureNode) {
        ArgumentChecker.notNull(timeSeries, "time series");
        final ExternalId id = node.getIdentifier();
        final HistoricalTimeSeries historicalTimeSeries = timeSeries.get(node.getDataField(), id);
        if (historicalTimeSeries == null) {
          if (node.getCurveNode() instanceof DeliverableSwapFutureNode) {
            final double lastMarginPrice = 0.99;
            return ((InstrumentDefinitionWithData<?, Double>) definition).toDerivative(now, lastMarginPrice);
          }
          throw new OpenGammaRuntimeException("Could not get price time series for " + id);
        }
        final DoubleTimeSeries<?> ts = historicalTimeSeries.getTimeSeries();
        if (ts == null) {
          throw new OpenGammaRuntimeException("Could not get price time series for " + id);
        }
        final int length = ts.size();
        if (length == 0) {
          throw new OpenGammaRuntimeException("Price time series for " + id + " was empty");
        }
        if (definition instanceof FederalFundsFutureTransactionDefinition) {
          final DoubleTimeSeries<ZonedDateTime>[] tsArray = new DoubleTimeSeries[2];
          tsArray[0] = convertTimeSeries(now.getZone(), historicalTimeSeries.getTimeSeries());
          return ((InstrumentDefinitionWithData<?, DoubleTimeSeries<ZonedDateTime>[]>) definition).toDerivative(now, tsArray); //CSIGNORE
        }
        final double lastMarginPrice = ts.getLatestValue();
        return ((InstrumentDefinitionWithData<?, Double>) definition).toDerivative(now, lastMarginPrice);
      }
      throw new OpenGammaRuntimeException("Cannot handle swaps with fixings");
    }
    return definition.toDerivative(now);
  }

  private static boolean requiresFixingSeries(final CurveNode node) {
    return node instanceof RateFutureNode || node instanceof DeliverableSwapFutureNode; // || (node instanceof SwapNode && ((SwapNode) node).isUseFixings());
  }

  private static ZonedDateTimeDoubleTimeSeries convertTimeSeries(final ZoneId timeZone, final LocalDateDoubleTimeSeries localDateTS) {
    // FIXME CASE Converting a daily historical time series to an arbitrary time. Bad idea
    final ZonedDateTimeDoubleTimeSeriesBuilder bld = ImmutableZonedDateTimeDoubleTimeSeries.builder(timeZone);
    for (final LocalDateDoubleEntryIterator it = localDateTS.iterator(); it.hasNext();) {
      final LocalDate date = it.nextTime();
      final ZonedDateTime zdt = date.atStartOfDay(timeZone);
      bld.put(zdt, it.currentValueFast());
    }
    return bld.build();
  }
}
