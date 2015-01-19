package it.ice.tabularasa.gtge;

import it.ice.nttz.core.Nttz;
import it.ice.nttz.core.model.Entity;
import it.ice.nttz.core.model.Property;
import it.ice.nttz.core.model.Relationship;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.object.SpriteGroup;

public class TabulaRasaGTGEGame extends Game {
	private final static int WIDTH = 640, HEIGHT = 480;
	private static TabulaRasaGTGEGame singleton;

	private long elapsedTime;
	private int width, height;
	private Nttz nttz;
	private SpriteGroup playerGroup;

	public static TabulaRasaGTGEGame getInstance() {
		if (singleton == null) {
			singleton = new TabulaRasaGTGEGame();
		}
		return singleton;
	}

	private TabulaRasaGTGEGame() {
		width = WIDTH;
		height = HEIGHT;
		nttz = new Nttz();
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public SpriteGroup getPlayerGroup() {
		return playerGroup;
	}

	public void setPlayerGroup(SpriteGroup playerGroup) {
		this.playerGroup = playerGroup;
	}

	@Override
	public void initResources() {
		nttz.loadEntityDefinitions("file:///home/ice/Progetti/tabularasa/TabulaRasaGTGE/res/entities.xml");

		Entity world = nttz.getWorld();
		world.setForm(Relationship.DEFAULT, new PlayFieldForm());

		Entity player = nttz.summonNew("humanoid");
		player.setName("Hera");
		player.addProperty(new Property("x", WIDTH / 2));
		player.addProperty(new Property("y", HEIGHT / 2));
		player.setForm(
				Relationship.DEFAULT,
				new PlayerSpriteForm(player.getPropertyValue("x"), player
						.getPropertyValue("y")));
	}

	@Override
	public void update(long elapsedTime) {
		this.elapsedTime = elapsedTime;
		nttz.act();
	}

	@Override
	public void render(Graphics2D g) {
		nttz.render(g);
	}

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(TabulaRasaGTGEGame.getInstance(), new Dimension(WIDTH, HEIGHT),
				false);
		game.start();
	}
}
