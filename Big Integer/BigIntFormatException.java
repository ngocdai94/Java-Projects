// The Exception for BigInt 
public class BigIntFormatException extends RuntimeException {
	public BigIntFormatException(String s) {
		super(s);
	}
	public BigIntFormatException(){
		this("Please try again! You may have wrong format form.");
	}
}
