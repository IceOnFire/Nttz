package it.ice.tabularasa.slick;


import it.ice.nttz.view.Form;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class PlayFieldForm extends Form {
	private Image land;

	public PlayFieldForm() {
		try {
			land = new Image("res/img/land.jpg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Object args) {
		land.draw(0, 0);
	}
}
