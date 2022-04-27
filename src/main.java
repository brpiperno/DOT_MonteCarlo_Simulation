import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ParticleTracker.IParticleTracker;
import ParticleTracker.Mesh.OpticalVoxelMesh;
import ParticleTracker.MonteCarloVoxelPhotonTracker;
import ParticleTracker.Optics.IOpticalMedia;
import ParticleTracker.Optics.OpticalMedia;
import Vector.Vector;
import Vector.Dimension;

public class main {

  public static void main(String[] args) throws IOException {
    //create the media
    IOpticalMedia i = new OpticalMedia(0.001, .001, 1.33);

    OpticalVoxelMesh o = new OpticalVoxelMesh(i,
            new Vector(new ArrayList<Double>(Arrays.asList(0.000, 0.00, 0.00))),
            new Vector(new ArrayList<Double>(Arrays.asList(0.02, 0.02, 0.02))),
            20, 20, 20);

    IParticleTracker m = new MonteCarloVoxelPhotonTracker(o,
            new Vector(new ArrayList<>(Arrays.asList(.0105, .0105, 0.00))),
            new Vector(new ArrayList<>(Arrays.asList(0.0, 0.0, 1.0))),
            (int) 500,
            1.00E-9);

    m.Run();

    double[][] slice = m.getSlice(Dimension.X, .011);
    FileWriter writer = new FileWriter("x=11.csv");
    for (double[] d: slice) {
      for (double dd: d) {
        writer.write(dd + ",");
      }
      writer.write("\n");
    }
    writer.close();
    double[][] slice2 = m.getSlice(Dimension.Z, 5);
    FileWriter writer2 = new FileWriter("z=5.csv");
    for (double[] d: slice2) {
      for (Double dd: d) {
        writer2.write(dd + System.lineSeparator());
      }
      writer2.write("\n");
    }
    writer2.close();
  }
}
