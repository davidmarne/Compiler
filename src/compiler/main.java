package compiler;

import java.util.ArrayList;


public class main {

	public static void main(String[] args) {
		String filename = "";
		if(args.length == 1) {
			filename = args[0];
		} else {
			filename = "12recursion.txt";


		}
		ArrayList<Token> tkns = new ArrayList<Token>();
		try{
			Scanner.openFile(filename);
			Token s = null;
			boolean scannerError = false;
			while(true){
				try {
					s = Scanner.getToken();
					tkns.add(s);
//					System.out.format("token: %-15s", s.token);
//					System.out.format("  line number: %-3s", s.lineNumber);
//					System.out.format("  column number: %-3s", s.colNumber);
//					System.out.println("  lexeme " + s.lexeme);
					if(s.token == "MP_EOF") {
						break;
					}
				} catch (Exception e) {
					System.out.println(e);
					scannerError = true;
				}

			}
			// only parse the program if there was no scanner error
			if(!scannerError) {
				Parser.parser(tkns); 
			} else {
				System.out.println("Program failed to scan");
			}
		}catch(Exception e){
			System.out.println(e);
			System.out.println("Program failed to parse");
		}
	}

}
