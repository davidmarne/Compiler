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
	
	public static void programDeclaration(int nestingLevel, int size, String label) throws IOException {
		bw.write("ADD SP #" + size + " SP\n");	
	}
	
	public static void procedureFunctionDeclaration(int nestingLevel, SymbolTable currTable, String name) throws Exception{
		int[] offset = currTable.getOffsetByLexeme(name);
		if(offset == null) {
			throw new Exception(name + " is not defined");
		}
		if(currTable.contains()) {
			nestingLevel--;
		}
		bw.write("PUSH D" + nestingLevel + "\n");
		//push function reference onto stack
		bw.write("PUSH D"+ offset[1] +"\n");
		bw.write("PUSH #" + offset[0]+"\n");
		bw.write("ADDS\n");
	}
	
	public static void updateStackPointer(int nestingLevel, SymbolTable currTable, String name) throws IOException {
		int numParams = currTable.getSymbolByLexeme(name).parameterList.size();
		
		if(currTable.contains()) {
			nestingLevel--;
		}
	
		// move the address of beginning of activation record into DX
		bw.write("PUSH SP\n");
		bw.write("PUSH #" + (numParams + 1) + "\n");
		bw.write("SUBS\n");
		bw.write("POP D" + (nestingLevel+1) + "\n");
	}
	
	public static void procedureFunctionDestroy(int nestingLevel, SymbolTable currTable, String name) throws IOException{
		if(currTable.contains()) {
			nestingLevel--;
		}
		bw.write("MOV D" + nestingLevel + " SP\n");
		bw.write("POP D" + nestingLevel + "\n");

	}
	
	public static void programDestroy(int nestingLevel, int size) throws IOException {
		if(nestingLevel == 0){
			bw.write("MOV D" + nestingLevel + " SP\n");
			bw.write("POP D" + nestingLevel + "\n");
			bw.write("HLT\n");
		}else{
			bw.write("SUB SP #" + size + " SP\n");
			bw.write("RET\n");
		}
	}
	
	
	public static void readStatement(String lexeme, SymbolTable currTable) throws IOException{
		int[] offset = currTable.getOffsetByLexeme(lexeme);
		String type = currTable.getTypeByLexeme(lexeme);
		boolean byRef = false;
		if(offset != null) {
			//by copy
			for (Symbol s: currTable.symbols) {
				if(s.iden.equals(lexeme) && s.mode.equals("ref")) {
					byRef = true;
					break;
				}	
			}
			if(byRef) {
				if(type.equals("MP_FLOAT") || type.equals("MP_FIXED")){
					bw.write("RDF @" + offset[0] + "(D" + offset[1] + ")\n");
				}else{
					bw.write("RD @" + offset[0] + "(D" + offset[1] + ")\n");
				}
			} else {
				if(type.equals("MP_FLOAT") || type.equals("MP_FIXED")){
					bw.write("RDF " + offset[0] + "(D" + offset[1] + ")\n");
				}else{
					bw.write("RD " + offset[0] + "(D" + offset[1] + ")\n");
				}
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
	
	// accessing parameter values
	public static void pushRegisterRef(String lexeme, SymbolTable currTable) throws IOException {
 		int[] offset = currTable.getOffsetByLexeme(lexeme);
		if(offset != null) {
			bw.write("PUSH @" + offset[0] + "(D" + offset[1] + ")\n");
		}	
	}
	
	// passing in parameters
	public static void pushRegisterByReference(String lexeme, SymbolTable currTable) throws IOException {
		int[] offset = currTable.getOffsetByLexeme(lexeme);
		
		//push parameter that is a ref onto stack (so it's original address)
		for(Symbol s : currTable.symbols) {
			if(s.mode == "ref") {
				bw.write("PUSH " + offset[0] + "(D" + offset[1] + ")\n");
				return;
			}
		}
		//push function reference onto stack
		bw.write("PUSH D"+ offset[1] +"\n");
		bw.write("PUSH #" + offset[0]+"\n");
		bw.write("ADDS\n");
		
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
			throw new Exception("Assigning incompatible types");
		}
		bw.write("POP " + offset[0] + "(D" + offset[1] + ")\n");
	}
	
	public static void assignByReference(String result, String exp, int[] offset) throws Exception{
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
		bw.write("POP @" + offset[0] + "(D" + offset[1] + ")\n");
	}
}
