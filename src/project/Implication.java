package project;

public class Implication extends Function{

	
	public boolean evaluate() {
		if(left.evaluate()==false||right.evaluate()==true) {
			return true;
		}else {
			return false;
		}
	}

}
