package Equations;

/**
 * A representation of some sort of single order equation that can be solved with a single variable
 */
public interface IEquation<k> {

  /**
   * Evaluate the equation at the given input.
   * @param input
   * @return the output
   */
  k evaluate(k input);
}
