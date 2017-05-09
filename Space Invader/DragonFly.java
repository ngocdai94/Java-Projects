import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.Triangle;

public class DragonFly extends MovingObject {

	// Size of an Alien
	public static final int RADIUS = 5;

	// Number of lives in this Dragon Fly
	// When 0, this Dragon Fly is dead
	private int lives;

	private static Color[] colors = { Color.RED, Color.GREEN, Color.BLUE,
			Color.black, Color.cyan };

	/**
	 * Create an Dragon Fly in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Dragon Fly belongs to
	 * @param center
	 *            the center Point of this Dragon Fly
	 */
	public DragonFly(GWindow window, Point center) {
		super(window, center);
		this.lives = (int) (Math.random() * colors.length + 1);

		// Display this Alien
		this.draw();
	}

	/**
	 * The Dragon Fly is being shot Decrement its number of lives and erase it
	 * from the graphics window if it is dead.
	 */
	public void isShot() {
		lives--;
		if (lives == 0) {
			erase();
		}
	}

	/**
	 * Is this Dragon Fly dead?
	 */
	public boolean isDead() {
		return this.lives == 0;
	}

	/**
	 * Return the location of this Dragon Fly
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Move this Dragon Fly As a start make all of the aliens move downward in Z
	 * order. If an alien reaches the bottom of the screen, it reappears at the
	 * top.
	 */
	public void move() {

		
		// Make dragon fly moving in Z order.
		if (isMoveZ) {
			center.x += 70;
			 this.isMoveZ = false;

		} else {
			center.x -= 70;
			this.isMoveZ = true;
		}
		center.y += 10;
		if (center.y >= this.window.getWindowHeight()) {
			center.y = 0;
		}
		

		erase();
		draw();
	}

	/**
	 * Display this Alien in the graphics window
	 */
	protected void draw() {
		// pick the color (according to the number of lives left)
		Color color = colors[lives - 1];

		this.shapes = new Shape[4];
		this.shapes[0] = new Triangle(this.center.x - 2 * RADIUS, this.center.y
				- RADIUS, this.center.x - 2 * RADIUS, this.center.y + RADIUS,
				this.center.x - RADIUS, this.center.y, color, true);
		this.shapes[1] = new Triangle(this.center.x + 2 * RADIUS, this.center.y
				- RADIUS, this.center.x + 2 * RADIUS, this.center.y + RADIUS,
				this.center.x + RADIUS, this.center.y, color, true);
		this.shapes[2] = new Triangle(this.center.x - RADIUS, this.center.y + 2
				* RADIUS, this.center.x + RADIUS, this.center.y + 2 * RADIUS,
				this.center.x, this.center.y + RADIUS, color, true);
		this.shapes[3] = new Oval(this.center.x - RADIUS, this.center.y
				- RADIUS, 2 * RADIUS, 2 * RADIUS, color, true);

		for (int i = 0; i < this.shapes.length; i++) {
			this.window.add(this.shapes[i]);
			if (i <= 1) {
				this.boundingBox = this.shapes[i].getBoundingBox();
			}
		}
		// Bounding box of this dragon fly
		this.boundingBox = new Rectangle(this.center.x + 2 * RADIUS,
				this.center.y - RADIUS, 4 * RADIUS, 4 * RADIUS, color, true);

		this.window.doRepaint();
	}

}
