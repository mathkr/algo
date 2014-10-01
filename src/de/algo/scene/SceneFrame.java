package de.algo.scene;

import java.awt.*;
import java.awt.event.*;

public class SceneFrame extends Frame {
	private SimpleScene scene;
	private OrthographicCamera camera;

	public SceneFrame() {
		super("3D Scene");

		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setVisible(true);

		init();
		loop();
	}

	private void init() {
		scene = new SimpleScene();
		camera = new OrthographicCamera(getGraphics(), getWidth(), getHeight(), 80f);

		Cube c = new Cube(new Point(0, 0, 0), 1.0f);
		scene.addObject(c);
	}

	private void loop() {
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// TODO: remove unnecessary
			camera.setWidth(getWidth());
			camera.setHeight(getHeight());

			getGraphics().clearRect(0, 0, getWidth(), getHeight());

			camera.render(scene);

			scene.getObjects().get(0).rotateY(1);
		}
	}
}
