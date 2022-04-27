package ParticleTracker.Mesh;

import java.util.ArrayList;

import ParticleTracker.Optics.IOpticalMedia;
import Vector.IVector;

/**
 * Tuple case for storing the bounds of a voxel and the optical media inside it
 */
public class VoxelTuple {
  private final ArrayList<IVector> voxelBounds;
  private final IOpticalMedia opticalInfo;

  public VoxelTuple(ArrayList<IVector> x, IOpticalMedia y) {
    this.voxelBounds = x;
    this.opticalInfo = y;
  }

  public ArrayList<IVector> getBound() {
    return voxelBounds;
  }

  public IOpticalMedia getOptics() {
    return opticalInfo;
  }
}
