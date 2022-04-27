package ParticleTracker.Optics;

import Distribution.RayleighPhaseFunction;
import Vector.IVector;
import Vector.Vector;

/**
 * Represents an un-polarized photon that acts only like a particle in 3D space.
 */
public class ParticlePhoton implements IPhoton {
  private final double weight;
  private final IVector position;
  private final IVector direction;

  public final static double ROULETTE_M = 100;

  public ParticlePhoton(IVector position, IVector direction, double w) {
    this.position = position;
    this.direction = direction;
    this.weight = w;
  }

  public ParticlePhoton(IVector position, IVector direction) {
    this.position = position;
    this.direction = direction;
    this.weight = 1;
  }

  @Override
  public IPhoton russianRoulette() {
    double rouletteWeight;
    if (Math.random() < 1/ROULETTE_M) {
      rouletteWeight = this.weight * ROULETTE_M;
    }
    else {
      rouletteWeight = 0;
    }
    return new ParticlePhoton(this.getPosition(), this.getDirection(), rouletteWeight);
  }

  public IPhoton scatterHenyeyGreenstein(double g) {
    double scatteringAzimuthal = Math.PI*2*Math.random();
    double scatteringZenith;
    if (g == 0) {
      scatteringZenith = Math.acos(2 * Math.random() - 1);
    }
    else {
      scatteringZenith = Math.acos((1.0/2.0*g)
              *(1 + Math.pow(g,2)
              - ((1-Math.pow(g,2))/ (1 - g + 2*g*Math.random()))));
    }
    return new ParticlePhoton(position,
            direction.rotate(scatteringZenith, scatteringAzimuthal),
            weight);
  }

  public IPhoton scatterRayleigh() {
    double scatteringAzimuthal = Math.PI*2*Math.random();
    double scatteringZenith = RayleighPhaseFunction.invertCDF(Math.random());
    return new ParticlePhoton(position,
            direction.rotate(scatteringZenith, scatteringAzimuthal),
            weight);
  }

  @Override
  public IPhoton scatter() {
    return scatterRayleigh();
  }

  @Override
  public IPhoton reflectOrTransmit(IVector normal, double n1, double n2) {
    //TODO: WRITE THIS LOGIC
    return this;
  }

  @Override
  public IPhoton propagate(double timeStep, IOpticalMedia sample) {
    return propagate(timeStep * sample.getC());
  }

  @Override
  public IPhoton propagate(double length) {
    return new ParticlePhoton(position.add(direction.multiply(length)), direction, weight);
  }

  @Override
  public IPhoton propagate(IVector displacement) {
    return new ParticlePhoton(position.add(displacement), direction, weight);
  }

  @Override
  public Vector getPosition() {
    return new Vector(position);
  }

  @Override
  public Vector getDirection() {
    return new Vector(direction);
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public double beerLambert(double distance, double muA) {
    return Math.min(getWeight(),getWeight() * Math.exp(-1 * muA * distance));
  }
}
