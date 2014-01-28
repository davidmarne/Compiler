package compiler;


public class Scanner {
	Token currentToken;
	String currentLexeme;
	int lineNumber;
	int colNumber;
	
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
	
	public static openFile(String fileName){
		//get file frome esus
		
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
