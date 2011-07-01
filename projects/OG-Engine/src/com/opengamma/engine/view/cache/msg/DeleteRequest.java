// Automatically created - do not modify
///CLOVER:OFF
// CSOFF: Generated File
package com.opengamma.engine.view.cache.msg;
public class DeleteRequest extends com.opengamma.engine.view.cache.msg.CacheMessage implements java.io.Serializable {
  public CacheMessage accept (CacheMessageVisitor visitor) { return visitor.visitDeleteRequest (this); }
  private static final long serialVersionUID = 35229501837005l;
  private com.opengamma.id.UniqueIdentifier _viewCycleId;
  public static final String VIEW_CYCLE_ID_KEY = "viewCycleId";
  private String _calculationConfigurationName;
  public static final String CALCULATION_CONFIGURATION_NAME_KEY = "calculationConfigurationName";
  public DeleteRequest (com.opengamma.id.UniqueIdentifier viewCycleId, String calculationConfigurationName) {
    if (viewCycleId == null) throw new NullPointerException ("'viewCycleId' cannot be null");
    else {
      _viewCycleId = viewCycleId;
    }
    if (calculationConfigurationName == null) throw new NullPointerException ("calculationConfigurationName' cannot be null");
    _calculationConfigurationName = calculationConfigurationName;
  }
  protected DeleteRequest (final org.fudgemsg.mapping.FudgeDeserializationContext fudgeContext, final org.fudgemsg.FudgeMsg fudgeMsg) {
    super (fudgeContext, fudgeMsg);
    org.fudgemsg.FudgeField fudgeField;
    fudgeField = fudgeMsg.getByName (VIEW_CYCLE_ID_KEY);
    if (fudgeField == null) throw new IllegalArgumentException ("Fudge message is not a DeleteRequest - field 'viewCycleId' is not present");
    try {
      _viewCycleId = com.opengamma.id.UniqueIdentifier.fromFudgeMsg (fudgeContext, fudgeMsg.getFieldValue (org.fudgemsg.FudgeMsg.class, fudgeField));
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException ("Fudge message is not a DeleteRequest - field 'viewCycleId' is not UniqueIdentifier message", e);
    }
    fudgeField = fudgeMsg.getByName (CALCULATION_CONFIGURATION_NAME_KEY);
    if (fudgeField == null) throw new IllegalArgumentException ("Fudge message is not a DeleteRequest - field 'calculationConfigurationName' is not present");
    try {
      _calculationConfigurationName = fudgeField.getValue ().toString ();
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException ("Fudge message is not a DeleteRequest - field 'calculationConfigurationName' is not string", e);
    }
  }
  public DeleteRequest (Long correlationId, com.opengamma.id.UniqueIdentifier viewCycleId, String calculationConfigurationName) {
    super (correlationId);
    if (viewCycleId == null) throw new NullPointerException ("'viewCycleId' cannot be null");
    else {
      _viewCycleId = viewCycleId;
    }
    if (calculationConfigurationName == null) throw new NullPointerException ("calculationConfigurationName' cannot be null");
    _calculationConfigurationName = calculationConfigurationName;
  }
  protected DeleteRequest (final DeleteRequest source) {
    super (source);
    if (source == null) throw new NullPointerException ("'source' must not be null");
    if (source._viewCycleId == null) _viewCycleId = null;
    else {
      _viewCycleId = source._viewCycleId;
    }
    _calculationConfigurationName = source._calculationConfigurationName;
  }
  public DeleteRequest clone () {
    return new DeleteRequest (this);
  }
  public org.fudgemsg.FudgeMsg toFudgeMsg (final org.fudgemsg.mapping.FudgeSerializationContext fudgeContext) {
    if (fudgeContext == null) throw new NullPointerException ("fudgeContext must not be null");
    final org.fudgemsg.MutableFudgeMsg msg = fudgeContext.newMessage ();
    toFudgeMsg (fudgeContext, msg);
    return msg;
  }
  public void toFudgeMsg (final org.fudgemsg.mapping.FudgeSerializationContext fudgeContext, final org.fudgemsg.MutableFudgeMsg msg) {
    super.toFudgeMsg (fudgeContext, msg);
    if (_viewCycleId != null)  {
      final org.fudgemsg.MutableFudgeMsg fudge1 = org.fudgemsg.mapping.FudgeSerializationContext.addClassHeader (fudgeContext.newMessage (), _viewCycleId.getClass (), com.opengamma.id.UniqueIdentifier.class);
      _viewCycleId.toFudgeMsg (fudgeContext, fudge1);
      msg.add (VIEW_CYCLE_ID_KEY, null, fudge1);
    }
    if (_calculationConfigurationName != null)  {
      msg.add (CALCULATION_CONFIGURATION_NAME_KEY, null, _calculationConfigurationName);
    }
  }
  public static DeleteRequest fromFudgeMsg (final org.fudgemsg.mapping.FudgeDeserializationContext fudgeContext, final org.fudgemsg.FudgeMsg fudgeMsg) {
    final java.util.List<org.fudgemsg.FudgeField> types = fudgeMsg.getAllByOrdinal (0);
    for (org.fudgemsg.FudgeField field : types) {
      final String className = (String)field.getValue ();
      if ("com.opengamma.engine.view.cache.msg.DeleteRequest".equals (className)) break;
      try {
        return (com.opengamma.engine.view.cache.msg.DeleteRequest)Class.forName (className).getDeclaredMethod ("fromFudgeMsg", org.fudgemsg.mapping.FudgeDeserializationContext.class, org.fudgemsg.FudgeMsg.class).invoke (null, fudgeContext, fudgeMsg);
      }
      catch (Throwable t) {
        // no-action
      }
    }
    return new DeleteRequest (fudgeContext, fudgeMsg);
  }
  public com.opengamma.id.UniqueIdentifier getViewCycleId () {
    return _viewCycleId;
  }
  public void setViewCycleId (com.opengamma.id.UniqueIdentifier viewCycleId) {
    if (viewCycleId == null) throw new NullPointerException ("'viewCycleId' cannot be null");
    else {
      _viewCycleId = viewCycleId;
    }
  }
  public String getCalculationConfigurationName () {
    return _calculationConfigurationName;
  }
  public void setCalculationConfigurationName (String calculationConfigurationName) {
    if (calculationConfigurationName == null) throw new NullPointerException ("calculationConfigurationName' cannot be null");
    _calculationConfigurationName = calculationConfigurationName;
  }
  public String toString () {
    return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this, org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
///CLOVER:ON
// CSON: Generated File
