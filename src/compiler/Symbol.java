package compiler;

import java.util.ArrayList;

public class Symbol {
	public String iden, type, kind, mode, label;
	public int size, offset;
	public ArrayList<Symbol> parameterList = new ArrayList<Symbol>();
	
	// Constructor for symbol table symbol 
	public Symbol(String iden, String type, String kind, String mode, int offset){
		this.iden = iden;
		this.type = type;
		this.kind = kind;
		this.mode = mode;
		this.offset = offset;
	}
	
	// Constructor for symbol table procedure or function 
		public Symbol(String iden, String type, String kind, ArrayList<Symbol> parameterList, int offset){
			this.iden = iden;
			this.type = type;
			this.kind = kind;
			this.offset = offset;
			for(Symbol p: parameterList) {
				this.parameterList.add(p);
			}
		}
	
	// Constructor for parameter lists
	public Symbol(String type, String kind) {
		this.type = type;
		this.kind = kind;
	}
	
	public void printSymbol(){
		if(parameterList.size() > 0) {
			System.out.printf("IDEN: %-10s TYPE: %-15s KIND: %-10s MODE: ->", iden, type, kind);
			for(Symbol s: parameterList) {
				System.out.printf(" (%s, %s)", s.type, s.kind);
			}
		} else {
			System.out.printf("IDEN: %-10s TYPE: %-15s KIND: %-10s MODE: %-10s", iden, type, kind, mode);
		}
		System.out.println("");
	}
}
