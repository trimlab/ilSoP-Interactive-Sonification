import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.midi.InvalidMidiDataException;
import org.jfugue.*;


/**
 * 
 * @author Michael
 *	An action.  One single action.  One thing that can be executed on
 *	Set value for tempo,
 */
public class action {
	
       
	public enum act {
		/**
		 * What are all of the actions we will need?
		 */
		internal, bpm, key, timesig, instruments, unset, notes, nlength, arpeg; 
	}//end enum act

	public enum arithmatic_operations{
		add, sub, mul, div, square, root, abs, mod, log, none, assign;
	}
	
	/** Instance Variables **/
	act typeOfAction = act.unset;											//Type of action
	item Parent;															//Parent mutator
	CONSTANTS.var var1 = CONSTANTS.var.NULL , var2 = CONSTANTS.var.NULL;	//var1, var2 for calcs
	arithmatic_operations oper = arithmatic_operations.none;				//arithmatic operation
	int internal_lhs;	//used for loading variables						//0-25, internal variables
	double var1_value, var2_value;											//used for actual calculations
	
	/** variables that will be used in special circumstances **/
	String keysig, timesig, instrument, note_length;
	String notes;
        int arpCount = 0;
	int timesig_top, timesig_bottom;										
	
        //Test timer stuff
        private Timer timer = new Timer();
        private int tickRate = 100;
	boolean canGetInput = true;
	
	//#region constructors
	
	public action(item Parent, String[] notes){
		this.Parent = Parent;
		if(notes[0].equalsIgnoreCase("notes")){
			typeOfAction = act.notes;
			this.notes = notes[1];
		}
                else if(notes[0].equalsIgnoreCase("instr")){
                        this.typeOfAction = act.instruments;
                        instrument = notes[1];
                }
                else if(notes[0].equalsIgnoreCase("length")){
                        this.typeOfAction = act.nlength;
                        note_length = notes[1];
                }
                else if(notes[0].equalsIgnoreCase("arpeg")){
                        this.typeOfAction = act.arpeg;
                        this.notes = notes[1];
                        
                }
		else{
			System.out.println("Unrecognized Notes Command :"+notes[0] +notes[1]);
		}
		
	}
	/**
	 * Arithmatic operation (internal)
	 * @param LHS = left hand side
	 * @param var1
	 * @param op
	 * @param var2
	 */
	public action(item Parent, char LHS, CONSTANTS.var var1, arithmatic_operations op, CONSTANTS.var var2){
			internal_lhs = LHS - 97;
			this.typeOfAction = act.internal;
			this.Parent = Parent;
			oper = op;
			this.var1 = var1;
			this.var2 = var2;
	}
	/**
	 * 
	 * @param LHS
	 * @param var1
	 * @param op
	 * @param var2
	 */
	public action(item Parent, String LHS, CONSTANTS.var var1, arithmatic_operations op, CONSTANTS.var var2){
		this.Parent = Parent;
		if (LHS.equalsIgnoreCase("bpm")){
			this.typeOfAction = act.bpm;
			oper = op;
			this.var1 = var1;
			this.var2 = var2;
		}
		else {
			System.out.println("I don't know how to handle the token :" + LHS);
		}//end else unknown token
	}//end constructor
	/**
	 * Key signiture, time signiture or instrument command
	 * @param command
	 */
	public action(item Parent, String command){
		this.Parent = Parent;
		//check for each of the possible strings...
		/** Time signature **/
		if (command.length() == 3){
			this.typeOfAction = act.timesig;
			timesig_top = command.charAt(0);
			timesig_bottom = command.charAt(2);
			
		}
		/** instrument command **/
		else if (command.equalsIgnoreCase("electronica") || command.equalsIgnoreCase("rock") || command.equalsIgnoreCase("orchestral")){
			this.typeOfAction = act.instruments;
			instrument = command;
		}
		/** key **/
		else{
			this.typeOfAction = act.key;
			keysig = command;
		}
	}
	/**
	 *  Generic constructor
	 * @param P - Parent
	 */
	public action(item P){
		Parent = P;	//get the parent information
	}
	
