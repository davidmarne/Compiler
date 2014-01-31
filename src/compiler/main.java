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
				System.out.println(s);
				if(!s.equals("")){
					System.out.println("LEXEME: " + Scanner.getLexeme());
					System.out.println("Line Number: " + Scanner.getLineNumber());
				}
			}
		}catch(Exception e){
			
		}
	}

}
