import java.awt.*;
import java.awt.image.*;

public class Particle {
	private Animation animation;
	private int lifeSpan;
	private int tick;
	private Point position;
	private Player parent;
	private Point offset;
	
	public Particle(Animation a, int life, Point p, Player par, Point off) {
		animation = a;
		lifeSpan = (int) ((double) life/16.66);
		position = p;
		parent = par;
		offset = off;
		if(parent != null) {
			position = new Point((int) (parent.getPosition().getX() + offset.getX()), (int) (parent.getPosition().getY() + offset.getY()));
		}
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public BufferedImage getFrame() {
		return animation.frame();
	}
	
	public int getTick() {
		return tick;
	}
	
	public int getLifeSpan() {
		return lifeSpan;
	}
	
	public void update() {
		tick++;
		if(parent != null) {
			position = new Point((int) (parent.getPosition().getX() + offset.getX()), (int) (parent.getPosition().getY() + offset.getY()));
		}
		animation.update();
	}
	

}
