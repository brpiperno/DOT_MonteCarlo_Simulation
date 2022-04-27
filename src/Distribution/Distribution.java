package Distribution;

/**
 * A Representation of a Random Distribution
 */
public interface Distribution {
  /**
   * Generate a random variable according to the given distribution
   */
  double generate();

  /**
   * Return the value of the probability distribution function at the given value.
   * @throws IllegalArgumentException if the value is
   * outside the bounds of the distribution.
   */
  double pdf(double value);

  /**
   * Return the value of the cumulative distribution function at the given value.
   * @throws IllegalArgumentException if the value is
   * outside the bounds of the distribution.
   */
  double cdf(double value);
}
