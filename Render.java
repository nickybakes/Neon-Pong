
public class Render implements Runnable{
	
	private BaseEngine engine;
	
	public Render(BaseEngine newEngine) {
		engine = newEngine;
	}

	public void run() {
		//engine.clearImage(); //running this makes it choppy
		
		try {
			for(Picture p : engine.getPictures()) {
				BaseEngine.drawImage(engine.renderImage, p, (int) p.getPos().getX(), (int)  p.getPos().getY());
			}
		}catch(Exception e) {
			
		}

	}

}
