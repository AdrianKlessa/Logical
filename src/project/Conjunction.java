package project;

public class Conjunction extends Function{

	
	public boolean evaluate() {
		if(left.evaluate()==true&&right.evaluate()==true) {
			return true;
		}else {
			return false;
		}
	}

}
