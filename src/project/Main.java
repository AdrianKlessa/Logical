package project;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	static int variableNumber=0;
	static ArrayList<String> variableNames = new ArrayList<String>();
	static ArrayList<Row> table = new ArrayList<Row>();
	static String expression;
	static boolean cantParseExpression;
	public static void main(String[] args) {
		
		boolean exit=false;
		Scanner scanner=new Scanner(System.in);
		intro();
		while(!exit) {
			System.out.println("Please input a formula...");
			expression = scanner.nextLine();
			if(expression.compareTo("exit")==0) {
				scanner.close();
				break;
			}
			cantParseExpression=false;
			System.out.println("");
			if(expression.compareTo("")!=0) { //Makes sure that user input isn't empty
				loadVariableNames(expression);
				generateRows();
				evaluateAllRows();
				if(cantParseExpression) {
					System.out.println("");
					System.out.println("Couldn't parse the expression");
					System.out.println("");
				}else {
					System.out.println("");
					printDNF();
					System.out.println("");
					printCNF();
					System.out.println("");
				}
			}

			table.clear();
			variableNames.clear();
			variableNumber=0;
		}

		
		
		
		
		
		
		
		/*
		//Testing
		Variable a = new Variable();
		Variable b = new Variable();
		Function operation = new Implication();
		
		a.value=true;
		b.value=false;
		
		operation.left=a;
		operation.right=b;
		
		
		System.out.println(operation.evaluate());
		*/
	}
	
	
	//Loads all letters in the expression as variables to the variableNames array (aside from "v")
	
	public static void intro() {
		System.out.println("Welcome to the CNF/DNF formula generator!");
		System.out.println("The program converts a valid formula into a CNF and DNF form");
		System.out.println("It also shows the true/false values for all evaluations");
		System.out.println("Made by Adrian Klessa, Jakub Rajewicza and Max Dmyterko");
		System.out.println("");
	}
	public static void loadVariableNames(String input) {
		boolean alreadyInList;
		for(int i=0;i<input.length();i++) {
			alreadyInList=false;
			if(input.substring(i,i+1).compareTo("v")==0) {
				continue;
			}
			
			
			if(Character.isLetter(input.charAt(i))) {
				for(String variableIterator : variableNames) {
					if(variableIterator.compareTo(input.substring(i,i+1))==0) {
						alreadyInList=true;
						break;
					}
				}
					if(alreadyInList==false) {
						variableNames.add(input.substring(i,i+1));
						variableNumber++;
					}

			}
		}
	}
	
	public static void evaluateAllRows() {
		
		for(Row currentRow : table) {

			// System.out.println("The expression is: "+expression);
			Item currentTree = stringToTree(expression,currentRow);
			
			if(!cantParseExpression) {
				for(Variable currentVariable : currentRow.variableList) {
					System.out.print(currentVariable.name+"="+currentVariable.value+" ");
				}
				currentRow.evaluation=currentTree.evaluate();
				System.out.println("Output value = "+currentRow.evaluation);
			}	

		}
	}
	
	public static void generateRows() {
		//Creating the first row
		Row currentRow = new Row();
		for(int i=0; i<variableNumber; i++) {
			Variable currentVariable = new Variable();
			currentVariable.name=variableNames.get(i);
			currentVariable.value=false;
			currentRow.variableList.add(i, currentVariable);
		}
		table.add(currentRow);
		boolean flag; //Is set to true if there was a bit change
		
		while(1==1) {
			
			flag=false;
			Row nextRow = new Row();
			copyRowValues(currentRow,nextRow);
			for(int i=0;i<variableNumber;i++) { //searches for the first false value
				if(nextRow.variableList.get(i).value==false) {
					nextRow.variableList.get(i).value=true;
					for(int x=0; x<i;x++) {
						nextRow.variableList.get(x).value=false; //Sets previous bits to 0
					}
					currentRow=nextRow;
					table.add(currentRow);
					flag=true;
					break;
				}
				
				
			}
			/* System.out.println("The current row values are: ");
			for(int s=0;s<variableNumber;s++) {
				System.out.print(currentRow.variableList.get(s).value+" ");
			}
			*/
			if(flag==false) {
				break;
			}
		}
		
		
	}
	
	public static void copyRowValues(Row oldRow, Row newRow) {
		for(int i=0;i<variableNumber;i++) {
			Variable newVariable = new Variable();
			newVariable.name=oldRow.variableList.get(i).name;
			newVariable.value=oldRow.variableList.get(i).value;
			newRow.variableList.add(newVariable);
		}
	}
	
	// Recursive method. To check if the provided user expression was correct you 
	// can check if the entire user expression evaluates to null. If yes, then it wasn't parsed correctly by the algorithm
	//Also assigns variable values
	public static Item stringToTree(String string, Row row) {
		if(string==null) {
			return null;
		}
		//Ends if the string is just "()"
		if(string.compareTo("()")==0) {
			cantParseExpression=true;
			return null;
		}
		//Ends if string is empty
		if(string.length()==0) {
			return null;
		}
		//Remove brackets if there's nothing outside them
		boolean flag=false;
		int parenthesisNumber=0; //How many opening parenthesis we've been through that were not closed yet
		
		for(int i=0;i<string.length();i++) {
			if(string.substring(i,i+1).compareTo("(")==0) {
				parenthesisNumber++;
			}else
			if(string.substring(i,i+1).compareTo(")")==0) {
				parenthesisNumber--;
			}else if(parenthesisNumber==0) {
				flag=true;
			}
		}
		if(flag==false) {
			if(string.substring(0,1).compareTo("(")==0) {
				if(string.substring(string.length()-1,string.length()).compareTo(")")==0) {
					string= string.substring(1,string.length()-1);
					return stringToTree(string,row);
					
				}
			}
		}
		
		
		//The string is just a single variable
		if(string.length()==1) {
			if(Character.isLetter(string.charAt(0))&&string.substring(0,1).compareTo("v")!=0) {
			//	System.out.println("Creating a variable");
				Variable variable = new Variable();
				variable.name=string;
				
				//Looking for it in the variable list
				boolean dictionaryValue=false;
				for(Variable tempVar : row.variableList) {
					if(tempVar.name.compareTo(string.substring(0,1))==0) {
						dictionaryValue=tempVar.value;
					}
				}
				variable.value=dictionaryValue;
				return variable;
				
			}
		}
		
		
		boolean foundNot = false; //If found a NOT
		int notIndex=0;
		int otherIndex; //Starting index of the operator other than NOT
		parenthesisNumber=0;
		//Finding the main operand
		for(int i=0; i<string.length();i++) {
			
			if(string.substring(i,i+1).compareTo("(")==0) {
				parenthesisNumber++;
			}
			if(string.substring(i,i+1).compareTo(")")==0) {
				parenthesisNumber--;
			}
			
			
			
			//If we're not in parenthesis
			if(parenthesisNumber==0) {
				//Found an !
				if(string.substring(i,i+1).compareTo("!")==0) {
					if(i==string.length()-1) {
						cantParseExpression=true;
					}
					if(foundNot==false) {
						foundNot=true;
						notIndex=i;
					}

				}
				//Found an v
				if(string.substring(i,i+1).compareTo("v")==0) {
				//	System.out.println("Creating a disjunction");
					if(i==string.length()-1||i==0) {
						cantParseExpression=true;
					}
					otherIndex = i;
					Disjunction disjunction = new Disjunction();
					disjunction.left=stringToTree(stringLeft(string,otherIndex),row);
					disjunction.right=stringToTree(stringRight(string,otherIndex),row);
					
					return disjunction;
				}
				//Found an ^
				if(string.substring(i,i+1).compareTo("^")==0) {
				//	System.out.println("Creating a conjunction");
					if(i==string.length()-1||i==0) {
						cantParseExpression=true;
					}
					otherIndex = i;
					Conjunction conjunction = new Conjunction();
					conjunction.left=stringToTree(stringLeft(string,otherIndex),row);
					conjunction.right=stringToTree(stringRight(string,otherIndex),row);
					return conjunction;
				}
				
				//Found an ->
				if(i+1<string.length()) {
					if(string.substring(i,i+2).compareTo("->")==0) {
					//	System.out.println("Creating an implication");
						if(i==string.length()-2||i==0) {
							cantParseExpression=true;
						}
						otherIndex = i;
						Implication implication = new Implication();
						implication.left=stringToTree(stringLeft(string,otherIndex),row);
						implication.right=stringToTree(stringRight(string,otherIndex+1),row);
						return implication;
					}
				}

				//Found an <->
				if(i+2<string.length()) {
					if(string.substring(i,i+3).compareTo("<->")==0) {
						if(i==string.length()-3||i==0) {
							cantParseExpression=true;
						}
						// System.out.println("Creating an equivalence");
						otherIndex = i;
						Equivalence equivalence = new Equivalence();
						equivalence.left=stringToTree(stringLeft(string,otherIndex),row);
						equivalence.right=stringToTree(stringRight(string,otherIndex+2),row);
						return equivalence;
					}
				}
			}


			
		}
		
		//The main operand is not, we need to handle it
		if(foundNot==true) {
			Negation negation = new Negation();
			negation.left=stringToTree(stringRight(string,notIndex),row);
			return negation;
		}
		
		cantParseExpression=true;
		return null; //Something went wrong
		
	}

	//Returns the string part before the provided index
	public static String stringLeft(String string, int index) {
		return(string.substring(0,index));
	}
	
	//Returns the string part after the provided index
	public static String stringRight(String string, int index) {
		return(string.substring(index+1,string.length()));
		
	}
	
	public static void printDNF() {
		System.out.println("Disjunctive normal form:");
		boolean gotOneTrueRow=false;
		boolean gotFirstVariable;
		StringBuilder DNF = new StringBuilder();
		for(Row row : table) {
			if(row.evaluation==true) {
				if(gotOneTrueRow) {
					DNF.append("v");
				}
				gotOneTrueRow=true;
				DNF.append("(");
				gotFirstVariable=false;
				for(Variable variable : row.variableList) {
					if(variable.value==true) {
						if(gotFirstVariable) {
							DNF.append("^");
						}
						DNF.append(variable.name);
						gotFirstVariable=true;
					}else {
						if(gotFirstVariable) {
							DNF.append("^");
						}
						DNF.append("!"+variable.name);
						gotFirstVariable=true;
					}
				}
				DNF.append(")");
				
			}
		}
		if(!gotOneTrueRow) {
			DNF.append("(p^!p)");
		}
		
		System.out.println(DNF.toString());
	}
	
	public static void printCNF() {
		System.out.println("Conjunctive normal form:");
		boolean gotOneFalseRow=false;
		boolean gotFirstVariable;
		StringBuilder DNF = new StringBuilder();
		for(Row row : table) {
			if(row.evaluation==false) {
				if(gotOneFalseRow) {
					DNF.append("^");
				}
				gotOneFalseRow=true;
				DNF.append("(");
				gotFirstVariable=false;
				for(Variable variable : row.variableList) {
					if(variable.value==false) {
						if(gotFirstVariable) {
							DNF.append("v");
						}
						DNF.append(variable.name);
						gotFirstVariable=true;
					}else {
						if(gotFirstVariable) {
							DNF.append("v");
						}
						DNF.append("!"+variable.name);
						gotFirstVariable=true;
					}
				}
				DNF.append(")");
				
			}
		}
		if(!gotOneFalseRow) {
			DNF.append("(pv!p)");
		}
		
		System.out.println(DNF.toString());
	}
	
	
}
