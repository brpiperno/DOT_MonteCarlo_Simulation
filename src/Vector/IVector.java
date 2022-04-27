package Vector;

import java.util.function.Function;

/**
 * A Representation of a 2D or 3D vector that can be manipulated for simple geometric calculations.
 */
public interface IVector {

  /**
   * Add two vectors together.
   * @param a the given vector
   * @return the resulting vector
   */
  IVector add(IVector a);

  /**
   * Subtract a given vector from this one.
   * @param a the given vector
   * @return the resulting vector
   */
  IVector subtract(IVector a);

  /**
   * Multiply all values in the vector by a given multiplier
   * @param multiplier the known multiplier
   * @return the vector with all values multiplied.
   */
  IVector multiply(double  multiplier);

  /**
   * Divide the vector by the given value.
   * @param divisor the divisor
   * @return the resulting vector, as if dividing each element by the given divisor
   * @throws IllegalArgumentException if 0 is passed in.
   */
  IVector divide(double divisor);

  IVector divide(IVector divisor);

  /**
   * Return the number of dimensions that the vector has.
   * @return the number of dimensions
   */
  int dimensions();

  /**
   * Calculate the magnitude of the vector
   * @return the magnitude of the vector
   */
  double magnitude();

  /**
   * Get a copy of the ith element of the vector.
   * @param index the index to look at
   * @return the value of the vector at the given dimension.
   */
  double get(int index);

  /**
   * Rotate a 3D Vector using given rotations relative to a 0,0,1 direction
   * @param theta the angle between the 0,0,1 direction  and the new direction
   * @param phi the angle around the 1,0,0 and 0,1,0 plane
   * @return the resulting vector
   * @throws IllegalArgumentException if the Vector is not 3D
   */
  IVector rotate(double theta, double phi);

  /**
   * Map over the vector and apply a transformation.
   * @param func a function that can return a double for making the new Vector
   * @return the new Vector with the function applied to each element
   */
  IVector map(Function<Double, Double> func);

  /**
   * Return the smallest element in the IVector.
   * @return the smallest element.
   */
  double minimum();

  /**
   * Return the greatest element in the IVector.
   * @return the greatest element.
   */
  double maximum();
}
