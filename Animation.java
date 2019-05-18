import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Animation {
	
	private Rectangle offset;
	private int frameNumber;
	private int frameTime;
	private int tick;
	private BufferedImage[] frames;
	private int[] frameOrder;
	
	public Animation(BufferedImage[] images, int time, int[] order, Rectangle off) {
		tick = 0;
		frames = images;
		frameTime =(int) ((double) time/16.66); //AUTOMATICALLY CONVERTS MILISECONDS TO 1/60TH OF A SECOND
		if(order == null) {
			frameOrder = new int[images.length];
			for(int i = 0; i < images.length; i++) {
				frameOrder[i] = i;
			}
		}else {
			frameOrder = order;
		}
		if(off == null) {
			offset = new Rectangle(0,0);
		}else {
			offset = off;
		}
	}
	
	public BufferedImage frame() {
		return frames[frameOrder[frameNumber]];
	}
	
	public Rectangle offset() {
		return offset;
	}
	
	public void reset() {
		tick = 0;
		frameNumber = 0;
	}
	
	public void update() {
		if(frameTime > 0) {
			tick++;
			if(tick >= frameTime) {
				tick = 0;
				if(frameNumber + 1 == frameOrder.length) {
					frameNumber = 0;
				}else {
					frameNumber++;
				}
			}
		}
	}
			

}
