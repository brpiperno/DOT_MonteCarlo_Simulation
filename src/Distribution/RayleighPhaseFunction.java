package Distribution;


import java.util.ArrayList;
import java.util.List;;

import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;

import Equations.ArrayEquation;
import Equations.IEquation;


public class RayleighPhaseFunction implements Distribution {
  public static final double DELTA = Math.PI * 0.0001;



  @Override
  public double generate() {
    return 0;
  }

  @Override
  public double pdf(double value) {
    if (value < 0 || value > Math.PI) {
      throw new IllegalArgumentException(String.format("Must Evaluate between 0 and pi,"
              + " tried evaluating at %.2f", value));
    }
    return .75 * (1 + Math.pow(Math.cos(value),2));
  }

  @Override
  //get the value of the CDF when mapped over 0 to pi
  //this approximates a value using the delta value defined below
  public double cdf(double value) {
    if (value < 0 || value > Math.PI) {
      throw new IllegalArgumentException("Must Evaluate between 0 and pi");
    }

    return CDFArray().get((int) Math.round(value/ DELTA));
  }

  public static double invertCDF(double input) {
    //take the CDFarray and turn it into an arrayEquation, then invert it

    List<Double> xValues = new ArrayList<>();
    for (double i = DELTA; i < Math.PI; i += DELTA) {
      xValues.add(i);
    }
    List<Double> yValues = CDFArray();
    IEquation<Double> arrEq = new ArrayEquation(xValues, yValues);
    //evaluate the arrayEq at the input and output it
    return arrEq.evaluate(input);
  }


  public static ArrayList<Double> CDFArray() {
    ArrayList<Double> values = new ArrayList<>();

    UnivariateIntegrator integrator = new SimpsonIntegrator();
    for (double i = DELTA; i < Math.PI; i += DELTA) {
      values.add(integrator.integrate(1000,
              (a) -> new RayleighPhaseFunction().pdf(a),
              0, i));
    }
    return values;
  }


}
