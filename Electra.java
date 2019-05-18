import java.awt.Point;
import java.awt.Rectangle;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Electra extends Character{
	
	public Electra(Stage stage) {
		super(stage);
		
		characterName = "Electra";
		animations = currentStage.getParentEngine().getElectraAnimations();
		flippedAnimations = currentStage.getParentEngine().getFlippedElectraAnimations();
		hitBox = new Rectangle(196, 335);
		runSpeed = 15;
		range = 200;
		jumpHeight = 35;
	}

	public boolean specialMove(Player parent) {
		if(currentStage.getBall() != null) {
			if(currentStage.getBallPos().getX() < currentStage.getBarrier()) {
				currentStage.addParticle(new Particle(currentStage.getParentEngine().getSparksLarge(), 500, new Point(parent.getPosition()), null, null));
				if(currentStage.getBallPos().getY() > (currentStage.getBottom() - hitBox.getHeight()+1)){
					parent.getPosition().setLocation(currentStage.getBallPos().getX(), currentStage.getBottom() - hitBox.getHeight()-2);
				}else {
					parent.getPosition().setLocation(currentStage.getBallPos());
				}
				parent.setVelocityX(0);
				parent.setVelocityY(0);
				currentStage.upRally();
				currentStage.addParticle(new Particle(currentStage.getParentEngine().getSparksLarge(), 500, new Point(parent.getPosition()), null, null));
				currentStage.getBall().hit((int) (-30-Math.random()*30));
				currentStage.getPaddle().determineWillHit();
				currentStage.setHitTime(currentStage.getBall().getVelocity() - 15);
				return true;
			}
		}
		return false;
	}

}
