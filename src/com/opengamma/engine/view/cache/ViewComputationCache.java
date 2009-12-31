/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.cache;

import com.opengamma.engine.value.NewComputedValue;
import com.opengamma.engine.value.ValueSpecification;

/**
 * The shared cache through which various elements in view recalculation will
 * store and retrieve values.
 *
 * @author kirk
 */
public interface ViewComputationCache {

  NewComputedValue getValue(ValueSpecification specification);
  
  void putValue(NewComputedValue value);
}
