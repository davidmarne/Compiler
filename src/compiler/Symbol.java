package compiler;

public class Symbol {
	public String iden, type, kind, mode, label;
	public int size, offset;
	
	public Symbol(String iden, String type, String kind, String mode){
		this.iden = iden;
		this.type = type;
		this.kind = kind;
		this.mode = mode;
	}
	
}
