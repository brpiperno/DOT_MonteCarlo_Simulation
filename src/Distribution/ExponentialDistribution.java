package Distribution;

import java.util.Random;

/**
 * Return a function object that evaluates itself with a given x-value
 */
public class ExponentialDistribution implements Distribution {
  private final double lambda;

  public ExponentialDistribution(double lambda) {
    this.lambda = lambda;
  }

  @Override
  public double generate() {
    return pdf(Math.random());
  }

  @Override
  public double pdf(double value) {
    if (value < 0) {
      return 0.00;
    }
    else {
      return lambda * Math.exp(-lambda * value);
    }
  }

  @Override
  public double cdf(double value) {
    if (value < 0) { return 0; }
    else { return 1 - Math.exp(-lambda * value); }
  }
}
