/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.bbg.config;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.opengamma.bbg.loader.SecurityType;
import com.opengamma.core.config.Config;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.ArgumentChecker;

/**
 * The mapping of Bloomberg security types description to OG security types.
 */
@Config(description = "Bloomberg security type definition")
@BeanDefinition
public class BloombergSecurityTypeDefinition implements Bean, Serializable, UniqueIdentifiable, MutableUniqueIdentifiable {

  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;
  /**
   * Name for the default configuration document.
   */
  public static final String DEFAULT_CONFIG_NAME = "DEFAULT_BBG_SEC_TYPE_DEFINITION";

  /**
   * The map of security types.
   */
  @PropertyDefinition(get = "manual", set = "manual")
  private final SetMultimap<SecurityType, String> _securityTypes = HashMultimap.create();
  /**
   * The unique identifier.
   */
  @PropertyDefinition
  private UniqueId _uniqueId;

  /**
   * Creates an instance.
   */
  public BloombergSecurityTypeDefinition() {
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the map of security types.
   * 
   * @return the map, not null
   */
  private SetMultimap<SecurityType, String> getSecurityTypes() {
    return _securityTypes;
  }

  /**
   * Gets the map of security types.
   * 
   * @param map  the map to set, not null
   */
  private void setSecurityTypes(SetMultimap<SecurityType, String> map) {
    ArgumentChecker.notNull(map, "map");
    _securityTypes.clear();
    _securityTypes.putAll(map);
  }

  //-------------------------------------------------------------------------
  /**
   * Looks up a security type.
   * 
   * @param bbgSecurityType  the security type, not null
   * @return the security type enum, null if no match
   */
  public SecurityType getSecurityType(String bbgSecurityType) {
    ArgumentChecker.notNull(bbgSecurityType, "bloomberg security type");
    for (Entry<SecurityType, String> entry : _securityTypes.entries()) {
      if (entry.getValue().equals(bbgSecurityType)) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * Gets the valid types.
   * 
   * @param securityType  the security type, not null
   * @return the set of valid types, not null
   */
  public ImmutableSet<String> getValidTypes(SecurityType securityType) {
    ArgumentChecker.notNull(securityType, "security type");
    return ImmutableSet.copyOf(_securityTypes.get(securityType));
  }

  /**
   * Gets all the enum types.
   * 
   * @return the enum types, not null
   */
  public ImmutableSet<SecurityType> getAllSecurityTypes() {
    return ImmutableSet.copyOf(_securityTypes.keySet());
  }

  /**
   * Adds a security type mapping.
   * 
   * @param bbgSecurityType  the bloomberg type, not null
   * @param type  the enum type, not null
   */
  public void addSecurityType(String bbgSecurityType, SecurityType type) {
    ArgumentChecker.notNull(bbgSecurityType, "bloomberg security type");
    ArgumentChecker.notNull(type, "security type");
    _securityTypes.put(type, bbgSecurityType);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BloombergSecurityTypeDefinition}.
   * @return the meta-bean, not null
   */
  public static BloombergSecurityTypeDefinition.Meta meta() {
    return BloombergSecurityTypeDefinition.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BloombergSecurityTypeDefinition.Meta.INSTANCE);
  }

  @Override
  public BloombergSecurityTypeDefinition.Meta metaBean() {
    return BloombergSecurityTypeDefinition.Meta.INSTANCE;
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
   * Gets the the {@code securityTypes} property.
   * @return the property, not null
   */
  public final Property<SetMultimap<SecurityType, String>> securityTypes() {
    return metaBean().securityTypes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier.
   * @return the value of the property
   */
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public BloombergSecurityTypeDefinition clone() {
    BeanBuilder<? extends BloombergSecurityTypeDefinition> builder = metaBean().builder();
    for (MetaProperty<?> mp : metaBean().metaPropertyIterable()) {
      if (mp.style().isBuildable()) {
        Object value = mp.get(this);
        if (value instanceof Bean) {
          value = ((Bean) value).clone();
        }
        builder.set(mp.name(), value);
      }
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BloombergSecurityTypeDefinition other = (BloombergSecurityTypeDefinition) obj;
      return JodaBeanUtils.equal(getSecurityTypes(), other.getSecurityTypes()) &&
          JodaBeanUtils.equal(getUniqueId(), other.getUniqueId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getSecurityTypes());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("BloombergSecurityTypeDefinition{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("securityTypes").append('=').append(JodaBeanUtils.toString(getSecurityTypes())).append(',').append(' ');
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BloombergSecurityTypeDefinition}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code securityTypes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<SetMultimap<SecurityType, String>> _securityTypes = DirectMetaProperty.ofReadWrite(
        this, "securityTypes", BloombergSecurityTypeDefinition.class, (Class) SetMultimap.class);
    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", BloombergSecurityTypeDefinition.class, UniqueId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "securityTypes",
        "uniqueId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -714180327:  // securityTypes
          return _securityTypes;
        case -294460212:  // uniqueId
          return _uniqueId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BloombergSecurityTypeDefinition> builder() {
      return new DirectBeanBuilder<BloombergSecurityTypeDefinition>(new BloombergSecurityTypeDefinition());
    }

    @Override
    public Class<? extends BloombergSecurityTypeDefinition> beanType() {
      return BloombergSecurityTypeDefinition.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code securityTypes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SetMultimap<SecurityType, String>> securityTypes() {
      return _securityTypes;
    }

    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -714180327:  // securityTypes
          return ((BloombergSecurityTypeDefinition) bean).getSecurityTypes();
        case -294460212:  // uniqueId
          return ((BloombergSecurityTypeDefinition) bean).getUniqueId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -714180327:  // securityTypes
          ((BloombergSecurityTypeDefinition) bean).setSecurityTypes((SetMultimap<SecurityType, String>) newValue);
          return;
        case -294460212:  // uniqueId
          ((BloombergSecurityTypeDefinition) bean).setUniqueId((UniqueId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((BloombergSecurityTypeDefinition) bean)._securityTypes, "securityTypes");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
