package de.tle.dso.evolution;

import de.tle.evolution.Individual;

public class FitnessComparator implements java.util.Comparator<Individual> {

  @Override
  public int compare(Individual o1, Individual o2) {
    if (o1.getFitness() == o2.getFitness()) {
      return o1.getGenom().crossSum() - o2.getGenom().crossSum();
    }

    return o1.getFitness() - o2.getFitness();
  }
}
