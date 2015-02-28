import java.util.Vector;

/**
 * 
 * @author Michael
 * An activity holds multiple condition lists and one action list.
 * If any of the condition lists return true, the action list will be carried out.
 */
public class activity {
	Vector<conditionList> conditions;
	actionList actions;
	item Parent;
	
	public String toString(){
		String output = "<Activity>\n--conditions--\n";
		for (int i = 0; i < conditions.size(); i++){
			output = output+"[SET]\n"+conditions.get(i).toString()+"\n";
		}
		output = output + "--actions--\n" + actions.toString();
		
		
		return output;
	}
	
	public activity(item parent){
		conditions = new Vector<conditionList>();
		Parent = parent;
	}
	
	/**
	 * 
	 * @param condit - anded conditionlist to add
	 */
	public void add_conditionList(conditionList condit){
		conditions.add(condit);
	}
	
	/**
	 * 
	 * @param action - action list to follow through with if conditions return true.
	 */
	public void set_actionList(actionList action){
		actions = action;
	}
	
	
	/**
	 * Test the condition lists, if any one of the condition lists returns true, execute the actions.
	 */
	private boolean test_conditions(String objNam){
		for(int i = 0; i < conditions.size(); i++){
			if(conditions.elementAt(i).testList(Parent,objNam)){
				return true;
			}
		}
		return false;
	}//end test_conditions()
	
	public boolean testAndExecute(String objNam){
		if(test_conditions(objNam)){
			actions.execute();
			return true;
		}
		return false;
	}//end testAndExecute
	
	
}
