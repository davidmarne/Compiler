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
		printTable();
		return parent;
	}
	
	public int[] findByLexeme(String lexeme){
		// search through current table
		for (Symbol symbol : symbols) {
			if(symbol.iden.equals(lexeme)){
				int[] returnArray = {symbol.offset, nestingLevel};
				return returnArray;
			}
		}
		
		//search through any parent tables
		if(parent != null) {
			return parent.findByLexeme(lexeme);
		} else {
			return null;
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
}
