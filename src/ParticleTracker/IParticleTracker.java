package ParticleTracker;

import Vector.Dimension;

/**
 * Tacks a number of particles through a domain, predicting its movement,
 * graphing information about it in a steady state.
 */
public interface IParticleTracker {
  /**
   * Run the simulation.
   */
  void Run();

  /**
   * Get a sliced view of the steady state output of the simulation at a given plane
   *
   * @return
   */
  double[][] getSlice(Dimension refPlane, double offset);
}
