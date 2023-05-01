package cn.taskeren.gtnn.util;

public class ToStringHelper {

	private ToStringHelper() {
		throw new UnsupportedOperationException();
	}

	public static String vecToString(int x, int y, int z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

	public static String vecToString(double x, double y, double z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

	public static String vecToString(float x, float y, float z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

}
