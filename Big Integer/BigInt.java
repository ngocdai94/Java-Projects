import java.util.ArrayList;
import java.util.List;

// Big Integer class for a really big integer
public class BigInt implements Comparable<BigInt> {

	// digits of the binary representation are stored in nodes
	private class Node {
		public int data;
		public Node next;
		public Node prev;

		public Node(int data) {
			if (data != 0 && data != 1) {
				throw new IllegalArgumentException("data = " + data);
			}
			this.data = data;
		}
	}

	// beginning and end of the doubly linked list
	private Node head, tail;
	// number of nodes in the linked list
	public int size;
	// check to if the that one is multiplication
	private boolean isMultiplicative;

	// BigInt(String val) Construct a BigInt object and initialize it with the
	// integer represented by the String. Throw an appropriate exception
	// (BigIntFormatException) if the string does not represent a signed integer
	// (i.e. contains illegal characters)
	public BigInt(String val) {
		val = val.trim();
		String[] parts = val.split("\\s+");
		// a valid number comes in 1 or 2 pieces
		if (parts.length == 0 || parts.length > 2 || parts[0].length() == 0) {
			throw new BigIntFormatException();
		}
		// if 2 pieces, the first piece must be + or -
		if (parts.length == 2 && !parts[0].equals("-") && !parts[0].equals("+")) {
			throw new BigIntFormatException();
		}

		String number = parts[0] + ((parts.length > 1) ? parts[1] : "");
		int sign = +1;
		if (number.charAt(0) == '-') {
			sign = -1;
		}
		if (number.charAt(0) == '+' || number.charAt(0) == '-') {
			number = number.substring(1); // drop the sign
		}

		// only digits in number?
		for (int i = 0; i < number.length(); i++) {
			char c = number.charAt(i);
			// if (!(c >= '0' && c <= '9')) {
			if (!Character.isDigit(c)) {
				throw new BigIntFormatException();
			}
		}

		// construct the linked list
		while (!number.equals("0")) {
			int d = mod2(number);
			Node n = new Node(d);
			if (tail == null) {
				tail = n;
				head = n;
			} else {
				n.next = head;
				head.prev = n;
				head = n;
			}
			size++;
			number = divideBy2(number);
		}
		// add a 0 for the sign (we will take the 2's complement
		// later if negative)
		Node n = new Node(0);
		n.next = head;
		if (head != null) {
			head.prev = n;
		} else {
			tail = n;
		}
		head = n;
		size++;

		if (sign == -1) {
			// 1's complement
			Node p = head;
			while (p != null) {
				p.data = 1 - p.data;
				p = p.next;
			}
			// 2's complement: just add 1
			int carry = 1;
			p = tail;
			while (p != null) {
				p.data += carry;
				carry = p.data / 2;
				p.data %= 2;
				p = p.prev;
			}
		}

		// remove any leading 0's and 1's (except the last 0 or 1)
		removeLeading0sor1s();
	}

	// BigInt(BigInt val) This is the copy constructor. It should make a deep
	// copy of val. Making a deep copy is not strictly necessary since as
	// designed a BigInt is immutable, but it is good practice.
	public BigInt(BigInt val) {
		this.size = val.size;
		Node p = val.head;
		Node temp = null;
		while (p != null) {
			Node n = new Node(p.data);
			if (this.tail == null) {
				this.head = n;
				this.tail = n;
				temp = head;
			} else {
				n.prev = temp;
				temp.next = n;
				temp = temp.next;
				tail = temp;
			}
			p = p.next;
		}
	}

	// BigInt(long val)
	// Construct a BigInt object and intitialize it wth the value stored in val
	public BigInt(long val) {
		this(val + "");
	}

	// BigInt add(BigInt val) Returns a BigInt whose value is (this + val)
	public BigInt add(BigInt val) {
		// Make two deep copy of two Big Int
		BigInt b1 = new BigInt(this);
		BigInt b2 = new BigInt(val);
		// adjust the binary of Big Int

		int count = Math.abs(b1.size - b2.size);
		if (!isMultiplicative) {
			if (b1.size > b2.size) {
				for (int i = 0; i < count; i++) {
					b2.addLeading0sor1s();
				}
			} else if (b1.size < b2.size) {
				for (int i = 0; i < count; i++) {
					b1.addLeading0sor1s();
				}
			}
			isMultiplicative = false;
		}
		boolean isSameSign = (b1.head.data == b2.head.data);
		BigInt sum = new BigInt(b1);
		// sum two binary
		Node temp = sum.tail;
		Node p = b1.tail;
		Node q = b2.tail;
		int carry = 0;
		while (p != null) {
			int d = p.data + q.data + carry;
			carry = d / 2;
			d %= 2;
			temp.data = d;
			temp = temp.prev;
			p = p.prev;
			q = q.prev;
		}
		// overflow may happen, so
		// we have to deal with it
		if (isSameSign && carry != sum.head.data) {
			Node n = new Node(1 - sum.head.data);
			n.next = sum.head;
			sum.head.prev = n;
			sum.head = n;
			sum.size++;
		}
		sum.removeLeading0sor1s();
		return sum;
	}

