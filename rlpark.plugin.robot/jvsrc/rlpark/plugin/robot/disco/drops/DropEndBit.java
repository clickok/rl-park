package rlpark.plugin.robot.disco.drops;

import rlpark.plugin.robot.disco.datatype.LightByteBuffer;



public class DropEndBit extends DropData {
  public DropEndBit(String label) {
    super(label, true);
  }

  @Override
  public DropData clone(String label, int index) {
    return new DropEndBit(label);
  }

  @Override
  public void putData(LightByteBuffer buffer) {
  }

  @Override
  public int size() {
    return ByteSize;
  }

}
