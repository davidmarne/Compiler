package compiler;

import java.util.ArrayList;

public class SymbolTable {
	public String name;
	public int label;
	public int sizeInBytes, nestingLevel;
	public SymbolTable parent;
	
	public ArrayList<Symbol> symbols = new ArrayList<Symbol>();
	
	//constructor for first table (Program's scope)
	public SymbolTable(String name, int label){
		this.name = name;
		this.label = label;
		this.nestingLevel = 0;
		this.parent = null;
	}
	
	//constructor for additional scopes
	public SymbolTable(String name, int label, SymbolTable parent){
		this.name = name;
		this.label = label;
		this.nestingLevel = parent.nestingLevel + 1;
		this.parent = parent;
	}
	
	public void insert(Symbol s){
		symbols.add(s);
	}
	
	public SymbolTable destroy(){
//		printTable();
		return parent;
	}
	
	public int[] getOffsetByLexeme(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				int[] returnArray = {symbol.offset, nestingLevel};
				return returnArray;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.getOffsetByLexeme(lexeme);
		} else {
			return null;
		}
	}
	
	public String getTypeByLexeme(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				return symbol.type;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.getTypeByLexeme(lexeme);
		} else {
			return null;
		}
	}
	
	public String getKindByLexeme(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				return symbol.kind;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.getKindByLexeme(lexeme);
		} else {
			return null;
		}
	}
	
	public Symbol getSymbolByLexeme(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				return symbol;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.getSymbolByLexeme(lexeme);
		} else {
			return null;
		}
	}
	
	public Boolean isFunction(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				if(symbol.kind.equals("function")){
					return true;
				}else{
					return false;
				}
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.isFunction(lexeme);
		} else {
			return null;
		}
	}
	public Boolean isProcedure(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				if(symbol.kind.equals("procedure")){
					return true;
				}else{
					return false;
				}
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.isProcedure(lexeme);
		} else {
			return null;
		}
	}
	public int getLabelByLexeme(String lexeme){
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				return symbol.label;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.getLabelByLexeme(lexeme);
		} else {
			return -1;
		}
	}
	
	public int getSize(){
		return symbols.size();
	}
	
	public void printTable(){
		String parentName;
		if(parent == null){
			parentName = "null";
		}else{
			parentName = parent.name;
		}
		System.out.printf("NAME: %-10s LABEL: %-10s NL: %-10d PARENT: %-10s \n", name, getLabel(), nestingLevel, parentName);
		for(Symbol s : symbols){
			s.printSymbol();
		}
	}
	
	public String getLabel() {
		return "L" + label;
	}
	
	public int getNumOfNonParams(){
		int counter = 0;
		for(Symbol s: symbols){
			if(!s.kind.equals("parameter")){
				counter++;
			}
		}
		return counter;
	}
	
	public boolean contains(){
		if(parent == null){
			return false;
		}
		for(Symbol s: parent.symbols){
			if(name.equals(s.iden)){
				return true;
			}
		}
		return false;
	}
}
