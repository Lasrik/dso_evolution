package de.tle.dso.evolution;

import de.tle.evolution.EvolutionAlgorithm;
import org.apache.log4j.Logger;


public class Launcher {

  public static void main(String[] args) {
    Logger.getLogger(Launcher.class).info("Start. Evaluiere: " + DSOConfig.PATTERN);
    EvolutionAlgorithm evolution = new EvolutionAlgorithm(new DSOConfig());
    evolution.evolve();
    Logger.getLogger(Launcher.class).info("Done.");
  }
}
