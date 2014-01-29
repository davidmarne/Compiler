package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Scanner {
	static Token currentToken;
	static String currentLexeme;
	static int lineNumber;
	static int colNumber;
	static BufferedReader input;
	
	public static Token getToken(){
		return dispatch();
	}
	
	public static Token dispatch(){
		//skip white space
		
		//set lineNum and colNum
		//peek
		//switch for each possible FSA
		//call that FSA's fucntion which returns token
	}
	
	public static void openFile(String fileName) throws FileNotFoundException{
		//get file frome esus
		input = new BufferedReader(new FileReader(fileName));
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
	
	private void peekedDigit(){
		
	}
	
}
