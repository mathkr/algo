package de.algo.scene;

public class Point {
	public float x, y, z;

	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Point p) {
		x += p.x;
		y += p.y;
		z += p.z;
	}

	public void subtract(Point p) {
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}

	public Point negate() {
		return new Point(-x, -y, -z);
	}

	public Point copy() {
		return new Point(x, y, z);
	}
}
