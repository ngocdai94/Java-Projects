import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LetterInventoryTest {

	@Test
	public void testInventoryConstructor() {
		int[] count = new int[26];
		int size = 0;
		for (int i = 0; i < count.length; i++) {
			count[i] = (int) (Math.random() * 100);
			size += count[i];
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < count.length; i++) {
			// Character to insert: letter can be uppercase or lowercase
			char cl = (char) ('a' + i);
			char cu = (char) ('A' + i);

			// A random character that is not a letter
			char junk;
			do {
				junk = (char) (Math.random() * 256);
			} while (Character.isLetter(junk));

			// Build a string made of characters as counted by count
			// interspersed with some non letters
			for (int k = 0; k < count[i]; k++) {
				// insert anywhere between 0 and sb.length included
				int index = (int) (Math.random() * (1 + sb.length()));
				switch ((int) (Math.random() * 3)) {
				case 0: // upper case
					sb.insert(index, cu);
					break;
				case 1: // lower case
					sb.insert(index, cl);
					break;
				default: // any non letter character
					sb.insert(index, junk);
					// don't count this one
					k--;
					break;
				}
			}
		}

		// Create the inventory
		LetterInventory in = new LetterInventory(sb.toString());

		// Check it
		assertTrue(in.size() == size);
		for (int i = 0; i < count.length; i++) {
			assertTrue(count[i] == in.get((char) ('a' + i)));
		}

	}

	@Test
	public void testPrintInventory() {
		String s = "[aaabbbbcdefffghhhikkkkllllmmmnnnooopppqqqssstuuuvvvxxxxyyyyzzz]";
		StringBuilder sb = new StringBuilder(s).reverse();
		LetterInventory in = new LetterInventory(sb.toString());
		assertTrue(in.toString().equals(s));
		assertTrue(in.size() == s.length() - 2); // [ and ] don't count
	}

	@Test
	public void testSet() {
		LetterInventory in = new LetterInventory("");
		assertTrue(in.size() == 0);
		assertTrue(in.toString().equals("[]"));
		int[] count = new int[26];
		int size = 0;
		for (int i = 0; i < count.length; i++) {
			count[i] = (int) (Math.random() * 100);
			size += count[i];
			// The letter to set can be uppercase or lowercase
			char c;
			if (Math.random() >= 0.5) {
				c = (char) ('A' + i);
			} else {
				c = (char) ('a' + i);
			}
			in.set(c, count[i]);
		}

		assertTrue(in.size() == size);
		for (int i = 0; i < count.length; i++) {
			assertTrue(in.get((char) ('A' + i)) == count[i]);
		}
	}

	@Test
	public void testAdd() {
		String s1 = "aaabbbbcdefffghhhikkkkllllmmmnnnooopppqqqssstuuuvvvxxxxyyyyzzz";
		LetterInventory in1 = new LetterInventory(s1);
		String s2 = "ajjjjrrrsss";
		LetterInventory in2 = new LetterInventory(s2);
		LetterInventory in3 = in1.add(in2);
		// in1 and in2 are unchanged
		assertTrue(checkInventory(in1, s1));
		assertTrue(checkInventory(in2, s2));
		// in3 contains s1 + s2
		assertTrue(checkInventory(in3, s1 + s2));
	}

	@Test
	public void testSubtract() {
		String s1 = "aaabbbbcdefffghhhikkkkllllmmmnnnooopppqqqssstuuuvvvxxxxyyyyzzz";
		LetterInventory in1 = new LetterInventory(s1);
		String s2 = "ajjjjrrrsss";
		LetterInventory in2 = new LetterInventory(s2);
		LetterInventory in3 = in1.subtract(in2); // should be null since no
		// j's in in1
		assertTrue(in3 == null);
		// in1 and in2 are unchanged
		assertTrue(checkInventory(in1, s1));
		assertTrue(checkInventory(in2, s2));

		s2 = "abcdefghiklmnopqstuuvvxxyyzz";
		in2 = new LetterInventory(s2);
		in3 = in1.subtract(in2);
		// in1 and in2 are unchanged
		assertTrue(checkInventory(in1, s1));
		assertTrue(checkInventory(in2, s2));
		// in3 contains s1 - s2
		StringBuilder sb = new StringBuilder(s1);
		for (int i = 0; i < s2.length(); i++) {
			char c = s2.charAt(i);
			for (int j = 0; j < sb.length(); j++) {
				if (sb.charAt(j) == c) {
					sb.replace(j, j + 1, "");
					break;
				}
			}
		}
		assertTrue(checkInventory(in3, sb.toString()));
	}

	private boolean checkInventory(LetterInventory in, String s) {
		int size = 0;
		int[] count = new int[26];
		for (int k = 0; k < s.length(); k++) {
			char c = Character.toLowerCase(s.charAt(k));
			if (c >= 'a' && c <= 'z') {
				count[c - 'a']++;
				size++;
			}
		}
		boolean isOK = (size == in.size());
		for (int i = 0; i < 26 && isOK; i++) {
			isOK = isOK && (count[i] == in.get((char) ('a' + i)));
		}
		return isOK;
	}
}
