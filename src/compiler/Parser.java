package compiler;

import java.util.ArrayList;

public class Parser {
	
	static String lookahead;
	static ArrayList<String> tokens;
	
	public static void parser(ArrayList<String> tkns) throws Exception{
		tokens = tkns;
		lookahead = tokens.remove(0);
		SystemGoal();
	}
	
	public static void SystemGoal() throws Exception{
		Program();
		EOF();
	}
	
	private static void EOF() {
		// TODO Auto-generated method stub
		
	}

	public static void Program() throws Exception{
		
		ProgramHeading();
		match("MP_SCOLON");
		Block();
		match("MP_PERIOD");
		/*
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
		}*/
	}
	
	public static void ProgramHeading() throws Exception{
		match("MP_PROGRAM");
		ProgramIdentifier();
		/*
		switch(lookahead){
		case "MP_PROGRAM":
			lookahead = tokens.remove(0);
			ProgramIdentifier();
			break;
		default:
			throw new Exception("PARSE ERROR");
		}*/
	}
	
	public static void Block() throws Exception{
		VariableDeclarationPart();
		ProcedureAndFunctionDeclarationPart();
		StatementPart();
	}
	
	public static void VariableDeclarationPart() throws Exception{
		switch(lookahead){
		case "MP_VAR":
			match("MP_VAR");
			
			VariableDeclaration();
			match("MP_SCOLON");
			VariableDeclarationTail();
			break;
		default:
			//epsilon
		}
			/*
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
		}*/
	}
	
	public static void VariableDeclarationTail() throws Exception{
		switch(lookahead){
		case "MP_IDENTIFIER":
			VariableDeclaration();
			match("MP_SCOLON");
			break;
		default:
			//epsilon
		}
			/*
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
		
		*/
	}
	
	public static void VariableDeclaration() throws Exception{
		IdentifierList();
		match("MP_COLON");
		switch(lookahead){
		case "MP_INTEGER":
		case "MP_BOOLEAN":
		case "MP_FLOAT":
		case "MP_STRING":
			Type();
		}
		
		/*
		Identifierlist();
		switch(lookahead){
		case "MP_COLON":
			lookahead = tokens.remove(0);
			Type();
			break;
		default:
			throw new Exception("PARSE ERROR");
		}*/
	}
	
