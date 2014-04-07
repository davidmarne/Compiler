package compiler;

import java.io.*;

public class SymanticAnalyzer {
	private static BufferedWriter bw;
	public static void init() {
		try { 
			File outFile = new File("output.pas");
 
			// if file doesn't exists, then create it
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
 
			FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() throws IOException {
		bw.close(); 
	}
	
	public static void programDeclaration(int nestingLevel, int size) throws IOException {
		bw.write("PUSH D" + nestingLevel + "\n");
		bw.write("MOV SP D" + nestingLevel + "\n");
		bw.write("ADD SP #" + size + " SP\n");	
		bw.write("BR L" + nestingLevel + "\n");	
	}
	
	public static void beginStatement(int label) throws IOException{
		bw.write("L" + label + ":\n");		
	}
	
	public static void readStatement(String lexeme, SymbolTable currTable) throws IOException{
		int[] offset = currTable.findByLexeme(lexeme);
		if(offset != null) {
			bw.write("RD " + offset[0] + "(D" + offset[1] + ")\n");
		}			
	}
	
	public static void writeStatement(String lexeme, SymbolTable currTable) throws IOException{
		int[] offset = currTable.findByLexeme(lexeme);
		if(offset != null) {
			bw.write("WRT " + offset[0] + "(D" + offset[1] + ")\n");
		}			
	}
	
	public static void pushLiteralVal(String intVal) throws IOException {
		bw.write("PUSH #" + intVal);	
	}
	
	public static void pushRegisterVal(String lexeme, SymbolTable currTable) throws IOException {
		int[] offset = currTable.findByLexeme(lexeme);
		if(offset != null) {
			bw.write("PUSH " + offset[0] + "(D" + offset[1] + ")\n");
		}	
	}
}
