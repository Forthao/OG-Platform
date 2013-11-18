/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.option;

import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.equity.EqyOptBjerksundStenslandPresentValueCalculator;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.equity.option.EquityIndexFutureOption;
import com.opengamma.analytics.financial.equity.option.EquityIndexOption;
import com.opengamma.analytics.financial.equity.option.EquityOption;
import com.opengamma.analytics.financial.equity.variance.pricing.AffineDividends;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.model.interestrate.curve.ForwardCurveAffineDividends;
import com.opengamma.analytics.financial.model.option.pricing.analytic.BjerksundStenslandModel;
import com.opengamma.analytics.financial.model.volatility.BlackFormulaRepository;
import com.opengamma.analytics.financial.model.volatility.BlackImpliedVolatilityFormula;
import com.opengamma.analytics.math.MathException;
import com.opengamma.analytics.math.rootfinding.BracketRoot;
import com.opengamma.core.value.MarketDataRequirementNames;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.target.ComputationTargetReference;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.security.FinancialSecurity;
/**
 * Calculates the implied volatility of an equity index or equity option using the {@link BjerksundStenslandModel} */
public class EquityOptionBjerksundStenslandImpliedVolFunction extends EquityOptionBjerksundStenslandFunction {

  /** The BjerksundStensland present value calculator */
  private static final EqyOptBjerksundStenslandPresentValueCalculator s_pvCalculator = EqyOptBjerksundStenslandPresentValueCalculator.getInstance();

  /** Default constructor */
  public EquityOptionBjerksundStenslandImpliedVolFunction() {
    super(ValueRequirementNames.IMPLIED_VOLATILITY);
  }


  @Override
  protected Set<ComputedValue> computeValues(final InstrumentDerivative derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs, final Set<ValueRequirement> desiredValues,
      final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {

    // Get market price
    Double marketPrice = null;
    final ComputedValue mktPriceObj = inputs.getComputedValue(MarketDataRequirementNames.MARKET_VALUE);
    if (mktPriceObj == null) {
      s_logger.info(MarketDataRequirementNames.MARKET_VALUE + " not available," + targetSpec);
    } else {
      marketPrice = (Double) mktPriceObj.getValue();
    }
    // Get details of option for impliedVol Call
    // If market price is not available. compute from model instead
    final double optionPrice;
    final double strike;
    final double timeToExpiry;
    final boolean isCall;
    if (derivative instanceof EquityOption) {
      final EquityOption option = (EquityOption) derivative;
      strike = option.getStrike();
      timeToExpiry = option.getTimeToExpiry();
      isCall = option.isCall();
      if (marketPrice == null) {
        optionPrice = derivative.accept(s_pvCalculator, market) / option.getUnitAmount();
      } else {
        optionPrice = marketPrice;
      }
    } else if (derivative instanceof EquityIndexOption) {
      final EquityIndexOption option = (EquityIndexOption) derivative;
      strike = option.getStrike();
      timeToExpiry = option.getTimeToExpiry();
      isCall = option.isCall();
      if (marketPrice == null) {
        optionPrice = derivative.accept(s_pvCalculator, market) / option.getUnitAmount();
      } else {
        optionPrice = marketPrice;
      }
    } else if (derivative instanceof EquityIndexFutureOption) {
      final EquityIndexFutureOption option = (EquityIndexFutureOption) derivative;
      strike = option.getStrike();
      timeToExpiry = option.getExpiry();
      isCall = option.isCall();
      if (marketPrice == null) {
        optionPrice = derivative.accept(s_pvCalculator, market) / option.getPointValue();
      } else {
        optionPrice = marketPrice;
      }

    } else {
      throw new OpenGammaRuntimeException("Unexpected InstrumentDerivative type");
    }

    final double volatility = market.getVolatilitySurface().getVolatility(timeToExpiry, strike);
    Double impliedVol = null;
    
    if (derivative instanceof EquityOption) {
      final double spot = market.getForwardCurve().getSpot();
      final double discountRate = market.getDiscountCurve().getInterestRate(timeToExpiry);
      final double costOfCarry =  discountRate;
      final BjerksundStenslandModel model = new BjerksundStenslandModel();
    
      final AffineDividends div = ((ForwardCurveAffineDividends) market.getForwardCurve()).getDividends();
      final int number = div.getNumberOfDividends();
      double modSpot = spot;
      int i = 0;
      while (i < number && div.getTau(i) < timeToExpiry) {
        modSpot = modSpot * (1. - div.getBeta(i)) - div.getAlpha(i) * market.getDiscountCurve().getDiscountFactor(div.getTau(i));
        ++i;
      }
    
      if (timeToExpiry < 7. / 365.) {
        impliedVol = volatility;
      } else {
        try {
          impliedVol = model.impliedVolatility(optionPrice, modSpot, strike, discountRate, costOfCarry, timeToExpiry, isCall, Math.min(volatility * 1.5, 0.2));
        } catch (final IllegalArgumentException e) {
          if (inputs.getComputedValue(MarketDataRequirementNames.MARKET_VALUE) == null) {
            impliedVol =  null;
          } else {
            s_logger.warn(MarketDataRequirementNames.IMPLIED_VOLATILITY + " not defined, reference value returned" + targetSpec);
            impliedVol = impliedVolatilityFailed(optionPrice, modSpot, strike, discountRate, costOfCarry, timeToExpiry, isCall);
          }
        }
      }
    } else {
      impliedVol = volatility;      
    }
    final ValueSpecification resultSpec = new ValueSpecification(getValueRequirementNames()[0], targetSpec, resultProperties);
    return Collections.singleton(new ComputedValue(resultSpec, impliedVol));
  }
  
  /*
   * ref value is returned if market price is not consistent, e.g., (market price) < S - K for call. 
   */
  private double impliedVolatilityFailed(final double price, final double s0, final double k, final double r, final double b, final double t, final boolean isCall) {
    final BjerksundStenslandModel model = new BjerksundStenslandModel();
    double sigma1 = 0.;
    double vega = model.getPriceAndVega(s0, k, r, b, t, sigma1, isCall)[0];
    if (Math.abs(vega) > 1.e-9) {
      return 0.01;
    }
    double sigma2 = 1.5;
    while (Math.abs(sigma1 - sigma2) > 1.e-7) {
      vega = model.getPriceAndVega(s0, k, r, b, t, 0.5 * (sigma1 + sigma2), isCall)[0];
      if (Math.abs(vega) > 1.e-9) {
        sigma2 = 0.5 * (sigma1 + sigma2);
      } else {
        sigma1 = 0.5 * (sigma1 + sigma2);
      }
    }
    return 0.5 * (sigma1 + sigma2);
  }
  

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> requirements = super.getRequirements(context, target, desiredValue);
    if (requirements == null) {
      return null;
    }
    // Add live market_value of the option
    final FinancialSecurity security = (FinancialSecurity) target.getSecurity();
    final ComputationTargetReference securityTarget = new ComputationTargetSpecification(ComputationTargetType.SECURITY, security.getUniqueId());
    final ValueRequirement securityValueReq = new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, securityTarget);
    requirements.add(securityValueReq);

    return requirements;
  }

  /** The logger */
  private static final Logger s_logger = LoggerFactory.getLogger(EquityOptionBjerksundStenslandImpliedVolFunction.class);
}
