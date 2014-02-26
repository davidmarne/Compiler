package compiler;


public class main {

	public static void main(String[] args) {
		//String filename = args[0];

		try{
			Scanner.openFile("test.txt");
			String s = "";
			while(true){
				try {
					s = Scanner.getToken();
					if(s == "") {
						break;
					}
					System.out.format("token: %-15s", Scanner.getTokenName());
					System.out.format("  line number: %-3s", Scanner.getLineNumber());
					System.out.format("  column number: %-3s", Scanner.getColumnNumber());
					System.out.println("  lexeme " + Scanner.getLexeme());
				} catch (Exception e) {
					System.out.println(e);
				}

			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
