/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure {

	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);

	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {
		// read file
		String fName = null;
		try {
			System.out.print("What will be your adventure today? ");
			fName = scan.nextLine();
			String cap = fName.substring(0, 1).toUpperCase();
			String lower = fName.substring(1, fName.length()).toLowerCase();
			fName = cap + lower;
			Adventure game = createGame(fName);
			game.run();
		} catch (IOException e) {
			System.out.println("Could not find the file which begins with "
					+ fName + ".");
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Please enter a valid name.");
			main(args);
		}
	}

	/**
	 * Create the scene of the games
	 * 
	 * @param fName
	 *            first few words of the file
	 * @return a scene of adventure game
	 * @throws IOException
	 */
	private static Adventure createGame(String fName) throws IOException {
		Adventure game = new Adventure();
		Scanner scan = new Scanner(new File(fName + "Rooms.txt"));

		// add data from room to the roomMap
		while (scan.hasNextInt()) {
			AdvRoom room = AdvRoom.readFromFile(scan);
			game.roomMap.put(room.getRoomNumber(), room);
		}
		scan.close();

		// add and save some of the command
		String[] commandKeys = { "QUIT", "TAKE", "DROP", "INVENTORY", "HELP",
				"LOOK" };
		AdvCommand[] commands = { AdvCommand.QUIT, AdvCommand.TAKE,
				AdvCommand.DROP, AdvCommand.INVENTORY, AdvCommand.HELP,
				AdvCommand.LOOK };
		for (int i = 0; i < commandKeys.length; i++) {
			game.commandMap.put(commandKeys[i], commands[i]);
		}

		// in case we choose tiny room
		if (fName.equals("Tiny")) {
			return game;
		}

		// for small and crowther rooms

		// read the objects
		scan = new Scanner(new File(fName + "Objects.txt"));
		while (scan.hasNextLine()) {
			AdvObject objects = AdvObject.readFromFile(scan);
			// add objects to the room which is supposed to have it
			game.roomMap.get(objects.getInitialLocation()).addObject(objects);
			// put on the map
			game.objectMap.put(objects.getName(), objects);
		}
		scan.close();

		// read the synonyms
		scan = new Scanner(new File(fName + "Synonyms.txt"));
		String line;
		while (scan.hasNextLine()
				&& (line = scan.nextLine()).trim().length() > 0) {
			game.commandList.add(line.split(DELIMITER));
		}
		scan.close();
		return game;
	}

	/**
	 * Run the adventure game
	 */
	private void run() {
		this.executeLookCommand();
		do {
			if (currentRoom.hasBeenVisited() && showName) {
				System.out.println(currentRoom.getName());
			} else if (!currentRoom.hasBeenVisited()) {
				for (String s : currentRoom.getDescription()) {
					System.out.println(s);
				}
				currentRoom.setVisited(true);
			}
			System.out.print("> ");
			String answer = scan.nextLine().toUpperCase();

			// change showName
			showName = true;
			// Check to see if the answer fits
			// to the synonyms list
			try {
				String obj = null;
				if (answer.contains(" ")) {
					String[] a = answer.split("\\s+");
					answer = a[0];
					obj = a[a.length - 1];
				}

				// divide the answer if the answer
				// have space like "take keys"
				for (String[] aList : commandList) {
					if (answer.equals(aList[0])) {
						answer = aList[aList.length - 1];
					} else if (answer.equals("")) {
						showName = false;
						break;
					}
					// store it in obj if we have object
					if (obj == null) {
						continue;
					} else if (obj.equals(aList[0])) {
						obj = aList[aList.length - 1];
					}
				}

				// divide the answer into two types, command and direction
				if (answer.contains("QUIT") || answer.contains("TAKE")
						|| answer.contains("INVENTORY")
						|| answer.contains("DROP") || answer.contains("HELP")
						|| answer.contains("LOOK")) {
					commandMap.get(answer).execute(this,
							(obj != null) ? objectMap.get(obj) : null);
					showName = false;
				} else if (!answer.equals("")) {
					new AdvMotionCommand(answer).execute(this, null);
				}
			} catch (NullPointerException e) {
				System.out.println("There is something wrong.");
				showName = false;
			}
		} while (running);
	}

	/* Method: executeMotionCommand(direction) */
	/**
	 * Executes a motion command. This method is called from the
	 * AdvMotionCommand class to move to a new room.
	 * 
	 * @param direction
	 *            The string indicating the direction of motion
	 */
	public void executeMotionCommand(String direction) {
		AdvRoom prevRoom = currentRoom;
		// check to see which destination the program aims to
		boolean hasDir = false;
		for (AdvMotionTableEntry advTable : currentRoom.getMotionTable()) {
			if (advTable.getDirection().equals(direction)) {
				if (inventory.isEmpty() && advTable.getKeyName() == null) {
					currentRoom = roomMap.get(advTable.getDestinationRoom());
					hasDir = true;
					break;
				} else {
					for (AdvObject obj : inventory) {
						if (advTable.getKeyName() != null
								&& advTable.getKeyName().equals(obj.getName())) {
							currentRoom = roomMap.get(advTable
									.getDestinationRoom());
							hasDir = true;
							break;
						} else {
							currentRoom = roomMap.get(advTable
									.getDestinationRoom());
						}
					}
					if (hasDir) {
						break;
					}
				}
			}
		}

		// announce error if the direction is not justified
		if (prevRoom.equals(currentRoom)) {
			System.out.println("Unvailable command.");
			showName = false;
		}

		// We should do something if the new current direction
		// has "FORCED", it should automatically go to
		// the next room without asking the input
		hasDir = false;
		boolean showDescription = false;

		// if table has force, force will be there no matter
		// which position it is in
		AdvMotionTableEntry[] table = currentRoom.getMotionTable();
		while (running && table.length > 0
				&& table[0].getDirection().equals("FORCED")) {
			for (AdvMotionTableEntry advTable : currentRoom.getMotionTable()) {
				if (!advTable.getDirection().equals("FORCED")) {
					break;
				} else {
					if (!showDescription) {
						for (String s : currentRoom.getDescription()) {
							System.out.println(s);
						}
					}
					if (advTable.getDestinationRoom() == 0) {
						showDescription = true;
						running = false;
						break;
					} else {
						if (advTable.getKeyName() != null) {
							for (AdvObject obj : inventory) {
								if (obj.getName() != null
										&& obj.getName().equals(
												advTable.getKeyName())) {
									currentRoom = roomMap.get(advTable
											.getDestinationRoom());
									hasDir = true;
									break;
								}
							}
							if (hasDir) {
								break;
							}
						} else {
							currentRoom = roomMap.get(advTable
									.getDestinationRoom());
						}
					}
				}
			}
			// check to see if the next room still have force
			// if yes we keep running the loop
			table = currentRoom.getMotionTable();
		}
	}

	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm
	 * the quit request and, if so, should exit from the play method. If not,
	 * the program should continue as usual.
	 */
	public void executeQuitCommand() {
		System.out.print("Are you sure (Y/N)? ");
		String answer = scan.nextLine().toUpperCase();
		if (answer.equals("Y")) {
			running = false;
		} else if (answer.equals("N")) {
			return;
		} else {
			this.executeQuitCommand();
		}
	}

	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for
	 * the user.
	 */
	public void executeHelpCommand() {
		// this one should show the direction
		// and objects that are available for
		// the player
		System.out.println("List of some available ways: ");
		System.out.println();
		for (AdvMotionTableEntry step : currentRoom.getMotionTable()) {
			System.out
					.println("Direction: "
							+ step.getDirection()
							+ " - Needed Item: "
							+ ((step.getKeyName() != null) ? step.getKeyName()
									: "None"));
		}
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description
	 * of the room and its contents.
	 */
	public void executeLookCommand() {
		if (this.currentRoom != null) {
			this.currentRoom.setVisited(false);
		} else {
			currentRoom = roomMap.get(roomMap.firstKey());
		}
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of
	 * what the user is carrying.
	 */
	public void executeInventoryCommand() {
		if (this.inventory.size() > 0) {
			for (int i = 0; i < this.inventory.size(); i++) {
				System.out.println("You have "
						+ this.inventory.get(i).getDescription());
			}
		} else {
			System.out.println("You are empty-handed.");
		}
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is
	 * in the room and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to take
	 */
	public void executeTakeCommand(AdvObject obj) {
		if (currentRoom.containsObject(obj)) {
			inventory.add(obj);
			currentRoom.removeObject(obj);
			System.out.println("TAKEN.");
		} else {
			System.out.println("There is no " + obj.getName() + " here.");
		}
	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
		boolean isDropped = false;
		if (!inventory.isEmpty()) {
			for (AdvObject object : inventory) {
				if (object.equals(obj)) {
					isDropped = true;
					break;
				}
			}
			if (isDropped) {
				inventory.remove(obj);
				currentRoom.addObject(obj);
				System.out.println("DROPPED.");
			} else {
				System.out
						.println("There is no " + obj.getName() + " to drop.");
			}
		} else {
			System.out.println("There is no " + obj.getName() + " to drop.");
		}
	}

	/* Private instance variables */
	// Add your own instance variables here
	private Map<String, AdvCommand> commandMap = new HashMap<String, AdvCommand>();
	private Map<String, AdvObject> objectMap = new HashMap<String, AdvObject>();
	private SortedMap<Integer, AdvRoom> roomMap = new TreeMap<Integer, AdvRoom>();
	private static final String DELIMITER = "=";
	private List<String[]> commandList = new ArrayList<String[]>();
	private AdvRoom currentRoom;
	private List<AdvObject> inventory = new ArrayList<AdvObject>();
	// if we know that where we are now, don't need to show
	// the name of a room again when we use some commands
	private boolean showName = true;
	private boolean running = true;
}
