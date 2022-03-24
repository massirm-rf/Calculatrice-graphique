package application;

import java.util.LinkedList;
import java.util.Stack;

/**
 * 
 * c'est la classe qui permet de parser la fonction saisie par l'utilisateur et la recupere dans une pile facile a manipuler pour effectuer
 * les operations de calculs
 */
public class Parser {

	private Stack<String> fileString = new Stack<String>();
	private Stack<String> pile = new Stack<String>();
	private String nom;
	private String strActuel;
	private String coeff="";
	private LinkedList<String> listeFonc = new LinkedList<>();

	private final String[] nombres = {"1","2","3","4","5","6","7","8","9"};
	private final String[] operateurs = {"+","-","/","*","^"};
	private final String[] operandes = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};



	public Parser(String nomFunc) {
		this.nom = nomFunc;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Stack<String> getFileString() {
		return fileString;
	}


	public boolean containsNum(String a) {
		for(int i=0;i<nombres.length;i++) {
			if(a.equals(nombres[i])) return true;
		}
		return false;
	}

	public boolean containsOp(String a) {
		for(int i=0;i<operateurs.length;i++) {
			if(a.equals(operateurs[i])) return true;
		}
		return false;
	}

	public boolean containsOperandes(String a) {
		for(int i=0;i<operandes.length;i++) {
			if(a.equals(operandes[i])) return true;
		}
		return false;
	}

	private static boolean letterOrDigit(char c) {
		if(Character.isLetterOrDigit(c)) return true;
		return false;
	}

	public static int getPrecedence(String c) {
		if("+".equals(c) || "-".equals(c)) return 1 ;
		else if("*".equals(c)||"/".equals(c)) return 2;
		else if("^".equals(c)) return 3;
		return -1;
	}

	public int recupererFunc(int ind) {
		int i = ind;
		String str = "";

		while(i<nom.length() && (containsOperandes(""+nom.charAt(i))&& nom.charAt(i) != '(' && nom.charAt(i)!=')')) {
			str+=nom.charAt(i);
			i++;
		}

		strActuel = str;
		return i-1;
	}

	public int recupererNum(int ind) {
        int i = ind;
        String str = "";

        while(i<nom.length() && (Character.isDigit(nom.charAt(i)) || '.' == nom.charAt(i)) ) {
            str+=nom.charAt(i);
            i++;
        }
        strActuel =str;
        return i-1;
    }

	public Stack<String> inverserPile(Stack<String> p){
		Stack<String> res = new Stack<String>();

		while(!p.empty()) {
			res.push(p.pop());
		}
		return res;
	}

	public boolean associatif(String op1, String op2) {
		return true;
	}

	public int priorite(String a, String b) {

		if(a.equals(b)) return 0;

		else if((a.equals("+") && b.equals("-")) || (a.equals("-") && b.equals("+"))
				|| (a.equals("*") && b.equals("/")) || (a.equals("/") && b.equals("*"))) {

			return 0;

		}
		else if((a.equals("*") && (b.equals("-") || b.equals("+")))
				|| (a.equals("/") && (b.equals("-") || b.equals("+"))) || (a.equals("^")) ) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public double apppliquerOperateur(double e1, double e2, String op) throws InputException {
		switch (op) {
			case "+" : return e1+e2;
			case "-" : return e1-e2;
			case "*" : return e1*e2;
			case "/" : return e2/e1;
			case "^" : return Math.pow(e2, e1);
			default : break;
		}
		return 0;
	}

	public double appliquerFonction(double e, String fonction) throws InputException {
		switch (fonction){
			case("exp"):
				return Math.exp(e);
			case("ln"):
				return Math.log(e);
			case("log"):
				return Math.log(e);
			case("log10"):
				return Math.log10(e);
			case("sqrt"):
				return Math.sqrt(e);
			case("cos"):
				return Math.cos(e);
			case("sin"):
				return Math.sin(e);
			case("tan"):
				return Math.tan(e);
			case("cosh"):
				return Math.cosh(e);
			case("sinh"):
				return Math.sinh(e);
			case("tanh"):
				return Math.tanh(e);
			case("abs"):
				return Math.abs(e);
			default:
				throw new InputException();
		}
	}

	public double calculer(double i) throws InputException {
		@SuppressWarnings("unchecked")
		Stack<String> p = (Stack<String>)fileString.clone();
		Stack<Double> save = new Stack<Double>();
		while(!p.empty()) {
			if(Character.isDigit(p.peek().charAt(0)) || (p.peek().charAt(0)=='-' && p.peek().length()>= 2 && Character.isDigit(p.peek().charAt(1)))) {
				save.push(Double.valueOf(p.pop()));
			}else if(p.peek().equals("x")) {
				save.push(i);
				p.pop();
			}else if(containsOp(p.peek())) {
				double e1=0,e2 = 0;
				if(!save.empty()) e1 = save.pop();
				if(!save.empty()) e2 = save.pop();
				save.push(apppliquerOperateur(e1, e2, p.pop()));
			}else {
				if(!p.isEmpty() && !save.isEmpty()) {
					save.push(appliquerFonction(save.pop(), p.pop()));
				}
				else {throw new InputException();}

			}
		}
		return save.pop();
	}

	public void parsing2() throws InputException{
		Stack<String> pile = new Stack<String>();
		int nbPar=0;
		for(int i=0;i<nom.length();i++) {
			char c =nom.charAt(i);
			if(letterOrDigit(c)) {
				if(Character.isLetter(c)) {
					i = recupererFunc(i);
					pile.push(strActuel);
					strActuel ="";
				}else {
					i =recupererNum(i);
					if(i+1<nom.length() && Character.isLetter(nom.charAt(i+1))){
						pile.push("*");
					}
					fileString.push(coeff+strActuel);
					strActuel = "";
				}
			} else if(c == '(') {
				nbPar+=1;
				pile.push(""+c);
			}else if(c == ')') {
				nbPar-=1;
				while(!pile.isEmpty() && !"(".equals(pile.peek()) ) {
					fileString.push(pile.pop());
				}
				if(!pile.isEmpty())pile.pop();
			}
			else {
				while(!pile.isEmpty() && (getPrecedence(""+c) <= getPrecedence(pile.peek()) || Character.isLetter(pile.peek().charAt(0)))) {
					fileString.push(pile.pop());
				}
				if(c!='-' )/*|| (i+1<nom.length() && !Character.isDigit(nom.charAt(i+1)))) */{
					pile.push(""+c);
					coeff = "";
				}
				else {
					//coeff = "-";
					fileString.push("-1");
					if(i!=0 && nom.charAt(i-1)!='(') pile.push("+");
					pile.push("*");
				}
			}
		}
		if (nbPar != 0 ){
			throw new InputException();
		}
		while(!pile.isEmpty()) {
			if("(".equals(pile.peek())|| ")".equals(pile.peek())) {
				throw new InputException();
			}
			fileString.push(pile.pop());
		}
		fileString = inverserPile(fileString);
	}

}