	//#end
	
	//#Setters
	public void setVar1(double val){
		var1_value = val;
	}
	public void setVar2(double val){
		var2_value = val;
	}
	
	/**
	 * 
	 * @param i = variable to load (1 or 2)
	 * @return 
	 */
	private void load_vars(){
		objectData tracked = Parent.Parent.tracked[Parent.object_number];
		if(var1 != CONSTANTS.var.NUMBER){ 
			var1_value = CONSTANTS.token_to_var(var1, Parent);
		}
		if(var2 != CONSTANTS.var.NUMBER){
			var2_value = CONSTANTS.token_to_var(var2, Parent);
		}
			
	}//end load vars
	
	private double arithmatic(){
		double value = 0;
		switch (oper){
		case abs: value = Math.abs(var1_value);
			break;
		case add: value = var1_value + var2_value;
			break;
		case div: value = var1_value/var2_value;
			break;
		case log: value = Math.log(var1_value);
			break;
		case mod: value = var1_value%var2_value;
			break;
		case mul: value = var1_value*var2_value;
			break;
		case root: value = Math.pow(var1_value, var2_value);
			break;
		case square: value = var1_value*var1_value;
			break;
		case sub: value = var1_value-var2_value;
			break;
		default:
			break;
		}
		
		
		return value;
	}
	
	/**
	 * Carry out the action
	 */
    public void execute() {
        load_vars();
        if(!Parent.init)
            initializeModule();
        
        switch (typeOfAction)
        {
        case arpeg:
            if(Parent.module == 0){
                //Arpeggio Mode
                Parent.arp.setBaseNote(notes);
            }else if(Parent.module == 3){
                //Pitch Melody line
                Parent.pitchMelody.setBaseNote(notes);
            }else if(Parent.module == 4){
                //Chords
                Parent.chord.setBaseNote(notes);
            }else if(Parent.module == 5){
                //TechnoMelody
                Parent.tmelody.setBaseNote(notes);
            }
        case bpm:
                Parent.Parent.musicTempo = (int)arithmatic();
                break;
        case instruments:
                Parent.Parent.musicInstrument = instrument;
                break;
        case internal:
                Parent.set_var(internal_lhs, arithmatic());
                break;
        case key:			
                Parent.Parent.musicKey = CONSTANTS.key.valueOf(keysig);
                break;
        case timesig:		
                Parent.Parent.top_timeSig = timesig_top;
                Parent.Parent.bottom_timeSig = timesig_bottom;
                break;
        case notes:
            Parent.bass.setBassNote(notes);
            Parent.obass.setBassNote(notes);
            break;
        case nlength:
        if(canGetInput){
            canGetInput = false;
            double l = Parent.vars[0];
            
            if(Parent.module == 0){
                //Arp
                if(l < .0001)
                Parent.Parent.note_length = "wwwww";
                else if(l <= .001)
                Parent.Parent.note_length = "wwww";
                else if(l <= .01)
                Parent.Parent.note_length = "www";
                else if(l <= .02)
                Parent.Parent.note_length = "ww";
                else if(l <= .03)
                Parent.Parent.note_length = "wh";
                else if(l <= .05)
                Parent.Parent.note_length = "wq";
            }else if(Parent.module == 1 || Parent.module == 2){
                //Melody
                if(l < .0001)
                Parent.Parent.note_length = "wwww";
                else if(l <= .001)
                Parent.Parent.note_length = "www";
                else if(l <= .01)
                Parent.Parent.note_length = "ww";
                else if(l <= .02)
                Parent.Parent.note_length = "w";
                else if(l <= .03)
                Parent.Parent.note_length = "q";
                else if(l <= .05)
                Parent.Parent.note_length = "i";
            }else if(Parent.module == 3 || Parent.module == 4){
                int r = (int)(Math.random()*100)%4;
                
                if(r == 0)
                    Parent.Parent.note_length = "hq";
                else if(r == 1)
                    Parent.Parent.note_length = "hw";
                else if(r == 2)
                    Parent.Parent.note_length = "www";
                else if(r == 3)
                    Parent.Parent.note_length = "whqi";
            }

//            Parent.Parent.note_length = "www";
//            System.out.println(Parent.Parent.note_length); 
            timer.schedule(new RemindTask(), tickRate);
        }
                break;
        default:
                System.out.println("Error, unrecognized token! :" + typeOfAction.toString());
                break; 

        }//end switch case what
    }
    
