/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.volatility.surface;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.financial.analytics.model.InstrumentTypeProperties;
import com.opengamma.id.UniqueId;
import com.opengamma.util.money.Currency;

/**
 * 
 */
public class RawSwaptionATMVolatilitySurfaceDataFunction extends RawVolatilitySurfaceDataFunction {

  public RawSwaptionATMVolatilitySurfaceDataFunction() {
    super(InstrumentTypeProperties.SWAPTION_ATM);
  }

  @Override
  public boolean isCorrectIdType(final ComputationTarget target) {
    final UniqueId uid = target.getUniqueId();
    return (uid != null) && Currency.OBJECT_SCHEME.equals(uid.getScheme());
  }
}
