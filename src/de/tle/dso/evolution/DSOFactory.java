package de.tle.dso.evolution;

import de.tle.evolution.Factory;
import de.tle.evolution.Individual;

public class DSOFactory extends Factory {

  @Override
  public Individual createRandomIndividual() {
    return Individual.createRandom();
  }
  
}
