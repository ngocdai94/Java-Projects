
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import uwcse.graphics.GWindow;
import uwcse.graphics.GWindowEvent;
import uwcse.graphics.GWindowEventAdapter;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;

/**
 * A SpaceInvader displays a fleet of alien ships and a space ship. The player
 * directs the moves of the spaceship and can shoot at the aliens.
 */

public class SpaceInvader extends GWindowEventAdapter {
	// Possible actions from the keyboard
	/** No action */
	public static final int DO_NOTHING = 0;

	/** Steer the space ship */
	public static final int SET_SPACESHIP_DIRECTION = 1;

	/** To shoot at the aliens */
	public static final int SHOOT = 2;

	// Period of the animation (in ms)
	// (the smaller the value, the faster the animation)
	private int animationPeriod = 100;

	// Current action from the keyboard
	private int action;

	// Game window
	private GWindow window;

	// The space ship
	private SpaceShip spaceShip;

	private MotherShipCore boss;

	// Direction of motion given by the player
	private int dirFromKeyboard = MovingObject.LEFT;

	// The aliens
	private ArrayList<Alien> aliens;

	private ArrayList<Centipade> aliens2;

	private ArrayList<DragonFly> aliens3;

	private ArrayList<MotherShipCore> aliens4;

	// Count games
	private int countGame;


	/**
	 * Construct a space invader game
	 */
	public SpaceInvader() {
		this.window = new GWindow("Space invaders", 500, 500);
		this.window.setExitOnClose();
		this.window.addEventHandler(this); // this SpaceInvader handles all of
		// the events fired by the graphics
		// window

		// Display the game rules
		String rulesOfTheGame = "Save the Earth! Destroy all of the aliens ships.\n"
				+ "To move left, press '<'.\n"
				+ "To move right, press '>'.\n"
				+ "To shoot, press the space bar.\n" + "To quit, press 'Q'.";
		JOptionPane.showMessageDialog(null, rulesOfTheGame, "Space invaders",
				JOptionPane.INFORMATION_MESSAGE);
		String AInfor = "                        X\n	"
				+ "Weekness: just shoot       Speed:*\n" + "Difficulty:*";
		JOptionPane.showMessageDialog(null, AInfor, "Allien 101: Minion X",
				JOptionPane.INFORMATION_MESSAGE);

		this.initializeGame();
	}

