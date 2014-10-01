package de.algo.scene;

import de.algo.util.*;

public class Cube extends SimpleObject {
	public Cube(Point center, float length) {
		super();

		float half = length / 2.0f;
		this.pivot = center;

		Point p1 = new Point(pivot.x - half, pivot.y + half, pivot.z + half);
		Point p2 = new Point(pivot.x - half, pivot.y + half, pivot.z - half);
		Point p3 = new Point(pivot.x + half, pivot.y + half, pivot.z - half);
		Point p4 = new Point(pivot.x + half, pivot.y + half, pivot.z + half);

		Point p5 = new Point(pivot.x - half, pivot.y - half, pivot.z + half);
		Point p6 = new Point(pivot.x - half, pivot.y - half, pivot.z - half);
		Point p7 = new Point(pivot.x + half, pivot.y - half, pivot.z - half);
		Point p8 = new Point(pivot.x + half, pivot.y - half, pivot.z + half);

		pairs.add(new Pair<Point>(p1, p5));
		pairs.add(new Pair<Point>(p1, p2));
		pairs.add(new Pair<Point>(p2, p6));
		pairs.add(new Pair<Point>(p6, p5));

		pairs.add(new Pair<Point>(p4, p8));
		pairs.add(new Pair<Point>(p4, p3));
		pairs.add(new Pair<Point>(p3, p7));
		pairs.add(new Pair<Point>(p7, p8));

		pairs.add(new Pair<Point>(p5, p8));
		pairs.add(new Pair<Point>(p1, p4));
		pairs.add(new Pair<Point>(p2, p3));
		pairs.add(new Pair<Point>(p6, p7));
	}
}
