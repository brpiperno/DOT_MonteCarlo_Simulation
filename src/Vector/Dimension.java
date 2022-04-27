package Vector;

/**
 * Each type of dimension in 3D space, X, Y, Z.
 */
public enum Dimension {
  X("X", 0), Y("Y", 1), Z("Z", 2);
  private final String disp;
  private final int dNum;

  Dimension (String disp, int dnum) {
    this.disp = disp;
    this.dNum = dnum;
  }

  /**
   * Return a string representation of the dimension
   */
  @Override
  public String toString() {
    return disp;
  }

  public int getdNum() {
    return dNum;
  }
}
