package compiler;

import java.util.ArrayList;

public class Parser {
	
	static String lookahead;
	static ArrayList<String> tokens;
	
	public static void parser(ArrayList<String> tkns) throws Exception{
		tokens = tkns;
		lookahead = tokens.remove(0);
		if(lookahead == "MP_PROGRAM"){
			SystemGoal();
		}else{
			throw new Exception("Parse Error");
		}
	}
	
	public static void SystemGoal() throws Exception{
		if(lookahead == "MP_PROGRAM"){
			Program();
		}else{
			throw new Exception("Parse Error");
		}
		match("MP_EOF");
	}


	public static void Program() throws Exception{
		if(lookahead == "MP_PROGRAM"){
			ProgramHeading();
			match("MP_SCOLON");
			if(lookahead == "MP_VAR" || lookahead == "MP_PROCEDURE" || lookahead == "MP_FUNCTION" || lookahead == "MP_BEGIN"){
				Block();
				match("MP_PERIOD");
			}else{
				throw new Exception("Parse Error");
			}
		}else{
			throw new Exception("Parse Error");
		}
	}
	
	public static void ProgramHeading() throws Exception{
		match("MP_PROGRAM");
		if(lookahead == "MP_IDENTIFIER"){
			ProgramIdentifier();
		}else{
			throw new Exception("Parse Error");
		}
	}
	
	public static void Block() throws Exception{
		if(lookahead == "MP_VAR"){
			VariableDeclarationPart();
			if(lookahead == "MP_PROCEDURE" || lookahead == "MP_FUNCTION"){
				ProcedureAndFunctionDeclarationPart();
				if(lookahead == "MP_BEGIN"){
					StatementPart();
				}else{
					throw new Exception("Parse Error");
				}
			}else if(lookahead == "MP_BEGIN"){
				StatementPart();
			}else{
				throw new Exception("Parse Error");
			}
		}else if(lookahead == "MP_PROCEDURE" || lookahead == "MP_FUNCTION"){
			ProcedureAndFunctionDeclarationPart();
			if(lookahead == "MP_BEGIN"){
				StatementPart();
			}else{
				throw new Exception("Parse Error");
			}
		}else if(lookahead == "MP_BEGIN"){
			StatementPart();
		}else{
			throw new Exception("Parse Error");
		}
		
	}
	
