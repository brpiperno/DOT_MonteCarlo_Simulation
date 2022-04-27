import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import Vector.IVector;
import Vector.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the functionality of the Vector Class
 */

public class VectorTest {

  IVector vector2D = new Vector(new ArrayList<>(Arrays.asList(0.00, 0.00)));
  IVector vector3D = new Vector(new ArrayList<>(Arrays.asList(0.00, 0.00, 0.00)));
  IVector vector1 = new Vector(new ArrayList<>(Arrays.asList(1.0, 1.0, 1.0)));
  IVector vector2 = new Vector(new ArrayList<>(Arrays.asList(2.0, 2.0, 2.0)));
  IVector vector3 = new Vector(new ArrayList<>(Arrays.asList(3.0, 3.0, 3.0)));
  IVector vectorCopy = new Vector(new ArrayList<>(Arrays.asList(3.00, 3.00, 3.00)));

  //Test the constructor
  @Test
  public void testConstructor() {
    Vector validVector = new Vector(new ArrayList<>(Arrays.asList(-3.5, 0.00, 100000.090)));
    assertEquals(3, validVector.dimensions());
    try {
      Vector invalidVector = new Vector(new ArrayList<Double>());
      fail();
    }
    catch (Exception e){
      //an exception should be expected, do nothing
    }
  }

  //Test the Vector Input constructor
  //Make sure that it makes a copy rather than getting a reference to the given one
  @Test
  public void testCopyVector() {
    Vector copy = new Vector(vector1);
    assertTrue(copy.equals(vector1));
    ArrayList<Double> newVal = new ArrayList<>();
    newVal.add(3.00);
    newVal.add(2.00);
    newVal.add(1.00);
    copy = new Vector(newVal);
    assertFalse(copy.equals(vector1));
  }

  //Test the add, subtract, multiply, and divide methods
  @Test
  public void testAddSubtractMultiplyDivide() {
    assertEquals(vector3, vector2.add(vector1));
    assertEquals(vector1, vector3.subtract(vector2));
    assertEquals(vector3, vector1.multiply(3));
    assertEquals(vector1, vector2.divide(2));
    try {
      vector3.divide(0);
      fail();
    }
    catch (Exception e) {
      //divide by zero exception expected
    }
    try {
      vector2D.add(vector3D);
      fail();
    }
    catch (Exception e) {
      //adding vectors of different dimensions should throw an error
    }
  }

  //Test the dimensions method
  @Test
  public void testDimensions() {
    assertEquals(2, vector2D.dimensions());
    assertEquals(3, vector3D.dimensions());
  }

  //Test the magnitude method
  @Test
  public void testMagnitude() {
    assertEquals(0, vector2D.magnitude(), Vector.DIFF_VALUE);
    assertEquals(Math.sqrt(3), vector1.magnitude(), Vector.DIFF_VALUE);
  }

  //Test the get method
  @Test(expected = IllegalArgumentException.class)
  public void testGet() {
    vector2D.get(2);
  }

  @Test
  public void testGet2() {
    assertEquals(1, vector1.get(0), Vector.DIFF_VALUE);
    assertEquals(2, vector2.get(1), Vector.DIFF_VALUE);
    assertEquals(3, vector3.get(2), Vector.DIFF_VALUE);
  }

  @Test
  public void testEquals() {
    assertTrue(vector3.equals(vectorCopy));
    assertFalse(vector3.equals(vector2));
    assertFalse(vector3.equals(vector2D));
    assertFalse(vector3.equals(new ArrayList<Double>()));
  }
}
