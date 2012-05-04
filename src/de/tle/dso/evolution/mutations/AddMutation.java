package de.tle.dso.evolution.mutations;

import de.tle.dso.evolution.DSOConfig;
import de.tle.evolution.Genom;
import de.tle.evolution.Individual;
import de.tle.evolution.mutation.Mutation;

public class AddMutation implements Mutation {

  protected DSOConfig config;

  public AddMutation(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void mutate(Individual individual) {
    Genom genom = individual.getGenom();
    int max = genom.length();

    int random = getRandom(max);
    int anotherRandom = getAnotherRandom(max, random);

    int value = genom.getChromosom(random);
    genom.setChromosom(anotherRandom, value);
    genom.setChromosom(random, 0);
  }

  private int getRandom(int max) {
    int random = config.getRandom().getNextInt(max);
    return random;
  }

  private int getAnotherRandom(int max, int random) {
    int result = getRandom(max);
    while (result == random) {
      result = getRandom(max);
    }

    return result;
  }
}
