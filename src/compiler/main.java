package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = args[0];
		try{
			
			Scanner.openFile(args[0]);
			Scanner.getToken(input);
		}catch(Exception e){
			
		}
	}

}
