/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.holiday;

import java.util.ArrayList;
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

import com.opengamma.core.holiday.HolidayType;
import com.opengamma.master.AbstractMetaDataResult;
import com.opengamma.util.PublicSPI;

/**
 * Result from obtaining meta-data for the holiday master.
 * <p>
 * Meta-data is only returned if requested.
 */
@PublicSPI
@BeanDefinition
public class HolidayMetaDataResult extends AbstractMetaDataResult {

  /**
   * The list if valid holiday types.
   * This is only populated if requested.
   */
  @PropertyDefinition
  private final List<HolidayType> _holidayTypes = new ArrayList<HolidayType>();

  /**
   * Creates an instance.
   */
  public HolidayMetaDataResult() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HolidayMetaDataResult}.
   * @return the meta-bean, not null
   */
  public static HolidayMetaDataResult.Meta meta() {
    return HolidayMetaDataResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(HolidayMetaDataResult.Meta.INSTANCE);
  }

  @Override
  public HolidayMetaDataResult.Meta metaBean() {
    return HolidayMetaDataResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list if valid holiday types.
   * This is only populated if requested.
   * @return the value of the property, not null
   */
  public List<HolidayType> getHolidayTypes() {
    return _holidayTypes;
  }

  /**
   * Sets the list if valid holiday types.
   * This is only populated if requested.
   * @param holidayTypes  the new value of the property, not null
   */
  public void setHolidayTypes(List<HolidayType> holidayTypes) {
    JodaBeanUtils.notNull(holidayTypes, "holidayTypes");
    this._holidayTypes.clear();
    this._holidayTypes.addAll(holidayTypes);
  }

  /**
   * Gets the the {@code holidayTypes} property.
   * This is only populated if requested.
   * @return the property, not null
   */
  public final Property<List<HolidayType>> holidayTypes() {
    return metaBean().holidayTypes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public HolidayMetaDataResult clone() {
    return (HolidayMetaDataResult) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      HolidayMetaDataResult other = (HolidayMetaDataResult) obj;
      return JodaBeanUtils.equal(getHolidayTypes(), other.getHolidayTypes()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getHolidayTypes());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("HolidayMetaDataResult{");
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
    buf.append("holidayTypes").append('=').append(JodaBeanUtils.toString(getHolidayTypes())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HolidayMetaDataResult}.
   */
  public static class Meta extends AbstractMetaDataResult.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code holidayTypes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<HolidayType>> _holidayTypes = DirectMetaProperty.ofReadWrite(
        this, "holidayTypes", HolidayMetaDataResult.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "holidayTypes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 15120129:  // holidayTypes
          return _holidayTypes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends HolidayMetaDataResult> builder() {
      return new DirectBeanBuilder<HolidayMetaDataResult>(new HolidayMetaDataResult());
    }

    @Override
    public Class<? extends HolidayMetaDataResult> beanType() {
      return HolidayMetaDataResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code holidayTypes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<HolidayType>> holidayTypes() {
      return _holidayTypes;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 15120129:  // holidayTypes
          return ((HolidayMetaDataResult) bean).getHolidayTypes();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 15120129:  // holidayTypes
          ((HolidayMetaDataResult) bean).setHolidayTypes((List<HolidayType>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((HolidayMetaDataResult) bean)._holidayTypes, "holidayTypes");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
