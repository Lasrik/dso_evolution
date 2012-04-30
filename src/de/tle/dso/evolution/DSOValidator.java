/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tle.dso.evolution;

import de.tle.dso.units.Army;
import de.tle.dso.units.util.UnitPatternHelper;
import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import de.tle.evolution.Validator;

class DSOValidator implements Validator {
  
  private DSOConfig config;

  public DSOValidator(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void validate(Population population) {
    for (Individual individual : population) {
      String armyPattern = config.getMapper().mapIndividualToArmyPattern(individual);
      Army army = UnitPatternHelper.createArmyFromPattern(armyPattern);
      if (!army.isValid()) {
        Individual newIndividual = config.getFactory().createRandomIndividual();
        population.addIndividual(newIndividual);
      }
    }
  }
}
