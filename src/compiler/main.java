package compiler;

import java.util.ArrayList;


public class main {

	public static void main(String[] args) {
		//String filename = args[0];
		ArrayList<String> tkns = new ArrayList<String>();
		try{
			Scanner.openFile("test.txt");
			String s = "";
			while(true){
				try {
					s = Scanner.getToken();
					tkns.add(s);
					System.out.format("token: %-15s", Scanner.getTokenName());
					System.out.format("  line number: %-3s", Scanner.getLineNumber());
					System.out.format("  column number: %-3s", Scanner.getColumnNumber());
					System.out.println("  lexeme " + Scanner.getLexeme());
					if(s == "MP_EOF") {
						break;
					}
				} catch (Exception e) {
					System.out.println(e);
				}

			}	
			
			//Parser.parser(tkns);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
