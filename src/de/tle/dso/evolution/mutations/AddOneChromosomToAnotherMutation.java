package de.tle.dso.evolution.mutations;

import de.tle.dso.evolution.DSOConfig;
import de.tle.evolution.Genom;
import de.tle.evolution.Individual;
import de.tle.evolution.mutation.Mutation;

public class AddOneChromosomToAnotherMutation implements Mutation {

  protected DSOConfig config;

  public AddOneChromosomToAnotherMutation(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void mutate(Individual individual) {
    Genom genom = individual.getGenom();
    int max = genom.length();

    int random = getRandom(max);
    int value1 = genom.getChromosom(random);

    int anotherRandom = getAnotherRandom(max, random);
    int value2 = genom.getChromosom(anotherRandom);

    int newValue = value1 + value2;

    genom.setChromosom(anotherRandom, newValue);
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
