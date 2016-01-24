import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Provides methods to convert an infix expression to a postfix expression
 * and to evaluate a postfix expression
 * @author Jake
 * @version 0.1
 */
public class ExpressionTools {
	
	/**
	 * Private constructor to prevent user from making instance of class
	 */
	private ExpressionTools(){
		
	}
	
	/**
	 * Converts an arithmetic infix expression to a postfix expression
	 * @param infix A string representing the infix expression
	 * @return A string representing the resulting postfix expression
	 * @throws PostFixException if the infix expression is invalid
	 */
	public static String infixToPostfix(String infix) throws PostFixException{
		//Create a new stack object to hold operators from infix expression
		Stack<String> operatorStack = new Stack<String>();
		String postfix = "";
		String curToken = "";
		Scanner getToken = new Scanner(infix);
		//holds the previous token type as a string
		String previousTokenType = "first token";
		
		//Iterate over all tokens in expression
		while(getToken.hasNext()){
			curToken = getToken.next();
			
			//if the token is an operand (meaning it is an integer), add to postfix expression
			if(isPositiveInteger(curToken)){
				//if the token sequence is valid, add the current token to the psotfix expression
				if(checkValidTokenSequence("operand", previousTokenType)){
					postfix += (curToken + " ");
					previousTokenType = "operand";
				}
				else{ //otherwise throw an exception
					throw new PostFixException("Invalid token sequence");
				}
			}
			//if the current token is a left bracket check that the token sequence is valid and push the
			//token on the stack
			else if(curToken.equals("(")){
				if(checkValidTokenSequence("left bracket", previousTokenType)){
					operatorStack.push(curToken);
					previousTokenType = "left bracket";
				}
				else{//if the token sequence is invalid throw and exception
					throw new PostFixException("Invalid token sequence");
				}
			}
			//if the token is an operator, check sequence validity
			else if(curToken.equals("+")||curToken.equals("-")||curToken.equals("*")||curToken.equals("/")){
				//if the token is an operator and it is the first token, the expression is invalid
				if(previousTokenType.equals("first token")){
					throw new PostFixException("Invalid operator placement");
				}
				//if the token sequence is valid, add all operator on stack with lower or equal precedence to
				//to the prefix expression
				if(checkValidTokenSequence("operator", previousTokenType)){
					while(!operatorStack.empty() && checkPrecedence(operatorStack.peek(),curToken)){
						postfix += (operatorStack.pop() + " ");
					}
					//push the current token on the operator stack
					operatorStack.push(curToken);
					previousTokenType = "operator";
				}
				else{
					//if the token sequence is invalid, throw exception
					throw new PostFixException("Invalid token sequence");
				}
			}
			
			//if the token is a right bracket, check that token sequence is valid
			else if(curToken.equals(")")){
				
				//if the token sequence is valid, add all operators on operator stack 
				//to the prefix expression until the matching left bracket is reached
				if(checkValidTokenSequence("right bracket", previousTokenType)){
					boolean hasMatchingBracket = false;
					
					while(!operatorStack.empty()){
						if(!operatorStack.peek().equals("(")){
							postfix += (operatorStack.pop() + " ");
						}
						else{
							operatorStack.pop();
							hasMatchingBracket = true;
							break;
						}
					}
					
					//If the right bracket does not have a matching left bracket, throw an exception
					if(!hasMatchingBracket){
						throw new PostFixException("Missing left bracket");
					}
					
					previousTokenType = "right bracket";
				}
				
				//If the token sequence is invalid throw an exception
				else{
					throw new PostFixException("Invalid token sequence");
				}
			}
			//if the current token is not a valid token, throw an exception
			else{
				throw new PostFixException("Invalid token: " + curToken);
			}
		}
		
		//Check to see if the last token was an operator, if so throw an  exception
		if(curToken.equals("+")||curToken.equals("-")||curToken.equals("*")||curToken.equals("/")){
			throw new PostFixException("Invalid operator placement");
		}
		
		//while there operator stack is not empty, pop whatever is in the stack and append
		while(!operatorStack.empty()){
			//if there is a bracket left in the stack, then the infix expression is invalid
			if(operatorStack.peek().equals("(") || operatorStack.peek().equals(")")){
				throw new PostFixException("Bracket mismatch");
			}
				postfix += (operatorStack.pop() + " ");
		}
		
		getToken.close();
		
		//return the postfix expression
		return postfix;
	}
	
