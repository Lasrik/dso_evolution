package de.tle.dso.evolution;

import de.tle.evolution.Factory;
import de.tle.evolution.Genom;
import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import java.util.ArrayList;
import java.util.List;
import static de.tle.dso.evolution.DSOConfig.*;

public class DSOFactory extends Factory {

  private DSOConfig config;

  public DSOFactory(DSOConfig config) {
    this.config = config;
  }

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
      int[] genomArray = new int[GENOM_SIZE];
      int number = MAX_PLAYER_ARMY_SIZE / GENOM_SIZE;
      for (int i = 0; i < genomArray.length; i++) {
        genomArray[i] = number;
      }
      Genom genom = new Genom(genomArray);
      individuals.add(new Individual(genom));
    }
  }

  private void createArmiesWithOnlyOneType(List<Individual> individuals, final int populationSize) {
    int numberOfFixedArmies = Math.min(populationSize, GENOM_SIZE);
    for (int i = 0; i < numberOfFixedArmies; i++) {
      int[] genomArray = new int[GENOM_SIZE];
      genomArray[i] = MAX_PLAYER_ARMY_SIZE;
      Genom genom = new Genom(genomArray);
      individuals.add(new Individual(genom));
    }
  }

  @Override
  public Individual createRandomIndividual() {
    return Individual.createRandom(GENOM_SIZE, MAX_PLAYER_ARMY_SIZE, MAX_PLAYER_ARMY_SIZE);
  }
}
