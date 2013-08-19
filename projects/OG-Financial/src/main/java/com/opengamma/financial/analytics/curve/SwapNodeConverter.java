/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.curve;

import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponFixedDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponIborDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponIborSpreadDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponONSimplifiedDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityDefinition;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexON;
import com.opengamma.analytics.financial.instrument.payment.PaymentDefinition;
import com.opengamma.analytics.financial.instrument.swap.SwapDefinition;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.marketdatasnapshot.SnapshotDataBundle;
import com.opengamma.core.region.RegionSource;
import com.opengamma.financial.analytics.conversion.CalendarUtils;
import com.opengamma.financial.analytics.ircurve.strips.SwapNode;
import com.opengamma.financial.convention.Convention;
import com.opengamma.financial.convention.ConventionSource;
import com.opengamma.financial.convention.IborIndexConvention;
import com.opengamma.financial.convention.OISLegConvention;
import com.opengamma.financial.convention.OvernightIndexConvention;
import com.opengamma.financial.convention.SwapFixedLegConvention;
import com.opengamma.financial.convention.VanillaIborLegConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 * Convert a swap node into an Instrument definition.
 * The dates of the swap are computed in the following way:
 * - The spot date is computed from the valuation date adding the "Settlement Days" (i.e. the number of business days) of the convention.
 * - The start date is computed from the spot date adding the "StartTenor" of the node and using the business-day-convention, calendar and EOM of the convention.
 * - The end date is computed from the start date adding the "MaturityTenor" of the node and using Annuity constructor.
 * The swap notional for each leg is 1.
 */
public class SwapNodeConverter extends CurveNodeVisitorAdapter<InstrumentDefinition<?>> {
  /** The convention source */
  private final ConventionSource _conventionSource;
  /** The holiday source */
  private final HolidaySource _holidaySource;
  /** The region source */
  private final RegionSource _regionSource;
  /** The market data */
  private final SnapshotDataBundle _marketData;
  /** The market data id */
  private final ExternalId _dataId;
  /** The valuation time */
  private final ZonedDateTime _valuationTime;

  /**
   * @param conventionSource The convention source, not null
   * @param holidaySource The holiday source, not null
   * @param regionSource The region source, not null
   * @param marketData The market data, not null
   * @param dataId The id of the market data, not null
   * @param valuationTime The valuation time, not null
   */
  public SwapNodeConverter(final ConventionSource conventionSource, final HolidaySource holidaySource, final RegionSource regionSource,
      final SnapshotDataBundle marketData, final ExternalId dataId, final ZonedDateTime valuationTime) {
    ArgumentChecker.notNull(conventionSource, "convention source");
    ArgumentChecker.notNull(holidaySource, "holiday source");
    ArgumentChecker.notNull(regionSource, "region source");
    ArgumentChecker.notNull(marketData, "market data");
    ArgumentChecker.notNull(dataId, "data id");
    ArgumentChecker.notNull(valuationTime, "valuation time");
    _conventionSource = conventionSource;
    _holidaySource = holidaySource;
    _regionSource = regionSource;
    _marketData = marketData;
    _dataId = dataId;
    _valuationTime = valuationTime;
  }

  @Override
  public InstrumentDefinition<?> visitSwapNode(final SwapNode swapNode) {
    final Convention payLegConvention = _conventionSource.getConvention(swapNode.getPayLegConvention());
    if (payLegConvention == null) {
      throw new OpenGammaRuntimeException("Convention with id " + swapNode.getPayLegConvention() + " was null");
    }
    final Convention receiveLegConvention = _conventionSource.getConvention(swapNode.getReceiveLegConvention());
    if (receiveLegConvention == null) {
      throw new OpenGammaRuntimeException("Convention with id " + swapNode.getPayLegConvention() + " was null");
    }
    final AnnuityDefinition<? extends PaymentDefinition> payLeg;
    final AnnuityDefinition<? extends PaymentDefinition> receiveLeg;
    if (payLegConvention instanceof SwapFixedLegConvention) {
      payLeg = getFixedLeg((SwapFixedLegConvention) payLegConvention, swapNode, true);
    } else if (payLegConvention instanceof VanillaIborLegConvention) {
      if (receiveLegConvention instanceof SwapFixedLegConvention) {
        payLeg = getIborLeg((VanillaIborLegConvention) payLegConvention, swapNode, true, false);
      } else {
        payLeg = getIborLeg((VanillaIborLegConvention) payLegConvention, swapNode, true, true);
      }
    } else if (payLegConvention instanceof OISLegConvention) {
      payLeg = getOISLeg((OISLegConvention) payLegConvention, swapNode, true);
    } else {
      throw new OpenGammaRuntimeException("Cannot handle convention type " + payLegConvention.getClass());
    }
    if (receiveLegConvention instanceof SwapFixedLegConvention) {
      receiveLeg = getFixedLeg((SwapFixedLegConvention) receiveLegConvention, swapNode, false);
    } else if (receiveLegConvention instanceof VanillaIborLegConvention) {
      if (payLeg instanceof AnnuityCouponIborDefinition) {
        receiveLeg = getIborLeg((VanillaIborLegConvention) receiveLegConvention, swapNode, false, true);
      } else {
        receiveLeg = getIborLeg((VanillaIborLegConvention) receiveLegConvention, swapNode, false, false);
      }
    } else if (receiveLegConvention instanceof OISLegConvention) {
      receiveLeg = getOISLeg((OISLegConvention) receiveLegConvention, swapNode, false);
    } else {
      throw new OpenGammaRuntimeException("Cannot handle convention type " + receiveLegConvention.getClass());
    }
    return new SwapDefinition(payLeg, receiveLeg);
  }


