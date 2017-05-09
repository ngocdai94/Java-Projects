import java.util.Scanner;
// Make an interface interacting to the user
// something like a calculator

public class BigIntUser {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String answer;
		try {
			do {
				System.out.print("Enter here > ");
				answer = scan.nextLine().trim();
				answer = answer.replaceAll("\\s+", "");
				if (answer.contains("*")) {
					int index = answer.indexOf("*");
					System.out.println(new BigInt(answer.substring(0, index))
							.multiply(new BigInt(answer.substring(index + 1,
									answer.length()))));
				} else if (answer != null
						&& answer.charAt(answer.length() - 1) == '!') {
					System.out.println(new BigInt(answer.substring(0,
							answer.length() - 1)).factorial());
				} else if (answer.equals("h")) {
					System.out
							.println("Small version of calculator!\n"
									+ "Use correct format form\n"
									+ "For example, 5 + 5, -1 * 2, 5!, 0-3\n"
									+ "You could leave some spaces among those syntax as much as you like\n"
									+ "You can write it down like this +5*+5, but no +++++5*-----5\n"
									+ "Use q to quit the this program when you are done.\n");
				} else if (answer.contains("+") || answer.contains("-")) {
					int index = 0;
					// Check if there is any sign after the first number
					for (index = 0; index < answer.length() - 1; index++) {
						if (Character.isDigit(answer.charAt(index))
								&& (answer.charAt(index + 1) == '+' || answer
										.charAt(index + 1) == '-')) {
							index++;
							break;
						}
					}
					// if there is one, calculate it
					if (index > 0) {
						if (answer.charAt(index) == '+') {
							System.out.println(new BigInt(answer.substring(0,
									index)).add(new BigInt(answer.substring(
									index + 1, answer.length()))));
						} else if (answer.charAt(index) == '-') {
							System.out.println(new BigInt(answer.substring(0,
									index)).subtract(new BigInt(answer
									.substring(index + 1, answer.length()))));
						} else {
							System.out.println(new BigInt(answer).toString());
						}
					} else {
						System.out.println(new BigInt(answer).toString());
					}
				} else if (!answer.equals("q")) {
					// Show the number if it is valid
					System.out.println(new BigInt(answer).toString());
				}
			} while (!answer.equals("q"));
		} catch (BigIntFormatException e) {
			System.out.println(e.getMessage());
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Invalid input");
		}
	}
}
