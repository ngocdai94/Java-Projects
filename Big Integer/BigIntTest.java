import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class BigIntTest {

	// to generate random integers
	private Random rand = new Random();

	@Test
	public void testConstructorsAndToString() {
		// BigInt(String)
		String[] s = { " - - 1424", "+ + 14324", "a142432", "1432 3413",
				"+242134.32421", "", "    \n", "\t ++" };
		for (int i = 0; i < s.length; i++) {
			try {
				new BigInt(s[i]);
				assertFalse(true);
			} catch (BigIntFormatException e) {
				assertTrue(true);
			}
		}
		s = new String[] {
				"- 1424",
				"  + 14324",
				"    142432",
				"\n\t+ 2402\n\t\r",
				"00000000000000",
				"000000000000100",
				"0000028402437802743027340273402734027340273402734027340732407204702740274027430732073027430730" };
		String[] trim = {
				"-1424",
				"14324",
				"142432",
				"2402",
				"0",
				"100",
				"28402437802743027340273402734027340273402734027340732407204702740274027430732073027430730" };

		for (int i = 0; i < s.length; i++) {
			BigInt b = new BigInt(s[i]);
			assertTrue(b.toString().equals(trim[i]));
		}

		// BigInt(BigInt)
		BigInt b = new BigInt("34972374932");
		BigInt c = new BigInt(b);
		assertTrue(c != b);
		assertTrue(b.toString().equals(c.toString()));

		// BigInt(long)
		b = new BigInt(-142423424l);
		assertTrue(b.toString().equals("-142423424"));
		b = new BigInt(+402480848404382l);
		assertTrue(b.toString().equals("402480848404382"));
	}

	@Test
	public void testEquals() {
		// some random tests
		BigInt maxLong = new BigInt(Long.MAX_VALUE);
		for (int i = 1; i <= 20; i++) {
			BigInt b = new BigInt(rand.nextInt());
			BigInt c = new BigInt(b);
			assertTrue(b.equals(b));
			assertTrue(c.equals(b));
			assertTrue(b.equals(c));
			assertFalse(b.equals(maxLong));
		}

		// And with big numbers
		BigInt b1 = new BigInt(
				"-347932749327493739743297493274932749327493275493274932749327492734937249732974");
		assertTrue(b1.equals(b1));
		BigInt copy = new BigInt(b1);
		assertTrue(copy.equals(b1));
		assertFalse(b1.equals(null));
		BigInt b2 = new BigInt(
				"-00000000000000347932749327493739743297493274932749327493275493274932749327492734937249732974");
		assertTrue(b2.equals(b1));
		assertTrue(b1.equals(b2));
		BigInt b3 = new BigInt(
				"+347932749327493739743297493274932749327493275493274932749327492734937249732974");
		assertFalse(b3.equals(b1));
		assertFalse(b1.equals(b3));
		b1 = new BigInt(0);
		b2 = new BigInt("-0000");
		assertTrue(b1.equals(b2));
	}

	@Test
	public void testCompareTo() {
		// some random tests
		BigInt maxLong = new BigInt(Long.MAX_VALUE);
		for (int i = 1; i <= 20; i++) {
			BigInt b = new BigInt(rand.nextInt(10000));
			BigInt copy = new BigInt(b);
			BigInt c = new BigInt(-20000 + rand.nextInt(10000));
			assertTrue(b.compareTo(c) > 0);
			assertTrue(copy.compareTo(b) == 0);
			assertTrue(c.compareTo(b) < 0);
			assertTrue(b.compareTo(maxLong) < 0);
		}

		// and with big numbers
		BigInt b1 = new BigInt(
				"840832400834083240283408234027590723407835035073250278340782340782");
		assertTrue(b1.compareTo(b1) == 0);
		BigInt copy = new BigInt(b1);
		assertTrue(copy.compareTo(b1) == 0);
		BigInt b2 = new BigInt(
				"840832400834083240283408234027590723407835035073250278340782340783");
		assertTrue(b1.compareTo(b2) < 0);
		assertTrue(b2.compareTo(b1) > 0);
		BigInt b3 = new BigInt("-37240782304320488303");
		BigInt b4 = new BigInt("-37240782304320488304");
		assertTrue(b3.compareTo(b4) > 0);
		assertTrue(b3.compareTo(b1) < 0);
		b1 = new BigInt(1);
		b2 = new BigInt(-1);
		assertTrue(b1.compareTo(b2) > 0);
		assertTrue(b2.compareTo(b1) < 0);
		assertTrue(b1.compareTo(new BigInt(0)) > 0);
		assertTrue(b2.compareTo(new BigInt(0)) < 0);
		assertTrue(b2.compareTo(b2) == 0);
	}

	@Test
	public void testAdd() {
		// some random tests
		for (int i = 1; i <= 20; i++) {
			long i1 = rand.nextInt();
			long i2 = rand.nextInt();
			BigInt b = new BigInt(i1);
			BigInt c = new BigInt(i2);
			assertTrue(b.add(c).equals(new BigInt(i1 + i2)));
			// b and c should not be modified
			assertTrue(b.equals(new BigInt(i1)));
			assertTrue(c.equals(new BigInt(i2)));
		}

		// with big numbers
		BigInt b1 = new BigInt(
				"374073204787074307340730730730730739023679628265");
		BigInt b2 = new BigInt(
				"-00000000374073204787074307340730730730730739023679628265");
		BigInt sum = b1.add(b2);
		assertTrue(sum.equals(new BigInt("0")));
		sum = b1.add(new BigInt(0));
		assertTrue(sum.equals(b1));
		b1 = new BigInt(
				"99999999999999999999999999999999999999999999999999999999999999");
		b2 = new BigInt(1);
		sum = b2.add(b1);
		assertTrue(sum
				.equals(new BigInt(
						"100000000000000000000000000000000000000000000000000000000000000")));
		b1 = new BigInt(
				"-1000000000000000000000000000000000000000000000000000000");
		b2 = new BigInt(1);
		sum = b1.add(b2);
		assertTrue(sum.equals(new BigInt(
				"-999999999999999999999999999999999999999999999999999999")));
		sum = b2.add(b1);
		assertTrue(sum.equals(new BigInt(
				"-999999999999999999999999999999999999999999999999999999")));
	}

	@Test
	public void testSubtract() {
		// some random tests
		for (int i = 1; i <= 20; i++) {
			long i1 = rand.nextInt();
			long i2 = rand.nextInt();
			BigInt b = new BigInt(i1);
			BigInt c = new BigInt(i2);
			assertTrue(b.subtract(c).equals(new BigInt(i1 - i2)));
			// b and c should not be modified
			assertTrue(b.equals(new BigInt(i1)));
			assertTrue(c.equals(new BigInt(i2)));
		}

		// with big numbers
		BigInt b1 = new BigInt(
				"374073204787074307340730730730730739023679628265");
		BigInt b2 = new BigInt(
				"00000000374073204787074307340730730730730739023679628265");
		BigInt sum = b1.subtract(b2);
		assertTrue(sum.equals(new BigInt("0")));
		sum = b1.subtract(new BigInt(0));
		assertTrue(sum.equals(b1));
		b1 = new BigInt(
				"99999999999999999999999999999999999999999999999999999999999999");
		b2 = new BigInt(-1);
		sum = b2.subtract(b1);
		assertTrue(sum
				.equals(new BigInt(
						"-100000000000000000000000000000000000000000000000000000000000000")));
		b1 = new BigInt(
				"-1000000000000000000000000000000000000000000000000000000");
		b2 = new BigInt(-1);
		sum = b1.subtract(b2);
		assertTrue(sum.equals(new BigInt(
				"-999999999999999999999999999999999999999999999999999999")));
		sum = b2.subtract(b1);
		assertTrue(sum.equals(new BigInt(
				"999999999999999999999999999999999999999999999999999999")));
	}

	@Test
	public void testMultiplyAndFactorial() {
		// some random tests
		for (int i = 1; i <= 20; i++) {
			long i1 = rand.nextInt();
			long i2 = rand.nextInt();
			BigInt b = new BigInt(i1);
			BigInt c = new BigInt(i2);
			assertTrue(b.multiply(c).equals(new BigInt(i1 * i2)));
			// b and c should not be modified
			assertTrue(b.equals(new BigInt(i1)));
			assertTrue(c.equals(new BigInt(i2)));
		}

		// small factorial
		long f = 1;
		for (int i = 1; i <= 15; i++) {
			f *= i;
			BigInt b = new BigInt(i);
			assertTrue(b.factorial().equals(new BigInt(f)));
			// b should not be modified
			assertTrue(b.equals(new BigInt(i)));
		}

		// more tests
		BigInt b1 = new BigInt("-1111");
		BigInt b2 = new BigInt("2222");
		BigInt m = b1.multiply(b2);
		assertTrue(m.equals(new BigInt("-2468642")));
		m = b1.multiply(new BigInt("-0"));
		assertTrue(m.equals(new BigInt(0)));
		BigInt n = new BigInt(5);
		BigInt fact5 = new BigInt(120);
		assertTrue(n.factorial().equals(fact5));
		n = new BigInt(50);
		BigInt fact50 = new BigInt(
				"30414093201713378043612608166064768844377641568960512000000000000");
		assertTrue(n.factorial().equals(fact50));
		n = new BigInt(100);
		BigInt fact100 = new BigInt(
				"93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000");
		assertTrue(n.factorial().equals(fact100));
		assertTrue(new BigInt(0).factorial().equals(new BigInt(1)));
	}

	@Test
	public void testToString2s() {
		// some random tests
		for (int i = 1; i <= 20; i++) {
			int[] a = new int[rand.nextInt(20) + 1];
			int n = 0;
			for (int j = 1; j < a.length; j++) {
				a[j] = rand.nextInt(2); // 0 or 1
				n = n * 2 + a[j];
			}
			// sign
			a[0] = rand.nextInt(2);
			if (a[0] == 1) {
				n -= (int) Math.pow(2, a.length - 1);
			}
			// String representation
			String s = "" + a[0];
			int k = 1;
			while (k < a.length && a[k] == a[0]) {
				k++; // trim any leading or trailing 0's
			}
			while (k < a.length) {
				s += a[k++];
			}
			BigInt b = new BigInt(n);
			assertTrue(b.toString2s().equals(s));
		}

		// more tests
		BigInt b1 = new BigInt("0");
		assertTrue(b1.toString2s().equals("0"));
		b1 = new BigInt("-1");
		assertTrue(b1.toString2s().equals("1"));
		b1 = new BigInt("2");
		assertTrue(b1.toString2s().equals("010"));
		b1 = new BigInt("-2");
		assertTrue(b1.toString2s().equals("10"));
		b1 = new BigInt("1423423141324124214");
		assertTrue(b1
				.toString2s()
				.equals("01001111000001000000111100111000101111101100011111000000110110"));
		b1 = new BigInt("-1423423141324124214");
		assertTrue(b1
				.toString2s()
				.equals("10110000111110111111000011000111010000010011100000111111001010"));

	}
}
