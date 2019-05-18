import java.awt.*;
import java.util.*;

public class Paddle {
	private Point position;
	private Point previousPos;
	private Stage stage;
	private Rectangle hitbox;
	private int gamemode;
	private int hitTime;
	private boolean hasHit;
	private boolean willHit = true;

	public Paddle(Point p, int gameType, Stage s) {
		position = p;
		gamemode = gameType;
		stage = s;
		hitbox = new Rectangle(29, 154);
	}

	public Point position() {
		return position;
	}
	
	public void setWillHit(boolean b) {
		willHit = b;
	}

	public void hitBall() {
		if(gamemode == 0) {
			if(willHit) {
				if((int) stage.getBallPos().getX() + 45 > (int) position.getX()-4) {
					hasHit = true;
					stage.getBall().hit((int) (175 - stage.getBall().getDirection() + Math.random()*10));
					stage.getBall().getPos().setLocation(stage.getBall().getPos().getX() - 45, stage.getBall().getPos().getY());
				}
			}
		}else if(gamemode == 1) {
			if((int) stage.getBallPos().getX() + 45 > (int) position.getX()-4) {
				hasHit = true;
				stage.getBall().hit((int) (175 - stage.getBall().getDirection() + Math.random()*10));
				stage.getBall().getPos().setLocation(stage.getBall().getPos().getX() - 45, stage.getBall().getPos().getY());
			}
		}
	}
	
	public void determineWillHit() {
		if(willHit) {
			if(stage.getBall().getVelocity() >= 42) {
				int d = stage.getBall().getDirection();
				while(d >= 360) {
					d = d - 360;
				}
				while(d < 0) {
					d = d + 360;
				}
				if(d < 30 || d > 330) {
					double r = Math.random();
					if(r < .6) {
						willHit = false;
					}else {
						willHit = true;
					}
				}else {
					double r = Math.random();
					if(r < .3) {
						willHit = false;
					}else {
						willHit = true;
					}
				}
			}else {
				willHit = true;
			}
		}
	}

	public void update() {
		previousPos = position;
		if(stage.getBall() != null) {
			if(gamemode == 0) {
				if(willHit) {
					position.setLocation(position.getX(), stage.getBallPos().getY() - hitbox.getHeight()/2);
				}else {
					position.setLocation(position.getX(), 720 - stage.getBallPos().getY() - hitbox.getHeight()/2);
				}
			}else if(gamemode == 1) {
				position.setLocation(position.getX(), stage.getBallPos().getY() - hitbox.getHeight()/2);
			}
			if(hitTime < 8) {
				hitTime++;
			}else {
				hasHit = false;
			}
			if(!hasHit) {
				hitBall();
			}
		}
	}
}
