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
	
	
	
	public static Token getToken(){
		return dispatch();
	}
	
	public static Token dispatch() throws IOException{
		//skip white space
		char currentChar;
		while(true){
			currentChar = file.charAt(index);
			if(Character.isWhitespace(currentChar)){
				if(currentChar == '\n'){
					lineNumber++;
					colNumber = 0;
				}else{
					colNumber++;
				}
				index++;
			}else{
				break;
			}
		}
		currentChar = file.charAt(index);
		if(Character.isDigit(currentChar)){
			//go to digit FSA
		}else if(Character.isAlphabetic(currentChar)){
			currentChar = Character.toLowerCase(currentChar);
			//go to identifier FSA
		}else{
			
		}
		//switch for each possible FSA
		//call that FSA's fucntion which returns token
	}
	
	public static void openFile(String fileName) throws IOException{
		//get file frome esus
		index = 0;
		input = new BufferedReader(new FileReader(fileName));
		file = "";
		int in = input.read();
		//read in file to string
		while(in != -1){
			file += ((char) in);
			in = input.read();
		}
		lineNumber = 0;
		colNumber = 0;
	}
	
	public static String getLexeme(){
		return currentLexeme;
	}
	
	public static int getLineNumber(){
		return lineNumber;
	}
	
	public static int getColumnNumber(){
		return colNumber;
	}
	
	public static Token digitFSA(){
		/*
		 * states are enumerated
		 */
		int state = 0;
		int indexOfLastAccept;
		char currentChar = file.charAt(index);
		String lexeme = "";
		
		while(true){
			currentChar = file.charAt(index);
			switch(state){
				case 0:
					switch(currentChar){
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
							//return mp_int_lit
							//index = indexOfLastAccept + 1
							//
					}
				case 1:
					switch(currentChar){
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
					switch(currentChar){
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
					switch(currentChar){
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
						case '+' :
							lexeme = lexeme + currentChar;
							index++;
							colNumber++;
							state = 4;
						case '-' :
							lexeme = lexeme + currentChar;
							index++;
							colNumber++;
							state = 4;
					}
				case 4:
					switch(currentChar){
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
					switch(currentChar){
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
					switch(currentChar){
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
}
