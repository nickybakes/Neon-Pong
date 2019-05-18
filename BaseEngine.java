import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BaseEngine extends JFrame implements Runnable{

	// rendering
	private mainPanel mainPanel;
	private Render render;
	protected Picture renderImage;
	private ArrayList<Picture> pictures = new ArrayList<Picture>();

	// engine/game state
	private boolean permitRun;
	Thread renderThread;

	private int gameState = 0;
	private int selection = 0;
	private int selectionLimit = 0;
	private int gamemode;
	private int stage;
	private int character;
	private ArrayList<Integer> menus;
	private Stage currentStage;
	private StageTitle currentStageTitle;

	private BufferedImage blank = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage[] precachedButtons = useFolder("Game_Resources\\UI\\buttons");
	private BufferedImage[] precachedUI = useFolder("Game_Resources\\UI");
	private BufferedImage[] precachedUI2 = useFolder("Game_Resources\\UI2");
	private BufferedImage[] precachedRooftops = useFolder("Game_Resources\\Stages\\Rooftops");
	private BufferedImage[] precachedUnderpass = useFolder("Game_Resources\\Stages\\Underpass");
	private BufferedImage[] precachedPongSprites = useFolder("Game_Resources\\Characters");
	private BufferedImage[] precachedElectra = useFolder("Game_Resources\\Characters\\Electra");
	private BufferedImage[] precachedSuit = useFolder("Game_Resources\\Characters\\Suit");
	private BufferedImage[] precachedSparks = resize(useFolder("Game_Resources\\Characters\\Electra\\Sparks"), 50);
	private BufferedImage[] precachedSparksLarge = useFolder("Game_Resources\\Characters\\Electra\\Sparks");
	private BufferedImage[] precachedCards = useFolder("Game_Resources\\Characters\\Suit\\Cards");
	Animation pressStart = new Animation(new BufferedImage[] {precachedUI[26], blank}, 200, new int[] {0,0,0,1}, null);
	Animation sparks = new Animation(precachedSparks, 30, null, null);
	Animation sparksLarge = new Animation(precachedSparksLarge, 30, null, null);

	//W, A, S, D, SPACE, ESC, ENTER, LEFT ARROW KEY, RIGHT ARROW KEY
	boolean[] keys = new boolean[9]; boolean[] pressOnce = new boolean[9]; int[] keyCodes = {87, 65, 83, 68, 32, 27, 10, 37, 39};
	boolean[] keysReset = {true, true, true, true, true, true, true, true, true};
	private final int w = 0, a = 1, s = 2, d = 3, space = 4, esc = 5, enter = 6, left = 7, right = 8;
	// setup
	BaseEngine(Dimension newSize) {

		initRenderer();
		initGameState();
		initDisplay(newSize);
		start();
	}

	public Animation getSparks() {
		return sparks;
	}
	
	public Animation getSparksLarge() {
		return sparksLarge;
	}

	public Animation getCardFall() {
		return new Animation(new BufferedImage[] {precachedCards[1], precachedCards[2], precachedCards[3], precachedCards[4]}, 50, null, null);
	}

	public Animation[] getElectraAnimations() {
		Animation idle = new Animation(new BufferedImage[] {precachedElectra[6], precachedElectra[7], precachedElectra[8]}, 400, new int[] {0,1,2,1}, new Rectangle(0, 0));
		Animation run = new Animation(new BufferedImage[] {precachedElectra[10], precachedElectra[11], precachedElectra[12], precachedElectra[13], precachedElectra[14], precachedElectra[15]}, 100, null, new Rectangle(-51, 0));
		Animation jump = new Animation(new BufferedImage[] {precachedElectra[9]}, 0, null, new Rectangle(-51, 0));
		Animation fall = new Animation(new BufferedImage[] {precachedElectra[0]}, 0, null, new Rectangle(-51, 0));
		Animation hitAir = new Animation(new BufferedImage[] {precachedElectra[1], precachedElectra[2], precachedElectra[3], precachedElectra[4]}, 100, null, new Rectangle(-29, 0));
		Animation hitGround = new Animation(new BufferedImage[] {precachedElectra[5]}, 0, null, new Rectangle(-69, 0));
		return new Animation[] {idle, run, jump, fall, hitAir, hitGround};
	}

	public Animation[] getFlippedElectraAnimations() {
		Animation idle = new Animation(new BufferedImage[] {flip(precachedElectra[6]), flip(precachedElectra[7]), flip(precachedElectra[8])}, 400, new int[] {0,1,2,1}, new Rectangle(0, 0));
		Animation run = new Animation(new BufferedImage[] {flip(precachedElectra[10]), flip(precachedElectra[11]), flip(precachedElectra[12]), flip(precachedElectra[13]), flip(precachedElectra[14]), flip(precachedElectra[15])}, 100, null, new Rectangle(-51, 0));
		Animation jump = new Animation(new BufferedImage[] {flip(precachedElectra[9])}, 0, null, new Rectangle(-51, 0));
		Animation fall = new Animation(new BufferedImage[] {flip(precachedElectra[0])}, 0, null, new Rectangle(-51, 0));
		Animation hitAir = new Animation(new BufferedImage[] {flip(precachedElectra[1]), flip(precachedElectra[2]), flip(precachedElectra[3]), flip(precachedElectra[4])}, 100, null, new Rectangle(-29, 0));
		Animation hitGround = new Animation(new BufferedImage[] {flip(precachedElectra[5])}, 0, null, new Rectangle(-69, 0));
		return new Animation[] {idle, run, jump, fall, hitAir, hitGround};
	}

	public Animation[] getSuitAnimations() {
		Animation idle = new Animation(new BufferedImage[] {precachedSuit[2], precachedSuit[3], precachedSuit[4]}, 400, new int[] {0,1,2,1}, new Rectangle(0, 0));
		Animation run = new Animation(new BufferedImage[] {precachedSuit[6], precachedSuit[7], precachedSuit[8], precachedSuit[9], precachedSuit[10], precachedSuit[11]}, 120, null, new Rectangle(-93, 0));
		Animation jump = new Animation(new BufferedImage[] {precachedSuit[5]}, 0, null, new Rectangle(-112, 0));
		Animation fall = new Animation(new BufferedImage[] {precachedSuit[0]}, 0, null, new Rectangle(-110, 0));
		Animation hitAir = new Animation(new BufferedImage[] {precachedSuit[1]}, 0, null, new Rectangle(-112, 0));
		Animation hitGround = new Animation(new BufferedImage[] {precachedSuit[1]}, 0, null, new Rectangle(-112, 0));
		return new Animation[] {idle, run, jump, fall, hitAir, hitGround};
	}

	public Animation[] getFlippedSuitAnimations() {
		Animation idle = new Animation(new BufferedImage[] {flip(precachedSuit[2]), flip(precachedSuit[3]), flip(precachedSuit[4])}, 400, new int[] {0,1,2,1}, new Rectangle(0, 0));
		Animation run = new Animation(new BufferedImage[] {flip(precachedSuit[6]), flip(precachedSuit[7]), flip(precachedSuit[8]), flip(precachedSuit[9]), flip(precachedSuit[10]), flip(precachedSuit[11])}, 120, null, new Rectangle(-93, 0));
		Animation jump = new Animation(new BufferedImage[] {flip(precachedSuit[5])}, 0, null, new Rectangle(-112, 0));
		Animation fall = new Animation(new BufferedImage[] {flip(precachedSuit[0])}, 0, null, new Rectangle(-110, 0));
		Animation hitAir = new Animation(new BufferedImage[] {flip(precachedSuit[1])}, 0, null, new Rectangle(-112, 0));
		Animation hitGround = new Animation(new BufferedImage[] {flip(precachedSuit[1])}, 0, null, new Rectangle(-112, 0));
		return new Animation[] {idle, run, jump, fall, hitAir, hitGround};
	}

	protected void initRenderer() {

		renderThread = new Thread(render = new Render(this));
	}

	private void initGameState() {
		menus = new ArrayList<Integer>(); menus.add(0);
	}

	private static BufferedImage[] useFolder(String s) {
		File path = new File(s);
		File[] listOfFiles = path.listFiles();
		ArrayList<File> realFiles = new ArrayList<File>();
		for(int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile() && (listOfFiles[i].getName().substring(listOfFiles[i].getName().length()-3).equals("png") || listOfFiles[i].getName().substring(listOfFiles[i].getName().length()-3).equals("jpg"))) {
				realFiles.add(listOfFiles[i]);
			}
		}
		BufferedImage[] images = new BufferedImage[realFiles.size()];
		for(int i = 0; i < realFiles.size(); i++) {
			try {
				System.out.println(i + ": " + realFiles.get(i).getPath());
				BufferedImage image = ImageIO.read(new File(realFiles.get(i).getPath()));
				images[i] = image;
			} catch (IOException e) {

			}
		}
		System.out.println("-----------");

		return images;
	}

	private BufferedImage[] resize(BufferedImage[] images, int scale) {
		BufferedImage[] newImages = new BufferedImage[images.length];
		int i = 0;
		for(BufferedImage src : images) {
			int targetWidth = (int) (images[0].getWidth()*((double) scale/100.0));
			int targetHeight = (int) (images[0].getHeight()*((double) scale/100.0));
			float ratio = ((float) src.getHeight() / (float) src.getWidth());
			if (ratio <= 1) { //square or landscape-oriented image
				targetHeight = (int) Math.ceil((float) targetWidth * ratio);
			} else { //portrait image
				targetWidth = Math.round((float) targetHeight / ratio);
			}
			BufferedImage bi = new BufferedImage(targetWidth, targetHeight, src.getType());
			Graphics2D g2d = bi.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //produces a balanced resizing (fast and decent quality)
			g2d.drawImage(src, 0, 0, targetWidth, targetHeight, null);
			g2d.dispose();
			newImages[i] = bi;
			i++;
		}
		return newImages;
	}

	private void initDisplay(Dimension imageSize) {
		renderImage = new Picture(imageSize);
		renderImage.setAllPixelsToAlpha(0);

		mainPanel = new mainPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setBackground(Color.BLACK);
		mainPanel.add(Box.createRigidArea(imageSize));
		this.add(mainPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setMinimumSize(new Dimension(1400, 840));
		this.pack();
		this.addKeyListener(new keyInput());
		this.setTitle("Neon Pong");
		this.setVisible(true);

		updateDisplay();
	}

	public ArrayList<Picture> getPictures() {
		return pictures;
	}

	protected static void drawImage(Picture canvas, Picture image, int xPos, int yPos) {

		canvas.drawImage(image, new Point(xPos, yPos));
	}

	public class mainPanel extends JPanel {

		public void paintComponent(Graphics g) {
			g.drawImage(renderImage.getImage(), (this.getWidth() - renderImage.getWidth()) / 2, (this.getHeight() - renderImage.getHeight()) / 2, this);
			if(gameState == 1) {
				g.setFont(new Font("Impact", Font.BOLD, 40));
				g.setColor(Color.WHITE);
				if(gamemode == 0) {
					g.drawString("" + currentStage.playerScore(), (this.getWidth() - renderImage.getWidth()) / 2 + 562, (this.getHeight() - renderImage.getHeight()) / 2 + 70);
					g.drawString("" + currentStage.cpuScore(), (this.getWidth() - renderImage.getWidth()) / 2 + 698, (this.getHeight() - renderImage.getHeight()) / 2 + 70);
				}else if(gamemode == 1) {
					if(currentStage.getRally() - 99 > 0) {
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 606, (this.getHeight() - renderImage.getHeight()) / 2 + 70);
					}else if(currentStage.getRally() - 9 > 0) {
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 621, (this.getHeight() - renderImage.getHeight()) / 2 + 70);
					}else {
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 628, (this.getHeight() - renderImage.getHeight()) / 2 + 70);
					}
				}
			}else if(gameState == 3) {
				g.setFont(new Font("Impact", Font.BOLD, 40));
				g.setColor(Color.WHITE);
				if(gamemode == 0) {
					g.drawString("" + currentStage.playerScore(), (this.getWidth() - renderImage.getWidth()) / 2 + 900, (this.getHeight() - renderImage.getHeight()) / 2 + 433);
					g.drawString("" + currentStage.cpuScore(), (this.getWidth() - renderImage.getWidth()) / 2 + 1053, (this.getHeight() - renderImage.getHeight()) / 2 + 433);
				}else if(gamemode == 1) {
					g.setFont(new Font("Impact", Font.BOLD, 70));
					if(currentStage.getRally() - 99 > 0) {
						g.setColor(Color.BLACK);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 996, (this.getHeight() - renderImage.getHeight()) / 2 + 432);
						g.setColor(Color.WHITE);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 990, (this.getHeight() - renderImage.getHeight()) / 2 + 426);
					}else if(currentStage.getRally() - 9 > 0) {
						g.setColor(Color.BLACK);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 972, (this.getHeight() - renderImage.getHeight()) / 2 + 432);
						g.setColor(Color.WHITE);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 966, (this.getHeight() - renderImage.getHeight()) / 2 + 426);
					}else {
						g.setColor(Color.BLACK);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 1002, (this.getHeight() - renderImage.getHeight()) / 2 + 432);
						g.setColor(Color.WHITE);
						g.drawString("" + currentStage.getRally(), (this.getWidth() - renderImage.getWidth()) / 2 + 996, (this.getHeight() - renderImage.getHeight()) / 2 + 426);
					}

				}
			}
			pictures.clear();
		}
	}

	public void start() {
		permitRun = true;
		new Thread(this).start();
	}

	public void run() {

		long initialTime = System.nanoTime();

		while (permitRun) {
			try {

				Thread.sleep((int) (1000.0 / 60.0));
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			if(gameState == 0) { //in menus
				if(currentStageTitle == null) {
					BufferedImage layer0, layer1, layer2, background, collision;
					collision = precachedRooftops[6];layer0 = precachedRooftops[7];layer1 = precachedRooftops[5];layer2 = blank;background = blank;
					currentStageTitle = new StageTitle(collision, layer0, layer1, layer2, background);
				}
				currentStageTitle.run();
				pictures.add(new Picture(currentStageTitle.getLayers()[3], new Point(currentStageTitle.getCameraPos(), 0)));
				pictures.add(new Picture(currentStageTitle.getLayers()[2], new Point(currentStageTitle.getCameraPos()+currentStageTitle.getParalaxScroll(16), 0)));
				pictures.add(new Picture(currentStageTitle.getLayers()[1], new Point(currentStageTitle.getCameraPos()+currentStageTitle.getParalaxScroll(7), 0)));
				pictures.add(new Picture(currentStageTitle.getLayers()[0], new Point(currentStageTitle.getCameraPos(), 0)));
				if(currentStageTitle.getBall() != null) {
					pictures.add(new Picture(precachedPongSprites[0], new Point((int) currentStageTitle.getBallPos().getX()+currentStageTitle.getCameraPos(),(int) currentStageTitle.getBallPos().getY())));
				}
				if(currentStageTitle.getBall() == null && currentMenu() != 0) {
					currentStageTitle.spawnBall();
				}else if(currentStageTitle.getBall() != null && currentMenu() == 0) {
					currentStageTitle.removeBall();
				}
				//W, A, S, D, SPACE, ESC, ENTER, LEFT ARROW KEY, RIGHT ARROW KEY
				if(currentMenu() == 0) {
					pressStart.update();
					pictures.add(new Picture(pressStart.frame(), new Point(294, 572)));
				}else {
					if(currentMenu() == 1) {
						selectionLimit = 2;
						pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						pictures.add(new Picture(precachedUI[10], new Point(663, 18)));
						pictures.add(new Picture(precachedButtons[6], new Point(207, 86)));
						pictures.add(new Picture(precachedButtons[4], new Point(575, 256)));
						pictures.add(new Picture(precachedButtons[8], new Point(324, 514)));
						if(selection == 0) {
							pictures.add(new Picture(precachedButtons[7], new Point(207, 86)));
						}else if(selection == 1) {
							pictures.add(new Picture(precachedButtons[5], new Point(575, 256)));
						}else if(selection == 2) {
							pictures.add(new Picture(precachedButtons[9], new Point(324, 514)));
						}	
					}else if(currentMenu() == 2) {
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						pictures.add(new Picture(precachedUI[5], new Point(0, 0)));
					}else if(currentMenu() == 3) {
						selectionLimit = 1;
						pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						pictures.add(new Picture(precachedUI[7], new Point(30, 30)));
						pictures.add(new Picture(precachedUI[0], new Point(930, 37)));
						pictures.add(new Picture(precachedButtons[2], new Point(313, 152)));
						pictures.add(new Picture(precachedButtons[12], new Point(533, 390)));
						if(selection == 0) {
							pictures.add(new Picture(precachedButtons[3], new Point(313, 152)));
						}else if(selection == 1) {
							pictures.add(new Picture(precachedButtons[13], new Point(533, 390)));
						}

					}else if(currentMenu() == 4) {
						selectionLimit = 1;
						pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						pictures.add(new Picture(precachedUI[6], new Point(30, 30)));
						pictures.add(new Picture(precachedUI[0], new Point(930, 37)));
						pictures.add(new Picture(precachedButtons[0], new Point(123, 84)));
						pictures.add(new Picture(precachedButtons[20], new Point(423, 404)));
						if(selection == 0) {
							pictures.add(new Picture(precachedButtons[1], new Point(123, 84)));
						}else if(selection == 1) {
							pictures.add(new Picture(precachedButtons[21], new Point(423, 404)));
						}
					}else if(currentMenu() == 5) {
						selectionLimit = 1;
						pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						pictures.add(new Picture(precachedUI[8], new Point(30, 30)));
						pictures.add(new Picture(precachedUI[0], new Point(930, 37)));
						pictures.add(new Picture(precachedButtons[18], new Point(423, 124)));
						pictures.add(new Picture(precachedButtons[22], new Point(123, 424)));
						if(selection == 0) {
							pictures.add(new Picture(precachedButtons[19], new Point(423, 124)));
						}else if(selection == 1) {
							pictures.add(new Picture(precachedButtons[23], new Point(123, 424)));
						}
					}else if(currentMenu() == 6) {
						pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
						pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
						if(gamemode == 0) {
							pictures.add(new Picture(precachedButtons[3], new Point(740, 60)));
						}else if (gamemode == 1){
							pictures.add(new Picture(precachedButtons[13], new Point(740, 60)));
						}
						if(character == 0) {
							pictures.add(new Picture(precachedButtons[1], new Point(0, 20)));
						}else if (character == 1){
							pictures.add(new Picture(precachedButtons[21], new Point(0, 20)));
						}
						if(stage == 0) {
							pictures.add(new Picture(precachedButtons[19], new Point(0, 340)));
						}else if (stage == 1){
							pictures.add(new Picture(precachedButtons[23], new Point(0, 340)));
						}
						pictures.add(new Picture(precachedUI[15], new Point(740, 323)));
					}
				}
			}else if(gameState == 1) {
				if(keys[space]) {
					currentStage.getPlayer().jump();
				}
				if(keys[d]) {
					currentStage.getPlayer().moveRight();
				}
				if(keys[a]) {
					currentStage.getPlayer().moveLeft();
				}
				if(!keys[d] && !keys[a]) {
					currentStage.getPlayer().stopMovingX();
				}
				if(keys[w]) {
					currentStage.getPlayer().aim(0);
				}
				if(keys[s]) {
					currentStage.getPlayer().aim(2);
				}
				if(!keys[w] && !keys[s]) {
					currentStage.getPlayer().aim(1);
				}
				if(keys[left]){
					currentStage.getPlayer().hit();
				}
				currentStage.run();
				//				pictures.add(new Picture(currentStage.getLayers()[3], new Point(currentStage.getCameraPos(), 0)));
				//				pictures.add(new Picture(currentStage.getLayers()[2], new Point(currentStage.getCameraPos()+currentStage.getParalaxScroll(16), 0)));
				//				pictures.add(new Picture(currentStage.getLayers()[1], new Point(currentStage.getCameraPos()+currentStage.getParalaxScroll(7), 0)));
				pictures.add(new Picture(currentStage.getLayers()[0], new Point(currentStage.getCameraPos(), 0)));
				if(currentStage.getCardWall() != null) {
					pictures.add(new Picture(precachedCards[0], new Point((int) currentStage.getCardWall().getPosition().getX()+currentStage.getCameraPos(),(int) currentStage.getCardWall().getPosition().getY())));
				}
				pictures.add(new Picture(currentStage.getPlayer().getFrame(), new Point((int) currentStage.getPlayer().getPosition().getX()+currentStage.getCameraPos() + (int) currentStage.getPlayer().getAnimation().offset().getWidth(),(int) currentStage.getPlayer().getPosition().getY())));
				if(currentStage.getCountDown() > 60 && currentStage.getCountDown() < 120) {
					pictures.add(new Picture(precachedUI[14], new Point(345, 270)));
				}else if(currentStage.getCountDown() > 120 && currentStage.getCountDown() < 180) {
					pictures.add(new Picture(precachedUI[4], new Point(345, 270)));
				}
				if(currentStage.getBall() != null) {
					pictures.add(new Picture(precachedPongSprites[0], new Point((int) currentStage.getBallPos().getX()+currentStage.getCameraPos(),(int) currentStage.getBallPos().getY())));
				}
				pictures.add(new Picture(precachedPongSprites[1], new Point((int) currentStage.getPaddle().position().getX()+currentStage.getCameraPos(),(int) currentStage.getPaddle().position().getY())));
				for(Particle p : currentStage.getParticles()) {
					pictures.add(new Picture(p.getFrame(), new Point((int) p.getPosition().getX()+currentStage.getCameraPos() + (int) p.getAnimation().offset().getWidth(),(int) p.getPosition().getY())));
				}
				for(int i = 0; i < 7; i++) {
					if(currentStage.getPlayer().getSpecialAmount() > i*15) {
						pictures.add(new Picture(precachedUI2[1], new Point(424+(62*i), 87)));
					}
				}
				if(gamemode == 0) {
					if(currentStage.getPlayer().getSpecialAmount() == 100) {
						pictures.add(new Picture(precachedUI[22], new Point(398, 0)));
					}else {
						pictures.add(new Picture(precachedUI[21], new Point(398, 0)));
					}
				}else if(gamemode == 1) {
					if(currentStage.getPlayer().getSpecialAmount() == 100) {
						pictures.add(new Picture(precachedUI[13], new Point(398, 0)));
					}else {
						pictures.add(new Picture(precachedUI[12], new Point(398, 0)));
					}
				}
				if(gamemode == 0 && currentStage.getScored()) {
					if(currentStage.getWhoScored() == 0) {
						if(currentStage.getScoreTime() < 30) {
							pictures.add(new Picture(precachedUI[27], new Point((int) (-1162 + 40.7*currentStage.getScoreTime()), 235)));
						}else if(currentStage.getScoreTime() >= 30 && currentStage.getScoreTime() < 90) {
							pictures.add(new Picture(precachedUI[27], new Point(59, 235)));
						}else if(currentStage.getScoreTime() >= 90 && currentStage.getScoreTime() < 120) {
							pictures.add(new Picture(precachedUI[27], new Point((int) (59 + 40.7*(currentStage.getScoreTime()-90)), 235)));
						}
					}else if(currentStage.getWhoScored() == 1) {
						if(currentStage.getScoreTime() < 30) {
							pictures.add(new Picture(precachedUI[2], new Point((int) (1280 - 37*currentStage.getScoreTime()), 235)));
						}else if(currentStage.getScoreTime() >= 30 && currentStage.getScoreTime() < 90) {
							pictures.add(new Picture(precachedUI[2], new Point(168, 235)));
						}else if(currentStage.getScoreTime() >= 90 && currentStage.getScoreTime() < 120) {
							pictures.add(new Picture(precachedUI[2], new Point((int) (168 - 37*(currentStage.getScoreTime()-90)), 235)));
						}
					}
				}
			}else if(gameState == 2) {
				selectionLimit = 2;
				//				pictures.add(new Picture(currentStage.getLayers()[3], new Point(currentStage.getCameraPos(), 0)));
				//				pictures.add(new Picture(currentStage.getLayers()[2], new Point(currentStage.getCameraPos()+currentStage.getParalaxScroll(16), 0)));
				//				pictures.add(new Picture(currentStage.getLayers()[1], new Point(currentStage.getCameraPos()+currentStage.getParalaxScroll(7), 0)));
				pictures.add(new Picture(currentStage.getLayers()[0], new Point(currentStage.getCameraPos(), 0)));
				if(currentStage.getCardWall() != null) {
					pictures.add(new Picture(precachedCards[0], new Point((int) currentStage.getCardWall().getPosition().getX()+currentStage.getCameraPos(),(int) currentStage.getCardWall().getPosition().getY())));
				}
				if(currentStage.getBall() != null) {
					pictures.add(new Picture(precachedPongSprites[0], new Point((int) currentStage.getBallPos().getX()+currentStage.getCameraPos(),(int) currentStage.getBallPos().getY())));
				}
				pictures.add(new Picture(currentStage.getPlayer().getFrame(), new Point((int) currentStage.getPlayer().getPosition().getX()+currentStage.getCameraPos() + (int) currentStage.getPlayer().getAnimation().offset().getWidth(),(int) currentStage.getPlayer().getPosition().getY())));
				pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
				pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
				pictures.add(new Picture(precachedUI[11], new Point(20, 20)));
				pictures.add(new Picture(precachedButtons[16], new Point(708, 110)));
				pictures.add(new Picture(precachedButtons[14], new Point(700, 300)));
				pictures.add(new Picture(precachedButtons[10], new Point(700, 420)));
				if(selection == 0) {
					pictures.add(new Picture(precachedButtons[17], new Point(708, 110)));
				}else if(selection == 1) {
					pictures.add(new Picture(precachedButtons[15], new Point(700, 300)));
				}else if(selection == 2) {
					pictures.add(new Picture(precachedButtons[11], new Point(700, 420)));
				}
			}else if(gameState == 3) {
				pictures.add(new Picture(currentStage.getLayers()[0], new Point(currentStage.getCameraPos(), 0)));
				if(currentStage.getCardWall() != null) {
					pictures.add(new Picture(precachedCards[0], new Point((int) currentStage.getCardWall().getPosition().getX()+currentStage.getCameraPos(),(int) currentStage.getCardWall().getPosition().getY())));
				}
				if(currentStage.getBall() != null) {
					pictures.add(new Picture(precachedPongSprites[0], new Point((int) currentStage.getBallPos().getX()+currentStage.getCameraPos(),(int) currentStage.getBallPos().getY())));
				}
				pictures.add(new Picture(currentStage.getPlayer().getFrame(), new Point((int) currentStage.getPlayer().getPosition().getX()+currentStage.getCameraPos() + (int) currentStage.getPlayer().getAnimation().offset().getWidth(),(int) currentStage.getPlayer().getPosition().getY())));
				pictures.add(new Picture(precachedUI[24], new Point(0, 0)));
				pictures.add(new Picture(precachedUI2[0], new Point(0, 0)));
				if(gamemode == 0) {
					if(currentStage.playerScore() == 7) {
						if(character == 0) {
							pictures.add(new Picture(precachedUI[19], new Point(0, 0)));
						}else if(character == 1) {
							pictures.add(new Picture(precachedUI[20], new Point(0, 0)));
						}
					}else {
						pictures.add(new Picture(precachedUI[16], new Point(0, 0)));
					}
				}else if(gamemode == 1) {
					if(character == 0) {
						pictures.add(new Picture(precachedUI[17], new Point(0, 0)));
					}else if(character == 1) {
						pictures.add(new Picture(precachedUI[18], new Point(0, 0)));
					}
				}
			}
			renderThread.run();
			updateDisplay();
		}

		updateDisplay();
	}

	public void switchMenu(String operation, int newMenu) {
		if(operation.equals("add")) {
			menus.add(newMenu);
			selection = 0; selectionLimit = 0;
		}else {
			if(menus.size() > 1) {
				menus.remove(menus.size() - 1);
				selection = 0; selectionLimit = 0;
			}
		}
	}

	public int currentMenu() {
		return menus.get(menus.size()-1);
	}

	public void setGameState(int s) {
		gameState = s;
	}

	public void updateDisplay() {
		mainPanel.repaint();
	}

	public void startStage() {
		if(stage == 0) {
			currentStage = new Stage(precachedRooftops[1], precachedRooftops[8], blank, blank, blank, 1477, character, gamemode, 76, 659, BaseEngine.this);
		}else if(stage == 1) {
			currentStage = new Stage(precachedUnderpass[0], precachedUnderpass[1], blank, blank, blank, 1535, character, gamemode, 42, 695, BaseEngine.this);
		}
		currentStage.getPlayer().setFlip(true);
	}

	class keyInput implements KeyListener {

		public void keyPressed(KeyEvent e) {
			if(gameState == 0) {
				if(e.getKeyCode() == keyCodes[esc]) {
					switchMenu("remove", 0);
				}
				if(menus.get(menus.size()-1) == 0) {
					if(e.getKeyCode() == keyCodes[space] || e.getKeyCode() == keyCodes[enter]) {
						switchMenu("add", 1);
					}
				}else {
					if(e.getKeyCode() == keyCodes[space] || e.getKeyCode() == keyCodes[enter]) {
						if(currentMenu() == 1) {
							if(selection == 0) {
								switchMenu("add", 3);
							}else if(selection == 1) {
								switchMenu("add", 2);
							}else if(selection == 2) {
								System.exit(0);
							}
						}else if(currentMenu() == 3) {
							gamemode = selection;
							switchMenu("add", 4);
						}else if(currentMenu() == 4) {
							character = selection;
							switchMenu("add", 5);
						}else if(currentMenu() == 5) {
							stage = selection;
							switchMenu("add", 6);
						}else if(currentMenu() == 6) {
							startStage();
							gameState = 1;
						}
					}
					else if(e.getKeyCode() == keyCodes[w] || e.getKeyCode() == keyCodes[a]) {
						if(selection == 0) {
							selection = selectionLimit;
						}else {
							selection--;
						}
					}else if(e.getKeyCode() == keyCodes[s] || e.getKeyCode() == keyCodes[d]) {
						if(selection == selectionLimit) {
							selection = 0;
						}else {
							selection++;
						}
					}
				}
			}else if(gameState == 1) {
				if(e.getKeyCode() == keyCodes[esc]) {
					selection = 0;
					gameState = 2;
				}else if(e.getKeyCode() == keyCodes[right]){
					currentStage.getPlayer().specialMove();
				}
				for(int i = 0; i < 9; i++) {
					if(e.getKeyCode() == keyCodes[i]) {
						if(pressOnce[i]) {
							if(keysReset[i]) {
								keys[i] = true;
								keysReset[i] = false;
							}else {
								keys[i] = false;
							}
						}else {
							keys[i] = true;
						}
					}
				}
			}else if(gameState == 2){
				if(e.getKeyCode() == keyCodes[esc]) {
					selection = 0;
					gameState = 1;
				}else if(e.getKeyCode() == keyCodes[w] || e.getKeyCode() == keyCodes[a]) {
					if(selection == 0) {
						selection = selectionLimit;
					}else {
						selection--;
					}
				}else if(e.getKeyCode() == keyCodes[s] || e.getKeyCode() == keyCodes[d]) {
					if(selection == selectionLimit) {
						selection = 0;
					}else {
						selection++;
					}
				}else if(e.getKeyCode() == keyCodes[space] || e.getKeyCode() == keyCodes[enter]) {
					if(selection == 0) {
						selection = 0;
						gameState = 1;
					}else if(selection == 1) {
						startStage();
						gameState = 1;
					}else if(selection == 2) {
						switchMenu("remove",0);switchMenu("remove",0);
						gameState = 0;
						currentStage = null;
					}
				}
			}else if(gameState == 3){
				if(e.getKeyCode() == keyCodes[esc]) {
					switchMenu("remove",0);switchMenu("remove",0);
					gameState = 0;
					currentStage = null;
				}else if(e.getKeyCode() == keyCodes[enter]) {
					startStage();
					gameState = 1;
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			for(int i = 0; i < 9; i++) {
				if(e.getKeyCode() == keyCodes[i]) {
					keys[i] = false;
					keysReset[i] = true;
				}
			}
		}
		public void keyTyped(KeyEvent e) {}
	}

	public static BufferedImage flip(BufferedImage i) {
		Picture image = new Picture(i);
		Picture flippedImage = new Picture(image.getWidth(), image.getHeight());

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int newX = x;
				int newY = y;
				if (image.getWidth() % 2 != 0) {
					newX -= (image.getWidth() / 2);
					newX *= -1;
					newX += (image.getWidth() / 2);
				} else {
					newX = ((image.getWidth() - 1) - newX); 
				}
				flippedImage.setBufferedPixel(newX, newY, image.getBufferedPixel(x, y));
			}
		}

		return flippedImage.getImage();
	}

	public static void main(String[] args) {

		BaseEngine b = new BaseEngine(new Dimension(1280, 720));
	}
}