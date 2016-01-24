/**
 * An exception class for use with errors encountered in conversion of infix 
 * expressions to postfix expressions and errors encountered in the evaluation
 *  of postfix expressions
 * @author Jacob George
 * @version 0.1
 */
public class PostFixException extends Exception {
	
	/**
	 * No-args constructor for PostFixException
	 */
	public PostFixException(){
		super();
	}
	
	/**
	 * Constructor for PostFixException that accepts a message
	 * @param message A string message relating to the error that occured
	 */
	public PostFixException(String message){
		super(message);
	}
	
}