/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZonedDateTime;

import com.google.common.collect.ImmutableList;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for bond futures.
 */
@BeanDefinition
public class BondFutureSecurity extends FutureSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The deliverables.
   */
  @PropertyDefinition(validate = "notNull")
  private final List<BondFutureDeliverable> _basket = new ArrayList<BondFutureDeliverable>();  
  /**
   * The first delivery date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _firstDeliveryDate;
  /**
   * The last delivery date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _lastDeliveryDate;

  BondFutureSecurity() { //For builder
    super();
  }

  public BondFutureSecurity(Expiry expiry, String tradingExchange, String settlementExchange, Currency currency, double unitAmount,
      Collection<? extends BondFutureDeliverable> basket, ZonedDateTime firstDeliveryDate, ZonedDateTime lastDeliveryDate, String category) {
    super(expiry, tradingExchange, settlementExchange, currency, unitAmount, category);
    setBasket(ImmutableList.copyOf(basket));    
    setFirstDeliveryDate(firstDeliveryDate);
    setLastDeliveryDate(lastDeliveryDate);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitBondFutureSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BondFutureSecurity}.
   * @return the meta-bean, not null
   */
  public static BondFutureSecurity.Meta meta() {
    return BondFutureSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BondFutureSecurity.Meta.INSTANCE);
  }

  @Override
  public BondFutureSecurity.Meta metaBean() {
    return BondFutureSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the deliverables.
   * @return the value of the property, not null
   */
  public List<BondFutureDeliverable> getBasket() {
    return _basket;
  }

  /**
   * Sets the deliverables.
   * @param basket  the new value of the property, not null
   */
  public void setBasket(List<BondFutureDeliverable> basket) {
    JodaBeanUtils.notNull(basket, "basket");
    this._basket.clear();
    this._basket.addAll(basket);
  }

  /**
   * Gets the the {@code basket} property.
   * @return the property, not null
   */
  public final Property<List<BondFutureDeliverable>> basket() {
    return metaBean().basket().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the first delivery date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getFirstDeliveryDate() {
    return _firstDeliveryDate;
  }

  /**
   * Sets the first delivery date.
   * @param firstDeliveryDate  the new value of the property, not null
   */
  public void setFirstDeliveryDate(ZonedDateTime firstDeliveryDate) {
    JodaBeanUtils.notNull(firstDeliveryDate, "firstDeliveryDate");
    this._firstDeliveryDate = firstDeliveryDate;
  }

  /**
   * Gets the the {@code firstDeliveryDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> firstDeliveryDate() {
    return metaBean().firstDeliveryDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the last delivery date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getLastDeliveryDate() {
    return _lastDeliveryDate;
  }

  /**
   * Sets the last delivery date.
   * @param lastDeliveryDate  the new value of the property, not null
   */
  public void setLastDeliveryDate(ZonedDateTime lastDeliveryDate) {
    JodaBeanUtils.notNull(lastDeliveryDate, "lastDeliveryDate");
    this._lastDeliveryDate = lastDeliveryDate;
  }

  /**
   * Gets the the {@code lastDeliveryDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> lastDeliveryDate() {
    return metaBean().lastDeliveryDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public BondFutureSecurity clone() {
    return (BondFutureSecurity) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BondFutureSecurity other = (BondFutureSecurity) obj;
      return JodaBeanUtils.equal(getBasket(), other.getBasket()) &&
          JodaBeanUtils.equal(getFirstDeliveryDate(), other.getFirstDeliveryDate()) &&
          JodaBeanUtils.equal(getLastDeliveryDate(), other.getLastDeliveryDate()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getBasket());
    hash += hash * 31 + JodaBeanUtils.hashCode(getFirstDeliveryDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getLastDeliveryDate());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("BondFutureSecurity{");
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
    buf.append("basket").append('=').append(JodaBeanUtils.toString(getBasket())).append(',').append(' ');
    buf.append("firstDeliveryDate").append('=').append(JodaBeanUtils.toString(getFirstDeliveryDate())).append(',').append(' ');
    buf.append("lastDeliveryDate").append('=').append(JodaBeanUtils.toString(getLastDeliveryDate())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BondFutureSecurity}.
   */
  public static class Meta extends FutureSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code basket} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<BondFutureDeliverable>> _basket = DirectMetaProperty.ofReadWrite(
        this, "basket", BondFutureSecurity.class, (Class) List.class);
    /**
     * The meta-property for the {@code firstDeliveryDate} property.
     */
    private final MetaProperty<ZonedDateTime> _firstDeliveryDate = DirectMetaProperty.ofReadWrite(
        this, "firstDeliveryDate", BondFutureSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code lastDeliveryDate} property.
     */
    private final MetaProperty<ZonedDateTime> _lastDeliveryDate = DirectMetaProperty.ofReadWrite(
        this, "lastDeliveryDate", BondFutureSecurity.class, ZonedDateTime.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "basket",
        "firstDeliveryDate",
        "lastDeliveryDate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1396196922:  // basket
          return _basket;
        case 1755448466:  // firstDeliveryDate
          return _firstDeliveryDate;
        case -233366664:  // lastDeliveryDate
          return _lastDeliveryDate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BondFutureSecurity> builder() {
      return new DirectBeanBuilder<BondFutureSecurity>(new BondFutureSecurity());
    }

    @Override
    public Class<? extends BondFutureSecurity> beanType() {
      return BondFutureSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code basket} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<BondFutureDeliverable>> basket() {
      return _basket;
    }

    /**
     * The meta-property for the {@code firstDeliveryDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> firstDeliveryDate() {
      return _firstDeliveryDate;
    }

    /**
     * The meta-property for the {@code lastDeliveryDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> lastDeliveryDate() {
      return _lastDeliveryDate;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1396196922:  // basket
          return ((BondFutureSecurity) bean).getBasket();
        case 1755448466:  // firstDeliveryDate
          return ((BondFutureSecurity) bean).getFirstDeliveryDate();
        case -233366664:  // lastDeliveryDate
          return ((BondFutureSecurity) bean).getLastDeliveryDate();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1396196922:  // basket
          ((BondFutureSecurity) bean).setBasket((List<BondFutureDeliverable>) newValue);
          return;
        case 1755448466:  // firstDeliveryDate
          ((BondFutureSecurity) bean).setFirstDeliveryDate((ZonedDateTime) newValue);
          return;
        case -233366664:  // lastDeliveryDate
          ((BondFutureSecurity) bean).setLastDeliveryDate((ZonedDateTime) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((BondFutureSecurity) bean)._basket, "basket");
      JodaBeanUtils.notNull(((BondFutureSecurity) bean)._firstDeliveryDate, "firstDeliveryDate");
      JodaBeanUtils.notNull(((BondFutureSecurity) bean)._lastDeliveryDate, "lastDeliveryDate");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
