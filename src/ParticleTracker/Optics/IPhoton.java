package ParticleTracker.Optics;

import Vector.IVector;

/**
 * Represents a single photon for use in MonteCarlo Optical Simulations
 */
public interface IPhoton {
  /**
   * Perform a Russian Roulette termination of the photon as follows:
   * If the particle is below the threshold weight, randomly determine to terminate or bump
   * Randomness uses a uniform distribution
   * if the random variable is above 1/m, then terminate
   * if below, then multiply the weight of the photon by m
   */
  IPhoton russianRoulette();

  /**
   * Update the photon's direction when it hits a scatterer.
   */
  IPhoton scatter();

  /**
   * Update the photon's direction when it hits a boundary layer. It can either reflect or transmit.
   * Reflection and Transmission are ruled by Snell's Law. This does not account for evanescence.
   * @param normal is the direction of the normal of the boundary.
   * @param n1 is the index of refraction of the medium that the photon is leaving.
   * @param n2 is the index of refraction of the medium that the photon is entering.
   */
  IPhoton reflectOrTransmit(IVector normal, double n1, double n2);

  /**
   * Move the particle in its current direction through the given media
   * @param timeStep the amount of time the photon has time to move in.
   * @param sample the sample that the photon moves through, determining its speed
   */
  IPhoton propagate(double timeStep, IOpticalMedia sample);

  /**
   * Move the particle according to the given displacement vector
   * @param displacement the changes in its XY(Z) position.
   */
  IPhoton propagate(IVector displacement);

  /**
   * Move the particle a given length along its current trajectory
   * @param length the magnitude of displacement the particle should move.
   */
  IPhoton propagate(double length);

  /**
   * Get the current position of the photon
   * @return the photon's current position, in displacement vector form
   */
  IVector getPosition();

  /**
   * Get the current direction of the photon.
   * @return the photon's direction in vector form.
   */
  IVector getDirection();

  /**
   * Get the current weight of the photon.
   * @return the photon's weight.
   */
  double getWeight();

  /**
   * Drop off weight in the photon according to the Beer-Lambert Law
   * @param distance the distance that the photon has travelled.
   * @param muA the absorption coefficient of the media the photon is in
   * @return the weight lost by the photon
   */
  double beerLambert(double distance, double muA);
}
