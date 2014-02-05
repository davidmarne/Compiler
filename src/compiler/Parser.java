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
				throw new Exception("PARSE ERROR");
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
			break;
		case "MP_PROCEDURE":
			ProcedureDeclaration();
			ProcedureAndFunctionDeclarationPart();
			break;
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
				break;
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
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void EmptyStatement(){	
		//Epsilon
	}
	
	public static void ReadStatement(){
		switch(lookahead){
		case "MP_READ":
			lookahead = tokens.remove(0);
			switch(lookahead){
			case "MP_LPAREN":
				lookahead = tokens.remove(0);
				ReadParameter();
				ReadParameterTail();
				switch(lookahead){
				case "MP_RPAREN":
					lookahead = tokens.remove(0);
					break;
				default:
					throw new Exception("PARSE ERROR");
				}
			default:
				throw new Exception("PARSE ERROR");

			}
		default:
			throw new Exception("PARSE ERROR");

		}
	}
	
	public static void ReadParameterTail(){
		switch(lookahead){
		case "MP_COMMA":
			lookahead = tokens.remove(0);
			ReadParameter();
			ReadParameterTail();
			break;
		default:
			//Epsilon 
		}
	}
	
	public static void ReadParameter(){
		VariableIdentifer();
	}
	
	public static void WriteStatement(){
		switch(lookahead){
		case "MP_WRITE":
			lookahead = tokens.remove(0);
			switch(lookahead){
			case "LPAREN":
				WriteParameter();
				WriteParameterTail();
				switch(lookahead){
				case "RPAREN":
					lookahead = tokens.remove(0);
					break;
				default:
					throw new Exception("PARSE ERROR");
				}
			default:
				throw new Exception("PARSE ERROR");
			}
		case "MP_WRITELN": //Does not exist yet..
			lookahead = tokens.remove(0);
			switch(lookahead){
			case "LPAREN":
				WriteParameter();
				WriteParameterTail();
				switch(lookahead){
				case "RPAREN":
					lookahead = tokens.remove(0);
					break;
				}
			}
		}
	}
	
	public static void WriteParameterTail(){
		switch(lookahead){
		case "MP_COMMA":
			WriteParameter();
			WriteParameterTail();
		default:
			//Epsilon
		}
	}
	
	public static void WriteParameter(){
		OrdinalExpression();
	}
	
	public static void AssignmentStatement(){
		switch(lookahead){
		case "MP_IDENTIFER":
			lookahead = tokens.remove(0);
			switch(lookahead){
			case "MP_ASSIGN":
				Expression();
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void IfStatement(){
		switch(lookahead){
		case "MP_IF":
			lookahead = tokens.remove(0);
			BooleanExpression();
			switch(lookahead){
			case "MP_THEN":
				lookahead = tokens.remove(0);
				Statement();
				OptionalElsePart();
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void OptionalElsePart(){
		switch(lookahead){
		case "MP_ELSE":
			lookahead = tokens.remove(0);
			Statement();
		default:
			//Epsilon
		}
	}
	
	public static void RepeatStatement(){
		switch(lookahead){
		case "MP_REPEAT":
			lookahead = tokens.remove(0);
			StatementSequence();
			switch(lookahead){
			case "MP_UNTIL":
				lookahead = tokens.remove(0);
				BooleanExpression();
				break;
			}
		}
	}
	
	public static void WhileStatement(){
		switch(lookahead){
		case "MP_WHILE":
			lookahead = tokens.remove(0);
			BooleanExpression();
			switch(lookahead){
			case "MP_DO":
				lookahead = tokens.remove(0);
				Statement();
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ForStatement(){
		switch(lookahead){
		case "MP_FOR":
			lookahead = tokens.remove(0);
			ControlVariable();
			switch(lookahead){
			case "MP_ASSIGN":
				lookahead = tokens.remove(0);
				InitialValue();
				StepValue();
				FinalValue();
				switch(lookahead){
				case "MP_DO":
					lookahead = tokens.remove(0);
					Statement();
					break;
				default:
					throw new Exception("PARSE ERROR");
				}
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ControlVariable(){
		VariableIdentifer();
	}
	
	public static void InitialValue(){
		OrdinalExpression();
	}
	
	public static void StepValue(){
		switch(lookahead){
		case "MP_TO":
			lookahead = tokens.remove(0);
			break;
		case "MP_DOWNTO":
			lookahead = tokens.remove(0);
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void FinalValue(){
		OrdinalExpression();
	}
	
	public static void ProcedureStatement(){
		ProcedureIdentifer();
		OptionalActualParameterList();
	}
	
	public static void OptionalActualParameterList(){
		switch(lookahead){
		case "MP_LPAREN":
			lookahead = tokens.remove(0);
			ActualParameter();
			ActualParameterTail();
			switch(lookahead){
			case "MP_RPAREN":
				lookahead = tokens.remove(0);
				break;
			default:
				throw new Exception("PARSE ERROR");
			}
		default:
			//Epsilon
		}
	}
	
	public static void ActualParameterTail(){
		switch(lookahead){
		case "MP_COMMA":
			lookahead = tokens.remove(0);
			ActualParameter();
			ActualParameterTail();
			break;
		default:
			//Epsilon
		}
	}
	
	public static void Expression(){
		SimpleExpression();
		OptionalRelationalPart();
	}
	
	public static void OptionalRelationalPart(){
		RelationalOperator();
		SimpleExpression();
	}
	
	public static void RelationalOperator(){
		switch(lookahead){
		case "MP_EQUALS":
		case "MP_LTHAN":
		case "MP_GTHAN":
			lookahead = tokens.remove(0);
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}

	public static void SimpleExpression(){
		OptionalSign();
		Term();
		TermTail();
	}
	
	public static void TermTail() {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
		case "MP_OR":
			AddingOperator();
			Term();
			TermTail();
			break;
		default:
			// epsilon
		}
	}
	
	public static void OptionalSign() {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
			lookahead = tokens.remove(0);
		default:
			// epsilon
		}
	}
	
	public static void AddingOperator() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
		case "MP_OR":
			lookahead = tokens.remove(0);
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void Term() {
		Factor();
		FactorTail();
	}
	
	public static void FactorTail() {
		switch(lookahead) {
		case "MP_MULT":
//		case "/":
		case "MP_DIV":
		case "MP_MOD":
		case "MP_AND":
			MultiplyingOperator();
			Factor();
			FactorTail();
		default:
			// epsilon
		}
			
	}
		
}


