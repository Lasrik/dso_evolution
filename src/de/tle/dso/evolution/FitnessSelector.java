package de.tle.dso.evolution;

import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import de.tle.evolution.Random;
import de.tle.evolution.Selector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FitnessSelector implements Selector {

  private DSOConfig config;

  public FitnessSelector(DSOConfig config) {
    this.config = config;
  }

  @Override
  public List<List<Individual>> selectParents(Population fromPopulation) {
    List<List<Individual>> result = new LinkedList<List<Individual>>();
    Random random = config.getRandom();

    for (int i = 0; i < config.getNumberOfChildren(); i++) {
      List<Individual> parents = new ArrayList<Individual>(config.getNumberOfParents());
      for (int p = 0; p < config.getNumberOfParents(); p++) {
        int nextParentPosition = random.getNextInt(fromPopulation.getIndividuals().size());
        Individual parent = fromPopulation.getIndividuals().get(nextParentPosition);
        parents.add(parent);
      }
      result.add(parents);
    }
    return result;
  }

  @Override
  public Population selectNextGeneration(Population fromPopulation) {
    int number = config.getPopulationSize();
    List<Individual> newGeneration = fromPopulation.getIndividuals().subList(0, number);
    int age = fromPopulation.getAge() + 1;

    Population result = new Population(new ArrayList(newGeneration), age);

    return result;

  }
}
