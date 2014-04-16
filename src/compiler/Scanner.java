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
	static int indexOfLastAccept;

	static String[][] reservedWords = {{"MP_AND","and"},{"MP_BEGIN", "begin"},{"MP_DIV", "div"},{"MP_DO", "do"},{"MP_DOWNTO", "downto"},
		{"MP_ELSE", "else"},{"MP_END","end"},{"MP_FIXED","fixed"},{"MP_FLOAT", "float"},{"MP_FOR","for"},
		{"MP_FUNCTION","function"},{"MP_IF","if"},{"MP_INTEGER","integer"},{"MP_MOD","mod"},{"MP_NOT","not"},
		{"MP_OR","or"}, {"MP_PROCEDURE","procedure"},{"MP_PROGRAM","program"},{"MP_READ","read"},{"MP_REPEAT","repeat"},
		{"MP_THEN","then"},{"MP_TO","to"},{"MP_UNTIL","until"},{"MP_VAR","var"},{"MP_WHILE","while"},{"MP_WRITE","write"},
		{"MP_BOOLEAN", "boolean"},{"MP_FALSE", "false"},{"MP_STRING", "string"},{"MP_TRUE", "true"}, {"MP_WRITELN","writeln"}};

	public static Token getToken() throws Exception {
		dispatch();
		if (currentToken == "MP_RUN_STRING") {
			throw new Exception("Scanned in a run-on string: " + lineNumber + ":" + colNumber + " " + currentLexeme);
		} else if (currentToken == "MP_ERROR") {
			throw new Exception("Unknown first character of token: " + lineNumber + ":" + colNumber + " " + currentLexeme);
		} else if (currentToken == "MP_RUN_COMMENT") {
			throw new Exception("Scanned in run-on comment: " + lineNumber + ":" + colNumber);
		}

		return new Token(currentToken, currentLexeme, lineNumber, colNumber);	
	}

	public static void dispatch() throws IOException {
		// skip white space
		char currentChar;
		while (true) {
			currentChar = file.charAt(index);
			// Check for EOF
			if(currentChar == (char) 3){
				currentLexeme = "";
				colNumber = currentColNumber;
				currentToken = "MP_EOF";
				return;
			}
			if (Character.isWhitespace(currentChar)) {
				if (currentChar == '\n') {
					lineNumber++;
					currentColNumber = 1;
				} else {
					currentColNumber++;
				}
				index++;
			} else {
				// add whitespace to index of last accept state
				indexOfLastAccept = index;
				break;
			}
		}
		
		colNumber = currentColNumber;
		currentChar = file.charAt(index);
		if (Character.isDigit(currentChar)) {
			digitFSA();
		} else if (Character.isAlphabetic(currentChar) || currentChar == '_') {
			MP_IDENTIFIER();
			getTokenFromIdentifier(currentLexeme);
		} else {
			switch(currentChar){
			case('{'):
				MP_COMMENT();
				break;
			case('\''):
				MP_STRING_LIT();
				break;
			case('.'):
				MP_PERIOD();
				break;
			case(','):
				MP_COMMA();
				break;
			case(';'):
				MP_SCOLON();
				break;
			case('('):
				MP_LPAREN();
				break;
			case(')'):
				MP_RPAREN();
				break;
			case('='):
				MP_EQUAL();
				break;
			case('>'):
				MP_GTHAN();
				break;
			case('<'):
				MP_LTHAN();
				break;
			case(':'):
				MP_ASSIGN();
				break;
			case('+'):
				MP_PLUS();
				break;
			case('-'):
				MP_MINUS();
				break;
			case('*'):
				MP_TIMES();
				break;
			case('/'):
				MP_FLOAT_DIV();
				break;
			default:
				index++;
				currentLexeme = "" + currentChar;
				currentToken = "MP_ERROR";
			}
			return;
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
		// add EOF character
		file += (char) 3;
		lineNumber = 1;
		currentColNumber = 1;
	}

	public static void MP_STRING_LIT() {
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
					return;
				} else { //other
					tempLexeme += currentChar;
					index++;
					currentColNumber++;
				}
				break;
			case 2:
				if (currentChar == '\'') {     
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
					state = 1;	
				} else {
					//done
					index = ++indexOfLastAccept;
					currentToken = "MP_STRING_LIT";
					return;
				}
				break;
			}
		}
	}

	public static void digitFSA(){
		/*
		 * states are enumerated
		 */
		int state = 0;
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
				}else{
					// we should never get here
					index = ++indexOfLastAccept;
					currentToken = "MP_ERROR";
					return;
				}
				break;
			case 1:
				if(Character.isDigit(currentChar)){
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
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
					// done
					index = ++indexOfLastAccept;
					currentToken = "MP_INTEGER_LIT";
					return;
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
				}else{
					// error
					index = ++indexOfLastAccept;
					currentToken = "MP_ERROR";
					return;
				}
				break;
			case 3:
				if (Character.isDigit(currentChar)) {
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
					state = 5;
				} else if (currentChar == '+' || currentChar == '-') {
					tempLexeme += currentChar;
					index++;
					currentColNumber++;
					state = 4;
				} else {
					// error
					index = ++indexOfLastAccept;
					currentToken = "MP_ERROR";
					return;
				}
				break;
			case 4:
				if (Character.isDigit(currentChar)) {
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
					state = 5;
				}
				break;
			case 5:
				if(Character.isDigit(currentChar)){
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
				}else{
					// done
					index = ++indexOfLastAccept;
					currentToken = "MP_FLOAT_LIT";
					return;
				}
				break;
			case 6:
				if(Character.isDigit(currentChar)){
					tempLexeme += currentChar;
					currentLexeme = tempLexeme;
					indexOfLastAccept = index;
					index++;
					currentColNumber++;
				}else if(currentChar == 'e' || currentChar == 'E'){
					tempLexeme += currentChar;
					index++;
					currentColNumber++;
					state = 3;
				}else{
					// done
					index = ++indexOfLastAccept;
					currentToken = "MP_FIXED_LIT";
					return;
				}
				break;
			}
		}
	}

	public static void MP_COMMENT() {
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
					index++;
					currentColNumber++;
					currentLexeme = tempLexeme;
					currentToken = "MP_COMMENT";
					return;
				} else if (currentChar == (char) 3) { //if we get EOF, we have a run-on comment
					currentLexeme = tempLexeme;
					currentToken = "MP_RUN_COMMENT";
					return;
				} else { //other
					tempLexeme += currentChar;
					index++;
					currentColNumber++;
				}
				break;
			}
		}
	}
	public static void MP_PERIOD() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '.':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_PERIOD";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_COMMA() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ',':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_COMMA";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_SCOLON() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ';':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_SCOLON";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_LPAREN() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '(':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_LPAREN";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_RPAREN() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case ')':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_RPAREN";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_EQUAL() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '=':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_EQUAL";
				return;
			default:
				// Should never reach here
			}
		}
	}


	public static void MP_GTHAN() {
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
					return;
				default:
					currentToken = "MP_GTHAN";
					return;
				}

			}
		}
	}

	public static void MP_LTHAN() {
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
					return;
				case '>':
					currentLexeme = currentLexeme+ currentChar;
					index++;
					currentColNumber++;
					currentToken = "MP_NEQUAL";
					return;
				default:
					currentToken = "MP_LTHAN";
					return;
				}

			}
		}
	}


	public static void MP_ASSIGN() {
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
					return;
				default:
					currentToken = "MP_COLON";
					return;
				}
			}
		}
	}

	public static void MP_PLUS() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '+':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_PLUS";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_MINUS() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '-':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_MINUS";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_TIMES() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '*':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_TIMES";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_FLOAT_DIV() {
		char currentChar = file.charAt(index);
		currentLexeme= "";

		while (true) {
			switch (currentChar) {
			case '/':
				currentLexeme = currentLexeme+ currentChar;
				index++;
				currentColNumber++;
				currentToken = "MP_FLOAT_DIV";
				return;
			default:
				// Should never reach here
			}
		}
	}

	public static void MP_IDENTIFIER() {
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
				} else {
					//error
					index = ++indexOfLastAccept;
					currentToken = "MP_ERROR";
					return;
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
				} else { 
					// done
					index = ++indexOfLastAccept;
					currentToken = "MP_IDENTIFIER";
					return;
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
					// error
					index = ++indexOfLastAccept;
					currentToken = "MP_ERROR";
					return;
				}
				break;
			}
		}
	}
	private static void getTokenFromIdentifier(String lexeme){
		if(currentToken == "MP_ERROR") {
			return;
		}
		
		for(int i = 0; i < reservedWords.length; i++){
			if(lexeme.toLowerCase().equals(reservedWords[i][1].toLowerCase())){
				currentToken = reservedWords[i][0];
				return;
			}
		}
		currentToken = "MP_IDENTIFIER";
		return;
	}
}
