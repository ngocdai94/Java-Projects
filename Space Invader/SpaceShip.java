import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.Triangle;
import uwcse.io.Sound;

/**
 * The space ship
 */
public class SpaceShip extends MovingObject {
	/** Height of a space ship */
	public static final int HEIGHT = 40;

	/** Width of a space ship */
	public static final int WIDTH = 20;

	/** Is the space ship shooting with its laser? */
	private boolean isShooting;
	
	// Spaceship gets injured
	private int getInjured = 0;

	// Spaceship's health
	private int shipHealth = 200;
	
	// Sound effect
	private static Sound explosion = new Sound("12.wav");

	/**
	 * Construct this SpaceShip
	 */
	public SpaceShip(GWindow window, Point center) {
		super(window, center);
		this.direction = MovingObject.LEFT;
		// Draw this SpaceShip
		this.draw();
	}

	/**
	 * Move this SpaceShip. The space ship should be constantly moving. Select a
	 * new direction if the space ship can't move in the current direction of
	 * motion.
	 */
	public void move() {
		// A space ship moves left or right
		if (this.direction != MovingObject.LEFT
				&& this.direction != MovingObject.RIGHT
				&& this.direction != MovingObject.UP
				&& this.direction != MovingObject.DOWN
				&& this.direction != MovingObject.STOP)
			throw new IllegalArgumentException("Invalid space ship direction");

		// move this SpaceShip
		boolean isMoveOK;
		// Distance covered by the space ship in one step
		int stepWidth = this.boundingBox.getWidth() / 2;
		int stepHeight = this.boundingBox.getWidth() / 2;

		do {
			int x = this.center.x;
			int y = this.center.y;
			switch (this.direction) {
			case UP:
				y -= stepHeight;
				this.direction = LEFT;
				break;
			case DOWN:
				y += stepHeight;
				this.direction = RIGHT;
				break;
			case LEFT:
				x -= stepWidth;
				break;
			case RIGHT:
				x += stepWidth;
				break;
			}

			// Is the new position in the window?
			// past the right edge
			if (x + this.boundingBox.getWidth() / 2 >= this.window
					.getWindowWidth()) {
				isMoveOK = false;
				this.direction = MovingObject.LEFT;

				// past the left edge.
			} else if (x - this.boundingBox.getWidth() / 2 <= 0) {
				isMoveOK = false;
				this.direction = MovingObject.RIGHT;

				// past the top edge
			} else if (y - this.boundingBox.getWidth() * 1.5 <= 0) {
				isMoveOK = true;

				// past the bottom edge
			} else if (y + this.boundingBox.getWidth() / 2 >= this.window
					.getWindowWidth()) {
				isMoveOK = true;

				// it is in the window
			} else {
				isMoveOK = true;
				this.center.x = x;
				this.center.y = y;
			}
		} while (!isMoveOK);

		// Show the new location of this SpaceShip
		this.erase();
		this.draw();
	}

	/**
	 * Shoot at the aliens If an alien is hit, decrease its number of lives or
	 * remove it from the array list if it is dead.
	 * 
	 * @param aliens
	 *            the ArrayList of aliens, aliens1, aliens2, aliens3
	 */
	public void shoot(ArrayList<Alien> aliens, ArrayList<Centipade> aliens2,
			ArrayList<DragonFly> aliens3, ArrayList<MotherShipCore> aliens4) {
		explosion.play();
		this.isShooting = true;
		
		// Is an alien shot?
		for (int i = 0; i < aliens.size(); i++) {
			Alien a = aliens.get(i);
			Rectangle box = a.getBoundingBox();
			if (center.x >= box.getX()
					&& center.x <= box.getX() + box.getWidth()) {
				a.isShot();
			}
		}
		
		// Is Centipade shot?
		if (aliens2 != null) {
			for (int i = 0; i < aliens2.size(); i++) {
				Centipade b = aliens2.get(i);
				Rectangle box = b.boundingBox;
				if (center.x >= box.getX()
						&& center.x <= box.getX() + box.getWidth()) {
					b.isShot();
				}
			}
		}
		
		// Is Dragon Fly shot?
		if (aliens3 != null) {
			for (int i = 0; i < aliens3.size(); i++) {
				DragonFly b = aliens3.get(i);
				Rectangle box = b.boundingBox;
				if (center.x >= box.getX()
						&& center.x <= box.getX() + box.getWidth()) {
					b.isShot();
				}
			}
		}
		
		// Is MotherShipCore shot?
		if (aliens4 != null) {
			for (int i = 0; i < aliens4.size(); i++) {
				MotherShipCore b = aliens4.get(i);
				Rectangle box = b.boundingBox;
				if (center.x >= box.getX()
						&& center.x <= box.getX() + box.getWidth()) {
					b.isShot();
				}
			}
		}

	}
	
	/**
	 * The Spaceship is being shot or hit Decrement its number of health and erase it from
	 * the graphics window if it is dead.
	 */
	public void isShot() {
		getInjured += 10;
		if (getInjured >= shipHealth) {
			erase();
		}
	}

	/**
	 * Is this Ship dead?
	 */
	public boolean isDead() {
		return getInjured >= shipHealth;
	}

	/**
	 * Draw this SpaceShip in the graphics window
	 */
	protected void draw() {
		this.shapes = new Shape[7];

		// Body of the space ship
		Rectangle body = new Rectangle(this.center.x - SpaceShip.WIDTH / 2,
				this.center.y - SpaceShip.HEIGHT / 2, SpaceShip.WIDTH,
				SpaceShip.HEIGHT, Color.cyan, true);

		this.shapes[0] = body;

		// Cone on top
		int x1 = body.getX();
		int y1 = body.getY();
		int x2 = x1 + body.getWidth();
		int y2 = y1;
		int x3 = body.getCenterX();
		int y3 = y1 - body.getWidth();
		this.shapes[1] = new Triangle(x1, y1, x2, y2, x3, y3, Color.pink, true);

		// Show the laser beam if the rocket is shooting
		if (this.isShooting) {
			this.shapes[4] = new Line(x3, y3, x3, 0, Color.yellow);
			this.isShooting = false;
		}

		// Wings on the sides
		x1 = body.getX();
		y1 = body.getY() + body.getHeight();
		x2 = body.getX() - body.getWidth() / 2;
		y2 = y1;
		x3 = x1;
		y3 = y1 - body.getWidth() / 2;
		this.shapes[2] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);
		x1 = body.getX() + body.getWidth();
		x2 = x1 + body.getWidth() / 2;
		x3 = x1;
		this.shapes[3] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);

		this.shapes[5] = new Rectangle(10, 20, shipHealth + 3, 30, Color.green,
				false);
		this.shapes[6] = new Rectangle(12, 22, shipHealth - getInjured, 27,
				Color.green, true);

		// The bounding box of this SpaceShip
		this.boundingBox = this.shapes[0].getBoundingBox();

		// Put everything in the window
		for (int i = 0; i < this.shapes.length; i++)
			if (this.shapes[i] != null)
				this.window.add(this.shapes[i]);

		this.window.doRepaint();
	}
}
