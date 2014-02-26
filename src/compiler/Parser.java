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
		match("MP_EOF");
	}


	public static void Program() throws Exception{
		ProgramHeading();
		match("MP_SCOLON");
		Block();
		match("MP_PERIOD");
	}
	
	public static void ProgramHeading() throws Exception{
		match("MP_PROGRAM");
		ProgramIdentifier();
	}
	
	public static void Block() throws Exception{
		VariableDeclarationPart();
		ProcedureAndFunctionDeclarationPart();
		StatementPart();
	}
	
	public static void VariableDeclarationPart() throws Exception{
		if(lookahead == "MP_VAR"){
			match("MP_VAR");
			VariableDeclaration();
			match("MP_SCOLON");
			VariableDeclarationTail();
		}else{
			//epsilon
		}
	}
	
	public static void VariableDeclarationTail() throws Exception{
		if(lookahead == "MP_IDENTIFIER"){
			VariableDeclaration();
			match("MP_SCOLON");
			VariableDeclarationTail();
		}else{
			//epsilon
		}
	}
	
	public static void VariableDeclaration() throws Exception{
		IdentifierList();
		match("MP_COLON");
		Type();
	}
	
	public static void Type() throws Exception{
		switch(lookahead){
		case "MP_INTEGER":
			match("MP_INTEGER");
		case "MP_STRING":
			match("MP_STRING");
		case "MP_BOOLEAN":
			match("MP_BOOLEAN");
		case "MP_FIXED":
			match("MP_FIXED");
		case "MP_FLOAT":
			match("MP_FLOAT");
		default:
			throw new Exception("Parse Error");
		}
	}
	
	public static void ProcedureAndFunctionDeclarationPart() throws Exception{
		if(lookahead == "MP_FUNCTION"){
			FunctionDeclaration();
			ProcedureAndFunctionDeclarationPart();
			
		}else if(lookahead == "MP_PROCEDURE"){
			ProcedureDeclaration();
			ProcedureAndFunctionDeclarationPart();
		}else{
			//epsilon
		}
	}
	
	public static void ProcedureDeclaration() throws Exception{
		ProcedureHeading();
		match("MP_SCOLON");
		Block();
		match("MP_SCOLON");
	}
	
	public static void FunctionDeclaration() throws Exception{
		FunctionHeading();
		match("MP_SCOLON");
		Block();
		match("MP_SCOLON");
	}
	
	public static void ProcedureHeading() throws Exception{
		match("MP_PROCEDURE");
		ProcedureIdentifier();
		OptionalFormalParameterList();
	}
	
	public static void FunctionHeading() throws Exception{
		match("MP_FUNCTION");
		ProcedureIdentifier();
		OptionalFormalParameterList();
		match("MP_COLON");
		Type();
	}
	
	public static void OptionalFormalParameterList() throws Exception{
		if(lookahead == "MP_LPAREN"){
			match("MP_LPAREN");
			FormalParameterSection();
			FormalParameterSectionTail();
			match("MP_RPAREN");
		}else{
			//epsilon
		}
	}
	
	public static void FormalParameterSectionTail() throws Exception{
		if(lookahead == "MP_SCOLON"){
			match("MP_SCOLON");
			FormalParameterSection();
			FormalParameterSectionTail();
		}else{
			//epsilon
		}
	}
	
	public static void FormalParameterSection() throws Exception{
		if(lookahead == "MP_IDENTIFIER"){
			ValueParameterSection();
		}else if(lookahead == "MP_VAR"){
			VariableParameterSection();
		}else{
			throw new Exception("Parse Error");
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
		if(lookahead == "MP_SCOLON"){
			match("MP_SCOLON");
			Statement();
			StatementTail();
		}else{
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
			try{
				AssignmentStatement();  //Could be assignment statment or procedure statment WTH do i do?
			}catch(Exception e){
				OptionalActualParameterList();
			}
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
		match("MP_READ");
		match("MP_LPAREN");
		ReadParameter();
		ReadParameterTail();
		match("MP_RPAREN");
	}
	
	public static void ReadParameterTail() throws Exception{
		if(lookahead == "MP_COMMA"){
			match("MP_COMMA");
			ReadParameter();
			ReadParameterTail();
		}else{
			//epsilon
		}
	}
	
	public static void ReadParameter() throws Exception{
		VariableIdentifier();
	}
	
	public static void WriteStatement() throws Exception{
		if(lookahead == "MP_WRITE"){
			match("MP_WRITE");
			match("MP_LPAREN");
			WriteParameter();
			WriteParameterTail();
			match("MP_RPAREN");
		}else if(lookahead == "MP_WRITELN"){ //Does not exist yet..
			match("MP_WRITELN");
			match("MP_LPAREN");
			WriteParameter();
			WriteParameterTail();
			match("MP_RPAREN");
		}else{
			throw new Exception("PARSE ERROR");
		}
	}
	
	public static void WriteParameterTail() throws Exception{
		if(lookahead == "MP_COMMA"){
			match("MP_COMMA");
			WriteParameter();
			WriteParameterTail();
		}else{
			//Epsilon
		}
	}
	
	public static void WriteParameter() throws Exception{
		OrdinalExpression();
	}
	
	public static void AssignmentStatement() throws Exception{
		match("MP_IDENTIFIER");
		match("MP_ASSIGN");
		Expression();
	}
	
	public static void IfStatement() throws Exception{
		match("MP_IF");
		BooleanExpression();
		match("MP_THEN");
		Statement();
		OptionalElsePart();
	}
	
	public static void OptionalElsePart() throws Exception{
		if(lookahead == "MP_ELSE"){
			match("MP_ELSE");
			Statement();
		}else{
			//epsilon
		}
	}
	
	public static void RepeatStatement() throws Exception{
		match("MP_REPEAT");
		StatementSequence();
		match("MP_UNTIL");
		BooleanExpression();
	}
	
	public static void WhileStatement() throws Exception{
		match("MP_WHILE");
		BooleanExpression();
		match("MP_DO");
		Statement();
	}
	
	public static void ForStatement() throws Exception{
		match("MP_FOR");
		ControlVariable();
		match("MP_ASSIGN");
		InitialValue();
		StepValue();
		FinalValue();
		match("MP_DO");
		Statement();
	}
	
	public static void ControlVariable() throws Exception{
		VariableIdentifier();
	}
	
	public static void InitialValue() throws Exception{
		OrdinalExpression();
	}
	
	public static void StepValue() throws Exception{
		if(lookahead == "MP_TO"){
			match("MP_TO");
		}else if(lookahead == "MP_DOWNTO"){
			match("MP_DOWNTO");
		}else{
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
		if(lookahead == "MP_LPAREN"){
			match("MP_LPAREN");
			ActualParameter();
			ActualParameterTail();
			match("MP_RPAREN");
		}else{
			//epsilon
		}
	}
	
	public static void ActualParameterTail() throws Exception{
		if(lookahead == "MP_COMMA"){
			match("MP_COMMA");
			ActualParameter();
			ActualParameterTail();
		}else{
			//epsilon
		}
	}
	
	public static void ActualParameter() throws Exception{
		OrdinalExpression();
	}
	
	
	public static void Expression() throws Exception{
		SimpleExpression();
		OptionalRelationalPart();
	}
	
	public static void OptionalRelationalPart() throws Exception{
		if(lookahead == "MP_EQUAL" || lookahead == "MP_GTHAN" || lookahead == "MP_LTHAN" || lookahead == "MP_LEQUAL" || lookahead == "MP_GEQUAL" || lookahead == "MP_NEQUAL"){
			RelationalOperator();
			SimpleExpression();
		}else{
			//epsilon
		}
	}
	
	public static void RelationalOperator() throws Exception{
		switch(lookahead){
		case "MP_EQUAL":
			match("MP_EQUAL");
			break;
		case "MP_LTHAN":
			match("MP_LTHAN");
			break;
		case "MP_GTHAN":
			match("MP_GTHAN");
			break;
		case "MP_LEQUAL":
			match("MP_LEQUAL");
			break;
		case "MP_GEQUAL":
			match("MP_GEQUAL");
			break;
		case "MP_NEQUAL":
			match("MP_NEQUAL");
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


