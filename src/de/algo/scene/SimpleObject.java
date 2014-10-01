package de.algo.scene;

import java.util.*;
import de.algo.util.*;

class SimpleObject {
	protected ArrayList<Pair<Point>> pairs;
	protected Point pivot;

	public SimpleObject() {
		this.pairs = new ArrayList<Pair<Point>>();
		this.pivot = new Point(0, 0, 0);
	}

	public ArrayList<Pair<Point>> getPairs() {
		return pairs;
	}

	public void translate(Point p) {
		pivot.add(p);
		for (Pair<Point> line : pairs) {
			line.first.add(p);
			line.second.add(p);
		}
	}

	public void rotateY(float alpha) {
		//Point oldPivot = pivot.copy();
		//translate(pivot.negate());

		for (Pair<Point> line : pairs) {
			float oldX = line.first.x;
			float oldZ = line.first.z;

			// TODO: refactor
			line.first.x = oldX * (float)Math.cos(alpha)
				     + oldZ * (float)Math.sin(alpha);
			line.first.z = -oldX * (float)Math.sin(alpha)
				      + oldZ * (float)Math.cos(alpha);

			oldX = line.second.x;
			oldZ = line.second.z;

			line.second.x = oldX * (float)Math.cos(alpha)
				      + oldZ * (float)Math.sin(alpha);
			line.second.z = -oldX * (float)Math.sin(alpha)
				       + oldZ * (float)Math.cos(alpha);
		}

		//translate(oldPivot);
	}
}
