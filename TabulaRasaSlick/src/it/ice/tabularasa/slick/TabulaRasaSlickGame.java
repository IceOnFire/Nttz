package it.ice.tabularasa.slick;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Relationship;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TabulaRasaSlickGame extends BasicGame {
	private final static int WIDTH = 640, HEIGHT = 480;
	private static TabulaRasaSlickGame singleton;

	private Input input;
	private int delta;
	private int width, height;
	private Nttz nttz;

	public static TabulaRasaSlickGame getInstance() {
		if (singleton == null) {
			singleton = new TabulaRasaSlickGame();
		}
		return singleton;
	}

	private TabulaRasaSlickGame() {
		super("TabulaRasa");
		width = WIDTH;
		height = HEIGHT;
		nttz = new Nttz();
	}

	public Input getInput() {
		return input;
	}

	public int getDelta() {
		return delta;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.input = container.getInput();
		nttz.loadEntityDefinitions("file:///home/ice/Progetti/tabularasa/TabulaRasaSlick/res/entities.xml");

		Entity world = nttz.getWorld();
		world.setForm(Relationship.DEFAULT, new PlayFieldForm());

		Entity player = nttz.summon("plane");
		player.setName("Player");
		player.getProperty("x").setValue(WIDTH / 2);
		player.getProperty("y").setValue(HEIGHT / 2);
		player.setForm(Relationship.DEFAULT, new PlayerSpriteForm());
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		this.delta = delta;
		nttz.act();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		nttz.render(g);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(
					TabulaRasaSlickGame.getInstance());
			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
