package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
	static String currentToken;
	static String currentLexeme;
	static int lineNumber;
	static int colNumber;
	static BufferedReader input;
	static String file;
	static int index;
	
	static String[][] reservedWords = {{"MP_AND","and"},{"MP_BEGIN", "begin"},{"MP_DIV", "div"},{"MP_DO", "do"},{"MP_DOWNTO", "downto"},
										{"MP_ELSE", "else"},{"MP_END","end"},{"MP_FIXED","fixed"},{"MP_FLOAT", "float"},{"MP_FOR","for"},
										{"MP_FUNCTION","function"},{"MP_IF","if"},{"MP_INTEGER","integer"},{"MP_MOD","mod"},{"MP_NOT","not"},
										{"MP_OR","or"}, {"MP_PROCEDURE","procedure"},{"MP_PROGRAM","program"},{"MP_READ","read"},{"MP_REPEAT","repeat"},
										{"MP_THEN","then"},{"MP_TO","to"},{"MP_UNTIL","until"},{"MP_VAR","var"},{"MP_WHILE","while"},{"MP_WRITE","write"}};

	public static String getToken() throws IOException {
		return dispatch();
	}

	public static String dispatch() throws IOException {
		// skip white space
		char currentChar;
		while (true) {
			currentChar = file.charAt(index);
			if (Character.isWhitespace(currentChar)) {
				if (currentChar == '\n') {
					lineNumber++;
					colNumber = 0;
				} else {
					colNumber++;
				}
				index++;
			} else {
				break;
			}
		}
		currentChar = file.charAt(index);
		if (Character.isDigit(currentChar)) {
			return digitFSA();
		} else if (currentChar == '\'') {
			return MP_STRING_LIT();
			// go to identifier FSA
		} else {

		}
		return "";
		//switch for each possible FSA
		//call that FSA's function which returns token
	}
	
	public static void openFile(String fileName) throws IOException{
		//get file from esus
		index = 0;
		input = new BufferedReader(new FileReader(fileName));
		file = "";
		int in = input.read();
		// read in file to string
		while (in != -1) {
			file += ((char) in);
			in = input.read();
		}
		file += (char) 3;
		System.out.println(file);
		lineNumber = 0;
		colNumber = 0;
	}

	public static String getLexeme() {
		return currentLexeme;
	}

	public static int getLineNumber() {
		return lineNumber;
	}

	public static int getColumnNumber() {
		return colNumber;
	}

	public static String MP_STRING_LIT() {
		int indexOfLastAccept = index - 1;
		char currentChar = file.charAt(index);
		currentLexeme = "";
		String tempLexeme = "";
		int state = 0;
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					if(currentChar == '\'') {
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 1;
					} else {
						// we should never make it here
					}
					break;
				case 1:
					if(currentChar == '\'') { // to accept state
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						currentLexeme = tempLexeme;
						state = 2;
					} else if (currentChar == '\n') {
						index = ++indexOfLastAccept;
						return "MP_STRING_LIT";
					} else { //other
						tempLexeme += currentChar;
						index++;
						colNumber++;
					}
					break;
				case 2:
					if (currentChar == '\'') {
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 1;	
					} else {
						index = ++indexOfLastAccept;
						return "MP_STRING_LIT";
					}
					break;
			}
		}
	}

	public static String digitFSA(){
	/*
	 * states are enumerated
	 */
		int state = 0;
		int indexOfLastAccept = index - 1;
		char currentChar = file.charAt(index);
		currentLexeme = "";
		String tempLexeme = "";
		
		
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					if(Character.isDigit(currentChar)){
						currentLexeme += currentChar;
						tempLexeme += currentChar;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 1;
						currentToken = "MP_INTEGER_LIT";
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
				case 1:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						currentToken = "MP_INTEGER_LIT";
					}else if(currentChar == 'e' || currentChar == 'E'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 3;
					}else if(currentChar == '.'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 2;
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
				case 2:
					if(Character.isDigit(currentChar)){
						indexOfLastAccept = index;
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						index++;
						colNumber++;
						state = 6;
						currentToken = "MP_FIXED_LIT";
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
				case 3:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 5;
						currentToken = "MP_FLOAT_LIT";
					}else if(currentChar == '+' || currentChar == '-'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 4;
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
				case 4:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 5;
						currentToken = "MP_FLOAT_LIT";
					}
					break;
				case 5:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						currentToken = "MP_FLOAT_LIT";
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
				case 6:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						currentToken = "MP_FIXED_LIT";
					}else if(currentChar == 'e' || currentChar == 'E'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 3;
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
			}
		}
	}

	public static String MP_PERIOD() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '.':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_COMMA() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case ',':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_SCOLON() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case ';':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_LPAREN() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '(':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_RPAREN() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case ')':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_EQUAL() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '=':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_GTHAN() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '>':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_LTHAN() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '<':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_GEQUAL() {
		int state = 0;
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (state) {
			case 0:
				switch (currentChar) {
				case '>':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				}
			case 1:
				switch (currentChar) {
				case '=':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					return lexeme;
				default:
					return lexeme;
				}

			}
		}
	}
	
	public static String MP_LEQUAL() {
		int state = 0;
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (state) {
			case 0:
				switch (currentChar) {
				case '<':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				}
			case 1:
				switch (currentChar) {
				case '=':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					return lexeme;
				default:
					return lexeme;
				}

			}
		}
	}
	
	public static String MP_NEQUAL() {
		int state = 0;
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (state) {
			case 0:
				switch (currentChar) {
				case '<':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				}
			case 1:
				switch (currentChar) {
				case '>':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					return lexeme;
				default:
					return lexeme;
				}

			}
		}
	}
	
	public static String MP_ASSIGN() {
		int state = 0;
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (state) {
			case 0:
				switch (currentChar) {
				case ':':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				}
			case 1:
				switch (currentChar) {
				case '=':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					return lexeme;
				default:
					return lexeme;
				}

			}
		}
	}
	
	public static String MP_PLUS() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '+':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_MINUS() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '-':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_TIMES() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case '*':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_COLON() {
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			switch (currentChar) {
			case ':':
				lexeme = lexeme + currentChar;
				index++;
				colNumber++;
				return lexeme;
			default:
				// Should never reach here
			}
		}
	}
}
