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
	
	public static void programDestroy(int nestingLevel, int size) throws IOException {
		bw.write("MOV D" + nestingLevel + " SP\n");
		bw.write("POP D" + nestingLevel + "\n");
		bw.write("HLT");
	}
	
	public static void beginStatement(int label) throws IOException{
		bw.write("L" + label + ":\n");		
	}
	
	public static void readStatement(String lexeme, SymbolTable currTable) throws IOException{
		int[] offset = currTable.getOffsetByLexeme(lexeme);
		String type = currTable.getTypeByLexeme(lexeme);
		if(offset != null) {
			if(type.equals("MP_FLOAT") || type.equals("MP_FIXED")){
				bw.write("RDF " + offset[0] + "(D" + offset[1] + ")\n");
			}else{
				bw.write("RD " + offset[0] + "(D" + offset[1] + ")\n");
			}
		}			
	}
	
	public static void writeStatement(Boolean writeLine) throws IOException{
		if(writeLine){
			bw.write("WRTLNS\n");
		}else{
			bw.write("WRTS\n");	
		}
	}
	
	public static void pushLiteralVal(String intVal) throws IOException {
		bw.write("PUSH #" + intVal + "\n");	
	}
	
	public static void pushRegisterVal(String lexeme, SymbolTable currTable) throws IOException {
		int[] offset = currTable.getOffsetByLexeme(lexeme);
		if(offset != null) {
			bw.write("PUSH " + offset[0] + "(D" + offset[1] + ")\n");
		}	
	}
	
	public static void computeExpression(String factorType, String factorTailType, String operator) throws Exception {
		if(factorType.equals("MP_FIXED")){
			factorType = "MP_FLOAT";
		}
		if(factorTailType.equals("MP_FIXED")){
			factorTailType = "MP_FLOAT";
		}
		// check for type compatibility
		if(factorType.equals(factorTailType)) {
			// do nothing
		} else if(factorType.equals("MP_FLOAT") && factorTailType.equals("MP_INTEGER")) {
			bw.write("CASTSF\n");
		} else if (factorType.equals("MP_INTEGER") && factorTailType.equals("MP_FLOAT")) {
			bw.write("CASTSI\n");
		} else {
			throw new Exception("incompatible types");
		}
		if(factorType.equals("MP_FLOAT")) {
			operator += "F";
		}
		bw.write(operator + "\n");
	}
	
	public static void relationalExpression() {
		
	}
	
	public static void write(String s) throws IOException {
		bw.write(s);
	}
	
	public static void assign(String result, String exp, int[] offset) throws Exception{
		if(result.equals("MP_FIXED")){
			result = "MP_FLOAT";
		}
		if(exp.equals("MP_FIXED")){
			exp = "MP_FLOAT";
		}
		if(result.equals(exp)) {
			// do nothing
		} else if(result.equals("MP_FLOAT") && exp.equals("MP_INTEGER")) {
			bw.write("CASTSF\n");
		} else if (result.equals("MP_INTEGER") && exp.equals("MP_FLOAT")) {
			bw.write("CASTSI\n");
		} else {
			throw new Exception("Assiging incompatible types");
		}
		bw.write("POP " + offset[0] + "(D" + offset[1] + ")\n");
	}
	
}
