package Distribution;

import Equations.IEquation;

public class HenyeyGreenstein implements IEquation<Double> {
  private final double g;

  public HenyeyGreenstein(double anisotropy) {
    this.g = anisotropy;
  }

  @Override
  public Double evaluate(Double input) {
    double firstTerm = 1 / 4 * Math.PI;
    double secondTermNumerator = 1 - Math.pow(g, 2);
    double secondTermDenominator = Math.pow(1 + Math.pow(g, 2) - 2* g * Math.cos(input),
            3/2);
    return firstTerm * (secondTermNumerator / secondTermDenominator);
  }
}
