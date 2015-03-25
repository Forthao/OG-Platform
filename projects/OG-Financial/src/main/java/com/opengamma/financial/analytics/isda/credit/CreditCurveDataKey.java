/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.isda.credit;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableDefaults;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.analytics.financial.credit.RestructuringClause;
import com.opengamma.core.legalentity.SeniorityLevel;
import com.opengamma.util.money.Currency;

/**
 * A key used to reference a credit curve. Curves are typically referenced
 * by the same five attributes that would identify a CDS, i.e. reference
 * entity, seniority, currency, restructuring clause and CDS type. Fields are
 * intentionally optional since they may not apply to all curves. Whether
 * or not they are assigned with values is left as a decision for the user
 * and what is most suitable for their curve management system.
 */
@BeanDefinition
public final class CreditCurveDataKey implements ImmutableBean {

  /**
   * The name of the curve. 
   * 
   * Commonly the name will refer to the legal entity being
   * priced, but this is not mandatory. Ability to use free-form
   * labels allows the user to specify more abstract curves 
   * which can be used to price a set of legal entities, e.g. 
   * all triple-A entities in a certain currency. This may be 
   * desirable when no observable liquid quotes exist for the 
   * entity.
   */
  @PropertyDefinition(validate = "notEmpty")
  private final String _curveName;
  
  /**
   * The currency. 
   */
  @PropertyDefinition(validate = "notNull")
  private final Currency _currency;
  
  /**
   * The seniority of the referenced debt.
   */
  @PropertyDefinition
  private final SeniorityLevel _seniority;
  
  /**
   * The restructuring clause.
   */
  @PropertyDefinition
  private final RestructuringClause _restructuring;

