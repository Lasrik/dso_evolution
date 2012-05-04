package de.tle.dso.evolution;

import de.tle.dso.sim.Simulation;
import de.tle.dso.sim.SimulationResult;
import de.tle.dso.sim.battle.InvalidArmyException;
import de.tle.dso.units.Army;
import de.tle.dso.units.Unit;
import de.tle.dso.units.util.UnitPatternHelper;
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
  public void evaluate(Population population) {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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

  @Override
  public void evaluate(Individual individual) {
    if (fitnessAlreadyCalculated(individual)) {
      return;
    }

    int fitness;

    try {
      String attackingArmyPattern = config.getMapper().getPatternFromIndividual(individual);

      Simulation simulation = new Simulation(attackingArmyPattern, DSOConfig.PATTERN);
      simulation.setNumberOfRounds(DSOConfig.SIMULATE_ROUNDS);
      SimulationResult simResult = simulation.simulate();

      fitness = calculateFitness(simResult);
    } catch (Exception ex) {
      // Ungültige Armee - maximaler Fitness Malus. Individuum ist nicht lebensfähig
      fitness = Integer.MAX_VALUE;
    }

    individual.setFitness(fitness);
  }

  private boolean fitnessAlreadyCalculated(Individual individual) {
    return individual.getFitness() > Integer.MIN_VALUE;
  }

  private int calculateFitness(SimulationResult simResult) throws InvalidArmyException {
    int resourceCost = simResult.getMaxResourceCosts().totalWeightPoints();

    int fitness = resourceCost;

    if (!simResult.isAlwaysWin()) {
      fitness += DSOConfig.BATTLE_LOST_MALUS;
    }

    return fitness;
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