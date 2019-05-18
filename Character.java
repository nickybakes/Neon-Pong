import java.awt.Point;
import java.awt.Rectangle;

public abstract class Character {
	
	protected static String animPath;
	
	// geometry and visuals
	protected Animation[] animations;
	protected Animation[] flippedAnimations;
	protected final int idle = 0, run = 1, jump = 2, fall = 3, hitAir = 4, hitGround = 5;
	protected Rectangle hitBox;
	protected Stage currentStage;
	
	// physics
	protected int range;
	protected double runSpeed;
	protected double gravityModifier;
	protected double weight;
	protected double jumpHeight;
	
	// attributes
	protected String characterName;
	
	Character(Stage stage) {
		currentStage = stage;
	}
	
	public int getRange() {
		return range;
	}
	
	public String getCharacterName() {
		
		return characterName;
	}
	
	public double getRunSpeed() {
		
		return runSpeed;
	}
	
	public double getGravityModifier() {
		
		return gravityModifier;
	}
	
	public double getWeight() {
		
		return weight;
	}
	
	public double getJumpHeight() {
		
		return jumpHeight;
	}
	
	public Rectangle getHitBox() {
		
		return new Rectangle(hitBox);
	}
	
	public Animation[] getAnimations() {
		return animations;
	}
	
	public Animation[] getFlippedAnimations() {
		return flippedAnimations;
	}

	public abstract boolean specialMove(Player parent);
}