	/**
	 * Arithmetically evaluates a postfix expression
	 * @param postfix The postfix expression as a String
	 * @return an integer resulting from the evaluation of the postfix expression
	 * @throws PostFixException if the postfix expression is invalid
	 */
	public static int evalPostfix(String postfix) throws PostFixException{
		//MyStack instance to store results of evaluation
		Stack<Integer> result = new Stack<Integer>();
		
		//Scanner instance to scan tokens
		Scanner getToken = new Scanner(postfix);
		
		while(getToken.hasNext()){
			
			String curToken = getToken.next();
			//if the token is a valid operator, push it onto stack as a Double object
			if(isPositiveInteger(curToken)){
				result.push(Integer.parseInt(curToken));
			}
			
			//if the current token is an operator
			else if(curToken.equals("+")||curToken.equals("-")||curToken.equals("*")||curToken.equals("/")){
				try{
					//Get the operands from the stack
					int operand2 = result.pop().intValue();
					int operand1 = result.pop().intValue();
					
					//find the operator and perform the arithmetic
					switch(curToken){
						case "+": result.push(operand1 + operand2);
								  break;
						case "-": result.push(operand1 - operand2);
								  break;
						case "*": result.push(operand1 * operand2);
								  break;
						case "/": result.push(operand1 / operand2);
								  break;
						default: throw new PostFixException("Invalid token: " + curToken);
					}
					
				}
				//if the stack is empty when attempting to pop, throw a PostFixException
				catch(EmptyStackException e){
					throw new PostFixException("Not enough operands");
				}
				//If there is an ArithMetic Exception, rethrow it
				catch(ArithmeticException e){
					throw new ArithmeticException("Divide by 0");
				}
			}
			
			//if the token is not an operand or operator it is invalid,
			//throw a PostFixException
			else{
				throw new PostFixException("Invalid token: " + curToken);
			}
		}
		
		getToken.close();
		
		//if the result is not in the stack
		if(result.empty()){
			throw new PostFixException("Not enough operands");
		}
		
		int resultNum = result.pop().intValue();
		
		//If there are still operands in the stack
		if(!result.empty()){
			throw new PostFixException("Too many operands");
		}
		
		return resultNum;
	}
	

	/**
	 * Determines if a token is a positive integer
	 * @param s The string token to be examined
	 * @return True if the token is a positive integer, false otherwise
	 */
	private static boolean isPositiveInteger(String s){
		try{
			//If the token is an integer less than 0 return false
			if(Integer.parseInt(s) < 0){
				return false;
			}
		}
		//If an exception occurs return false
		catch(NumberFormatException e){
			return false;
		}
		//otherwise return true
		return true;
	}
	
	/**
	 * Determines the order of precedence between two operators
	 * @param operator1 A string representing an operator
	 * @param operator2 Another string representing an operator
	 * @return True if operator1 has higher or equal precedence than operator 2, false otherwise
	 */
	private static boolean checkPrecedence(String operator1, String operator2){

		if((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("*") || operator2.equals("/"))){
			return true;
		}
		else if((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"))){
			return true;
		}
		else if((operator1.equals("+") || operator1.equals("-")) && (operator2.equals("+") || operator2.equals("-"))){
			return true;
		}
		else if((operator1.equals("+") || operator1.equals("-")) && (operator2.equals("*") || operator2.equals("/"))){
			return false;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Determines if an infix token sequence is valid by examining the current token and the last token
	 * @param curToken The current token being examined as a String
	 * @param lastToken The previous token as a String
	 * @return True if the token sequence is valid for an infix expression, false otherwise
	 */
	private static boolean checkValidTokenSequence(String curToken, String lastToken){
		//If an operand or right bracket comes directly before an operand return false
		if(curToken.equals("operand") && (lastToken.equals("operand") || lastToken.equals("right bracket"))){
			return false;
		}
		//If an operator or left bracket comes directly before an operator, return false
		else if(curToken.equals("operator") && (lastToken.equals("operator") || lastToken.equals("left bracket"))){
			return false;
		}
		//If a left bracket or operator comes directly before a right bracket return false
		else if(curToken.equals("right bracket") && (lastToken.equals("left bracket") || lastToken.equals("operator"))){
			return false;
		}
		//If an operand or right bracket comes directly before a left bracket, return false
		else if(curToken.equals("left bracket") && (lastToken.equals("operand") || lastToken.equals("right bracket"))){
			return false;
		}

		//Otherwise the sequence is valid
		return true;
	}
}