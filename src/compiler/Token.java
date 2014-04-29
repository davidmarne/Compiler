package compiler;

public class Token {

	public String token;
	public String lexeme;
	public int lineNumber;
	public int colNumber;
	
	public Token(String token, String lexeme, int lineNumber, int colNumber) {
		this.token = token;
		this.colNumber = colNumber;
		this.lexeme = lexeme;
		this.lineNumber = lineNumber;
	}
}