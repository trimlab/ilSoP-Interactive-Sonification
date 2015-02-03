/**
 * An if statement.
 * If left hand side <Operator> right hand side is true
 * 
 */
public class condition {
	enum op {
		less, greater, equal, always_true, not_equal, object
	};
	enum obj {
		left_hand, right_hand, left_foot, right_foot, head, either_foot, both_feet, either_hand, both_hands
	}
	
	boolean time_once = false;	//true after time passes once
	double next_time;			//If we're using a repeatable time condition
	boolean compare;			//true if this is a comparison
	op Operator;				//Comparison operator
	CONSTANTS.var LHS, RHS;		//variables
	double dLHS, dRHS;			//Used for comparisons
	String ifobject;				
	
	item Parent;
	
	/*<#reg GetSet */
	public void setdLHS(double d){
		dLHS = d;
	}
	public void setdRHS(double d){
		dRHS = d;
	}
	/*#end> */
	
	//<#region Constructors {
	public condition(CONSTANTS.var LH, op OP, CONSTANTS.var RH){
		LHS = LH;
		Operator = OP;
		RHS = RH;
		time_once = true;
		
		if (LH == CONSTANTS.var.elapsed_time_once || RH == CONSTANTS.var.elapsed_time_once){ //If we're looking for an elapsed time once
			time_once = false;
		}
		if (RH == CONSTANTS.var.elapsed_time_repeatable || LH == CONSTANTS.var.elapsed_time_repeatable){
			
		}
	}
	
	/**
	 * 
	 * @param strToken the token that was passed to be parsed
	 * @param type: the type of token. eg. "object"
	 */
	public condition(String strToken, String type){
		if (type.equalsIgnoreCase("object")){
			Operator = op.object;
			ifobject = strToken;
		}
		
		
	}
	/**
	 * 
	 * @param bool - true for always true
	 */
	public condition(boolean bool){
		if (bool){
			this.Operator = op.always_true;
		}
		else{
			System.out.println("What? - How can a condition be *never true*");
		}
	}//end constructor always true
	
	//#end>region }
	
	/**
	 * 	Test the stored condition
	 * @param intA
	 * @param intB
	 * @param dblA
	 * @param dblB
	 * @returns true if the condition is true, false if it is not
	 */
	public boolean testCondition(item Parent){
		/* Populate L and R */
		double L = 0, R = 0;		//Left and Right
		boolean retCode = false;			//return code

		if (RHS == CONSTANTS.var.NUMBER){
			R = dRHS;
		}
		else{
			R = CONSTANTS.token_to_var(RHS, Parent);
		}
		if(LHS == CONSTANTS.var.NUMBER){
			L = dLHS;
		}
		else{
			L = CONSTANTS.token_to_var(LHS, Parent);
		}//end populate L and R
		
		if(RHS == CONSTANTS.var.elapsed_time_repeatable){
			L = Math.max(L, next_time);
		}
		if(LHS == CONSTANTS.var.elapsed_time_repeatable){
			R = Math.max(R, next_time);
		}
		
		/* Operate */
		if((RHS == CONSTANTS.var.elapsed_time_once || LHS == CONSTANTS.var.elapsed_time_once) && time_once == true){
			return false;
		}
		
		/* Test the Math */
		switch (Operator)
		{
		case always_true:
			return true;
		case less:
			if(L < R) retCode = true;
			break;
		case greater:
			if(L > R) retCode = true;
			break;
		case equal:
			if(L == R) retCode = true;
			break;
		case not_equal:
			if(L != R) retCode = true;
			break;
		case object:
			/*
			 * Note on tracking things like "Both Feet" or "Both Hands"
			 * --
			 * Each object is tested and updated seperate from one-another.  The reason that there is an object
			 * modifier in here is so that you can make sure calculations only happen when the object you want to track
			 * is the one that is getting tested right now.  Using both hands/feet will not work, for this reason.
			 * 
			 */
			if (Parent.Parent.tracked[Parent.object_number].getName().equalsIgnoreCase(this.ifobject)){
				retCode = true;
			}
			break;
		default:
			break;
		}//end switch case
		
		/* Test elapsed time codes - Bookkeeping */
		if(retCode == true){
			if(LHS == CONSTANTS.var.elapsed_time_once || RHS == CONSTANTS.var.elapsed_time_once){
				time_once = true;
			}//end if time once
			if(RHS == CONSTANTS.var.elapsed_time_repeatable){
				next_time += dLHS;
			}
			else if (LHS == CONSTANTS.var.elapsed_time_repeatable){
				next_time += dRHS;
			}
		}//end if we returned true book-keeping for times
		
		return retCode;
	}//end test condition

	public String toString(){
		String output = "Condition: ~Op: ";
		output = output + Operator.toString();
		if (Operator == op.object){
			output = output + " == "+ifobject.toString();
		}//end if object
		else{
			switch (Operator){
			case always_true:output.concat("true");
				break;
			case equal:output.concat("==");
				break;
			case greater:output.concat(">");
				break;
			case less:output.concat("<");
				break;
			case not_equal:output.concat("!=");
				break;
			default:
				break;
			
			}
		}//end else
		output = output + "; [vars: "+ LHS +", "+RHS+"]; {Numbers : "+dLHS+", "+dRHS+"}";
		return output;
	}
}//end condition
