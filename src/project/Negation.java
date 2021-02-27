package project;

public class Negation extends Function {

	
	public boolean evaluate() {
		return !left.evaluate();
	}

}
