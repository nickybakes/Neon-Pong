import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Suit extends Character {
	
	public Suit(Stage stage) {
		super(stage);
		
		characterName = "Suit";
		animations = currentStage.getParentEngine().getSuitAnimations();
		flippedAnimations = currentStage.getParentEngine().getFlippedSuitAnimations();
		hitBox = new Rectangle(179,345);
		runSpeed = 14;
		range = 230;
		jumpHeight = 33;
		
	}
	
	@Override
	public boolean specialMove(Player parent) {
		currentStage.createCardWall(parent.getPosition());
		return true;
		
	}

}