	public static void VariableDeclarationPart() throws Exception{
		match("MP_VAR");
		if(lookahead == "MP_IDENTIFER"){
			VariableDeclaration();
			match("MP_SCOLON");
			if(lookahead == "MP_IDENTIFIER"){
				VariableDeclarationTail();
			}else if(lookahead == "MP_FUNCTION" || lookahead == "MP_PROCEDURE"){
				
			}else{
				throw new Exception("Parse Error");
			}
		}else{
			throw new Exception("Parse Error");
		}
		
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
			match("MP_PLUS");
			AddingOperator();
			Term();
			TermTail();
			break;
		case "MP_MINUS":
			match("MP_MINUS");
			AddingOperator();
			Term();
			TermTail();
			break;
		case "MP_OR":
			match("MP_OR");
			AddingOperator();
			Term();
			TermTail();
			break;
		case "MP_DO":
		case "MP_DOWNTO":
		case "MP_END":
		case "MP_TO":
		case "MP_UNTIL":
		case "MP_COMMA":
		case "MP_EQUAL":
		case "MP_GEQUAL":
		case "MP_GTHAN":
		case "MP_LEQUAL":
		case "MP_LTHAN":
		case "MP_NEQUAL":
		case "MP_RPAREN":
		case "MP_SCOLON":
			// epsilon
			break;
		default:
			throw new Exception("Parse Error");
		}
	}
	
	public static void OptionalSign() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
			match("MP_PLUS");
			break;
		case "MP_MINUS":
			match("MP_MINUS");
			break;
		case "MP_FALSE":
		case "MP_NOT":
		case "MP_TRUE":
		case "MP_IDENTIFIER":
		case "MP_INTEGER_LIT":
		case "MP_FIXED_LIT":
		case "MP_FLOAT_LIT":
		case "MP_STRING_LIT":
		case "MP_LPAREN":	
			//epsilon
			break;
		default:
			throw new Exception("Parse Error");
		}
	}

	public static void AddingOperator() throws Exception {
		switch(lookahead) {
		case "MP_PLUS":
			match("MP_PLUS");
			break;
		case "MP_MINUS":
			match("MP_MINUS");
			break;
		case "MP_OR":
			match("MP_OR");
			break;
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
			match("MP_MULT");
			MultiplyingOperator();
			Factor();
			FactorTail();
			break;
		case "MP_FLOAT_DIVIDE":
			match("MP_FLOAT_DIVIDE");
			MultiplyingOperator();
			Factor();
			FactorTail();
			break;
		case "MP_DIV":
			match("MP_DIV");
			MultiplyingOperator();
			Factor();
			FactorTail();
			break;
		case "MP_MOD":
			match("MP_MOD");
			MultiplyingOperator();
			Factor();
			FactorTail();
			break;
		case "MP_AND":
			match("MP_AND");
			MultiplyingOperator();
			Factor();
			FactorTail();
			break;
		case "MP_TO":
		case "MP_UNTIL":
		case "MP_EQUAL":
		case "MP_GEQUAL":
		case "MP_GTHAN":
		case "MP_LEQUAL":
		case "MP_LTHAN":
		case "MP_MINUS":
		case "MP_NEQUAL":
		case "MP_PLUS":
		case "MP_RPAREN":
		case "MP_SCOLON":
			//epsilon
			break;
		default:
			throw new Exception("Parse Error");
		}
	}
	
	public static void MultiplyingOperator() throws Exception {
		switch(lookahead) {
		case "MP_TIMES":
			match("MP_TIMES");
			break;
		case "MP_FLOAT_DIVIDE":
			match("MP_FLOAT_DIVIDE");
			break;
		case "MP_DIV":
			match("MP_DIV");
			break;
		case "MP_MOD":
			match("MP_MOD");
			break;
		case "MP_AND":
			match("MP_AND");
			break;
		default:
			throw new Exception("Parse Error");
		}
	}
	
	public static void Factor() throws Exception {
		switch(lookahead) {
		case "MP_INTEGER_LIT":
			match("MP_INTEGER_LIT");
			break;
		case "MP_FIXED_LIT":
			match("MP_FIXED_LIT");
			break;
		case "MP_FLOAT_LIT":
			match("MP_FLOAT_LIT");
			break;
		case "MP_STRING_LIT":
			match("MP_STRING_LIT");
			break;
		case "MP_TRUE":
			match("MP_TRUE");
			break;
		case "MP_FALSE":
			match("MP_FALSE");
			break;
		case "MP_NOT":
			match("MP_NOT");
			Factor();
			break;
		case "MP_LPAREN":
			match("MP_LPAREN");
			Expression();
			match("MP_RPAREN");
			break;
		case "MP_IDENTIFIER":
			FunctionIdentifier();
			OptionalActualParameterList();
			break;
		default:
			throw new Exception("Parse Error");
		}
	}
		
	public static void ProgramIdentifier() throws Exception {
		match("MP_IDENTIFIER");
	}
	
	public static void VariableIdentifier() throws Exception {
		match("MP_IDENTIFIER");
	}
	
	public static void ProcedureIdentifier() throws Exception {
		match("MP_IDENTIFIER");
	}
	
	public static void FunctionIdentifier() throws Exception {
		match("MP_IDENTIFIER");
	}
	
	public static void BooleanExpression() throws Exception {
		Expression();
	}
	
	public static void OrdinalExpression() throws Exception {
		Expression();
	}
	
	public static void IdentifierList() throws Exception {
		match("MP_IDENTIFIER");
		IdentifierTail();
	}
	
	public static void IdentifierTail() throws Exception {
		switch(lookahead) {
		case "MP_COMMA":
			match("MP_COMMA");
			match("MP_IDENTIFIER");
			IdentifierTail();
			break;
		case "MP_COLON":
			//epsilon
			break;
		default:
			throw new Exception("Parse Error");
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


