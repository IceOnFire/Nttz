package it.ice.gtge;

import java.awt.image.BufferedImage;

import com.golden.gamedev.object.sprite.AdvanceSprite;
import com.golden.gamedev.util.ImageUtil;

public class PlayerSprite extends AdvanceSprite {
	private static final long serialVersionUID = 1L;

	public final static int STILL = 0, WALKING = 1;
	public final static int LEFT = -1, RIGHT = 1;
	public final static double WALKING_SPEED = 0.625;
	
	private BufferedImage[] images;
	private BufferedImage[] flippedImages;

	public PlayerSprite(BufferedImage[] images) {
		super(images);
		this.images = images;
		flippedImages = new BufferedImage[images.length];
		for (int i=0; i<images.length; i++) {
			flippedImages[i] = ImageUtil.flip(images[i]);
		}
	}

	@Override
	protected void animationChanged(int oldStat, int oldDir, int status,
			int direction) {
		switch (direction) {
		case LEFT:
			setImages(images);
			break;
		case RIGHT:
			setImages(flippedImages);
			break;
		}

		switch (status) {
		case STILL:
			setAnimationFrame(0, 0);
			setAnimate(false);
			break;
		case WALKING:
			if (oldStat != WALKING) {
				getAnimationTimer().setDelay(100);
				setAnimationFrame(1, 4);
				setAnimate(true);
				setLoopAnim(true);
			}
			break;
		default:
			break;
		}
	}
}
