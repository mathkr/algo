package de.algo.scene;

import java.awt.Graphics;
import de.algo.util.*;

public class OrthographicCamera {
	private Graphics g;

	private int width;
	private int height;

	private final float scale;

	public OrthographicCamera(Graphics g, int width, int height, float scale) {
		this.g = g;
		this.width = width;
		this.height = height;
		this.scale = scale;
	}

	public void render(SimpleScene scene) {
		for (SimpleObject object : scene.getObjects()) {
			renderSimpleObject(object);
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private void renderSimpleObject(SimpleObject object) {
		for (Pair<Point> line : object.getPairs()) {
			int x1 = toCamX(line.first.x);
			int y1 = toCamY(line.first.y);

			int x2 = toCamX(line.second.x);
			int y2 = toCamY(line.second.y);

			g.drawLine(x1, y1, x2, y2);
		}
	}

	private int toCamX(float x) {
		return Math.round(width / 2.0f + (x * scale));
	}

	private int toCamY(float y) {
		return Math.round(height / 2.0f - (y * scale));
	}
}
