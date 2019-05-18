import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Stage implements Runnable{
	private int[][] collisionGrid;
	private BufferedImage collisionData;
	private BufferedImage layer0;
	private BufferedImage layer1;
	private BufferedImage layer2;
	private BufferedImage background;
	private BaseEngine parentEngine;
	
	private boolean scored;
	private int scoreTime;
	private int whoScored;
	private int rally;
	private int gamemode;
	private boolean gameEnded;
	private int endTimer;
	private int hitTime;
	
	private int playerScore;
	private int cpuScore;
	
	private CardWall cardWall;
	
	private Player player;
	private int barrierPos;
	private int top;
	private int bottom;
	private Ball ball;
	private Paddle paddle;
	private int cameraPosX;
	private int countDownTimer;
	
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public Stage(BufferedImage collisionData_, BufferedImage layer0_, BufferedImage layer1_, BufferedImage layer2_, BufferedImage background_, int barrier, int character, int gameType, int t, int b, BaseEngine engine) {
		collisionData = collisionData_;layer0 = layer0_;layer1 = layer1_;
		layer2 = layer2_;background = background_;
		
		gamemode = gameType;
		parentEngine = engine;
		barrierPos = barrier;
		top = t;
		bottom = b;
		player = new Player(character, this);
		paddle = new Paddle(new Point(1790,223), gameType, this);
		
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
				}else if(color.getRed()==0 && color.getGreen()==160 && color.getBlue()==255) {
					collisionGrid[x][y] = 3;
					barriers++;
				}else if(color.getRed()==255 && color.getGreen()==255 && color.getBlue()==255){
					collisionGrid[x][y] = 0;
					air++;
				}else if(color.getRed()==0 && color.getGreen()==255 && color.getBlue()==0){
					collisionGrid[x][y] = 4;
					scoreZones++;
				}else if(color.getRed()==255 && color.getGreen()==0 && color.getBlue()==0){
					player.setPosition(new Point(x,y));
				}
			}
		}
		countDownTimer = 0;
		System.out.println("Stage size: " + collisionGrid.length + ", " + collisionGrid[0].length);
		System.out.println("Built stage with " + solids + " solids, " + halfsolids + " half solids, " + barriers + " barriers, " + scoreZones + " score zones, and " + air + " air.");
		System.out.println("Spawn point for player: " + player.getPosition().toString());
	}
	
	public Paddle getPaddle() {
		return paddle;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int playerScore() {
		return playerScore;
	}
	
	public int cpuScore() {
		return cpuScore;
	}
	
	public int getCameraPos() {
		return cameraPosX;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public BaseEngine getParentEngine() {
		return parentEngine;
	}
	
	public int getPoint(int x, int y) {
		if(x >= collisionGrid.length || x < 0 || y >= collisionGrid[0].length || y < 0) {
			return 1;
		}else {
			return collisionGrid[x][y];
		}
	}
	
	public int getBarrier() {
		return barrierPos;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public BufferedImage[] getLayers() {
		return new BufferedImage[] {layer0, layer1, layer2, background};
	}
	
	public Point getBallPos() {
		return ball.getPos();
	}
	
	public int getCountDown() {
		return countDownTimer;
	}
	
	public int getScoreTime() {
		return scoreTime;
	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public void setHitTime(int h) {
		hitTime = h;
	}
	
	public void upRally() {
		if(rally + 1 <= 999) {
			rally++;
		}
	}
	
	public int getRally() {
		return rally;
	}
	
	public void spawnBall(Point p, int d) {
		ball = new Ball(p, this, 9, d, false);
	}
	
	public void removeBall() {
		ball = null;
	}

	public void run() {
		updateStage();
	}
	
	public boolean getScored() {
		return scored;
	}
	
	public int getWhoScored() {
		return whoScored;
	}
	
	public void score(int who) {
		paddle.setWillHit(true);
		if(gamemode == 0) {
			removeBall();
			scored = true;
			scoreTime = 0;
			whoScored = who;
			if(whoScored == 0) {
				playerScore++;
			}else if(whoScored == 1) {
				cpuScore++;
			}
			if(playerScore == 7 || cpuScore == 7) {
				gameEnded = true;
			}
		}else if(gamemode == 1) {
			removeBall();
			gameEnded = true;
		}
	}
	
	public CardWall getCardWall() {
		return cardWall;
	}
	
	public void createCardWall(Point p) {
		cardWall = new CardWall(p, this);
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public void updateStage() {
		if(gameEnded && endTimer < 120) {
			endTimer++;
		}else if(gameEnded) {
			parentEngine.setGameState(3);
		}
		if(scored) {
			scoreTime++;
			if(scoreTime > 120) {
				scored = false;
				countDownTimer = 40;
			}
		}
		if(countDownTimer < 180) {
			countDownTimer++;
		}else if(!gameEnded && !scored && ball == null) {
			spawnBall(new Point(barrierPos,300), 180);
		}
		if(cardWall != null) {
			cardWall.update();
			if(cardWall.getUsed()) {
				particles.add(new Particle(parentEngine.getCardFall(), 150, new Point((int) cardWall.getPosition().getX() - 153, (int) cardWall.getPosition().getY()), null, null));
				cardWall = null;
			}
		}
		if(hitTime > 0) {
			hitTime--;
		}else {
			if(ball != null) {
				ball.update();
			}
			player.update();
			paddle.update();
			
		}
		this.cameraScroll();
		ArrayList<Particle> removeParticles = new ArrayList<Particle>();
		for(Particle p : particles) {
			if(p.getTick() >= p.getLifeSpan()) {
				removeParticles.add(p);
			}
			p.update();
		}
		particles.removeAll(removeParticles);
	}
	
	public int getParalaxScroll(int amount) {
		return (320+cameraPosX)/amount;
	}
	
	private void cameraScroll() {
		int centerOfPlayer = (int) player.getPosition().getX() + (int) (player.getCharacter().getHitBox().getWidth()/2);
		if(centerOfPlayer - 640 <= 1) {
			cameraPosX = -1;
		}else if(centerOfPlayer + 640 >= collisionGrid.length){
			cameraPosX = -640;
		}else {
			cameraPosX = 640 - centerOfPlayer;
		}
	}



}
