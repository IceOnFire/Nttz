package it.ice.tabularasa.slick;

import it.ice.nttz.model.Entity;
import it.ice.nttz.view.Form;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class PlayerSpriteForm extends Form {
	private Image plane;

	public PlayerSpriteForm() {
		try {
			plane = new Image("res/img/plane.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		TabulaRasaSlickGame game = TabulaRasaSlickGame.getInstance();
		Input input = game.getInput();
		int delta = game.getDelta();

		Entity entity = getEntity();
		float x = entity.getPropertyValue("x");
		float y = entity.getPropertyValue("y");
		float rotation = entity.getPropertyValue("rotation");
		float scale = entity.getPropertyValue("scale");
		float speed = entity.getPropertyValue("speed");

		float angle = 0;
		float distance = 0;
		if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
			angle = -0.2f * delta;
		}

		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			angle = 0.2f * delta;
		}

		if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
			distance = speed * delta * scale;
		}

		if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
			distance = -speed * delta * scale;
		}

		if (input.isKeyDown(Input.KEY_1)) {
			scale -= (scale <= 1f) ? 0 : 0.1f;
		}

		if (input.isKeyDown(Input.KEY_2)) {
			scale += (scale >= 4f) ? 0 : 0.1f;
		}

		rotation += angle;
		rotation %= 360;
		x += distance * Math.sin(Math.toRadians(rotation));
		y -= distance * Math.cos(Math.toRadians(rotation));
		entity.getProperty("x").setValue(x);
		entity.getProperty("y").setValue(y);
		entity.getProperty("rotation").setValue(rotation);
		entity.getProperty("scale").setValue(scale);

		plane.setCenterOfRotation(plane.getWidth() / 2 * scale, plane.getHeight()
				/ 2 * scale);
		plane.rotate(angle);
	}

	@Override
	public void render(Object args) {
		Entity entity = getEntity();
		float x = entity.getPropertyValue("x");
		float y = entity.getPropertyValue("y");
		float scale = entity.getPropertyValue("scale");
		plane.draw(x - plane.getWidth() / 2f * scale, y - plane.getHeight() / 2f
				* scale, scale);
	}
}
