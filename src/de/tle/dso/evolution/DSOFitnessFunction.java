package de.tle.dso.evolution;

import de.tle.dso.sim.Simulation;
import de.tle.dso.sim.SimulationResult;
import de.tle.dso.sim.battle.InvalidArmyException;
import de.tle.evolution.FitnessFunction;
import de.tle.evolution.Individual;
import de.tle.evolution.Population;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class DSOFitnessFunction extends FitnessFunction {

  protected DSOConfig config;

  public DSOFitnessFunction(DSOConfig config) {
    this.config = config;
  }

  @Override
  public void evaluate(Individual individual) {
    String attackingArmyPattern = config.getMapper().mapIndividualToArmyPattern(individual);

    Simulation simulation = new Simulation(attackingArmyPattern, DSOConfig.PATTERN);
    int fitness;

    try {
      SimulationResult simResult = simulation.simulate();

      int resourceCost = simResult.getMaxResourceCosts().totalWeightPoints();
      int numberOfArmies = individual.getGenom().crossSum();

      fitness = resourceCost + numberOfArmies;

      if (!simResult.isAlwaysWin()) {
        fitness += DSOConfig.LOSS_MALUS;
      }
    } catch (InvalidArmyException ex) {
      fitness = Integer.MAX_VALUE;
    }

    individual.setFitness(fitness);
  }

  @Override
  public void evaluate(Population population) {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    
    for (Individual individual : population) {
      DSOFitnessRunner runner = new DSOFitnessRunner(individual);
      executor.submit(runner);
    }

    executor.shutdown();
    try {
      executor.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException ex) {
      Logger.getLogger(getClass()).warn("DSO Runner liefen nicht zu Ende.", ex);
    }
  }

  private class DSOFitnessRunner implements Runnable {

    private Individual individual;

    public DSOFitnessRunner(Individual individual) {
      this.individual = individual;
    }

    @Override
    public void run() {
      evaluate(individual);
    }
  }
}