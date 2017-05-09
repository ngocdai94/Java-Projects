import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

public class Centipade extends MovingObject {

	// Size of an Centipade
	public static final int RADIUS = 5;

	// Number of lives in this Centipade
	// When 0, this Centipade is dead
	private int lives;

	private static Color[] colors = { Color.RED, Color.GREEN, Color.BLUE };


	/**
	 * Create an Centipade in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Centipade belongs to
	 * @param center
	 *            the center Point of this Centipade
	 */
	public Centipade(GWindow window, Point center) {
		super(window, center);
		this.lives = (int) (Math.random() * colors.length + 1);

		// Display this Alien
		this.draw();
	}

	/**
	 * The Centipade is being shot Decrement its number of lives and erase it from
	 * the graphics window if it is dead.
	 */
	public void isShot() {
		lives--;
		if (lives == 0) {
			erase();
		}
	}

	/**
	 * Is this Centipade dead?
	 */
	public boolean isDead() {
		return this.lives == 0;
	}

	/**
	 * Return the location of this Centipade
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Move this Centipade As a start make all of the aliens move downward in Z order. If an
	 * Centipede reaches the bottom of the screen, it reappears at the top.
	 */
	public void move() {
		if (isMoveZ) {
			center.x += 10;
			 this.isMoveZ = false;

		} else {
			center.x -= 10;
			this.isMoveZ = true;
		}
		center.y += 7;
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
		Color color1 = colors[lives - 1];

		this.shapes = new Shape[8];
		int j = 1;

		this.shapes[0] = new Line(this.center.x - 2 * RADIUS, this.center.y - 2
				* RADIUS, this.center.x + 2 * RADIUS, this.center.y + 2
				* RADIUS, color1);
		this.shapes[1] = new Line(this.center.x + 2 * RADIUS, this.center.y - 2
				* RADIUS, this.center.x - 2 * RADIUS, this.center.y + 2
				* RADIUS, color1);
		this.shapes[2] = new Oval(this.center.x - RADIUS, this.center.y
				- RADIUS, 2 * RADIUS, 2 * RADIUS, color1, true);
		for (int i = 0; i <= 2; i++) {
			this.window.add(this.shapes[i]);
		}

		for (int i = 3; i < this.shapes.length; i++) {
			if (i % 2 == 0) {

				this.shapes[i] = new Oval(this.center.x + j * RADIUS,
						this.center.y - RADIUS / 2, 2 * RADIUS, 2 * RADIUS,
						Color.gray, true);
				this.window.add(this.shapes[i]);
				j++;
			} else {
				this.shapes[i] = new Oval(this.center.x + j * RADIUS,
						this.center.y - RADIUS, 2 * RADIUS, 2 * RADIUS,
						Color.gray, true);
				this.window.add(this.shapes[i]);
				j++;

			}
			this.window.add(this.shapes[i]);
		}

		// Bounding box of this Centipade
		this.boundingBox = new Rectangle(this.center.x - 2 * RADIUS,
				this.center.y - 2 * RADIUS, 4 * RADIUS, 4 * RADIUS);

		this.window.doRepaint();
	}

}
