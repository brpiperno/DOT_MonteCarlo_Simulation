package ParticleTracker.Mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ParticleTracker.Optics.IOpticalMedia;
import Vector.IVector;
import Vector.Vector;
import Vector.Dimension;

public class OpticalVoxelMesh {
  private final IVector minBound;
  private final IVector maxBound;
  public final List<Integer> cellCount = new ArrayList<>();
  private IOpticalMedia[][][] voxels;


  //Testing parameter
  public final IOpticalMedia homogeneous;



  //rewrite Default Constructor
  public OpticalVoxelMesh(IOpticalMedia homogeneousMedia,
                          IVector minBoundingBox, IVector maxBoundingBox,
                          int cellCountX, int cellCountY, int cellCountZ) {
    cellCount.add(cellCountX);
    cellCount.add(cellCountY);
    cellCount.add(cellCountZ);
    this.minBound = minBoundingBox;
    this.maxBound = maxBoundingBox;
    this.homogeneous = homogeneousMedia;

    //set the value of each cell in voxels to the homogeneousMedia
    this.voxels = new IOpticalMedia[cellCountX][cellCountY][cellCountZ];
  }

  /**
   * set the optical properties of a cell to the given local value.
   * @param local the optical properties to set the specific cell to.
   *             This resets the light attenuation in it
   * @param key an identifier for the cell to be affected
   */
  public void setVoxel(IOpticalMedia local, int[] key) {
    this.voxels [key[0]][key[1]][key[2]] = local;
  }

  /**
   * return the identifiers of a specific cell of a matrix from a point with direction
   * @param position a point in space that is within a cell
   * @param direction the direction the point is going into
   * @return the x,y,z indices that can uniquely identify the cell.
   * @throws IllegalArgumentException if the position and direction point outside of the mesh
   */
  public int[] getKey(IVector position, IVector direction) {
    //make sure that the position and direction both have 3 dimensions
    if (position.dimensions() != 3 || direction.dimensions() != 3) {
      throw new IllegalArgumentException("Position and Direction must both be 3D");
    }
    if (direction.magnitude() != 1) {
      throw new IllegalArgumentException("Direction must be a unit vector");
    }
    //make sure that the position is within the bounds
    if (!withinBounds(position)) {
      throw new IllegalArgumentException("Position out of bounds of mesh");
    }

    //set the current answer to some cell that is at least close
    int[] key = new int[position.dimensions()];
    //round the position to a lower value that aligns with the number of voxels along each dimension
    for (int i = 0; i < position.dimensions(); i++) {
      key[i] = (int) Math.floor(position.get(i)
              / (maxBound.get(i) - minBound.get(i) / cellCount.get(i)));
    }

    //iterate until some number N same answers are met
    int consecutiveCorrect = 0;
    double delta = 1.00;
    IVector positionPrime;
    int[] currentKey = key.clone();
    while (consecutiveCorrect < 30) { //some arbitrary value = numCorrectKeys*dimensions
      //propogate the position by the direction according to magnitude delta
      positionPrime = position.add(direction.multiply(delta));
      for (int i = 0; i < positionPrime.dimensions(); i++) {
        currentKey[i] = (int) Math.floor(positionPrime.get(i)
                / (maxBound.get(i) - minBound.get(i) / cellCount.get(i)));
        if (currentKey[i] == key[i]) {
          consecutiveCorrect++;
        }
        else {
          //if it isn't, change the answer
          key = currentKey;
          consecutiveCorrect = 0;
        }
      }

      delta *= .1;
    }
    //make sure that the final key is within the bounds of the mesh
    for (int i = 0; i < key.length; i++) {
      if (key[i] > voxels[i].length) {
        throw new IllegalArgumentException("Position is on boundary of mesh, direction points out");
      }
    }

    if (key == null) {
      throw new IllegalStateException("key is null");
    }

    //output the final key
    return key;
  }

  public boolean withinBounds(IVector position) {
    if (position.dimensions() != minBound.dimensions()) {
      throw new IllegalStateException("Vector must be of same dimensionality");
    }
    boolean withinSoFar = true;
    for (int d = 0; d < position.dimensions(); d++) {
      withinSoFar = withinSoFar
              && position.get(d) >= minBound.get(d)
              && position.get(d) <= maxBound.get(d);
    }
    return withinSoFar;
  }


  public IOpticalMedia getCellInfo(int[] cellID) {
    return voxels[cellID[0]] [cellID[1]] [cellID[2]];
  }

  public double rayCellLength(IVector position, IVector direction, int[] key) {
    if (!withinCell(position, key)) {
      return 0;
    }
    Function<Double, Double> greaterThanZero = (Double x)-> {
      if (x > 0) return 1.00; else return 0.00;};

    return position.map(Math::floor).subtract(position)
            .add(direction.map(greaterThanZero)).divide(direction).minimum();
  }

  public boolean withinCell(IVector position, int[] key) {
    if (!withinBounds(position)) {
      return false;
    }
    ArrayList<Double> min = new ArrayList<>();
    ArrayList<Double> max = new ArrayList<>();
    for (int i = 0; i < position.dimensions(); i++) {
      min.add(key[i]*getCellSize(Dimension.values()[i]));
      max.add((key[i]+1)*getCellSize(Dimension.values()[i]));
    }
    return checkBounds(new Vector(min), new Vector(max), position);
  }

  //PRIVATE METHODS

  private static boolean checkBounds(IVector min, IVector max, IVector position) {
    if (position.dimensions() != min.dimensions()) {
      throw new IllegalStateException("Vector must be of same dimensionality");
    }
    boolean withinSoFar = true;
    for (int d = 0; d < position.dimensions(); d++) {
      withinSoFar = withinSoFar
              && position.get(d) >= min.get(d)
              && position.get(d) <= max.get(d);
    }
    return withinSoFar;
  }

  public double getCellSize(Dimension d) {
    int v = d.getdNum();
    return (maxBound.get(v) - minBound.get(v) / cellCount.get(v));
  }

  public Double[][] slice(Dimension d, double offset) {
    Double[][] output = new Double[cellCount.get(0)][cellCount.get(1)];
    IOpticalMedia[][] voxelSlice;
    int k = (int) Math.floor(offset / getCellSize(d));
    voxelSlice = voxels[k];
    for (int i = 0; i < voxelSlice.length; i++) {
      for (int j = 0 ; j < voxelSlice[i].length; j++) {
        output[i][j] = voxelSlice[i][j].getLight();
      }
    }
    return output;
  }
}
