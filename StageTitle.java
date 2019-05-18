import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class StageTitle implements Runnable{
	private int[][] collisionGrid;
	private BufferedImage collisionData;
	private BufferedImage layer0;
	private BufferedImage layer1;
	private BufferedImage layer2;
	private BufferedImage background;
	
	private Ball ball;
	private int cameraPosX;
	
	public StageTitle(BufferedImage collisionData_, BufferedImage layer0_, BufferedImage layer1_, BufferedImage layer2_, BufferedImage background_) {
		collisionData = collisionData_;layer0 = layer0_;layer1 = layer1_;
		layer2 = layer2_;background = background_;
		
		collisionGrid = new int[collisionData.getWidth()][collisionData.getHeight()];
		int solids = 0, halfsolids = 0, barriers = 0, scoreZones = 0, air = 0; 
		for(int x = 0; x < collisionData.getWidth(); x++) {
			for(int y = 0; y < collisionData.getHeight(); y++) {
				Color color = new Color(collisionData.getRGB(x, y), true);
				if(color.getRed()==0 && color.getGreen()==0 && color.getBlue()==0) {
					collisionGrid[x][y] = 1;
					solids++;
				}else if(color.getRed()==128 && color.getGreen()==128 && color.getBlue()==128) {
					collisionGrid[x][y] = 2;
					halfsolids++;
				}else {
					collisionGrid[x][y] = 0;
					air++;
				}
			}
		}
		System.out.println("Stage size: " + collisionGrid.length + ", " + collisionGrid[0].length);
		System.out.println("Built stage with " + solids + " solids, " + halfsolids + " half solids, " + barriers + " barriers, " + scoreZones + " score zones, and " + air + " air.");
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public int getCameraPos() {
		return cameraPosX;
	}
	
	public void spawnBall() {
		int num = (int) (Math.random()*4);
		if(num == 0) {
			ball = new Ball(new Point(938, 360), this, 3, 290, true);
		}else if(num == 1) {
			ball = new Ball(new Point(938, 360), this, 3, 45, true);
		}else if(num == 2) {
			ball = new Ball(new Point(938, 360), this, 3, 135, true);
		}else if(num == 3) {
			ball = new Ball(new Point(938, 360), this, 3, 213, true);
		}else {
			ball = new Ball(new Point(938, 360), this, 3, 213, true);
		}
	}
	
	public void removeBall() {
		ball = null;
	}
	
	public BufferedImage[] getLayers() {
		return new BufferedImage[] {layer0, layer1, layer2, background};
	}
	
	public Point getBallPos() {
		return ball.getPos();
	}
	
	public int getPoint(int x, int y) {
		if(x >= collisionGrid.length || x < 0 || y >= collisionGrid[0].length || y < 0) {
			return 1;
		}else {
			return collisionGrid[x][y];
		}
	}
	
	public int getPoint(double x, double y) {
		if(x >= collisionGrid.length || x < 0 || y >= collisionGrid[0].length || y < 0) {
			return 1;
		}else {
			return collisionGrid[(int) x][(int) y];
		}
	}
	
	public void run() {
		updateStage();
	}
	
	public void updateStage() {
		if(ball != null) {
			ball.update();
			this.cameraScroll();
		}else {
			if(cameraPosX > -320) {
				cameraPosX = cameraPosX - (10+Math.abs((-320 - cameraPosX) % 10));
			}else if(cameraPosX < -320) {
				cameraPosX = cameraPosX + (10+Math.abs((-320 - cameraPosX) % 10));
			}
		}
		//System.out.println(player.getPosition().toString());
		//System.out.println("cameraX : " + cameraPosX);
	}
	
	public int getParalaxScroll(int amount) {
		return (320+cameraPosX)/amount;
	}
	
	private void cameraScroll() {
		int centerOfBall = (int) ball.getPos().getX() + (int) (ball.getHitBox().getWidth()/2);
		if(centerOfBall - 640 <= 1) {
			cameraPosX = -1;
		}else if(centerOfBall + 640 >= collisionGrid.length){
			cameraPosX = -640;
		}else {
			cameraPosX = 640 - centerOfBall;
		}
	}
}
