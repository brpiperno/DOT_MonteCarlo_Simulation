package ParticleTracker;

import ParticleTracker.Mesh.OpticalVoxelMesh;
import ParticleTracker.Optics.IPhoton;
import ParticleTracker.Optics.IOpticalMedia;
import ParticleTracker.Optics.ParticlePhoton;
import Vector.Vector;
import Vector.Dimension;

/**
 * Simulates the movement of light in a medium of known optical properties from a known source.
 * It tracks light over a defined interval of time.
 * Light is described as a number of packets of photons that scatter, absorb, and react
 * using a MonteCarlo Scheme.
 */
public class MonteCarloVoxelPhotonTracker implements IParticleTracker {
  private final OpticalVoxelMesh sample;
  private final int numPhotons;
  private final double maxTime;
  private final Vector lightStartPos;
  private final Vector lightStartDir;

  private final double[][][] lightAttenuation = new double[20][20][20];


  public final static double INDEX_DIFF_VALUE = 0.0001;

  // Default constructor
  public MonteCarloVoxelPhotonTracker(OpticalVoxelMesh s, Vector position, Vector direction,
                                      int numPhotons, double maxTime) {
    this.sample = s;
    this.numPhotons = numPhotons;
    this.maxTime = maxTime;
    this.lightStartDir = direction;
    this.lightStartPos = position;
  }


  @Override
  public void Run() {
    //set the time
    double currentTime;
    //setup the particle
    IPhoton p;
    //assign the scattering length
    double scatteringLength;

    //Run the simulation for each particle
    for (int n = 0; n < numPhotons; n++) {
      System.out.print(String.format("running photon %d of %d\n", n, numPhotons));
      currentTime = 0;
      //create a single photon using the lightsource at the boundary of Optical Mesh
      p = new ParticlePhoton(lightStartPos, lightStartDir);

      // simulate its motion while its under the time limit
      while (currentTime < maxTime) {
        int[] currentCellKey = sample.getKey(p.getPosition(), p.getDirection());
        IOpticalMedia currentCell = sample.getCellInfo(currentCellKey);


        double cellTravelDist;
        double energyTransfer;
        double previousIndexOfRefraction = sample.homogeneous.getN();
        //double previousIndexOfRefraction = currentCell.getN();

        //determine unitless scattering length
        //scatteringLength = -Math.log(Math.random()) * currentCell.getMuS();
        scatteringLength = -Math.log(Math.random()) * sample.homogeneous.getMuS();
        // iterate while the scattering length is greater than 0
        while (scatteringLength >= 0) {
          //make sure it is in bounds and the photon has weight
          if (!sample.withinBounds(p.getPosition()) || p.getWeight() <= 0) {
            currentTime = maxTime;
            break;
          }
          currentCellKey = sample.getKey(p.getPosition(), p.getDirection());
          currentCell = sample.getCellInfo(currentCellKey);
          cellTravelDist = Math.min(sample.rayCellLength(
                  p.getPosition(), p.getDirection(), currentCellKey),
                  scatteringLength);
          scatteringLength -= cellTravelDist;

          //energyTransfer = p.beerLambert(cellTravelDist, currentCell.getMuA());
          energyTransfer = p.beerLambert(cellTravelDist, sample.homogeneous.getMuA());
          //currentCell.accumulateLight(energyTransfer);





          //ALTERNATIVE: track light attenuation in the lightAttenuation field
          //divide the rounded down position of the photon to a voxel
          int x = (int) Math.round(p.getPosition().get(0) / sample.cellCount.get(0));
          int y = (int) Math.round(p.getPosition().get(1) / sample.cellCount.get(1));
          int z = (int) Math.round(p.getPosition().get(2) / sample.cellCount.get(2));
          lightAttenuation[x][y][z] += energyTransfer;








          p = new ParticlePhoton(p.getPosition(),
                  p.getDirection(),
                  p.getWeight() - energyTransfer);

          p = p.propagate(cellTravelDist);

          //currentTime += cellTravelDist / currentCell.getC();
          currentTime += cellTravelDist / sample.homogeneous.getC();
          // check for russian roulette
          if (p.getWeight() <= .005) { p.russianRoulette(); }

          //handle reflections if crossing a boundary
//          if (previousIndexOfRefraction - currentCell.getN() > INDEX_DIFF_VALUE) {
//            p = p.reflectOrTransmit(null,
//                    previousIndexOfRefraction, currentCell.getN());
//            //TODO: create a normal vector for the boundary, this doesn't work
//          }
        }
        //now scatter the photon (this implementation uses Rayleigh scattering)
        p = p.scatter();
      }
    }
  }

  @Override
  public double[][] getSlice(Dimension refPlane, double offset) {
    //return sample.slice(refPlane, offset);
    Double[][] output = new Double[sample.cellCount.get(0)][sample.cellCount.get(1)];
    double[][] voxelSlice;
    int k = (int) Math.floor(offset / sample.getCellSize(refPlane));
    //TODO:handle other reference planes
    voxelSlice = lightAttenuation[k];
    return voxelSlice;
  }



}