	// BigInt multiply(BigInt val)
	// Returns a BigInt whose value is (this * val)
	public BigInt multiply(BigInt val) {
		// Set up some conditions for multiplication
		BigInt b2 = new BigInt(val);
		BigInt b1 = new BigInt(this);
		boolean isSameSign = (b1.head.data == b2.head.data);
		if (isSameSign) {
			if (b1.head.data == 1) {
				b1.convert();
				b2.convert();
			}
		} else {
			if (b1.head.data == 0) {
				b1.convert();
				b2.convert();
			}
			// first add more digits
			// in order for b1 and b2
			// to be equal
			while (b1.size > b2.size) {
				b2.addLeading0sor1s();
			}

			while (b1.size < b2.size) {
				b1.addLeading0sor1s();
			}

			// double their digits
			int count = b1.size;
			for (int i = 0; i < count; i++) {
				b1.addLeading0sor1s();
				b2.addLeading0sor1s();
			}
		}
		int count = b1.size;
		// Put any line of multiplication in list
		List<BigInt> bList = new ArrayList<BigInt>();
		Node p = b2.tail;
		int tCount = 0;
		int hCount;
		for (int i = 0; i < b2.size; i++) {
			hCount = b1.size - 1 - i;
			if (p.data == 1) {
				BigInt bI = new BigInt(b1);
				while (tCount < i) {
					bI.addTailing0();
					tCount++;
				}
				while (hCount > 0) {
					bI.addHeading0();
					hCount--;
				}
				bList.add(bI);
				tCount = 0;
				p = p.prev;
			} else {
				bList.add(new BigInt(0));
				p = p.prev;
			}
		}
		BigInt sum = bList.get(0);
		for (int i = 1; i < bList.size(); i++) {
			isMultiplicative = true;
			sum = sum.add(bList.get(i));
		}

		// we just need to take the good last amount of digits
		// if we double the size at the beginning
		if (!isSameSign) {
			while (sum.size > count) {
				sum.head = sum.head.next;
				sum.size--;
			}
		}
		sum.removeLeading0sor1s();
		return sum;
	}

	// BigInt subtract(BigInt val) Returns a BigInt whose value is (this - val)
	public BigInt subtract(BigInt val) {
		// Make a deep copy version of val
		BigInt newVal = new BigInt(val);
		newVal.convert();
		return this.add(newVal);
	}

	// BigInt factorial() Returns a BigInt whose value is this!
	public BigInt factorial() {
		BigInt p = new BigInt(1);
		BigInt fact = new BigInt(1);
		while (!p.equals(this.add(new BigInt(1)))) {
			fact = fact.multiply(p);
			p = p.add(new BigInt(1));
		}
		return fact;
	}

	// int compareTo(BigInt) Have the BigInt class implement the Comparable
	// interface.
	public int compareTo(BigInt b) {
		// Set -1, 0, 1 as less than, equal to, greater than
		// if b is negative or this is negative
		// and it compared to positive big int
		// the result is clear
		if (b.head.data == 1 && this.head.data == 0) {
			return 1;
		} else if (b.head.data == 0 && this.head.data == 1) {
			return -1;
		} else {
			// compare the values when it has different size
			// or same size
			boolean negative = (b.head.data == 1 && this.head.data == 1);
			if (this.size > b.size) {
				if (!negative) {
					return 1;
				} else {
					return -1;
				}
			} else if (this.size < b.size) {
				if (!negative) {
					return -1;
				} else {
					return 1;
				}
			} else {
				Node b1 = this.head;
				Node b2 = b.head;
				while (b1 != null) {
					if (b1.data > b2.data) {
						return 1;
					} else if (b1.data < b2.data) {
						return -1;
					}
					b1 = b1.next;
					b2 = b2.next;
				}
				return 0;
			}
		}
	}

