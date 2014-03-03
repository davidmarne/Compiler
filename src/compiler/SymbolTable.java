package compiler;

import java.util.ArrayList;

public class SymbolTable {
	public String name, label, nestingLabel;
	public int sizeInBytes;
	public SymbolTable parent;
	
	private ArrayList<Symbol> table = new ArrayList<Symbol>();
	
	public SymbolTable(String name, String label, String nestingLabel, SymbolTable parent){
		this.name = name;
		this.label = label;
		this.nestingLabel = nestingLabel;
		this.parent = parent;
	}
	
	public void insert(Symbol s){
		table.add(s);
	}
	
	public void destroy(SymbolTable t){
		t = parent;
	}
	
	public void find(){
		
	}
}
