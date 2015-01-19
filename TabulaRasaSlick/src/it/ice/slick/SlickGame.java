package it.ice.slick;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class SlickGame extends BasicGame {
	private Image land;
	private Image plane;
	private float x, y, scale;

	public SlickGame() {
		super("Slick Game");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		land = new Image("res/img/land.jpg");
		plane = new Image("res/img/plane.png");
		x = 400;
		y = 300;
		scale = 1;
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();

		if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
			plane.rotate(-0.2f * delta);
		}

		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			plane.rotate(0.2f * delta);
		}

		if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
			float hip = 0.4f * delta * scale;

			float rotation = plane.getRotation();

			x += hip * Math.sin(Math.toRadians(rotation));
			y -= hip * Math.cos(Math.toRadians(rotation));
		}

		if (input.isKeyDown(Input.KEY_1)) {
			scale -= (scale <= 1f) ? 0 : 0.1f;
			plane.setCenterOfRotation(plane.getWidth() / 2 * scale, plane.getHeight()
					/ 2 * scale);
		}

		if (input.isKeyDown(Input.KEY_2)) {
			scale += (scale >= 5f) ? 0 : 0.1f;
			plane.setCenterOfRotation(plane.getWidth() / 2 * scale, plane.getHeight()
					/ 2 * scale);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		land.draw(0, 0);
		plane.draw(x - plane.getWidth() / 2f * scale, y - plane.getHeight() / 2f * scale, scale);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new SlickGame());
			app.setDisplayMode(800, 600, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
