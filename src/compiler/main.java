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
			while(s != ""){
				Scanner.openFile("test.txt");
				s = Scanner.getToken();
				System.out.println(s);
			}
		}catch(Exception e){
			
		}
	}

}
