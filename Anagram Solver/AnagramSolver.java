import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The simple program that solves for all possible anagrams of the given words in
 * a dictionary.
 * 
 * @author DaiNguyen
 *
 */
public class AnagramSolver {
	private List<String> dictionary;
	private Map<String, LetterInventory> inventory;

	/**
	 * Construct an anagram solver that will use the given word list as its
	 * dictionary. The list must not be modified.
	 * 
	 * @param list
	 */
	public AnagramSolver(List<String> list) {
		dictionary = list;
		inventory = new HashMap<String, LetterInventory>();
	}

	/**
	 * Print to System.out all combinations of words from its dictionary that
	 * are anagrams of the String s and that include at most max words (or
	 * unlimited number of words if max is 0). Throw an IllegalArgumentException
	 * if max is less than 0.
	 * 
	 * @param s
	 * @param max
	 */
	public void print(String s, int max) {
		if (max < 0) {
			throw new IllegalArgumentException("Invalid number!");
		}

		// Create a reduce dictionary that holds relevant words.
		List<String> smallDict = new ArrayList<String>();

		// Get all the letters in given phrase.
		LetterInventory phrase = new LetterInventory(s);

		// Get relevant words that is relevant to this phrase and reduce the
		// size of the dictionary.
		for (String key : dictionary) {
			LetterInventory value = new LetterInventory(key);
			// The worlds are relevant if they can be subtract.
			if (phrase.subtract(value) != null) {
				inventory.put(key, value);
				smallDict.add(key);
			}
		}

		// Anagram will be store to words.
		List<String> words = new ArrayList<String>();

		// A recursive method to find and print relevant words.
		lookForWords(inventory, smallDict, words, phrase, max, 0);

	}

	/**
	 * A backtracking recursive helper method search through the relevant
	 * dictionary
	 * 
	 * @param smallDictMap
	 *            - a dictionary map of the input phrase holding words from
	 *            dictionary and their LetterInventory
	 * @param searchingWords
	 *            - list of words from the small dictionary but in the same
	 *            order as the original dictionary. This is for making sure the
	 *            outputs would have correct orders
	 * @param result
	 *            - A list to hold anagrams that will be printed out
	 * @param phrase
	 *            - current characters left of the original string input
	 * @param max
	 *            - max words
	 * @param level
	 *            - current level of words, used for bounding the condition of
	 *            max. Increasing by 1 every time a word added
	 */

	/**
	 * A recursive backtracking method will look for words in dictionary.
	 * 
	 * @param inventory
	 * @param smallDict
	 * @param words
	 * @param currentPhrase
	 * @param max
	 * @param wordLevel
	 */
	private void lookForWords(Map<String, LetterInventory> inventory,
			List<String> smallDict, List<String> words,
			LetterInventory currentPhrase, int max, int wordLevel) {

		// Base case: phrase is empty.
		if (currentPhrase.size() == 0) {
			AnagramMain.out.println(words.toString());
		}
		
		// Execute if max = 0 or max > level, to get unlimited words or a
		// desired level.
		if (max == 0 || max > wordLevel) {
			// Searching words in the small dictionary.
			for (String word : smallDict) {
				// Get a new phrase if any.
				LetterInventory newPhrase = currentPhrase.subtract(inventory
						.get(word));

				if (newPhrase != null) {
					// Add this word to the words list
					words.add(word);
					// Check again and increase the level by 1
					lookForWords(inventory, smallDict, words, newPhrase, max,
							wordLevel + 1);
					// Remove the last word.
					words.remove(words.size() - 1);
				}
			}
		}
	}

}
