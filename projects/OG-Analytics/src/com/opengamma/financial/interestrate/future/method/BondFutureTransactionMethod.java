/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate.future.method;

import com.opengamma.financial.interestrate.future.definition.BondFutureTransaction;
import com.opengamma.financial.interestrate.method.PricingMethod;

/**
 * Methods for the pricing of bond futures generic to all models.
 */
public abstract class BondFutureTransactionMethod implements PricingMethod {

  /**
   * Compute the present value of a future transaction from a quoted price.
   * @param future The future.
   * @param price The quoted price.
   * @return The present value.
   */
  public double presentValueFromPrice(final BondFutureTransaction future, final double price) {
    double pv = (price - future.getReferencePrice()) * future.getUnderlyingFuture().getNotional() * future.getQuantity();
    return pv;
  }

}
