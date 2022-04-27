package ParticleTracker.Optics;

import Equations.IEquation;
import Vector.Vector;


/**
 * Represents some sort of physical object with known optical properties.
 */
public interface IOpticalMedia {

  /**
   * Get the Index of Refraction of the Media
   * @return the index of refraction
   */
  double getN();

  /**
   * Return in vector form the energy passing through the media
   * @return the energy vector (not a unit vector)
   */
  Vector getS();

  /**
   * Get the absorption coefficient of the media
   * @return the absorption coefficient
   */
  double getMuA();

  /**
   * Get the scattering coefficient of the media
   * @return the absorption coefficient
   */
  double getMuS();

  /**
   * Get the speed of light within the media
   * @return the speed of light
   */
  double getC();

  /**
   * Increase the photon energy in the given media
   * @param energy
   */
  void accumulateLight(double energy);


  /**
   * Get the amount of light that has accumulated in this media.
   * @return the weight dropped off in the media
   */
  double getLight();
}
