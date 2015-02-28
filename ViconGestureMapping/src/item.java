import java.util.Timer;
import java.util.Vector;

/**
 * 
 * @author Michael
 * intA, intB, dblA, dblB used for testing conditions.
 * intVar and dblVar are used for accumulation, if needed. (storing variables)
 */
public class item {
	//a = 97;
	//int intA, intB, intVar;			//Used for calculating intermediate values
	public double[] vars = new double[26];		//Used for calculating intermediate values
	public int object_number;					//Which object are we currently investigating?
	public MessageHandler Parent;				//The Messagehandler this is associated with
	private Vector<activity> activities = new Vector<activity>();	//A list of activities within this mutator
       
        //Instrument lines
        Arpeggio arp;
        BassLine bass;
        OctaveBassLine obass;
        MelodyLine melody;
        PitchMelody pitchMelody;
        ChordLine chord;
        TechnoDrumLine tdrums;
        TechnoMelody tmelody;
        
        //Module to use
        int module = 0;
        boolean init = false;
	
	
	/**Create a string representation of this mutator including all condition lists and action lists.
	 * 
	 */
	public String toString(){
		String output = "{ITEM}\n";
		for (int i = 0; i < activities.size(); i++){
			output = output + activities.get(i).toString();
		}
		return output;
	}
	
	/* Each condition list contains a set of anded conditions that if true, this happens 
	 * If any of the condition lists return true, this will happen
	 */
	/**
	 * Constructor
	 */
	public item(MessageHandler P){
		Parent = P;
	}
	/** Add an activity to this mutator
	 * 
	 * @param A - The activity to add to this mutator.
	 */
	public void addActivity(activity A){
		activities.add(A);
	}
	
	/**Test condition lists, and carry out action lists based on returned trues
	 * 
	 */
	public void test(int object_number, String objNam){
		this.object_number = object_number;
		for(int i = 0; i < activities.size(); i++){
			activities.get(i).testAndExecute(objNam);
		}//end for
	}//end test

	public void set_var(int i, double val){
		vars[i] = val;
	}
/** Get'n'Set **/
	
	
}//end class
