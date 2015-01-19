package it.ice.tabularasa.gtge;


import it.ice.nttz.core.view.Form;

import java.awt.Color;
import java.awt.Graphics2D;


import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.background.ColorBackground;

public class PlayFieldForm extends Form {
	private PlayField playfield;

	public PlayFieldForm() {
		TabulaRasaGTGEGame game = TabulaRasaGTGEGame.getInstance();
		playfield = new PlayField(new ColorBackground(Color.DARK_GRAY,
				game.getWidth(), game.getHeight()));
		SpriteGroup playerGroup = playfield.addGroup(new SpriteGroup("players"));
		game.setPlayerGroup(playerGroup);
	}

	@Override
	public void update() {
		long elapsedTime = TabulaRasaGTGEGame.getInstance().getElapsedTime();
		playfield.update(elapsedTime);
	}

	@Override
	public void render(Object args) {
		Graphics2D g = (Graphics2D) args;
		playfield.render(g);
	}
}
