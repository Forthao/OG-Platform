/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.fixedincome;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.analytics.financial.instrument.payment.PaymentDefinition;
import com.opengamma.analytics.financial.interestrate.CouponFixedRateVisitor;
import com.opengamma.analytics.financial.interestrate.CouponPaymentVisitor;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.util.money.CurrencyAmount;

/**
 * Details of a cash flow that pays fixed amount on a given payment date.
 */
@BeanDefinition(hierarchy = "immutable")
public final class FixedCashFlowDetails extends AbstractCashFlowDetails implements CashFlowDetails {
  
  /**
   * The visitor used to retrieve the fixed rate from the cash flow.
   */
  private static final CouponFixedRateVisitor FIXED_RATE_VISITOR = new CouponFixedRateVisitor();
  
  /**
   * The visitor used to calculate the projected amount of the cash flow.
   */
  private static final CouponPaymentVisitor PROJECTED_AMOUNT_VISITOR = new CouponPaymentVisitor();
  
  /**
   * The fixed rate of the cash flow.
   */
  @PropertyDefinition(validate = "notNull")
  private final double _rate;

  /**
   * The projected amount of the cash flow.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurrencyAmount _projectedAmount;

  /**
   * The present value of the cash flow.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurrencyAmount _presentValue;
  
  /**
   * Constructor that uses the definition and derivative versions of a payment to construct a description of a fixed cash
   * flow.
   * @param definition the definition representation of a cash flow.
   * @param derivative the derivative representation of a cash flow.
   * @param curves the curve bundle used to retrieve discount factors.
   */
  public FixedCashFlowDetails(PaymentDefinition definition, Payment derivative, MulticurveProviderInterface curves) {
    super(definition, derivative, curves);
    _rate = derivative.accept(FIXED_RATE_VISITOR);
    _projectedAmount = derivative.accept(PROJECTED_AMOUNT_VISITOR);
    _presentValue = getProjectedAmount().multipliedBy(getDf());
  }
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FixedCashFlowDetails}.
   * @return the meta-bean, not null
   */
  public static FixedCashFlowDetails.Meta meta() {
    return FixedCashFlowDetails.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FixedCashFlowDetails.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FixedCashFlowDetails.Builder builder() {
    return new FixedCashFlowDetails.Builder();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  private FixedCashFlowDetails(FixedCashFlowDetails.Builder builder) {
    super(builder);
    JodaBeanUtils.notNull(builder._rate, "rate");
    JodaBeanUtils.notNull(builder._projectedAmount, "projectedAmount");
    JodaBeanUtils.notNull(builder._presentValue, "presentValue");
    this._rate = builder._rate;
    this._projectedAmount = builder._projectedAmount;
    this._presentValue = builder._presentValue;
  }

  @Override
  public FixedCashFlowDetails.Meta metaBean() {
    return FixedCashFlowDetails.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the rate.
   * @return the value of the property, not null
   */
  public double getRate() {
    return _rate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the projectedAmount.
   * @return the value of the property, not null
   */
  public CurrencyAmount getProjectedAmount() {
    return _projectedAmount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the presentValue.
   * @return the value of the property, not null
   */
  public CurrencyAmount getPresentValue() {
    return _presentValue;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FixedCashFlowDetails other = (FixedCashFlowDetails) obj;
      return JodaBeanUtils.equal(getRate(), other.getRate()) &&
          JodaBeanUtils.equal(getProjectedAmount(), other.getProjectedAmount()) &&
          JodaBeanUtils.equal(getPresentValue(), other.getPresentValue()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getRate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getProjectedAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPresentValue());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("FixedCashFlowDetails{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("rate").append('=').append(JodaBeanUtils.toString(getRate())).append(',').append(' ');
    buf.append("projectedAmount").append('=').append(JodaBeanUtils.toString(getProjectedAmount())).append(',').append(' ');
    buf.append("presentValue").append('=').append(JodaBeanUtils.toString(getPresentValue())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedCashFlowDetails}.
   */
  public static final class Meta extends AbstractCashFlowDetails.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code rate} property.
     */
    private final MetaProperty<Double> _rate = DirectMetaProperty.ofImmutable(
        this, "rate", FixedCashFlowDetails.class, Double.TYPE);
    /**
     * The meta-property for the {@code projectedAmount} property.
     */
    private final MetaProperty<CurrencyAmount> _projectedAmount = DirectMetaProperty.ofImmutable(
        this, "projectedAmount", FixedCashFlowDetails.class, CurrencyAmount.class);
    /**
     * The meta-property for the {@code presentValue} property.
     */
    private final MetaProperty<CurrencyAmount> _presentValue = DirectMetaProperty.ofImmutable(
        this, "presentValue", FixedCashFlowDetails.class, CurrencyAmount.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "rate",
        "projectedAmount",
        "presentValue");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          return _rate;
        case -5687312:  // projectedAmount
          return _projectedAmount;
        case 686253430:  // presentValue
          return _presentValue;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FixedCashFlowDetails.Builder builder() {
      return new FixedCashFlowDetails.Builder();
    }

    @Override
    public Class<? extends FixedCashFlowDetails> beanType() {
      return FixedCashFlowDetails.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code rate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> rate() {
      return _rate;
    }

    /**
     * The meta-property for the {@code projectedAmount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurrencyAmount> projectedAmount() {
      return _projectedAmount;
    }

    /**
     * The meta-property for the {@code presentValue} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurrencyAmount> presentValue() {
      return _presentValue;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          return ((FixedCashFlowDetails) bean).getRate();
        case -5687312:  // projectedAmount
          return ((FixedCashFlowDetails) bean).getProjectedAmount();
        case 686253430:  // presentValue
          return ((FixedCashFlowDetails) bean).getPresentValue();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code FixedCashFlowDetails}.
   */
  public static final class Builder extends AbstractCashFlowDetails.Builder {

    private double _rate;
    private CurrencyAmount _projectedAmount;
    private CurrencyAmount _presentValue;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(FixedCashFlowDetails beanToCopy) {
      this._rate = beanToCopy.getRate();
      this._projectedAmount = beanToCopy.getProjectedAmount();
      this._presentValue = beanToCopy.getPresentValue();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          return _rate;
        case -5687312:  // projectedAmount
          return _projectedAmount;
        case 686253430:  // presentValue
          return _presentValue;
        default:
          return super.get(propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          this._rate = (Double) newValue;
          break;
        case -5687312:  // projectedAmount
          this._projectedAmount = (CurrencyAmount) newValue;
          break;
        case 686253430:  // presentValue
          this._presentValue = (CurrencyAmount) newValue;
          break;
        default:
          super.set(propertyName, newValue);
          break;
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public FixedCashFlowDetails build() {
      return new FixedCashFlowDetails(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code rate} property in the builder.
     * @param rate  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder rate(double rate) {
      JodaBeanUtils.notNull(rate, "rate");
      this._rate = rate;
      return this;
    }

    /**
     * Sets the {@code projectedAmount} property in the builder.
     * @param projectedAmount  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder projectedAmount(CurrencyAmount projectedAmount) {
      JodaBeanUtils.notNull(projectedAmount, "projectedAmount");
      this._projectedAmount = projectedAmount;
      return this;
    }

    /**
     * Sets the {@code presentValue} property in the builder.
     * @param presentValue  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder presentValue(CurrencyAmount presentValue) {
      JodaBeanUtils.notNull(presentValue, "presentValue");
      this._presentValue = presentValue;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("FixedCashFlowDetails.Builder{");
      buf.append("rate").append('=').append(JodaBeanUtils.toString(_rate)).append(',').append(' ');
      buf.append("projectedAmount").append('=').append(JodaBeanUtils.toString(_projectedAmount)).append(',').append(' ');
      buf.append("presentValue").append('=').append(JodaBeanUtils.toString(_presentValue));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
