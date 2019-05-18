import java.awt.*;

public class Ball {
	private Point position;
	private Stage stage;
	private StageTitle stageTitle;
	boolean titleScreenBall;
	private Rectangle hitBox;
	private int velocity;
	private int direction;
	
	public Ball(Point startPosition, StageTitle st, int v, int d, boolean title) {
		position = startPosition;
		stageTitle = st;
		hitBox = new Rectangle(44,44);
		velocity = v; direction = d;
		titleScreenBall = title;
	}
	
	public Ball(Point startPosition, Stage st, int v, int d, boolean title) {
		position = startPosition;
		stage = st;
		hitBox = new Rectangle(44,44);
		velocity = v; direction = d;
		titleScreenBall = title;
	}
	
	public Rectangle getHitBox() {
		return hitBox;
	}
	
	public Point getPos() {
		return position;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public void hit(int d) {
		direction = d;
		if(velocity < 46) {
			velocity = velocity + 2;
		}
	}
	
	public void update() {
		if(titleScreenBall) {
			if(velocity < 11) {
				velocity++;
			}
			if(stageTitle.getPoint((int) position.getX()-1, (int) position.getY() + 22) == 1) {
				direction = 180 - direction;
			}
			if(stageTitle.getPoint((int) position.getX()+45, (int) position.getY() + 22) == 1) {
				direction = 180 - direction;
			}
			if(stageTitle.getPoint((int) position.getX() + 22, (int) position.getY()-1) == 1) {
				direction = -1*direction;
			}
			if(stageTitle.getPoint((int) position.getX() + 22, (int) position.getY()+45) == 1) {
				direction = -1*direction;
			}
		}else {
			if(stage.getPoint((int) position.getX() + 22, (int) position.getY()-1) == 1) {
				direction = -1*direction;
			}
			if(stage.getPoint((int) position.getX() + 22, (int) position.getY()+45) == 1) {
				direction = -1*direction;
			}
			if((int) position.getY() <= stage.getTop()) {
				position.setLocation(position.getX(), stage.getTop()+5);
			}
			if((int) position.getY() + 50 >= stage.getBottom()) {
				position.setLocation(position.getX(), stage.getBottom()-50);
			}
		}
		if(position.getX() > 1900) {
			stage.score(0);
		}else if(position.getX() < 10) {
			stage.score(1);
		}
		position.setLocation(position.getX() + Math.cos(Math.toRadians(direction))*velocity, position.getY() - Math.sin(Math.toRadians(direction))*velocity);
	}

}
