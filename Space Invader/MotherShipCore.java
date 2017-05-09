import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

public class MotherShipCore extends MovingObject {

	// Size of an Alien
	public static final int RADIUS = 5;

	// Mother Ship Core get injured
	private int getInjured = 0;

	// Health of this Mother Ship Core
	private int healthBar = 1000;



	private int timeRandomShoot;

	/**
	 * Create an alien in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Mother Ship Core belongs to
	 * @param center
	 *            the center Point of this Mother Ship Core
	 */
	public MotherShipCore(GWindow window, Point center) {
		super(window, center);
		this.draw();
	}

	public void shoot(SpaceShip spaceShip) {
		if (center.x >= spaceShip.boundingBox.getX()
				&& center.x <= spaceShip.boundingBox.getX()
						+ spaceShip.boundingBox.getWidth()) {
			spaceShip.isShot();
		}

	}

	/**
	 * The Mother Ship Core is being shot Decrement its number of health and
	 * erase it from the graphics window if it is dead.
	 */
	public void isShot() {
		getInjured += 10;
		if (getInjured >= healthBar/10) {
			erase();
		}
	}

	/**
	 * Is this Mother Ship Core dead?
	 */
	public boolean isDead() {
		return getInjured >= healthBar/10;
	}

	/**
	 * Return the location of this Mother Ship Cores
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Move this Mother Ship Core left and right.
	 * 
	 */
	public void move() {
		// Make mother ship core moving left and right.
		if (isMoveZ) {
			center.x += 10;
			if (center.x > (this.window.getWindowWidth() - boundingBox
					.getWidth())) {
				isMoveZ = false;
			}
		} else {
			center.x -= 10;
			if (0 == center.x) {
				isMoveZ = true;
			}

		}
		erase();
		draw();
	}

	/**
	 * Display this Alien in the graphics window
	 */
	protected void draw() {

		this.shapes = new Shape[12];
		this.shapes[0] = new Oval(this.center.x + 15 * RADIUS, this.center.y
				- 15 * RADIUS, 20 * RADIUS, 20 * RADIUS, Color.MAGENTA, true);
		this.shapes[1] = new Oval(this.center.x - 15 * RADIUS, this.center.y
				- 15 * RADIUS, 20 * RADIUS, 20 * RADIUS, Color.magenta, true);
		this.shapes[2] = new Oval(this.center.x - 15 * RADIUS, this.center.y
				- 15 * RADIUS, 50 * RADIUS, 50 * RADIUS, Color.black, true);
		this.shapes[3] = new Oval(this.center.x - 7 * RADIUS, this.center.y
				+ 10 * RADIUS, 35 * RADIUS, 10 * RADIUS, Color.red, true);
		this.shapes[4] = new Oval(this.center.x + 3 * RADIUS, this.center.y
				+ RADIUS, 15 * RADIUS, 15 * RADIUS, Color.blue, true);
		this.shapes[5] = new Oval(this.center.x + 8 * RADIUS, this.center.y
				+ RADIUS, 5 * RADIUS, 15 * RADIUS, Color.yellow, true);
		this.shapes[6] = new Line(this.center.x + 15 * RADIUS, this.center.y
				+ 15 * RADIUS, this.center.x + 23 * RADIUS, this.center.y + 28
				* RADIUS, Color.red);
		this.shapes[7] = new Line(this.center.x + 5 * RADIUS, this.center.y
				+ 15 * RADIUS, this.center.x - 2 * RADIUS, this.center.y + 28
				* RADIUS, Color.red);
		this.shapes[8] = new Oval(this.center.x + 8 * RADIUS, this.center.y + 6
				* RADIUS, 5 * RADIUS, 5 * RADIUS, Color.red, true);

		this.shapes[10] = new Rectangle(this.center.x + RADIUS,
				this.center.y - 30, 95, 30, Color.GREEN, false);
		this.shapes[11] = new Rectangle(this.center.x + (int) (0.5 * RADIUS),
				this.center.y - 28, healthBar / 10 - getInjured, 27,
				Color.GREEN, true);

		// Mother Ship Core randomly shoot.
		timeRandomShoot = (int) (Math.random() * 8);
		if (timeRandomShoot == 1) {
			this.shapes[9] = new  Line(this.center.x + 8 * RADIUS + 10,
					this.center.y + 6 * RADIUS + 10,
					this.center.x + 8 * RADIUS, this.window.getWindowHeight(),
					Color.yellow);
		}

		for (int i = 0; i < this.shapes.length; i++)
			if (this.shapes[i] != null) {
				this.window.add(this.shapes[i]);
			}

		// Bounding box of this mother ship core.
		this.boundingBox = new Rectangle(this.center.x + RADIUS,
				this.center.y - 30, 90, 30);
		this.window.doRepaint();
	}
}
