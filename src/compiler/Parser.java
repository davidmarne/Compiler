package compiler;

import java.util.ArrayList;

public class Parser {
	
	static String lookahead;
	static ArrayList<String> tokens;
	
	public static void parser(ArrayList<String> tkns){
		tokens = tkns;
		lookahead = tokens.remove(0);
		SystemGoal();
	}
	
	public static void SystemGoal(){
		Program();
		EOF();
	}
	
	public static void Program(){
		ProgramHeading();
		switch(lookahead){
		case "MP_SCOLON":
			lookahead = tokens.remove(0);
			Block();
			switch(lookahead){
			case "MP_PERIOD":
				lookahead = tokens.remove(0);
				break; //unnecessary but i'm leaving it
			}
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ProgramHeading(){
		switch(lookahead){
		case "MP_PROGRAM":
			lookahead = tokens.remove(0);
			ProgramIdentifier();
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void Block(){
		VariableDeclarationPart();
		ProcedureAndFunctionDeclarationPart();
		StatementPart();
	}
	
	public static void VariableDeclarationPart(){
		switch(lookahead){
		case "MP_VAR":
			lookahead = tokens.remove(0);
			VariableDeclaration();
			switch(lookahead){
			case "MP_SCOLON":
				lookahead = tokens.remove(0);
				VariableDeclarationTail();
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
			break;
		default:
			//empty -- could be epsilon
		}
	}
	
	public static void VariableDeclarationTail(){
		switch(lookahead){
		case "MP_IDENTIFIER":
			VariableDeclaration();
			switch(lookahead){
			case "MP_SCOLON":
				lookahead = tokens.remove(0);
				VariableDeclarationTail();
				break;
			default:
				//empty --- im confused about this one
			}
		default:
			//epsilon
		}
		
		
	}
	
	public static void VariableDeclaration(){
		Identifierlist();
		switch(lookahead){
		case "MP_COLON":
			lookahead = tokens.remove(0);
			Type();
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void Type(){
		switch(lookahead){
		case "MP_INTEGER":
			//term
		case "MP_FLOAT":
			//term
		case "MP_STRING":
			//term
		case "MP_BOOLEAN":
			//term
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ProcedureAndFunctionDeclarationPart(){
		switch(lookahead){
		case "MP_FUNCTION":
			FunctionDeclaration();
			ProcedureAndFunctionDeclarationPart();
		case "MP_PROCEDURE":
			ProcedureDeclaration();
			ProcedureAndFunctionDeclarationPart();
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ProcedureDeclaration(){
		ProcedureHeading();
		switch(lookahead){
		case "MP_SCOLON":
			lookahead = tokens.remove(0);
			Block();
			switch(lookahead){
			case "MP_SCOLON":
				//good to go
				lookahead = tokens.remove(0);
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void FunctionDeclaration(){
		FunctionHeading();
		switch(lookahead){
		case "MP_SCOLON":
			lookahead = tokens.remove(0);
			Block();
			switch(lookahead){
			case "MP_SCOLON":
				//good to go
				lookahead = tokens.remove(0);
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
}
