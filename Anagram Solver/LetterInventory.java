/**
 * This class will be used to keep track of an inventory of letters of the
 * alphabet.
 * 
 * @author DaiNguyen
 *
 */
public class LetterInventory {
	// Initializing variables
	private String data;
	private int letterSize;
	private int[] inventory;
	private int[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };

	/**
	 * Construct an inventory (a count) of the alphabetic letters in the given
	 * string, ignoring the case of letters and ignoring any non-alphabetic
	 * characters.
	 * 
	 * @param data
	 */
	public LetterInventory(String data) {
		// Change given words to lower case.
		this.data = data.toLowerCase();

		// Create 26 slots for 26 letters in alphabet.
		inventory = new int[26];

		// Look through the letters in given words and add to the inventory if
		// their letters are on the alphabet list.
		for (int i = 0; i < this.data.length(); i++) {
			char charLetter = this.data.charAt(i);
			for (int j = 0; j < 26; j++) {
				if (charLetter == alphabet[j]) {
					inventory[j]++;
					letterSize++;
				}
			}
		}
	}

	/**
	 * Return a count of how many of this letter are in the inventory. Letter
	 * might be lowercase or uppercase (your method shouldn't care). If a
	 * nonalphabetic character is passed, your method should throw an
	 * IllegalArgumentException.
	 * 
	 * @param letter
	 * @return
	 */
	public int get(char letter) {
		letter = Character.toLowerCase(letter);
		if (letter < 'a' || letter > 'z') {
			throw new IllegalArgumentException(
					"Your letters are not in the alphabet!");
		}
		return inventory[letter - 'a'];
	}

	/**
	 * Set the count for the given letter to the given value. Letter might be
	 * lowercase or uppercase. If a nonalphabetic character is passed or if
	 * value is negative, your method should throw an IllegalArgumentException
	 * 
	 * @param letter
	 * @param val
	 */
	public void set(char letter, int val) {
		letter = Character.toLowerCase(letter);
		if (val < 0 || letter < 'a' || letter > 'z') {
			throw new IllegalArgumentException("Your letter is not acceptable!");
		}
		letterSize -= inventory[(int) letter - (int) 'a'];
		letterSize += val;
		inventory[(int) letter - (int) 'a'] = val;
	}

	/**
	 * Return the sum of all of the counts in this inventory. This operation
	 * should be �fast� in that it should store the size rather than having to
	 * compute it each time this method is called.
	 * 
	 * @return
	 */
	public int size() {
		return letterSize;
	}

	/**
	 * Return true if this inventory is empty (all counts are 0). This operation
	 * should be fast in that it should not need to examine each of the 26
	 * counts when it is called.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return letterSize == 0;
	}

	/**
	 * Return a String representation of the inventory with the letters all in
	 * lowercase and in sorted order and surrounded by square brackets. The
	 * number of occurrences of each letter should match its count in the
	 * inventory. For example, an inventory of 4 a�s, 1 b, 1 l and 1 m would be
	 * represented as �[aaaablm]�.
	 */
	public String toString() {
		String str = "[";
		for (char letter = 'a'; letter <= 'z'; letter++) {
			for (int i = 0; i < inventory[letter - 'a']; i++) {
				str += letter;
			}
		}
		return str + "]";
	}

	/**
	 * Construct and returns a new LetterInventory object that represents the
	 * sum of this letter inventory and the other given LetterInventory. The
	 * counts for each letter should be added together. The two LetterInventory
	 * objects being added together (this and other) should not be changed by
	 * this method.
	 * 
	 * @param other
	 * @return
	 */
	public LetterInventory add(LetterInventory other) {
		LetterInventory result = new LetterInventory(this.toString());
		result.letterSize = this.letterSize + other.letterSize;
		for (int i = 0; i < inventory.length; i++) {
			result.inventory[i] = this.inventory[i] + other.inventory[i];
		}
		return result;
	}

	/**
	 * Construct and returns a new LetterInventory object that represents the
	 * result of subtracting the other inventory from this inventory (i.e.,
	 * subtracting the counts in the other inventory from this object�s counts).
	 * If any resulting count would be negative, your method should return null.
	 * The two LetterInventory objects being subtracted (this and other) should
	 * not be changed by this method.
	 * 
	 * @param other
	 * @return
	 */
	public LetterInventory subtract(LetterInventory other) {
		LetterInventory result = new LetterInventory(this.toString());
		result.letterSize = this.letterSize - other.letterSize;
		for (int i = 0; i < inventory.length; i++) {
			result.inventory[i] = this.inventory[i] - other.inventory[i];
			if (result.inventory[i] < 0) {
				return null;
			}
		}
		return result;
	}
}
