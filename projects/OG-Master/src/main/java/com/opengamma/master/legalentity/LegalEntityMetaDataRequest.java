/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.legalentity;

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

import com.opengamma.master.AbstractMetaDataRequest;
import com.opengamma.util.PublicSPI;

/**
 * Request for meta-data about the legal entity master.
 * <p/>
 * This will return meta-data valid for the whole master.
 */
@PublicSPI
@BeanDefinition
public class LegalEntityMetaDataRequest extends AbstractMetaDataRequest {

  /**
   * Whether to fetch the db_schema_version, false by default.
   */
  @PropertyDefinition
  private boolean _schemaVersion;

  /**
   * Creates an instance.
   */
  public LegalEntityMetaDataRequest() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LegalEntityMetaDataRequest}.
   * @return the meta-bean, not null
   */
  public static LegalEntityMetaDataRequest.Meta meta() {
    return LegalEntityMetaDataRequest.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LegalEntityMetaDataRequest.Meta.INSTANCE);
  }

  @Override
  public LegalEntityMetaDataRequest.Meta metaBean() {
    return LegalEntityMetaDataRequest.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether to fetch the db_schema_version, false by default.
   * @return the value of the property
   */
  public boolean isSchemaVersion() {
    return _schemaVersion;
  }

  /**
   * Sets whether to fetch the db_schema_version, false by default.
   * @param schemaVersion  the new value of the property
   */
  public void setSchemaVersion(boolean schemaVersion) {
    this._schemaVersion = schemaVersion;
  }

  /**
   * Gets the the {@code schemaVersion} property.
   * @return the property, not null
   */
  public final Property<Boolean> schemaVersion() {
    return metaBean().schemaVersion().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public LegalEntityMetaDataRequest clone() {
    return (LegalEntityMetaDataRequest) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LegalEntityMetaDataRequest other = (LegalEntityMetaDataRequest) obj;
      return (isSchemaVersion() == other.isSchemaVersion()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(isSchemaVersion());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("LegalEntityMetaDataRequest{");
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
    buf.append("schemaVersion").append('=').append(JodaBeanUtils.toString(isSchemaVersion())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LegalEntityMetaDataRequest}.
   */
  public static class Meta extends AbstractMetaDataRequest.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code schemaVersion} property.
     */
    private final MetaProperty<Boolean> _schemaVersion = DirectMetaProperty.ofReadWrite(
        this, "schemaVersion", LegalEntityMetaDataRequest.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "schemaVersion");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -233564169:  // schemaVersion
          return _schemaVersion;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LegalEntityMetaDataRequest> builder() {
      return new DirectBeanBuilder<LegalEntityMetaDataRequest>(new LegalEntityMetaDataRequest());
    }

    @Override
    public Class<? extends LegalEntityMetaDataRequest> beanType() {
      return LegalEntityMetaDataRequest.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code schemaVersion} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> schemaVersion() {
      return _schemaVersion;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -233564169:  // schemaVersion
          return ((LegalEntityMetaDataRequest) bean).isSchemaVersion();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -233564169:  // schemaVersion
          ((LegalEntityMetaDataRequest) bean).setSchemaVersion((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}