	// boolean equals(Object)
	// Override the equals() method from Object.
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof BigInt) {
			BigInt b = (BigInt) obj;
			if (size != b.size) {
				return false;
			}
			Node p = head;
			Node q = b.head;
			while (p != null) {
				if (p.data != q.data) {
					return false;
				}
				p = p.next;
				q = q.next;
			}
			return true;
		} else {
			return false;
		}
	}

	// String toString() Returns the decimal representation of this BigInt as a
	// String
	public String toString() {
		String s = "";
		// Check the sign
		boolean negative = (this.head.data == 1);
		BigInt bI = new BigInt(this);

		// Convert for calculating
		if (negative) {
			bI.convert();
		}

		// There is some values needed for direct return
		Node p;
		if (bI.size == 1) {
			return "0";
		} else if (bI.size < 3) {
			if (this.head.data == 1) {
				return "-1";
			} else {
				return "1";
			}
		} else {
			p = bI.head.next;
		}

		// Calculating and converting to decimal in String
		while (p != null) {
			if (p.next == null) {
				break;
			}
			s = multiplyBy2(s);
			if (p.next.data == 1) {
				s = plus1(s);
			}
			p = p.next;
		}
		return (negative) ? "-" + s : s;
	}

	// String toString2s() Returns the 2's complement representation of this
	// BigInt as a String using the minimum number of digits necessary (e.g. 0
	// is "0", -1 is "1", 2 is "010", -2 is "10", etc).
	public String toString2s() {
		String s = head.data + "";
		Node p = head.next;
		while (p != null) {
			s += p.data;
			p = p.next;
		}
		return s;
	}

	// Dividing the current value in representative of String
	private String divideBy2(String s) {
		String q = "";
		int carry = 0;
		for (int i = 0; i < s.length(); i++) {
			int d = s.charAt(i) - '0';
			q += (d + 10 * carry) / 2;
			carry = d % 2;
		}

		// remove any leading 0's (except if q is "0")
		if (q.charAt(0) == '0' && q.length() > 1) {
			int i = 1;
			while (i < q.length() && q.charAt(i) == '0') {
				i++;
			}
			q = q.substring(i);
			if (q.length() == 0) {
				q = "0";
			}
		}

		return q;
	}

	// Moding the value in representative of String
	private int mod2(String s) {
		int d = s.charAt(s.length() - 1) - '0';
		return d % 2;
	}

	// Remove abundant 0 or 1 of the head
	private void removeLeading0sor1s() {
		int data = this.head.data;
		Node p = this.head.next;
		while (p != null && p.data == data) {
			this.head = p;
			this.head.prev = null;
			this.size--;
			p = p.next;
		}
	}

	// Add 0 or 1 to the head When we don't know the actual value of the head of
	// the BigInt
	private void addLeading0sor1s() {
		Node n = new Node(this.head.data);
		n.next = this.head;
		this.head.prev = n;
		this.head = n;
		this.size++;
	}

	// Add 0 to the tail
	private void addTailing0() {
		Node p = this.tail;
		Node n = new Node(0);
		p.next = n;
		n.prev = p;
		this.tail = n;
		this.size++;
	}

	// Add 0 to the head
	private void addHeading0() {
		Node p = this.head;
		Node n = new Node(0);
		n.next = p;
		p.prev = n;
		this.head = n;
		this.size++;
	}

	// Switch sign of the BigInt value like (5 to -5 or -5 to 5)
	private void convert() {
		Node p = this.head;
		boolean negative = (this.head.data == 1);
		// 1's complement
		while (p != null) {
			p.data = 1 - p.data;
			p = p.next;
		}
		// 2's complement: just add 1
		int carry = 1;
		p = this.tail;
		while (p != null) {
			p.data += carry;
			carry = p.data / 2;
			p.data %= 2;
			p = p.prev;
		}
		// if sign is negative
		// we should switch it to positive
		// to make a convert successfully
		if (negative) {
			Node n = new Node(0);
			n.next = this.head;
			this.head.prev = n;
			this.head = n;
			this.size++;
		}
		this.removeLeading0sor1s();
	}

	// Multiplying by 2 and manipulating more if there is a carry
	private String multiplyBy2(String s) {
		if (s.equals("")) {
			s = "1";
		}
		List<Integer> value = this.getDecimalArray(s);

		s = "";
		int carry = 0;
		// basic calculation in the list
		for (int i = value.size() - 1; i >= 0; i--) {
			int number = value.get(i);
			number = carry + (2 * number);
			carry = number / 10;
			number %= 10;
			s = number + s;
		}
		return (carry != 0) ? carry + s : s;
	}

	// Add 1 when the method is called plus manipulating if there is a carry
	private String plus1(String s) {
		List<Integer> value = this.getDecimalArray(s);

		boolean onceTime = false;
		s = "";
		int carry = 0;
		for (int i = value.size() - 1; i >= 0; i--) {
			int number = value.get(i);
			if (onceTime) {
				number += carry;
			} else {
				number += carry + 1;
				onceTime = true;
			}
			carry = number / 10;
			number %= 10;
			s = number + s;
		}
		return s;
	}

	// Put the piece of integer into the List
	private List<Integer> getDecimalArray(String s) {
		List<Integer> value = new ArrayList<Integer>();

		// We should store each letter(number) into
		// array to manipulate the calculation
		for (char c : s.toCharArray()) {
			value.add(c - '0');
		}
		return value;
	}
}
