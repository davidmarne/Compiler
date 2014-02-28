package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
	static String currentToken;
	static String currentLexeme;
	static int lineNumber;
	static int currentColNumber;
	static int colNumber;
	static BufferedReader input;
	static String file;
	static int index;
	
	static String[][] reservedWords = {{"MP_AND","and"},{"MP_BEGIN", "begin"},{"MP_DIV", "div"},{"MP_DO", "do"},{"MP_DOWNTO", "downto"},
										{"MP_ELSE", "else"},{"MP_END","end"},{"MP_FIXED","fixed"},{"MP_FLOAT", "float"},{"MP_FOR","for"},
										{"MP_FUNCTION","function"},{"MP_IF","if"},{"MP_INTEGER","integer"},{"MP_MOD","mod"},{"MP_NOT","not"},
										{"MP_OR","or"}, {"MP_PROCEDURE","procedure"},{"MP_PROGRAM","program"},{"MP_READ","read"},{"MP_REPEAT","repeat"},
										{"MP_THEN","then"},{"MP_TO","to"},{"MP_UNTIL","until"},{"MP_VAR","var"},{"MP_WHILE","while"},{"MP_WRITE","write"},
										{"MP_BOOLEAN", "Boolean"},{"MP_FALSE", "false"},{"MP_STRING", "string"},{"MP_TRUE", "true"}, {"MP_WRITELN","writeln"}};

	public static String getToken() throws Exception {
		String token = "";
		token = dispatch();
		if(token == "MP_RUN_STRING") {
			throw new Exception("Scanned in a run-on string: " + lineNumber + ":" + colNumber + " " + currentLexeme);
		} else if(token == "MP_ERROR") {
			throw new Exception("Unknown first character of token: " + lineNumber + ":" + colNumber + " " + currentLexeme);
		} else if (token == "MP_RUN_COMMENT") {
			throw new Exception("Scanned in run-on comment: " + lineNumber + ":" + colNumber);
		}
		return token;
		
	}

	public static String dispatch() throws IOException {
		// skip white space
		char currentChar;
		while (true) {
			currentChar = file.charAt(index);
			// Check for EOF
			if(currentChar == (char) 3){
				return "MP_EOF";
			}
			if (Character.isWhitespace(currentChar)) {
				if (currentChar == '\n') {
					lineNumber++;
					currentColNumber = 0;
				} else {
					currentColNumber++;
				}
				index++;
			} else {
				break;
			}
		}
		colNumber = currentColNumber;
		currentChar = file.charAt(index);
		if (Character.isDigit(currentChar)) {
			return digitFSA();
		} else if (Character.isAlphabetic(currentChar) || currentChar == '_') {
			MP_IDENTIFIER();
			return getTokenFromIdentifier(currentLexeme);
		} else {
			switch(currentChar){
			case('{'):
				return MP_COMMENT();
			case('\''):
				return MP_STRING_LIT();
			case('.'):
				return MP_PERIOD();
			case(','):
				return MP_COMMA();
			case(';'):
				return MP_SCOLON();
			case('('):
				return MP_LPAREN();
			case(')'):
				return MP_RPAREN();
			case('='):
				return MP_EQUAL();
			case('>'):
				return MP_GTHAN();
			case('<'):
				return MP_LTHAN();
			case(':'):
				return MP_ASSIGN();
			case('+'):
				return MP_PLUS();
			case('-'):
				return MP_MINUS();
			case('*'):
				return MP_TIMES();
			default:
				index++;
				currentLexeme = "" + currentChar;
				return "MP_ERROR";
			}
			
		}
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
		lineNumber = 1;
		currentColNumber = 0;
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
	
	public static String getTokenName() {
		return currentToken;
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
						currentColNumber++;
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
						currentColNumber++;
						currentLexeme = tempLexeme;
						state = 2;
					} else if (currentChar == '\n') { //if we get a new line in the middle of our string return run on string
						currentLexeme = tempLexeme;
						currentToken = "MP_RUN_STRING";
						return currentToken;
					} else { //other
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
					}
					break;
				case 2:
					if (currentChar == '\'') {     //Should this exist? didnt it already find the end ' in case 1?
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						currentColNumber++;
						state = 1;	
					} else {
						index = ++indexOfLastAccept;
						currentToken = "MP_STRING_LIT";
						return currentToken;
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
						currentColNumber++;
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
						currentColNumber++;
						currentToken = "MP_INTEGER_LIT";
					}else if(currentChar == 'e' || currentChar == 'E'){
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
						state = 3;
					}else if(currentChar == '.'){
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
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
						currentColNumber++;
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
						currentColNumber++;
						state = 5;
						currentToken = "MP_FLOAT_LIT";
					}else if(currentChar == '+' || currentChar == '-'){
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
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
						currentColNumber++;
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
						currentColNumber++;
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
						currentColNumber++;
						currentToken = "MP_FIXED_LIT";
					}else if(currentChar == 'e' || currentChar == 'E'){
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
						state = 3;
					}else{
						index = ++indexOfLastAccept;
						return currentToken;
					}
					break;
			}
		}
	}

	public static String MP_COMMENT() {
		int indexOfLastAccept = index - 1;
		char currentChar = file.charAt(index);
		currentLexeme = "";
		String tempLexeme = "";
		int state = 0;
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					if(currentChar == '{') {
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
						state = 1;
					} else {
						// we should never make it here
					}
					break;
				case 1:
					if(currentChar == '}') { // accept
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						currentColNumber++;
						currentLexeme = tempLexeme;
						currentToken = "MP_COMMENT";
						return currentToken;
					} else if (currentChar == (char) 3) { //if we get EOF, we have a run-on comment
						currentLexeme = tempLexeme;
						currentToken = "MP_RUN_COMMENT";
						return currentToken;
					} else { //other
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
					}
					break;
			}
		}
	}
	public static String MP_PERIOD() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '.':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_PERIOD";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_COMMA() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ',':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_COMMA";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_SCOLON() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ';':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_SCOLON";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_LPAREN() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '(':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_LPAREN";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_RPAREN() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ')':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_RPAREN";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}

	public static String MP_EQUAL() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '=':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_EQUAL";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}


	public static String MP_GTHAN() {
		int state = 0;
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			currentChar = file.charAt(index);
			switch (state) {
			case 0:
				switch (currentChar) {
				case '>':
					currentLexeme = currentLexeme+ currentChar;
					index++;
					currentColNumber++;
					state = 1;
				}
				break;
			case 1:
				switch (currentChar) {
				case '=':
					currentLexeme = currentLexeme+ currentChar;
					index++;
					currentColNumber++;
					currentToken = "MP_GEQUAL";
					return currentToken;
				default:
					currentToken = "MP_GTHAN";
					return currentToken;
				}

			}
		}
	}
	
	public static String MP_LTHAN() {
		int state = 0;
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			currentChar = file.charAt(index);
			switch (state) {
				case 0:
					switch (currentChar) {
					case '<':
						currentLexeme = currentLexeme+ currentChar;
						index++;
						currentColNumber++;
						state = 1;
					}
					break;
				case 1:
					switch (currentChar) {
					case '=':
						currentLexeme = currentLexeme+ currentChar;
						index++;
						currentColNumber++;
						currentToken = "MP_LEQUAL";
						return currentToken;
					case '>':
						currentLexeme = currentLexeme+ currentChar;
						index++;
						currentColNumber++;
						currentToken = "MP_NEQUAL";
						return currentToken;
					default:
						currentToken = "MP_LTHAN";
						return currentToken;
					}

			}
		}
	}
	
	
	public static String MP_ASSIGN() {
		int state = 0;
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			currentChar = file.charAt(index);
			switch (state) {
			case 0:
				switch (currentChar) {
				case ':':
					currentLexeme = currentLexeme+ currentChar;
					index++;
					currentColNumber++;
					state = 1;
				}
				break;
			case 1:
				switch (currentChar) {
				case '=':
					currentLexeme = currentLexeme+ currentChar;
					index++;
					currentColNumber++;
					currentToken = "MP_ASSIGN";
					return currentToken;
				default:
					currentToken = "MP_COLON";
					return currentToken;
				}
			}
		}
	}
	
	public static String MP_PLUS() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '+':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_PLUS";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_MINUS() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '-':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_MINUS";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_TIMES() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '*':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_TIMES";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_FLOAT_DIVID() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '/':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_FLOAT_DIVIDE";
				return currentToken;
			default:
				// Should never reach here
			}
		}
	}
	
	public static String MP_IDENTIFIER() {
		int indexOfLastAccept = index - 1;
		char currentChar = file.charAt(index);
		currentLexeme = "";
		String tempLexeme = "";
		int state = 0;
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					if(Character.isAlphabetic(currentChar)) { // to accept state
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						currentColNumber++;
						currentLexeme = tempLexeme;
						state = 1;
					} else if (currentChar == '_') {
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
						state = 2;
					}
					break;
				case 1:
					if(Character.isLetterOrDigit(currentChar)) { // to accept state
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						currentColNumber++;
						currentLexeme = tempLexeme;
					} else if (currentChar == '_') {
						tempLexeme += currentChar;
						index++;
						currentColNumber++;
						state = 2;
					} else { //other
						index = ++indexOfLastAccept;
						currentToken = "MP_IDENTIFIER";
						return currentToken;
					}
					break;
				case 2:
					if(Character.isLetterOrDigit(currentChar)) { // to accept state
						tempLexeme += currentChar;
						currentLexeme = tempLexeme;
						indexOfLastAccept = index;
						index++;
						currentColNumber++;
						currentLexeme = tempLexeme;
						state = 1;
					} else {
						index = ++indexOfLastAccept;
						currentToken = "MP_IDENTIFIER";
						return currentToken;
					}
					break;
			}
		}
	}
	private static String getTokenFromIdentifier(String lexeme){
		for(int i = 0; i < reservedWords.length; i++){
			if(lexeme.equals(reservedWords[i][1])){
				currentToken = reservedWords[i][0];
				return currentToken;
			}
		}
		currentToken = "MP_IDENTIFIER";
		return currentToken;
	}
}
