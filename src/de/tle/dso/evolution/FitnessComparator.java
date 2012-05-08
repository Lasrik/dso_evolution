package de.tle.dso.evolution;

import de.tle.dso.sim.SimulationResult;
import de.tle.evolution.Individual;

public class FitnessComparator implements java.util.Comparator<Individual> {

  @Override
  public int compare(Individual o1, Individual o2) {
    if (o1.getFitness() == o2.getFitness()) {
      SimulationResult sim1 = (SimulationResult) o1.getCustomPayload();
      SimulationResult sim2 = (SimulationResult) o2.getCustomPayload();

      return sim1.getAvgResourceCostWeightPoints() - sim2.getAvgResourceCostWeightPoints();
    }
    
    return o1.getFitness() - o2.getFitness();
  }
}