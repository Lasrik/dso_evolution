package de.tle.dso.evolution;

import de.tle.evolution.Factory;
import de.tle.evolution.Genom;
import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import java.util.ArrayList;
import java.util.List;

public class DSOFactory extends Factory {

  @Override
  public Population createInitialPopulation(final int populationSize) {
    List<Individual> individuals = new ArrayList<Individual>(populationSize);

    createArmiesWithOnlyOneType(individuals, populationSize);
    createEvenlyDistributedArmy(individuals, populationSize);
    fillWithRandomArmies(individuals, populationSize);

    return new Population(individuals, 0);
  }

  private void fillWithRandomArmies(List<Individual> individuals, final int populationSize) {
    while (individuals.size() < populationSize) {
      individuals.add(createRandomIndividual());
    }
  }

  private void createEvenlyDistributedArmy(List<Individual> individuals, final int populationSize) {
    if (individuals.size() < populationSize) {
      int[] genomArray = new int[Genom.NUMBER_OF_CHROMOSOMES];
      int number = Genom.MAX_SINGLE_VALUE / Genom.NUMBER_OF_CHROMOSOMES;
      for (int i = 0; i < genomArray.length; i++) {
        genomArray[i] = number;
      }
      Genom genom = new Genom(genomArray);
      individuals.add(new Individual(genom));
    }
  }

  private void createArmiesWithOnlyOneType(List<Individual> individuals, final int populationSize) {
    int numberOfFixedArmies = Math.min(populationSize, Genom.NUMBER_OF_CHROMOSOMES);
    for (int i = 0; i < numberOfFixedArmies; i++) {
      int[] genomArray = new int[Genom.NUMBER_OF_CHROMOSOMES];
      genomArray[i] = Genom.MAX_SINGLE_VALUE;
      Genom genom = new Genom(genomArray);
      individuals.add(new Individual(genom));
    }
  }

  @Override
  public Individual createRandomIndividual() {
    return Individual.createRandom();
  }
}
