import java.util.Vector;

/**
 * 
 * @author Michael
 * List of actions to go through if conditions for those actions are met.
 */
public class actionList {

	public Vector<action> actions = new Vector<action>();
	
	
	public String toString(){
		String output = "actionList: \n";
		
		for (int i = 0; i < actions.size(); i++){
			output = output + actions.get(i).toString() + ";\n";
		}
		
		return output;
	}
	/**
	 * 
	 * @param this_action - add this action to the list
	 */
	public void addAction(action this_action){
		actions.add(this_action);
	}
	
	/**
	 * Execute all actions in the list
	 */
	public void execute(){
		for (int i = 0; i < actions.size(); i++){
			actions.get(i).execute();
		}//end for each action
	}//end execute
	
}
