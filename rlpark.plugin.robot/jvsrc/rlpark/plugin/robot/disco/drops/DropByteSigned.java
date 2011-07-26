package rlpark.plugin.robot.disco.drops;


import rlpark.plugin.robot.disco.datatype.GrayCodeConverter;
import rlpark.plugin.robot.disco.datatype.LightByteBuffer;
import rlpark.plugin.robot.disco.datatype.Ranged;
import rlpark.plugin.robot.disco.datatype.ScalarReader;
import rltoys.math.GrayCode;
import rltoys.math.ranges.Range;

public class DropByteSigned extends DropData implements Ranged, ScalarReader, GrayCodeConverter {
  private byte value;

  public DropByteSigned(String label) {
    this(label, -1);
  }

  public DropByteSigned(String label, int index) {
    super(label, false, index);
  }

  @Override
  public DropData clone(String label, int index) {
    return new DropByteSigned(label, index);
  }

  @Override
  public int getInt(LightByteBuffer buffer) {
    return buffer.get(index);
  }

  @Override
  public void convert(LightByteBuffer source, LightByteBuffer target) {
    value = (byte) getInt(source);
    value = GrayCode.byteToGrayCode(value);
    putData(target);
  }

  @Override
  public void putData(LightByteBuffer buffer) {
    buffer.put(value);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public Range range() {
    return new Range(Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  @Override
  public double getDouble(LightByteBuffer buffer) {
    return getInt(buffer);
  }
}
