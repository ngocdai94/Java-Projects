/*
 * File: AdvRoom.java
 * ------------------
 * This file defines a class that models a single room in the
 * Adventure game.
 */

import java.util.*;

/* Class: AdvRoom */
/**
 * This class defines a single room in the Adventure game. A room is
 * characterized by the following properties:
 * 
 * <ul>
 * <li>A room number, which must be greater than zero
 * <li>Its name, which is a one-line string identifying the room
 * <li>Its description, which is a multiline array describing the room
 * <li>A list of objects contained in the room
 * <li>A flag indicating whether the room has been visited
 * <li>A motion table specifying the exits and where they lead</li>
 * 
 * The external format of the room data file is described in the assignment
 * handout. The comments on the methods exported by this class show how to use
 * the initialized data structure.
 */

public class AdvRoom {

	/* Method: getRoomNumber() */
	/**
	 * Returns the room number.
	 * 
	 * @usage int roomNumber = room.getRoomNumber();
	 * @return The room number
	 */
	public int getRoomNumber() {
		return this.roomNumber;
	}

	/* Method: getName() */
	/**
	 * Returns the room name, which is its one-line description.
	 * 
	 * @usage String name = room.getName();
	 * @return The room name
	 */
	public String getName() {
		return this.roomName;
	}

	/* Method: getDescription() */
	/**
	 * Returns an array of strings that correspond to the long description of
	 * the room (including the list of the objects in the room).
	 * 
	 * @usage String[] description = room.getDescription();
	 * @return An array of strings giving the long description of the room
	 */
	public String[] getDescription() {
		// make a new array string
		String[] newDescription = new String[this.description.length
				+ this.getObjectCount()];
		// copy the original to the new one
		System.arraycopy(this.description, 0, newDescription, 0,
				this.description.length);
		// add String to a new one
		for (int i = 0; i < this.getObjectCount(); i++) {
			newDescription[this.description.length + i] = "There is "
					+ this.getObject(i).getDescription() + ".";
		}
		return newDescription;
	}

	/* Method: addObject(obj) */
	/**
	 * Adds an object to the list of objects in the room.
	 * 
	 * @usage room.addObject(obj);
	 * @param The
	 *            AdvObject to be added
	 */
	public void addObject(AdvObject obj) {
		this.objects.add(obj);
	}

	/* Method: removeObject(obj) */
	/**
	 * Removes an object from the list of objects in the room.
	 * 
	 * @usage room.removeObject(obj);
	 * @param The
	 *            AdvObject to be removed
	 */
	public void removeObject(AdvObject obj) {
		this.objects.remove(obj);
	}

	/* Method: containsObject(obj) */
	/**
	 * Checks whether the specified object is in the room.
	 * 
	 * @usage if (room.containsObject(obj)) . . .
	 * @param The
	 *            AdvObject being tested
	 * @return true if the object is in the room, and false otherwise
	 */
	public boolean containsObject(AdvObject obj) {
		if (objects != null) {
			for (AdvObject o : objects) {
				if (o.equals(obj)) {
					return true;
				}
			}
		}
		return false;
	}

	/* Method: getObjectCount() */
	/**
	 * Returns the number of objects in the room.
	 * 
	 * @usage int nObjects = room.getObjectCount();
	 * @return The number of objects in the room
	 */
	public int getObjectCount() {
		return this.objects.size();
	}

	/* Method: getObject(index) */
	/**
	 * Returns the specified element from the list of objects in the room.
	 * 
	 * @usage AdvObject obj = room.getObject(index);
	 * @return The AdvObject at the specified index position
	 */
	public AdvObject getObject(int index) {
		return objects.get(index);
	}

	/* Method: setVisited(flag) */
	/**
	 * Sets the flag indicating that this room has been visited according to the
	 * value of the parameter. Calling setVisited(true) means that the room has
	 * been visited; calling setVisited(false) restores its initial unvisited
	 * state.
	 * 
	 * @usage room.setVisited(flag);
	 * @param flag
	 *            The new state of the "visited" flag
	 */
	public void setVisited(boolean flag) {
		this.flag = flag;
	}

	/* Method: hasBeenVisited() */
	/**
	 * Returns true if the room has previously been visited.
	 * 
	 * @usage if (room.hasBeenVisited()) . . .
	 * @return true if the room has been visited; false otherwise
	 */
	public boolean hasBeenVisited() {
		return flag;
	}

	/* Method: getMotionTable() */
	/**
	 * Returns the motion table associated with this room, which is an array of
	 * directions, room numbers, and enabling objects stored in a
	 * AdvMotionTableEntry.
	 * 
	 * @usage AdvMotionTableEntry[] motionTable = room.getMotionTable();
	 * @return The array of motion table entries associated with this room
	 */
	public AdvMotionTableEntry[] getMotionTable() {
		return this.motionTable;
	}

	/* Method: readFromFile(rd) */
	/**
	 * Reads the data for this room from the Scanner scan, which must have been
	 * opened by the caller. This method returns a room if the room
	 * initialization is successful; if there are no more rooms to read,
	 * readFromFile returns null.
	 * 
	 * @usage AdvRoom room = AdvRoom.readFromFile(scan);
	 * @param scan
	 *            A scanner open on the rooms data file
	 * @return a room if successfully read; null if at end of file
	 */
	public static AdvRoom readFromFile(Scanner scan) {
		AdvRoom room = new AdvRoom();
		room.roomNumber = scan.nextInt();
		scan.nextLine();
		room.roomName = scan.nextLine();
		// make an ArrayList used to store temporary description
		List<String> tempList = new ArrayList<String>();
		// text for the description
		String line;
		while (!(line = scan.nextLine()).equals(DELIMITER1)) {
			// add text to description
			tempList.add(line);
		}
		// make temporary array list motionTable
		ArrayList<AdvMotionTableEntry> tempTable = new ArrayList<AdvMotionTableEntry>();
		// direction commands and the objects(if it has)
		while (scan.hasNext() && (line = scan.nextLine()).trim().length() > 0) {
			String[] parts = line.split(DELIMITER2);
			// direction
			String dir = parts[0];
			// the destination room number
			int number;
			// the object
			String item = null;
			// check to see if the last index has or doesn't has "/"
			boolean hasDelimiter = false;
			for (int i = 0; i < parts[parts.length - 1].length(); i++) {
				if (parts[parts.length - 1].charAt(i) == '/') {
					hasDelimiter = true;
				}
			}
			if (hasDelimiter) {
				String[] lastIndex = parts[parts.length - 1].trim().split(
						DELIMITER3);
				number = Integer.parseInt(lastIndex[0]);
				item = lastIndex[1];
			} else {
				number = Integer.parseInt(parts[parts.length - 1]);
			}
			// add these to temporary AdvMotionTableEntry list
			tempTable.add(new AdvMotionTableEntry(dir, number, item));
		}
		// give the temp data to this field
		room.description = tempList.toArray(new String[0]);
		room.motionTable = tempTable.toArray(new AdvMotionTableEntry[0]);
		// declare the list of objects in this room
		room.objects = new ArrayList<AdvObject>();
		return room;
	}

	/* Private instance variables */
	// Add your own instance variables here
	private int roomNumber;
	private String roomName;
	private String[] description;
	private List<AdvObject> objects;
	private boolean flag;
	private AdvMotionTableEntry[] motionTable;
	private static final String DELIMITER1 = "-----";
	private static final String DELIMITER2 = " ";
	private static final String DELIMITER3 = "/";
}
