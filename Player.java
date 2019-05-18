import java.awt.Point;
import java.awt.image.BufferedImage;

public class Player {

	private Character playerCharacter;
	private Point position;
	private int specialAmount;
	
	private final int idle = 0, run = 1, jump = 2, fall = 3, hitAir = 4, hitGround = 5;
	private int currentAnimation;
	private boolean flip;
	private int hitRecover = 0;
	private boolean hitting;
	private boolean hasHit;
	private boolean forceHit;
	private int hitTime = 25;
	private int aim; //0 up, 1 noAim, 2 down
	private boolean hitInAir;
	
	private double velocityX = 0;
	private double velocityY = 0;
	private boolean onGround;
	private Stage stage;
	
	public Player(int character, Stage parent) {
		stage = parent;
		if(character == 0) {
			playerCharacter = new Electra(stage);
		}else if(character == 1) {
			playerCharacter = new Suit(stage);
		}
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Character getCharacter() {
		return playerCharacter;
	}
	
	public int getSpecialAmount() {
		return specialAmount;
	}
	
	public double getVelocityX() {
		return velocityX;
	}
	
	public double getVelocityY() {
		return velocityY;
	}
	
	public void setVelocityX(int v) {
		velocityX = v;
	}
	
	public void setVelocityY(int v) {
		velocityY = v;
	}
	
	public void setPosition(Point p) {
		position = p;
	}
	
	public void specialMove() {
		if(specialAmount == 100) {
			if(playerCharacter.specialMove(this)) {
				specialAmount = 0;
			}
		}
	}
	
	public void hit() {
		if(hitRecover >= 25) {
			try {
				Character c = (Electra) (playerCharacter);
				if(flip) {
					stage.addParticle(new Particle(stage.getParentEngine().getSparks(), 500, null, this, new Point(140, 40)));
				}else {
					stage.addParticle(new Particle(stage.getParentEngine().getSparks(), 500, null, this, new Point(-170, 40)));
				}
			}catch(Exception e) {
				
			}
			hitRecover = 0;
			hitTime = 0;
			if(!onGround) {
				hitInAir = true;
			}else{
				hitInAir = false;
			}
		}
	}
	
	public void forceHit() {
		forceHit = true;
		hitRecover = 0;
		hitTime = 0;
		if(!onGround) {
			hitInAir = true;
		}else{
			hitInAir = false;
		}
	}

	public void jump() {
		if(onGround && (!hitting || forceHit)) {
			velocityY = playerCharacter.getJumpHeight() * -1;
		}
	}

	public void moveRight() {
		if(!hitting || forceHit) {
			velocityX = playerCharacter.getRunSpeed();
		}
	}

	public void moveLeft() {
		if(!hitting || forceHit) {
			velocityX = playerCharacter.getRunSpeed() * -1;
		}
	}

	public void stopMovingX() {
		velocityX = 0;
	}
	
	public void setFlip(boolean f) {
		flip = f;
	}

	private void checkFloor() {
		onGround = false;
		for(int j = 0; j < playerCharacter.getHitBox().getWidth(); j++) {
			int x = (int) position.getX() + j;
			int y = (int) position.getY() + (int) playerCharacter.getHitBox().getHeight() + 1;
			if(stage.getPoint(x, y) == 1 || stage.getPoint(x, y) == 2) {
				onGround = true;
			}
		}
	}

	private void moveX() {
		boolean canMove = true;
		for(int i = 0; i < Math.abs(velocityX) && canMove; i++) {
			for(int j = 1; j < playerCharacter.getHitBox().getHeight() && canMove; j++) {
				if(velocityX > 0) {
					int x = (int) position.getX() + (int) playerCharacter.getHitBox().getWidth() + i;
					int y = (int) position.getY() + j;
					if(stage.getPoint(x, y) == 1 || stage.getPoint(x, y) == 3 || stage.getPoint(x, y) == 4) {
						canMove = false;
					}
				}else if(velocityX < 0) {
					int x = (int) position.getX() - i;
					int y = (int) position.getY() + j;
					if(stage.getPoint(x, y) == 1 || stage.getPoint(x, y) == 3 || stage.getPoint(x, y) == 4) {
						canMove = false;
					}
				}
			}
			if(canMove) {
				if(velocityX > 0) {
					position.setLocation(position.getX() + 1, position.getY());
				}else if(velocityX < 0) {
					position.setLocation(position.getX() - 1, position.getY());
				}
			}
		}
	}

	private void moveY() {
		boolean canMove = true;
		for(int i = 0; i < Math.abs(velocityY) && canMove; i++) {
			for(int j = 1; j < playerCharacter.getHitBox().getWidth() - 1 && canMove; j++) {
				if(velocityY > 0) {
					int x = (int) position.getX() + j;
					int y = (int) position.getY() + (int) playerCharacter.getHitBox().getHeight() + i;
					if(stage.getPoint(x, y) == 1 || stage.getPoint(x, y) == 2) {
						canMove = false;
					}
				}else if(velocityY < 0) {
					int x = (int) position.getX() + j;
					int y = (int) position.getY() - i;
					if(stage.getPoint(x, y) == 1) {
						canMove = false;
					}
				}
			}
			if(canMove) {
				if(velocityY > 0) {
					position.setLocation(position.getX(), position.getY() + 1);
				}else if(velocityY < 0) {
					position.setLocation(position.getX(), position.getY() - 1);
				}
			}else{
				velocityY = 0;
			}
		}
	}
	
	public BufferedImage getFrame() {
		if(flip) {
			return playerCharacter.getFlippedAnimations()[currentAnimation].frame();
		}else{
			return playerCharacter.getAnimations()[currentAnimation].frame();
		}
	}
	
	public Animation getAnimation() {
		if(flip) {
			return playerCharacter.getFlippedAnimations()[currentAnimation];
		}else{
			return playerCharacter.getAnimations()[currentAnimation];
		}
	}
	
	public int getHitRecover(){
		return hitRecover;
	}
	
	public void aim(int a) {
		aim = a;
	}

	public void update() {
		//System.out.println(onGround);
		if(!hitting && hitRecover < 30){
			hitRecover++;
		}
		if(hitTime < 10 && !hasHit) {
			if(stage.getBall() != null && new Point((int) stage.getBallPos().getX() + 22, (int) stage.getBallPos().getY() + 22).distance(new Point((int) (position.getX() + (playerCharacter.getHitBox().getWidth()/2)), (int) (position.getY() + (playerCharacter.getHitBox().getHeight()/2)))) <= playerCharacter.getRange()) {
				stage.upRally();
				hasHit = true;
				specialAmount = specialAmount + 15;
				if(specialAmount > 100) {
					specialAmount = 100;
				}
				stage.getPaddle().determineWillHit();
				stage.setHitTime(stage.getBall().getVelocity() - 15);
				if(aim == 0) {
					stage.getBall().hit((int) (35+Math.random()*30));
				}else if(aim == 1) {
					if(velocityY <= 0) {
						stage.getBall().hit((int) (Math.random()*20));
					}else {
						stage.getBall().hit((int) (-1*Math.random()*20));
					}
				}else if(aim == 2) {
					stage.getBall().hit((int) (-30-Math.random()*30));
				}
				
			}
		}
		if(hitTime < 20){
			hitting = true;
			hitTime++;
		}else{
			forceHit = false;
			hasHit = false;
			hitting = false;
		}
		checkFloor();
		moveX(); moveY();
		if(position.getX() > stage.getBarrier()) {
			velocityX = 0;
			position.setLocation(stage.getBarrier() - (playerCharacter.getHitBox().getWidth() +2), position.getY());
		}
		if(velocityY + 2 < playerCharacter.getJumpHeight()) {
			//System.out.println("vY: " + velocityY);
			velocityY = velocityY + 2;
		}
		if(!hitting){
			if(velocityX > 0) {
				flip = true;
			}else if(velocityX < 0) {
				flip = false;
			}
		}
		if(onGround && velocityY > 0) {
			if(velocityX == 0) {
				currentAnimation = idle;
			}
			if(velocityX > 0) {
				currentAnimation = run;
			}
			if(velocityX < 0) {
				currentAnimation = run;
			}
		}else {
			if(velocityY > 0) {
				currentAnimation = fall;
			}
			if(velocityY <= 0) {
				currentAnimation = jump;
			}
		}
		if(hitting){
			if(hitInAir){
				currentAnimation = hitAir;
			}else{
				currentAnimation = hitGround;
			}
		}
		if(flip) {
			playerCharacter.getFlippedAnimations()[currentAnimation].update();
		}else{
			playerCharacter.getAnimations()[currentAnimation].update();
		}

	}
}
