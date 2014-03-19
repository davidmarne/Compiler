package compiler;

import java.util.ArrayList;

public class SymbolTable {
	public String name;
	public char label;
	public int sizeInBytes, nestingLevel;
	public SymbolTable parent;
	
	public ArrayList<Symbol> table = new ArrayList<Symbol>();
	
	//constructor for additional scopes
	public SymbolTable(String name, char label, SymbolTable parent){
		this.name = name;
		this.label = label;
		this.nestingLevel = parent.nestingLevel + 1;
		this.parent = parent;
	}
	
	//constructor for first table (Program's scope)
	public SymbolTable(String name, char label){
		this.name = name;
		this.label = label;
		this.nestingLevel = 1;
		this.parent = null;
	}
	
	public void insert(Symbol s){
		table.add(s);
	}
	
	public SymbolTable destroy(){
		printTable();
		return parent;
	}
	
	public void find(){
		
	}
	
	public void printTable(){
		String parentName;
		if(parent == null){
			parentName = "null";
		}else{
			parentName = parent.name;
		}
		System.out.printf("NAME: %s, LABEL: %c, NL: %d, PARENT: %s \n", name, label, nestingLevel, parentName);
		for(Symbol s : table){
			s.printSymbol();
		}
	}
}
