package Vector;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Represents a direction vector in 3D space
 */
public class Vector implements IVector{
  private final ArrayList<Double> values;
  public static final double DIFF_VALUE = 0.0005;

  // Default Constructor
  public Vector(ArrayList<Double> v) {
    if (v.size() == 0) {
      throw new IllegalArgumentException("must have a size greater than 0");
    }
    this.values = (ArrayList<Double>) v.clone();
  }

  // Create a copy of a given vector
  public Vector(IVector v) {
    if (v.dimensions() == 0) {
      throw new IllegalArgumentException("size cannot be zero");
    }
    this.values = new ArrayList<>();
    for (int i = 0; i < v.dimensions(); i++) {
      this.values.add(v.get(i));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Vector)) {
      return false;
    }
    Vector casted = (Vector) o;
    if (casted.dimensions() != this.dimensions()) {
      return false;
    }
    boolean bothEqual = true;
    for (int i = 0; i < casted.dimensions(); i++) {
      bothEqual = bothEqual && this.get(i) - casted.get(i) <= DIFF_VALUE;
    }
    return bothEqual;
  }


  @Override
  public IVector add(IVector a) {
    validateVectorAddSub(a);
    ArrayList<Double> output = new ArrayList<>();
    for (int i = 0; i < a.dimensions(); i++) {
      output.add(this.values.get(i) + a.get(i));
    }
    return new Vector(output);
  }

  @Override
  public IVector subtract(IVector a) {
    return this.add(a.multiply(-1));
  }

  @Override
  public IVector multiply(double multiplier) {
    ArrayList<Double> output = new ArrayList<>(this.dimensions());
    for (int i = 0; i < this.dimensions(); i++) {
      output.add(values.get(i) * multiplier);
    }
    return new Vector(output);
  }

  @Override
  public IVector divide(double divisor) {
    if (divisor == 0) {
      throw new IllegalArgumentException("Cannot divide by zero");
    }
    return this.multiply(1/divisor);
  }

  @Override
  public IVector divide(IVector divisor) {
    if (divisor.dimensions() != this.dimensions()) {
      throw new IllegalArgumentException("Both Vectors must have same shape");
    }
    ArrayList<Double> temp = new ArrayList<>(this.dimensions());
    for (int i = 0; i < this.dimensions(); i++) {
      temp.add(this.get(i) / divisor.get(i));
    }
    return new Vector(temp);
  }

  @Override
  public int dimensions() {
    return values.size();
  }

  @Override
  public double magnitude() {
    double mag = 0;
    for (int i = 0; i < this.dimensions(); i++) {
      mag += Math.pow(get(i),2);
    }
    return Math.sqrt(mag);
  }

  @Override
  public double get(int index) {
    if (index < 0 || index > this.dimensions()) {
      throw new IllegalArgumentException("index out of bounds");
    }
    else {
      return values.get(index);
    }
  }

  @Override
  public IVector rotate(double theta, double phi) {
    ArrayList<Double> newVector = new ArrayList<>();
    if (this.get(2) != this.magnitude())  {
      double B = 1 - Math.pow(this.get(2), 2);
      double A = Math.sin(theta) / Math.sqrt(B);
      newVector.add(A * (this.get(0) * this.get(2) * Math.cos(phi) - this.get(1) * Math.sin(phi))
              + this.get(0) * Math.cos(theta));
      newVector.add(A * (this.get(1) * this.get(2) * Math.cos(phi)
              + this.get(0) * Math.sin(phi))
              + this.get(1) * Math.cos(theta));
      newVector.add(-A * B * Math.cos(phi));
    }
    else {
      newVector.add(Math.sin(theta) * Math.cos(phi));
      newVector.add(Math.sin(theta) * Math.sin(phi));
      newVector.add(Math.cos(theta) * (this.get(2)/Math.abs(this.get(2))));
    }
    return new Vector(newVector);
  }


  @Override
  public IVector map(Function<Double, Double> func) {
    ArrayList<Double> temp = new ArrayList<>();
    for (int j = 0; j < this.dimensions(); j++) {
      temp.add(func.apply(this.get(j)));
    }
    return new Vector(temp);
  }

  @Override
  public double minimum() {
    double min = this.get(0);
    for (int d = 0; d < this.dimensions(); d++) {
      min = Math.min(min, this.get(d));
    }
    return min;
  }

  @Override
  public double maximum() {
    double max = this.get(0);
    for (int d = 0; d < this.dimensions(); d++) {
      max = Math.max(max, this.get(d));
    }
    return max;
  }


  private void validateVectorAddSub(IVector v) {
    if (this.dimensions() != v.dimensions()) {
      throw new IllegalArgumentException(
              String.format("Vectors must be of the same size %d, %d",
                      this.dimensions(), v.dimensions()));
    }
  }
}
