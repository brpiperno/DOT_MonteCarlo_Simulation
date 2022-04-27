package Equations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an equation that is stored in two arrays of inputs and outputs.
 * output values that don't align directly to an input are interpolated between close outputs.
 */
public class ArrayEquation implements IEquation<Double> {
  private final List<Double> inputs;
  private final List<Double> outputs;

  public ArrayEquation(List<Double> in, List<Double> out) {
    if (in.size() != out.size()) {
      throw new IllegalArgumentException(String.format(
              "in and out must have same size in: %d out: %d", in.size(), out.size()));
    }
    this.inputs = in;
    this.outputs = out;
  }

  //reflect the equation over the line y=x
  public ArrayEquation invert(ArrayEquation a) {
    ArrayList<Double> tempIn = new ArrayList<>();
    ArrayList<Double> tempOut = new ArrayList<>();
    tempIn.addAll(outputs);
    tempOut.addAll(inputs);

    return new ArrayEquation(tempIn, tempOut);


  }

  @Override
  public Double evaluate(Double input) {
    int indexFirst = 0;
    int indexSecond = 1;

    //check if the input is greater than or less than the stored inputs
    if (input < inputs.get(0)) {
      //do nothing
    }
    else if (input > inputs.get(inputs.size() - 1)) {
      indexFirst = inputs.size() - 2;
      indexSecond = inputs.size() - 1;
    }
    else {
      //get the index of the closest input
      for (int i = 0; i < inputs.size(); i++) {
        if (Math.abs(inputs.get(i) - input)
                < Math.abs(inputs.get(indexFirst) - input)) {
          indexFirst = i;
        }
      }
      //now find the second index
      if (input >= inputs.get(indexFirst)) {
        indexSecond = indexFirst + 1;
      }
      else {
        indexSecond = indexFirst - 1;
      }
    }

    //now use the two points to linearly evaluate the input
    double slope = outputs.get(indexSecond) - outputs.get(indexFirst)
            / inputs.get(indexSecond) - inputs.get(indexFirst);
    return outputs.get(indexFirst) + slope*(inputs.get(indexFirst)-input);
  }
}
