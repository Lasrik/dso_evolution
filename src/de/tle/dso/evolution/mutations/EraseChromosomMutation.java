package de.tle.dso.evolution.mutations;

import de.tle.dso.evolution.DSOConfig;
import de.tle.evolution.Individual;
import de.tle.evolution.mutation.Mutation;

public class EraseChromosomMutation implements Mutation {

  protected DSOConfig config;

  public EraseChromosomMutation(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void mutate(Individual individual) {
    int max = individual.getGenom().length();
    int random = config.getRandom().getNextInt(max);

    individual.getGenom().setChromosom(random, 0);
  }
}
