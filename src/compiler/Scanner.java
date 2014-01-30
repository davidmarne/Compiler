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
			}
		}
	}
}
