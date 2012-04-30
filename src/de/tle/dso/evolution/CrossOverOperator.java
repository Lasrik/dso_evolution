package de.tle.dso.evolution;

import de.tle.evolution.GeneticOperator;
import de.tle.evolution.Genom;
import de.tle.evolution.Individual;
import java.util.List;

public class CrossOverOperator implements GeneticOperator{

  @Override
  public Individual operate(List<Individual> selectedIndividuals) {
    Individual parent1 = selectedIndividuals.get(0);
    Individual parent2 = selectedIndividuals.get(1);
    
    Genom childGenom = parent1.getGenom().crossover(parent2.getGenom());
    
    return new Individual(childGenom);
  }
  
}
