package de.algo.scene;

import java.util.*;

class SimpleScene {
	private ArrayList<SimpleObject> objects;

	public SimpleScene() {
		this.objects = new ArrayList<SimpleObject>();
	}

	public ArrayList<SimpleObject> getObjects() {
		return objects;
	}

	public void addObject(SimpleObject object) {
		objects.add(object);
	}
}
