package project;

public class Equivalence extends Function{

	public boolean evaluate() {
		if(left.evaluate()==right.evaluate()) {
			return true;
		}else {
			return false;
		}
	}

	
}
