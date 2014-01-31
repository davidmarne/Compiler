package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String filename = args[0];

		try{
			String s = " ";
			Scanner.openFile("test.txt");
			while(s != ""){
				
				s = Scanner.getToken();
				System.out.print("token: " + Scanner.getToken());
				System.out.print("  line number " + Scanner.getLineNumber());
				System.out.print("  column number " + Scanner.getColumnNumber());
				System.out.println("  lexeme " + Scanner.getLexeme());

			}	
		}catch(Exception e){
			
		}
	}

}