	public static void Type() throws Exception{
		switch(lookahead){
		case "MP_INTEGER":
			//term
			match("MP_INTEGER");
			break;
		case "MP_FLOAT":
			//term
			match("MP_FLOAT");
			break;
		case "MP_STRING":
			//term
			match("MP_STRING");
			break;
		case "MP_BOOLEAN":
			//term
			match("MP_BOOLEAN");
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ProcedureAndFunctionDeclarationPart() throws Exception{
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
			//epsilon
		}
	}
	
	public static void ProcedureDeclaration() throws Exception{
		ProcedureHeading();
		match("MP_SCOLON");
		Block();
		match("MP_SCOLON");
		/*
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
		}*/
	}
	
	public static void FunctionDeclaration() throws Exception{
		FunctionHeading();
		match("MP_SCOLON");
		Block();
		match("MP_SCOLON");
		/*
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
		}*/
	}
	
	public static void ProcedureHeading() throws Exception{
		match("MP_FUNCTION");
		ProcedureIdentifier();
		OptionalFormalParameterList();
	}
	
	public static void FunctionHeading() throws Exception{
		match("MP_FUNCTION");
		FunctionIdentifier();
		OptionalFormalParameterList();
		Type();
	}
	
	public static void OptionalFormalParameterList() throws Exception{
		switch(lookahead){
		case "MP_LPAREN":
			match("MP_LPAREN");
			FormalParameterSection();
			FormalParameterSectionTail();
			match("MP_RPAREN");
			break;
		default:
			//epsilon
		}
	}
	
	public static void FormalParameterSectionTail() throws Exception{
		switch(lookahead){
		case "MP_SCOLON":
			match("MP_SCOLON");
			FormalParameterSection();
			FormalParameterSectionTail();
			break;
		default:
			//epsilon
		}
	}
	
	public static void FormalParameterSection() throws Exception{
		switch(lookahead){
		case "MP_IDENTIFIER":
			ValueParameterSection();
			break;
		case "MP_VAR":
			VariableParameterSection();
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ValueParameterSection() throws Exception{
		IdentifierList();
		match("MP_COLON");
		Type();
	}
	
	public static void VariableParameterSection() throws Exception{
		match("MP_VAR");
		IdentifierList();
		match("MP_COLON");
		Type();
	}
	
	public static void StatementPart() throws Exception{
		CompoundStatement();
	}
	
	public static void CompoundStatement() throws Exception{
		match("MP_BEGIN");
		StatementSequence();
		match("MP_END");
	}
	
	public static void StatementSequence() throws Exception{
		Statement();
		StatementTail();
	}
	
	public static void StatementTail() throws Exception{
		switch(lookahead){
		case "MP_SCOLON":
			match("MP_SCOLON");
			Statement();
			StatementTail();
			break;
		default:
			//epsilon
		}
	}
	
	public static void Statement() throws Exception{
		switch(lookahead){
		case "MP_BEGIN":
			CompoundStatement();
			break;
		case "MP_READ":
			ReadStatement();
			break;
		case "MP_WRITE":
			WriteStatement();
			break;
		case "MP_IDENTIFIER()":
			AssignmentStatement();  //Could be assignment statment or procedure statment WTH do i do?
			break;
		case "MP_IF":
			IfStatement();
			break;
		case "MP_WHILE":
			WhileStatement();
			break;
		case "MP_REPEAT":
			RepeatStatement();
			break;
		case "MP_FOR":
			ForStatement();
			break;
		default:
			EmptyStatement();
		}
	}
	
	/********************
	 JOURDANS SECTION 
	 ********************/
	
	public static void EmptyStatement(){	
		//Epsilon
	}
	
	public static void ReadStatement() throws Exception{
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
	
	public static void ReadParameterTail() throws Exception{
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
	
	public static void ReadParameter() throws Exception{
		VariableIdentifier();
	}
	
	public static void WriteStatement() throws Exception{
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
	
	public static void WriteParameterTail() throws Exception{
		switch(lookahead){
		case "MP_COMMA":
			WriteParameter();
			WriteParameterTail();
		default:
			//Epsilon
		}
	}
	
	public static void WriteParameter() throws Exception{
		OrdinalExpression();
	}
	
	public static void AssignmentStatement() throws Exception{
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
	
	public static void IfStatement() throws Exception{
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
	
	public static void OptionalElsePart() throws Exception{
		switch(lookahead){
		case "MP_ELSE":
			lookahead = tokens.remove(0);
			Statement();
		default:
			//Epsilon
		}
	}
	
	public static void RepeatStatement() throws Exception{
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
	
	public static void WhileStatement() throws Exception{
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
	
	public static void ForStatement() throws Exception{
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
	
	public static void ControlVariable() throws Exception{
		VariableIdentifier();
	}
	
	public static void InitialValue() throws Exception{
		OrdinalExpression();
	}
	
	public static void StepValue() throws Exception{
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
	
	public static void FinalValue() throws Exception{
		OrdinalExpression();
	}
	
	public static void ProcedureStatement() throws Exception{
		ProcedureIdentifier();
		OptionalActualParameterList();
	}
	
	public static void OptionalActualParameterList() throws Exception{
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
	
	public static void ActualParameterTail() throws Exception{
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
	
	public static void ActualParameter() throws Exception{
		switch(lookahead){
		case "MP_PLUS":
			match("MP_PLUS");
			break;
		case "MP_MINUS":
			match("MP_MINUS");
			break;
		default:
			//Epsilon
		}
	}
	
	
	public static void Expression() throws Exception{
		SimpleExpression();
		OptionalRelationalPart();
	}
	
	public static void OptionalRelationalPart() throws Exception{
		RelationalOperator();
		SimpleExpression();
	}
	
	public static void RelationalOperator() throws Exception{
		switch(lookahead){
		case "MP_EQUALS":
		case "MP_LTHAN":
		case "MP_GTHAN":
		case "MP_LEQUAL":
		case "MP_GEQUAL":
		case "MP_NEQUAL":
			lookahead = tokens.remove(0);
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}

	
	/****************
	 * DOMS SECTION
	 ****************/
	public static void SimpleExpression() throws Exception {
		OptionalSign();
		Term();
		TermTail();
	}
	
	public static void TermTail() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
		case "MP_OR":
			match(lookahead);
			AddingOperator();
			Term();
			TermTail();
			break;
		default:
			// epsilon
		}
	}
	
	public static void OptionalSign() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
			match(lookahead);
		default:
			// epsilon
		}
	}
	
	public static void AddingOperator() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
		case "MP_MINUS":
		case "MP_OR":
			match(lookahead);
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void Term() throws Exception {
		Factor();
		FactorTail();
	}
	
	public static void FactorTail() throws Exception {
		switch(lookahead) {
		case "MP_MULT":
		case "MP_FLOAT_DIVIDE":
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
	
	public static void MultiplyingOperator() throws Exception {
		switch(lookahead) {
		case "MP_TIMES":
		case "MP_FLOAT_DIVIDE":
		case "MP_DIV":
		case "MP_MOD":
		case "MP_AND":
			match(lookahead);
			break;
		default:
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void Factor() throws Exception {
		switch(lookahead) {
		case "MP_INTEGER_LIT":
		case "MP_FIXED_LIT":
		case "MP_FLOAT_LIT":
		case "MP_STRING_LIT":
		case "MP_TRUE":
		case "MP_FALSE":
			match(lookahead);
			break;
		case "MP_NOT":
			match(lookahead);
			Factor();
			break;
		case "MP_LPAREN":
			match(lookahead); // "("
			Expression();
			match(lookahead); // ")"
			break;
		case "MP_IDENTIFIER":
			FunctionIdentifier();
			OptionalActualParameterList();
		default:
			throw new Exception("PARSE ERROR");
		}
	}
		
	public static void ProgramIdentifier() throws Exception {
		if(lookahead.equals("MP_IDENTIFIER")) {
			match(lookahead);
		} else {
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void VariableIdentifier() throws Exception {
		if(lookahead.equals("MP_IDENTIFIER")) {
			match(lookahead);
		} else {
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void ProcedureIdentifier() throws Exception {
		if(lookahead.equals("MP_IDENTIFIER")) {
			match(lookahead);
		} else {
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void FunctionIdentifier() throws Exception {
		if(lookahead.equals("MP_IDENTIFIER")) {
			match(lookahead);
		} else {
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void BooleanExpression() throws Exception {
		Expression();
	}
	
	public static void OrdinalExpression() throws Exception {
		Expression();
	}
	
	public static void IdentifierList() throws Exception {
		if(lookahead.equals("MP_IDENTIFIER")){
			match(lookahead);
			IdentifierTail();
		} else {
			throw new Exception("PARES ERROR");
		}
	}
	
	public static void IdentifierTail() throws Exception {
		switch(lookahead) {
		case "MP_COMMA":
			match(lookahead);
			if(lookahead.equals("MP_IDENTIFIER")){
				match(lookahead);
				IdentifierTail();
			} else {
				throw new Exception("PARES ERROR");
			}
			break;
		default:
			// epsilon
		}	
	}
	
	
	public static void match(String token) throws Exception{
		if(lookahead.equals(token)) {
			tokens.remove(0);
			lookahead = tokens.get(0);
		}else{
			throw new Exception("PARSE ERROR");
		}
	}
}


