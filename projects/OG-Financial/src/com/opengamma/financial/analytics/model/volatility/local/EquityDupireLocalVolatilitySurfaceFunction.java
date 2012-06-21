/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.volatility.local;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.analytics.ircurve.YieldCurveFunction;
import com.opengamma.financial.analytics.model.InstrumentTypeProperties;
import com.opengamma.financial.analytics.model.volatility.surface.black.BlackVolatilitySurfacePropertyNamesAndValues;
import com.opengamma.financial.analytics.model.volatility.surface.black.BlackVolatilitySurfaceUtils;
import com.opengamma.id.UniqueId;

/**
 * 
 */
public abstract class EquityDupireLocalVolatilitySurfaceFunction extends DupireLocalVolatilitySurfaceFunction {

  @Override
  protected boolean isCorrectIdType(ComputationTarget target) {
    final UniqueId uid = target.getUniqueId();
    if (uid == null) {
      return false;
    }
    final String targetScheme = uid.getScheme();
    return (targetScheme.equalsIgnoreCase(ExternalSchemes.BLOOMBERG_TICKER.getName()) ||
            targetScheme.equalsIgnoreCase(ExternalSchemes.BLOOMBERG_TICKER_WEAK.getName()));
  }

  @Override
  protected String getInstrumentType() {
    return InstrumentTypeProperties.EQUITY_OPTION;
  }
  
  /** 
   * Equity requires an additional two properties. 
   * This is to specify the Funding curve used to build the Equity Forwards.
   * @return ValueProperties specifying withAny FundingCurve's Currency and Name 
   */
  protected ValueProperties getCurrencyProperties() {
    ValueProperties equityProperties = createValueProperties()
      .withAny(ValuePropertyNames.CURVE_CURRENCY)
      .withAny(YieldCurveFunction.PROPERTY_FUNDING_CURVE)
      .get();
    return equityProperties;
  }
  
  /** 
   * Equity requires an additional two properties. 
   * This is to specify the Funding curve used to build the Equity Forwards.
   * @param desiredValue ValueRequirement containing "CurveCurrency" and "FundingCurve"
   * @return ValueProperties containing specified values
   */
  protected ValueProperties getCurrencyProperties(final ValueRequirement desiredValue) {
    final String curveCurrency = desiredValue.getConstraint(ValuePropertyNames.CURVE_CURRENCY);
    final String fundingCurve = desiredValue.getConstraint(YieldCurveFunction.PROPERTY_FUNDING_CURVE); 
    ValueProperties equityProperties = createValueProperties()
      .with(ValuePropertyNames.CURVE_CURRENCY, curveCurrency)
      .with(YieldCurveFunction.PROPERTY_FUNDING_CURVE, fundingCurve)
      .get();
    return equityProperties;
  }
  
  /** 
   * Equity requires an additional two properties. 
   * This is to specify the Funding curve used to build the Equity Forwards.
   * @param desiredValue ValueRequirement containing "CurveCurrency" and "FundingCurve"
   * @return ValueProperties containing specified values
   */
  protected ValueProperties getCurrencyPropertiesForVolatilitySurface(final ValueRequirement desiredValue) {
    final String curveCurrency = desiredValue.getConstraint(ValuePropertyNames.CURVE_CURRENCY);
    final String fundingCurve = desiredValue.getConstraint(YieldCurveFunction.PROPERTY_FUNDING_CURVE); 
    ValueProperties equityProperties = ValueProperties.builder()
      .with(ValuePropertyNames.CURVE_CURRENCY, curveCurrency)
      .with(YieldCurveFunction.PROPERTY_FUNDING_CURVE, fundingCurve)
      .get();
    return equityProperties;
  }
  
  @Override
  protected ValueProperties getResultProperties(final String parameterizationType) {
    ValueProperties equityProperties = getCurrencyProperties();
    return LocalVolatilitySurfaceUtils.addDupireLocalVolatilitySurfaceProperties(equityProperties, 
        getInstrumentType(), getBlackSmileInterpolatorName(), parameterizationType).get();
  }

  
  @Override
  protected ValueProperties getResultProperties(final ValueRequirement desiredValue, final String parameterizationType) {
    ValueProperties equityProperties = getCurrencyProperties(desiredValue);
    return LocalVolatilitySurfaceUtils.addDupireLocalVolatilitySurfaceProperties(equityProperties, 
        getInstrumentType(), getBlackSmileInterpolatorName(), parameterizationType, desiredValue).get();
  }
  
  @Override
  protected ValueRequirement getVolatilitySurfaceRequirement(final ComputationTarget target, final ValueRequirement desiredValue) {
    ValueProperties equityProperties = getCurrencyPropertiesForVolatilitySurface(desiredValue);
    final ValueProperties properties = BlackVolatilitySurfaceUtils.addAllBlackSurfaceProperties(equityProperties, getInstrumentType(), desiredValue).get();
    return new ValueRequirement(ValueRequirementNames.BLACK_VOLATILITY_SURFACE, target.toSpecification(), properties);
  }
  
  /**
   * Function producing a local volatility surface using a Black volatility surface with spline interpolation
   */
  public static class Spline extends EquityDupireLocalVolatilitySurfaceFunction {

    @Override
    protected String getBlackSmileInterpolatorName() {
      return BlackVolatilitySurfacePropertyNamesAndValues.SPLINE;
    }

  }

  /**
   * Function producing a local volatility surface using a Black volatility surface with SABR interpolation
   */
  public static class SABR extends EquityDupireLocalVolatilitySurfaceFunction {

    @Override
    protected String getBlackSmileInterpolatorName() {
      return BlackVolatilitySurfacePropertyNamesAndValues.SABR;
    }

  }

  /**
   * Function producing a local volatility surface using a Black volatility surface with mixed log-normal interpolation
   */
  public static class MixedLogNormal extends EquityDupireLocalVolatilitySurfaceFunction {

    @Override
    protected String getBlackSmileInterpolatorName() {
      return BlackVolatilitySurfacePropertyNamesAndValues.MIXED_LOG_NORMAL;
    }

  }
  
  
  
  
}
