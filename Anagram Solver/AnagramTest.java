import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Test;

public class AnagramTest {
	@Test
	public void testWithDict1() {
		String input = "";
		input += "Barbara Bush\n0\n";
		input += "George BUSH\n0\n";
		input += "GeorgeBush\n2\n";
		input += "GeOrGe\n2\n";
		input += "GEORGES\n2\n";
		input += "\n";
		runATest("1", input);
	}

	@Test
	public void testWithDict2() {
		String input = "";
		input += "City of Seattle\n3\n";
		input += "\n";
		runATest("2", input);
	}

	@Test
	public void testWithDict3() {
		String input = "";
		input += "Howard Dean\n2\n";
		input += "Wesley Clark\n2\n";
		input += "\n";
		runATest("3", input);
	}

	@Test
	public void testWithDict4() {
		String input = "";
		input += "Hugo Chavez\n0\n";
		input += "howard dean\n2\n";
		input += "\n";
		runATest("4", input);
	}

	private void runATest(String whichOne, String input) {
		input = "dict" + whichOne + ".txt\n" + input;
		Scanner scan = new Scanner(input);
		AnagramMain.console = scan;
		try {
			PrintStream out = new PrintStream("temp.txt");
			AnagramMain.out = out;
		} catch (FileNotFoundException e) {
			System.out
					.println("Couldn't create a temp file in the project folder");
			assertTrue(false);
		}

		try {
			AnagramMain.main(null);
			AnagramMain.out.close();
			// Compare temp and testWithdict1.txt (ignore blank lines)
			Scanner file1 = new Scanner(new File("temp.txt"));
			Scanner file2 = new Scanner(new File("testWithdict" + whichOne
					+ ".txt"));
			boolean read1 = true;
			boolean read2 = true;
			String line1 = null, line2 = null;
			while ((!read1 || file1.hasNextLine())
					&& (!read2 || file2.hasNextLine())) {
				if (read1) {
					line1 = file1.nextLine().trim();
				}
				if (read2) {
					line2 = file2.nextLine().trim();
				}
				if (line1.length() > 0 && line2.length() > 0) {
					assertTrue(line1.equals(line2));
					read1 = true;
					read2 = true;
				} else if (line1.length() > 0) {
					// blank line for line2
					read1 = false;
					read2 = true;
				} else if (line2.length() > 0) {
					// blank line for line1
					read1 = true;
					read2 = false;
				} else {
					// both lines are blank
					read1 = true;
					read2 = true;
				}
			}
			// Only blank lines should be left in any of the two files
			while (file1.hasNextLine()) {
				line1 = file1.nextLine().trim();
				assertTrue(line1.length() == 0);
			}
			while (file2.hasNextLine()) {
				line2 = file2.nextLine().trim();
				assertTrue(line2.length() == 0);
			}
			file1.close();
			file2.close();
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Place the file dict" + whichOne
					+ ".txt and testWithdict" + whichOne
					+ ".txt in your project folder");
			assertTrue(false);
		}

	}

}
