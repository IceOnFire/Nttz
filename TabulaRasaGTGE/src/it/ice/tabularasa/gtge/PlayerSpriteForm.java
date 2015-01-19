package it.ice.tabularasa.gtge;

import it.ice.gtge.PlayerSprite;
import it.ice.nttz.core.view.Form;

import java.awt.event.KeyEvent;


import com.golden.gamedev.object.sprite.AdvanceSprite;

public class PlayerSpriteForm extends Form {
	private AdvanceSprite playerSprite;

	public PlayerSpriteForm(float x, float y) {
		TabulaRasaGTGEGame game = TabulaRasaGTGEGame.getInstance();
		playerSprite = new PlayerSprite(game.getImages("res/img/player.png", 5, 1));
		playerSprite.setLocation(x - playerSprite.getWidth() / 2,
				y - playerSprite.getHeight());
		playerSprite.setAnimation(PlayerSprite.STILL, PlayerSprite.RIGHT);
		game.getPlayerGroup().add(playerSprite);
	}

	@Override
	public void update() {
		TabulaRasaGTGEGame game = TabulaRasaGTGEGame.getInstance();
		if (game.keyDown(KeyEvent.VK_LEFT)) {
			playerSprite.setAnimation(PlayerSprite.WALKING, PlayerSprite.LEFT);
			playerSprite.moveX(-PlayerSprite.WALKING_SPEED);
		} else if (game.keyDown(KeyEvent.VK_RIGHT)) {
			playerSprite.setAnimation(PlayerSprite.WALKING, PlayerSprite.RIGHT);
			playerSprite.moveX(PlayerSprite.WALKING_SPEED);
		} else {
			playerSprite.setStatus(PlayerSprite.STILL);
		}
	}

	@Override
	public void render(Object args) {
		// TODO Auto-generated method stub

	}
}
