import java.util.Iterator;
import java.util.Vector;


/**
 * 
 * @author Michael
 * Used for storing a list of conditions
 * Each condition is an if statement in the form of if lhs <,==,>, rhs
 */
public class conditionList {
	Vector<condition> conditions = new Vector<condition>();	//list of conditions
	
	public String toString(){
		String output = "ConditionList: \n";
		for (int i = 0; i < conditions.size(); i++){
			output = output + conditions.get(i).toString() + ";\n ";
		}//end for
		
		
		return output;
	}//end toString
	
	/**
	 * 
	 * @param if_statement - condition to add to the list
	 */
	public void addCondition(CONSTANTS.var LH, condition.op OP, CONSTANTS.var RH){
		conditions.add(new condition(LH, OP, RH));
	}
	public void addCondition(condition con){
		conditions.add(con);
	}
	
	/**
	 * 	Test the conditions within the list given the variables.
	 * @param intA
	 * @param intB
	 * @param dblA
	 * @param dblB
	 * @return
	 */
	public boolean testList(item Parent)
	{
		for(Iterator<condition> i = conditions.iterator(); i.hasNext();)
		{
			condition E = i.next();
			if(E.testCondition(Parent) == false)
			{
				return false;
			}//end if
		}//end for each condition in the list
		//if we made it this far return true
		return true;
	}//end testList
	
	

	
	
}//end class condition list
