/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view;

import java.util.Collection;
import java.util.Map;

import com.opengamma.engine.depgraph.NewDependencyGraphModel;
import com.opengamma.engine.position.Portfolio;
import com.opengamma.engine.position.PortfolioNode;
import com.opengamma.engine.position.Position;
import com.opengamma.engine.security.SecurityMaster;
import com.opengamma.engine.value.AnalyticValueDefinition;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.view.cache.ViewComputationCache;

/**
 * The data model represents the sum total of analytic functions applied to positions
 * in a particular view. It is the primary data repository for a particular
 * {@link View}.
 *
 * @author kirk
 */
public interface ViewComputationResultModel {
  
  // REVIEW kirk 2009-09-03 -- Should these be JSR-310 instants? Probably.
  
  long getInputDataTimestamp();
  
  long getResultTimestamp();
  
  /**
   * Obtain the positions that are part of this computation result.
   * This includes both the aggregate and leaf positions.
   * This may be different from the current state in the defining
   * {@link View} because positions can change over the course of time.
   * 
   * @return All positions part of this computation pass.
   */
  Collection<Position> getPositions();
  
  Map<AnalyticValueDefinition<?>, ComputedValue<?>> getValues(Position position);
  
  ComputedValue<?> getValue(Position position, AnalyticValueDefinition<?> valueDefinition);
  
  NewDependencyGraphModel getDependencyGraphModel();
  
  ViewComputationCache getComputationCache();
  
  Portfolio getPortfolio();
  
  PortfolioNode getRootPopulatedNode();
  
  // review this.
  // this should probably be removed now we package securities with positions.
  @Deprecated
  SecurityMaster getSecurityMaster();

  ComputedValue<?> getValue(PortfolioNode node, AnalyticValueDefinition<?> valueDefinition);

  Map<AnalyticValueDefinition<?>, ComputedValue<?>> getValues(PortfolioNode node);
}
