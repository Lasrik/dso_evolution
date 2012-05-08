package de.tle.dso.evolution;

import de.tle.evolution.Individual;

public class FitnessComparator implements java.util.Comparator<Individual> {

  @Override
  public int compare(Individual o1, Individual o2) {
    return o1.getFitness() - o2.getFitness();
  }
}