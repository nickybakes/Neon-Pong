import java.awt.*;

public class CardWall {
	private Point position;
	private Rectangle hitbox;
	private Stage stage;
	private boolean used = false;
	
	public CardWall(Point p, Stage s) {
		position = new Point(p);
		hitbox = new Rectangle(44, 312);
		stage = s;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public boolean getUsed() {
		return used;
	}
	
	public void update() {
		if(stage.getBall() != null) {
			int d = stage.getBall().getDirection();
			while(d >= 360) {
				d = d - 360;
			}
			while(d < 0) {
				d = d + 360;
			}
			if(d > 90 && d < 270) {
				if(stage.getBallPos().getX() > position.getX() && stage.getBallPos().getX() < position.getX() + hitbox.getWidth() + 3) {
					if(stage.getBallPos().getY() > position.getY()-55 && stage.getBallPos().getY() < position.getY() + hitbox.getHeight()+20) {
						stage.upRally();
						stage.getBall().hit((int) (-30-Math.random()*30));
						stage.getPaddle().determineWillHit();
						used = true;
					}
				}
			}
		}
	}

}
