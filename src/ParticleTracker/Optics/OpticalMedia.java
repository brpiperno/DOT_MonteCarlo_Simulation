package ParticleTracker.Optics;

import java.util.ArrayList;
import Vector.Vector;

import Equations.IEquation;

public class OpticalMedia implements IOpticalMedia {
  private ArrayList<Double> s;

  //PUBLIC PARAMETERS
  public final double muA;
  public final double muS;
  public final double indexOfRefraction;

  //PRIVATE PARAMETERS
  private double light = 0; //the number of photons that are absorbed in the media


  //Default Constructor
  public OpticalMedia(double muA, double muS, double n) {
    this.muA = muA;
    this.muS = muS;
    this.indexOfRefraction = n;
    this.s = new ArrayList<>(3);
    for (int i = 0; i < s.size(); i++) {
      this.s.set(i, 0.00);
    }
  }


  @Override
  public double getN() {
    return this.indexOfRefraction;
  }

  @Override
  public Vector getS() {
    return null;
  }

  @Override
  public double getMuA() {
    return this.muA;
  }

  @Override
  public double getMuS() {
    return this.muS;
  }

  @Override
  public double getC() {
    return 3.00E8 / getN();
  }

  @Override
  public void accumulateLight(double energy) {
    light += energy;
  }

  @Override
  public double getLight() { return this.light; }
}
