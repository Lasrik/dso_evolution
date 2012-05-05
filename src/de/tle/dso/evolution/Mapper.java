package de.tle.dso.evolution;

import de.tle.evolution.Individual;

public class Mapper {

  public String getPatternFromIndividual(Individual individual) {
    int[] genom = individual.getGenom().getChromosomes();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < genom.length; i++) {
      if (genom[i] > 0) {
        sb.append(genom[i]);
        sb.append(" ");

        switch (i) {
          case 0:
            sb.append("R");
            break;
          case 1:
            sb.append("M");
            break;
          case 2:
            sb.append("C");
            break;
          case 3:
            sb.append("S");
            break;
          case 4:
            sb.append("B");
            break;
          case 5:
            sb.append("LB");
            break;
          case 6:
            sb.append("A");
            break;
        }
        sb.append(", ");
      }
    }

    sb.append("1 G");
    return sb.toString();
  }
}
