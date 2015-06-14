public class RGB {
	private int r, g, b;

	public RGB(int red, int green, int blue) {
		r = red;
		g = green;
		b = blue;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public void setR(int red) {
		r = red;
	}

	public void setG(int green) {
		g = green;
	}

	public void setB(int blue) {
		b = blue;
	}

	public String toString() {
		return "("+ r+","+g+","+b+")";
	}
}