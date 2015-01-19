package it.ice.gtge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.background.ColorBackground;
import com.golden.gamedev.object.sprite.AdvanceSprite;

public class GTGEGame extends Game {
	private final static int WIDTH = 640, HEIGHT = 480;
	private PlayField playfield;
	private SpriteGroup playerGroup;
	private AdvanceSprite playerSprite;

	@Override
	public void initResources() {
		playfield = new PlayField(new ColorBackground(Color.DARK_GRAY, WIDTH,
				HEIGHT));
		playerGroup = playfield.addGroup(new SpriteGroup("players"));

		playerSprite = new PlayerSprite(getImages("res/img/player.png", 5, 1));
		playerSprite.setLocation(WIDTH / 2 - playerSprite.getWidth() / 2, HEIGHT
				/ 2 - playerSprite.getHeight());
		playerSprite.setAnimation(PlayerSprite.STILL, PlayerSprite.RIGHT);
		playerGroup.add(playerSprite);
	}

	@Override
	public void update(long elapsedTime) {
		playfield.update(elapsedTime);
		if (keyDown(KeyEvent.VK_LEFT)) {
			playerSprite.setAnimation(PlayerSprite.WALKING, PlayerSprite.LEFT);
			playerSprite.moveX(-PlayerSprite.WALKING_SPEED);
		} else if (keyDown(KeyEvent.VK_RIGHT)) {
			playerSprite.setAnimation(PlayerSprite.WALKING, PlayerSprite.RIGHT);
			playerSprite.moveX(PlayerSprite.WALKING_SPEED);
		} else {
			playerSprite.setStatus(PlayerSprite.STILL);
		}
	}

	@Override
	public void render(Graphics2D g) {
		playfield.render(g);
	}

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new GTGEGame(), new Dimension(WIDTH, HEIGHT), false);
		game.start();
	}
}
