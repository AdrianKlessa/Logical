package project;

public class Disjunction extends Function{
	
	
	
	public boolean evaluate() {
		if(left.evaluate()==true||right.evaluate()==true) {
			return true;
		}else {
			return false;
		}
	}
}
