package compiler;

import java.util.ArrayList;


public class main {

	public static void main(String[] args) {
		//String filename = args[0];
		ArrayList<Token> tkns = new ArrayList<Token>();
		try{
			Scanner.openFile("test.txt");
			Token s = null;
			while(true){
				try {
					s = Scanner.getToken();
					tkns.add(s);
					System.out.format("token: %-15s", s.token);
					System.out.format("  line number: %-3s", s.lineNumber);
					System.out.format("  column number: %-3s", s.colNumber);
					System.out.println("  lexeme " + s.lexeme);
					if(s.token == "MP_EOF") {
						break;
					}
				} catch (Exception e) {
					System.out.println(e);
				}

			}	
			
			Parser.parser(tkns);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