	/**
	 * Initialize the game (draw the background, aliens, and space ship)
	 */
	private void initializeGame() {
		countGame++;
		// Clear the window
		this.window.erase();

		
		Rectangle background = backGround();
		// ArrayList of aliens
		this.aliens = new ArrayList<Alien>();

		// Create 12 aliens
		// Initial location of the aliens
		// (Make sure that the space ship can fire at them)
		int x = 48;
		int y = 0;
		for (int i = 0; i < 12; i++) {
			y = (int) (Math.random() * (background.getHeight() / 2));
			this.aliens.add(new Alien(this.window, new Point(x, y)));
			x += Math.random() * (background.getWidth() / 8);
		}

		// draw the space ship
		spaceShip();
		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	private void initializeGame2() {

		animationPeriod = 100;
		countGame++;

		String AInfor = "You have cleared the first level\n"
				+ "More enermies are coming";
		JOptionPane.showMessageDialog(null, AInfor, "Congratuation",
				JOptionPane.INFORMATION_MESSAGE);

		// Show the information
		String AInfor1 = "                      XO0oo \n	"
				+ "Weekness: the head       Speed:***\n" + "Difficulty:**";
		JOptionPane.showMessageDialog(null, AInfor1, "Allien 101: Centipade",
				JOptionPane.INFORMATION_MESSAGE);

		String AInfor2 = "                      > O < \n	"
				+ "                         A\n" +

				"Weekness: No     Speed:****\n" + "Difficulty:****";
		JOptionPane.showMessageDialog(null, AInfor2, "Allien 101: Dragonfly",
				JOptionPane.INFORMATION_MESSAGE);
		// Clear the window
		this.window.erase();

	
		Rectangle background = backGround();
		// ArrayList of aliens
		this.aliens2 = new ArrayList<Centipade>();
		this.aliens3 = new ArrayList<DragonFly>();

		// Create 12 aliens
		// Initial location of the aliens
		// (Make sure that the space ship can fire at them)
		int x = 70;
		int y = 30; // 10 * Alien.RADIUS;
		for (int i = 0; i < 5; i++){
			y = (int) (Math.random() * (background.getHeight() / 2));
			
			
			this.aliens3.add(new DragonFly(this.window, new Point(x + 10,
					y + 20)));
			x +=  Math.random()*(background.getWidth() / 8);
		}

		x = 60;
		y = 40;
		for (int j = 0; j < 7; j++) {
			y = (int) (Math.random() * (background.getHeight() / 2));
			this.aliens2.add(new Centipade(this.window,
					new Point(x + 5, y - 10)));
			x += background.getWidth() / 8;
		}

		
		spaceShip();
		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	private void initializeGame3() {
		animationPeriod = 65;
		countGame++;
		String AInfor1 = "            Killed or get killed          	";
		JOptionPane.showMessageDialog(null, AInfor1, "WARNING",
				JOptionPane.INFORMATION_MESSAGE);
		String AInfor3 = "                (                       )\n"
				+ "                            O\n"
				+ "                    ^vvvvvvv^\n"
				+ "                            A         \n" +

				"Weekness: unknow     Speed:****\n" + "Difficulty:**********";
		JOptionPane.showMessageDialog(null, AInfor3,
				"Alien 101: Mothership Core", JOptionPane.INFORMATION_MESSAGE);

		// Clear the window
		this.window.erase();

		// Background (starry universe)
		Rectangle background = backGround();

		// ArrayList of aliens
		// this.aliens2 = new ArrayList<Centipade>();
		// this.aliens3 = new ArrayList<DragonFly>();
		this.aliens4 = new ArrayList<MotherShipCore>();
	

		// Create 12 aliens
		// Initial location of the aliens
		// (Make sure that the space ship can fire at them)
		int x = 40;
		int y = 100; // 10 * Alien.RADIUS;
		
			// if (y == 0) {
			this.aliens4.add(new MotherShipCore(this.window, new Point(x, y)));

			spaceShip();
		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	/**
	 * Move the objects within the graphics window every time the timer fires an
	 * event
	 */
	public void timerExpired(GWindowEvent we) {
		// Perform the action requested by the user?
		switch (this.action) {
		case SpaceInvader.SET_SPACESHIP_DIRECTION:
			this.spaceShip.setDirection(this.dirFromKeyboard);
			break;
		case SpaceInvader.SHOOT:
			this.spaceShip.shoot(this.aliens, this.aliens2, this.aliens3,
					this.aliens4);
			break;
		}
		if (this.boss != null) {
			this.boss.shoot(this.spaceShip);
		}
		this.action = SpaceInvader.DO_NOTHING; // Don't do the same action
		// twice

		// Show the new locations of the objects
		this.updateGame();

	}

	/**
	 * Select the action requested by the pressed key
	 */
	public void keyPressed(GWindowEvent e) {
		// Don't perform the actions (such as shoot) directly in this method.
		// Do the actions in timerExpired, so that the alien ArrayList can't be
		// modified at the same time by two methods (keyPressed and timerExpired
		// run in different threads).

		switch (Character.toLowerCase(e.getKey())) { // not case sensitive
		// Put here the code to move the space ship with the < and > keys

		case ' ': // shoot at the aliens
			this.action = SpaceInvader.SHOOT;
			break;
		case 'm':
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.UP;
			break;
		case 'n':
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.DOWN;
			break;
		case ',': // < if uppercase --> left
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.LEFT;
			break;
		case '.': // > if uppercase --> right
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.RIGHT;
			break;
		case 'q': // quit the game (BlueJ might not like that one)
			System.exit(0);

		default: // no new action
			this.action = SpaceInvader.DO_NOTHING;
			break;
		}
	}

	/**
	 * Update the game (Move aliens + space ship)
	 */
	private void updateGame() {
		// Is the game won (or lost)?
		// Put here code to end the game (= no more aliens)
		if (countGame == 1 && aliens.size() == 0) {
			initializeGame2();
		} else if (countGame == 2 && aliens2.size() == 0 && aliens3.size() == 0) {
			initializeGame3();
		} else if (countGame == 3 && aliens4.size() == 0) {
			anotherGame("Congratulation, You saved the Earth !!!!!");
		}
		
		// Remove dead spaceship and print out Lose notification.
		if (spaceShip.isDead()) {
			spaceShip.erase();
			anotherGame("You cannot save the World, Try Again !!!!");
		}

		// to speed up the drawing
		this.window.suspendRepaints();

		// Remove dead aliens from ArrayList
		if (aliens2 != null) {
			Iterator<Centipade> a2 = aliens2.iterator();
			while (a2.hasNext()) {
				Centipade a = a2.next();
				if (a.isDead()) {
					a2.remove();
				}
			}
		}

		if (aliens3 != null) {
			Iterator<DragonFly> a3 = aliens3.iterator();
			while (a3.hasNext()) {
				DragonFly a = a3.next();
				if (a.isDead()) {
					a3.remove();
				}
			}
		}

		if (aliens4 != null) {
			Iterator<MotherShipCore> a4 = aliens4.iterator();
			while (a4.hasNext()) {
				MotherShipCore a = a4.next();
				if (a.isDead()) {
					a4.remove();
				}
			}
		}

		Iterator<Alien> it = aliens.iterator();
		while (it.hasNext()) {
			Alien a = it.next();
			if (a.isDead()) {
				it.remove();
			}
		}

		// Move the aliens
		if (countGame == 1) {
			for (Alien a : aliens) {
				a.move();
				if (spaceShip.boundingBox.getY() <= a.boundingBox.getY()) {
					if (a.boundingBox.getX() >= spaceShip.boundingBox.getX() - 5
							&& a.boundingBox.getX() <= spaceShip.boundingBox
									.getX() + spaceShip.boundingBox.getWidth()) {
						spaceShip.isShot();
						break;
					}
				}
			}
		}

		if (countGame == 2) {
			for (Centipade c : aliens2) {
				c.move();
				if (spaceShip.boundingBox.getY() <= c.boundingBox.getY()) {
					if (c.boundingBox.getX() >= spaceShip.boundingBox.getX() - 5
							&& c.boundingBox.getX() <= spaceShip.boundingBox
									.getX() + spaceShip.boundingBox.getWidth()) {
						spaceShip.isShot();
						break;
					}
				}
			}

			for (DragonFly d : aliens3) {
				d.move();
				if (spaceShip.boundingBox.getY() <= d.boundingBox.getY()) {
					if (d.boundingBox.getX() >= spaceShip.boundingBox.getX() - 5
							&& d.boundingBox.getX() <= spaceShip.boundingBox
									.getX() + spaceShip.boundingBox.getWidth()) {
						spaceShip.isShot();
						break;
					}
				}
			}
		}

		if (countGame == 3) {
			for (MotherShipCore b : aliens4) {
				b.move();
				if (spaceShip.boundingBox.getY() <= b.boundingBox.getY()) {
					if (b.boundingBox.getX() >= spaceShip.boundingBox.getX() - 5
							&& b.boundingBox.getX() <= spaceShip.boundingBox
									.getX() + spaceShip.boundingBox.getWidth()) {
						spaceShip.isShot();
						break;
					}

				} else if ((b.boundingBox.getCenterX() >= spaceShip.boundingBox
						.getX() && b.getBoundingBox().getCenterX() <= spaceShip.boundingBox
						.getX() + spaceShip.getBoundingBox().getWidth())) {
					spaceShip.isShot();
				}
			}

		}

		// Move the space ship
		this.spaceShip.move();

		// Display it all
		this.window.resumeRepaints();
	}

	/**
	 * Does the player want to play again?
	 */
	public boolean anotherGame(String s) {
		// this method is useful at the end of a game if you want to prompt the
		// user
		// for another game (s would be a String describing the outcome of the
		// game
		// that just ended, e.g. "Congratulations, you saved the Earth!")
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		countGame = 0;
		animationPeriod = 100;
		this.initializeGame();
		return choice == JOptionPane.YES_OPTION;
	}
/**
 * Draw the back ground
 * @return the Rectangle of back ground
 */
	public Rectangle backGround() {
		// Background (starry universe)
		Rectangle background = new Rectangle(0, 0,
				this.window.getWindowWidth(), this.window.getWindowHeight(),
				Color.black, true);
		this.window.add(background);
		// Add 50 stars here and there (as small circles)
		Random rnd = new Random();
		for (int i = 0; i < 60; i++) {
			// Random radius between 1 and 3
			int radius = rnd.nextInt(3) + 1;
			// Random location (within the window)
			// Make sure that the full circle is visible in the window
			int x = rnd.nextInt(this.window.getWindowWidth() - 2 * radius);
			int y = rnd.nextInt(this.window.getWindowHeight() - 2 * radius);
			this.window.add(new Oval(x, y, 2 * radius, 2 * radius, Color.white,
					true));
		}
		return background;
	}
	/**
	 * Draw the space ship
	 */
	public void spaceShip(){
		// Create the space ship at the bottom of the window
				int x = this.window.getWindowWidth() / 2;
				int y = this.window.getWindowHeight() - SpaceShip.HEIGHT / 2;
				this.spaceShip = new SpaceShip(this.window, new Point(x, y));
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new SpaceInvader();
	}
}