    private void initializeModule(){
        Parent.init = true;

        if(Parent.arp == null)
            Parent.arp = new Arpeggio(Parent);
        if(Parent.bass == null){
            Parent.bass = new BassLine(Parent);
            
            //Select generic baseline instrument
            int r = (int)((Math.random()*100)%5);
            if(r == 0)
                Parent.bass.setInstrument("I97 ");
            if(r == 1)
                Parent.bass.setInstrument("I87 ");
            if(r == 2)
                Parent.bass.setInstrument("I58 ");
            if(r == 3)
                Parent.bass.setInstrument("I53 ");
            if(r == 4)
                Parent.bass.setInstrument("I39 ");
        }
        if(Parent.melody == null)
            if(Parent.module == 1)
            Parent.melody = new MelodyLine(Parent,0);
            else
            Parent.melody = new MelodyLine(Parent,1);
        if(Parent.pitchMelody == null)
            Parent.pitchMelody = new PitchMelody(Parent);
        if(Parent.chord == null)
            Parent.chord= new ChordLine(Parent);
        if(Parent.obass == null)
            Parent.obass = new OctaveBassLine(Parent);
        if(Parent.tdrums == null)
            Parent.tdrums = new TechnoDrumLine(Parent);
        if(Parent.tmelody == null)
            Parent.tmelody = new TechnoMelody(Parent);

        
        
        //Initialize the module
        if(Parent.module == 0){
            //Arpeggio Mode
            Parent.arp.isQuiet(false);
            Parent.bass.isQuiet(false);
            Parent.melody.isQuiet(true);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }else if(Parent.module == 1){
            //Melody line
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(false);
            Parent.melody.isQuiet(false);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }else if(Parent.module == 2){
            //Melody line 2
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(false);
            Parent.melody.isQuiet(false);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }else if(Parent.module == 3){
            //Pitch Melody line
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(false);
            Parent.melody.isQuiet(true);
            Parent.pitchMelody.isQuiet(false);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }else if(Parent.module == 4){
            //Chords
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(false);
            Parent.melody.isQuiet(true);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(false);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }else if(Parent.module == 5){
            //Techno
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(true);
            Parent.melody.isQuiet(true);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(false);
            Parent.tdrums.isQuiet(false);
            Parent.tmelody.isQuiet(false);
        }else{
            Parent.arp.isQuiet(true);
            Parent.bass.isQuiet(true);
            Parent.melody.isQuiet(true);
            Parent.pitchMelody.isQuiet(true);
            Parent.chord.isQuiet(true);
            Parent.obass.isQuiet(true);
            Parent.tdrums.isQuiet(true);
            Parent.tmelody.isQuiet(true);
        }
        
        
    }
	
	/**
	 * Return a string representation with all of the information within this action
	 */
	public String toString(){
		String output = "Type: ";
		output = output + typeOfAction.toString();
		output = output + "; Vars: "+var1.toString()+" "+var2.toString();
		output = output +"; Oper: "+oper.toString()+"; "+" [keysig, timesig, instrument]  [" + keysig+", "+ this.timesig_top + "|"+ this.timesig_bottom+", " + instrument+"];\n";
		output = output + "Notes: <";
		if (notes != null){
			
			output = output + notes +";";
			
		}//end if notes not null
		output = output + ">\nInternal_Memory_Number = "+internal_lhs;;
		return output;
	}//end to string method
	
        
public class RemindTask extends TimerTask{
        //Alternate
        public void run(){
           canGetInput = true;
        }
        
    }
}
