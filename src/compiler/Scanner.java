package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
	static Token currentToken;
	static String currentLexeme;
	static int lineNumber;
	static int colNumber;
	static BufferedReader input;
	static String file;
	static int index;
	char letter = 'a'|'b';
	
	static String[][] reservedWords = {{"MP_AND","and"},{"MP_BEGIN", "begin"},{"MP_DIV", "div"},{"MP_DO", "do"},{"MP_DOWNTO", "downto"},
										{"MP_ELSE", "else"},{"MP_END","end"},{"MP_FIXED","fixed"},{"MP_FLOAT", "float"},{"MP_FOR","for"},
										{"MP_FUNCTION","function"},{"MP_IF","if"},{"MP_INTEGER","integer"},{"MP_MOD","mod"},{"MP_NOT","not"},
										{"MP_OR","or"}, {"MP_PROCEDURE","procedure"},{"MP_PROGRAM","program"},{"MP_READ","read"},{"MP_REPEAT","repeat"},
										{"MP_THEN","then"},{"MP_TO","to"},{"MP_UNTIL","until"},{"MP_VAR","var"},{"MP_WHILE","while"},{"MP_WRITE","write"}};

	public static Token getToken() {
		return dispatch();
	}

	public static Token dispatch() throws IOException {
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
			// go to digit FSA
		} else if (Character.isAlphabetic(currentChar)) {
			currentChar = Character.toLowerCase(currentChar);
			// go to identifier FSA
		} else {

		}
<<<<<<< Updated upstream
		//switch for each possible FSA
		//call that FSA's function which returns token
	}
	
	public static void openFile(String fileName) throws IOException{
		//get file from esus
=======
		// switch for each possible FSA
		// call that FSA's fucntion which returns token
	}

	public static void openFile(String fileName) throws IOException {
		// get file frome esus
>>>>>>> Stashed changes
		index = 0;
		input = new BufferedReader(new FileReader(fileName));
		file = "";
		int in = input.read();
		// read in file to string
		while (in != -1) {
			file += ((char) in);
			in = input.read();
		}
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

<<<<<<< Updated upstream
	public static String MP_STRING_LIT() {
		int indexOfLastAccept;
		char currentChar = file.charAt(index);
		String lexeme = "";
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
				case 1:
					if(currentChar == '\'') { // to accept state
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						lexeme = tempLexeme;
						state = 2;
					} else if (currentChar == '\n') {
						return lexeme;
					} else { //other
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 2;
					}
				case 2:
					if (currentChar == '\'') {
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 1;	
					} else {
						return lexeme;
					}
			}
		}
	}

	public static String digitFSA(){
	/*
	 * states are enumerated
	 */
		int state = 0;
		int indexOfLastAccept = 0;
		char currentChar = file.charAt(index);
		String lexeme = "";
		String tempLexeme = "";
		
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					if(Character.isDigit(currentChar)){
						lexeme += currentChar;
						tempLexeme += currentChar;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 1;
					}else{
						index = ++indexOfLastAccept;
						return lexeme;
					}
				case 1:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
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
						return lexeme;
					}
				case 2:
					if(Character.isDigit(currentChar)){
						indexOfLastAccept = index;
						lexeme += currentChar;
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 6;
					}else{
						index = ++indexOfLastAccept;
						return lexeme;
					}
				case 3:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 5;
					}else if(currentChar == '+' || currentChar == '-'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 4;
					}else{
						index = ++indexOfLastAccept;
						return lexeme;
					}
				case 4:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
						state = 5;
					}
				case 5:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
					}else{
						index = ++indexOfLastAccept;
						return lexeme;
					}
				case 6:
					if(Character.isDigit(currentChar)){
						tempLexeme += currentChar;
						lexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						colNumber++;
					}else if(currentChar == 'e' || currentChar == 'E'){
						tempLexeme += currentChar;
						index++;
						colNumber++;
						state = 3;
					}else{
						index = ++indexOfLastAccept;
						return lexeme;
					}
=======
	public static Token digitFSA() {
		/*
		 * states are enumerated
		 */
		int state = 0;
		int indexOfLastAccept;
		char currentChar = file.charAt(index);
		String lexeme = "";

		while (true) {
			currentChar = file.charAt(index);
			switch (state) {
			case 0:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 1;
				default:
					// return mp_int_lit
					// index = indexOfLastAccept + 1
					//
				}
			case 1:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '.':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 2;
				case 'e':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 3;
				case 'E':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 3;
				}
			case 2:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 6;
				}
			case 3:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '+':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 4;
				case '-':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 4;
				}
			case 4:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 5;
				}
			case 5:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				}
			case 6:
				switch (currentChar) {
				case '0':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '1':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '2':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '3':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '4':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '5':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '6':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '7':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '8':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case '9':
					indexOfLastAccept = index;
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
				case 'e':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 3;
				case 'E':
					lexeme = lexeme + currentChar;
					index++;
					colNumber++;
					state = 3;
				}
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
>>>>>>> Stashed changes
			}
		}
	}
}