  private AnnuityCouponFixedDefinition getFixedLeg(final SwapFixedLegConvention convention, final SwapNode swapNode, final boolean isPayer) {
    final Calendar calendar = CalendarUtils.getCalendar(_regionSource, _holidaySource, convention.getRegionCalendar());
    final Double rate = _marketData.getDataPoint(_dataId);
    if (rate == null) {
      throw new OpenGammaRuntimeException("Could not get market data for " + _dataId);
    }
    final Currency currency = convention.getCurrency();
    final DayCount dayCount = convention.getDayCount();
    final BusinessDayConvention businessDayConvention = convention.getBusinessDayConvention();
    final boolean eomLeg = convention.isIsEOM();
    final int spotLagLeg = convention.getSettlementDays();
    final ZonedDateTime spotDateLeg = ScheduleCalculator.getAdjustedDate(_valuationTime, spotLagLeg, calendar);
    final ZonedDateTime startDate = ScheduleCalculator.getAdjustedDate(spotDateLeg, swapNode.getStartTenor().getPeriod(), businessDayConvention, calendar, eomLeg);
    final Period paymentPeriod = convention.getPaymentTenor().getPeriod();
    final Period maturityTenor = swapNode.getMaturityTenor().getPeriod();
    return AnnuityCouponFixedDefinition.from(currency, startDate, maturityTenor, paymentPeriod, calendar, dayCount, businessDayConvention, eomLeg, 1, rate, isPayer);
  }

  //TODO do we actually need the settlement days for the swap, not the index?
  private AnnuityDefinition<? extends PaymentDefinition> getIborLeg(final VanillaIborLegConvention convention, final SwapNode swapNode, final boolean isPayer,
       final boolean isIborIbor) {
    final Convention underlyingConvention = _conventionSource.getConvention(convention.getIborIndexConvention());
    if (!(underlyingConvention instanceof IborIndexConvention)) {
      if (underlyingConvention == null) {
        throw new OpenGammaRuntimeException("Could not get convention with id " + convention.getIborIndexConvention());
      }
      throw new OpenGammaRuntimeException("Convention of the underlying was not an ibor index convention; have " + underlyingConvention.getClass());
    }
    final IborIndexConvention indexConvention = (IborIndexConvention) underlyingConvention;
    final Currency currency = indexConvention.getCurrency();
    final DayCount dayCount = indexConvention.getDayCount();
    final BusinessDayConvention businessDayConvention = indexConvention.getBusinessDayConvention();
    final boolean eomIndex = indexConvention.isIsEOM();
    final boolean eomLeg = convention.isIsEOM();
    final Period indexTenor = convention.getResetTenor().getPeriod();
    final Calendar calendar = CalendarUtils.getCalendar(_regionSource, _holidaySource, indexConvention.getFixingCalendar());
    final int spotLag = indexConvention.getSettlementDays();
    final IborIndex iborIndex = new IborIndex(currency, indexTenor, spotLag, dayCount, businessDayConvention, eomIndex, indexConvention.getName());
    final Period maturityTenor = swapNode.getMaturityTenor().getPeriod();
    final int spotLagLeg = convention.getSettlementDays();
    final ZonedDateTime spotDateLeg = ScheduleCalculator.getAdjustedDate(_valuationTime, spotLagLeg, calendar);
    final ZonedDateTime startDate = ScheduleCalculator.getAdjustedDate(spotDateLeg, swapNode.getStartTenor().getPeriod(), businessDayConvention, calendar, eomLeg);
    if (!isPayer && isIborIbor) {
      final Double spread = _marketData.getDataPoint(_dataId);
      if (spread == null) {
        throw new OpenGammaRuntimeException("Could not get market data for " + _dataId);
      }
      return AnnuityCouponIborSpreadDefinition.from(startDate, maturityTenor, 1, iborIndex, spread, isPayer, calendar);
    }
    return AnnuityCouponIborDefinition.from(startDate, maturityTenor, 1, iborIndex, isPayer, calendar);
  }

  private AnnuityCouponONSimplifiedDefinition getOISLeg(final OISLegConvention convention, final SwapNode swapNode, final boolean isPayer) {
    final OvernightIndexConvention indexConvention = (OvernightIndexConvention) _conventionSource.getConvention(convention.getOvernightIndexConvention());
    final Currency currency = indexConvention.getCurrency();
    final DayCount dayCount = indexConvention.getDayCount();
    final int publicationLag = indexConvention.getPublicationLag();
    final Calendar calendar = CalendarUtils.getCalendar(_regionSource, _holidaySource, indexConvention.getRegionCalendar());
    final int spotLagLeg = convention.getSettlementDays();
    final ZonedDateTime spotDateLeg = ScheduleCalculator.getAdjustedDate(_valuationTime, spotLagLeg, calendar);
    final Period maturityTenor = swapNode.getMaturityTenor().getPeriod();
    final IndexON indexON = new IndexON(indexConvention.getName(), currency, dayCount, publicationLag);
    final Period paymentPeriod = convention.getPaymentTenor().getPeriod();
    final boolean eomLeg = convention.isIsEOM();
    final BusinessDayConvention businessDayConvention = convention.getBusinessDayConvention();
    final int paymentLag = convention.getPaymentLag();
    final ZonedDateTime startDate = ScheduleCalculator.getAdjustedDate(spotDateLeg, swapNode.getStartTenor().getPeriod(), businessDayConvention, calendar, eomLeg);
    return AnnuityCouponONSimplifiedDefinition.from(startDate, maturityTenor, 1, isPayer, indexON, paymentLag, calendar, businessDayConvention,
        paymentPeriod, eomLeg);
  }
}