  /**
   * The Credit Default Swap type. Defaults to Single Name
   */
  @PropertyDefinition
  private final CreditDefaultSwapType _cdsType;


  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    // by default set to single name
    builder.cdsType(CreditDefaultSwapType.SINGLE_NAME);
  }
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CreditCurveDataKey}.
   * @return the meta-bean, not null
   */
  public static CreditCurveDataKey.Meta meta() {
    return CreditCurveDataKey.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CreditCurveDataKey.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static CreditCurveDataKey.Builder builder() {
    return new CreditCurveDataKey.Builder();
  }

  private CreditCurveDataKey(
      String curveName,
      Currency currency,
      SeniorityLevel seniority,
      RestructuringClause restructuring,
      CreditDefaultSwapType cdsType) {
    JodaBeanUtils.notEmpty(curveName, "curveName");
    JodaBeanUtils.notNull(currency, "currency");
    this._curveName = curveName;
    this._currency = currency;
    this._seniority = seniority;
    this._restructuring = restructuring;
    this._cdsType = cdsType;
  }

  @Override
  public CreditCurveDataKey.Meta metaBean() {
    return CreditCurveDataKey.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the curve.
   * 
   * Commonly the name will refer to the legal entity being
   * priced, but this is not mandatory. Ability to use free-form
   * labels allows the user to specify more abstract curves
   * which can be used to price a set of legal entities, e.g.
   * all triple-A entities in a certain currency. This may be
   * desirable when no observable liquid quotes exist for the
   * entity.
   * @return the value of the property, not empty
   */
  public String getCurveName() {
    return _curveName;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the seniority of the referenced debt.
   * @return the value of the property
   */
  public SeniorityLevel getSeniority() {
    return _seniority;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the restructuring clause.
   * @return the value of the property
   */
  public RestructuringClause getRestructuring() {
    return _restructuring;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the Credit Default Swap type.
   * @return the value of the property
   */
  public CreditDefaultSwapType getCdsType() {
    return _cdsType;
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
      CreditCurveDataKey other = (CreditCurveDataKey) obj;
      return JodaBeanUtils.equal(getCurveName(), other.getCurveName()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getSeniority(), other.getSeniority()) &&
          JodaBeanUtils.equal(getRestructuring(), other.getRestructuring()) &&
          JodaBeanUtils.equal(getCdsType(), other.getCdsType());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurveName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSeniority());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRestructuring());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCdsType());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("CreditCurveDataKey{");
    buf.append("curveName").append('=').append(getCurveName()).append(',').append(' ');
    buf.append("currency").append('=').append(getCurrency()).append(',').append(' ');
    buf.append("seniority").append('=').append(getSeniority()).append(',').append(' ');
    buf.append("restructuring").append('=').append(getRestructuring()).append(',').append(' ');
    buf.append("cdsType").append('=').append(JodaBeanUtils.toString(getCdsType()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CreditCurveDataKey}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code curveName} property.
     */
    private final MetaProperty<String> _curveName = DirectMetaProperty.ofImmutable(
        this, "curveName", CreditCurveDataKey.class, String.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofImmutable(
        this, "currency", CreditCurveDataKey.class, Currency.class);
    /**
     * The meta-property for the {@code seniority} property.
     */
    private final MetaProperty<SeniorityLevel> _seniority = DirectMetaProperty.ofImmutable(
        this, "seniority", CreditCurveDataKey.class, SeniorityLevel.class);
    /**
     * The meta-property for the {@code restructuring} property.
     */
    private final MetaProperty<RestructuringClause> _restructuring = DirectMetaProperty.ofImmutable(
        this, "restructuring", CreditCurveDataKey.class, RestructuringClause.class);
    /**
     * The meta-property for the {@code cdsType} property.
     */
    private final MetaProperty<CreditDefaultSwapType> _cdsType = DirectMetaProperty.ofImmutable(
        this, "cdsType", CreditCurveDataKey.class, CreditDefaultSwapType.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "curveName",
        "currency",
        "seniority",
        "restructuring",
        "cdsType");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 771153946:  // curveName
          return _curveName;
        case 575402001:  // currency
          return _currency;
        case 184581246:  // seniority
          return _seniority;
        case -1787372195:  // restructuring
          return _restructuring;
        case 640293516:  // cdsType
          return _cdsType;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public CreditCurveDataKey.Builder builder() {
      return new CreditCurveDataKey.Builder();
    }

    @Override
    public Class<? extends CreditCurveDataKey> beanType() {
      return CreditCurveDataKey.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code curveName} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> curveName() {
      return _curveName;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code seniority} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SeniorityLevel> seniority() {
      return _seniority;
    }

    /**
     * The meta-property for the {@code restructuring} property.
     * @return the meta-property, not null
     */
    public MetaProperty<RestructuringClause> restructuring() {
      return _restructuring;
    }

    /**
     * The meta-property for the {@code cdsType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CreditDefaultSwapType> cdsType() {
      return _cdsType;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 771153946:  // curveName
          return ((CreditCurveDataKey) bean).getCurveName();
        case 575402001:  // currency
          return ((CreditCurveDataKey) bean).getCurrency();
        case 184581246:  // seniority
          return ((CreditCurveDataKey) bean).getSeniority();
        case -1787372195:  // restructuring
          return ((CreditCurveDataKey) bean).getRestructuring();
        case 640293516:  // cdsType
          return ((CreditCurveDataKey) bean).getCdsType();
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
   * The bean-builder for {@code CreditCurveDataKey}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<CreditCurveDataKey> {

    private String _curveName;
    private Currency _currency;
    private SeniorityLevel _seniority;
    private RestructuringClause _restructuring;
    private CreditDefaultSwapType _cdsType;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(CreditCurveDataKey beanToCopy) {
      this._curveName = beanToCopy.getCurveName();
      this._currency = beanToCopy.getCurrency();
      this._seniority = beanToCopy.getSeniority();
      this._restructuring = beanToCopy.getRestructuring();
      this._cdsType = beanToCopy.getCdsType();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 771153946:  // curveName
          return _curveName;
        case 575402001:  // currency
          return _currency;
        case 184581246:  // seniority
          return _seniority;
        case -1787372195:  // restructuring
          return _restructuring;
        case 640293516:  // cdsType
          return _cdsType;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 771153946:  // curveName
          this._curveName = (String) newValue;
          break;
        case 575402001:  // currency
          this._currency = (Currency) newValue;
          break;
        case 184581246:  // seniority
          this._seniority = (SeniorityLevel) newValue;
          break;
        case -1787372195:  // restructuring
          this._restructuring = (RestructuringClause) newValue;
          break;
        case 640293516:  // cdsType
          this._cdsType = (CreditDefaultSwapType) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
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
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public CreditCurveDataKey build() {
      return new CreditCurveDataKey(
          _curveName,
          _currency,
          _seniority,
          _restructuring,
          _cdsType);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code curveName} property in the builder.
     * @param curveName  the new value, not empty
     * @return this, for chaining, not null
     */
    public Builder curveName(String curveName) {
      JodaBeanUtils.notEmpty(curveName, "curveName");
      this._curveName = curveName;
      return this;
    }

    /**
     * Sets the {@code currency} property in the builder.
     * @param currency  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currency(Currency currency) {
      JodaBeanUtils.notNull(currency, "currency");
      this._currency = currency;
      return this;
    }

    /**
     * Sets the {@code seniority} property in the builder.
     * @param seniority  the new value
     * @return this, for chaining, not null
     */
    public Builder seniority(SeniorityLevel seniority) {
      this._seniority = seniority;
      return this;
    }

    /**
     * Sets the {@code restructuring} property in the builder.
     * @param restructuring  the new value
     * @return this, for chaining, not null
     */
    public Builder restructuring(RestructuringClause restructuring) {
      this._restructuring = restructuring;
      return this;
    }

    /**
     * Sets the {@code cdsType} property in the builder.
     * @param cdsType  the new value
     * @return this, for chaining, not null
     */
    public Builder cdsType(CreditDefaultSwapType cdsType) {
      this._cdsType = cdsType;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("CreditCurveDataKey.Builder{");
      buf.append("curveName").append('=').append(JodaBeanUtils.toString(_curveName)).append(',').append(' ');
      buf.append("currency").append('=').append(JodaBeanUtils.toString(_currency)).append(',').append(' ');
      buf.append("seniority").append('=').append(JodaBeanUtils.toString(_seniority)).append(',').append(' ');
      buf.append("restructuring").append('=').append(JodaBeanUtils.toString(_restructuring)).append(',').append(' ');
      buf.append("cdsType").append('=').append(JodaBeanUtils.toString(_cdsType));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
