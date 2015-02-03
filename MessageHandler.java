import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

import org.jfugue.StreamingPlayer;



/**
 * 
 * @author Michael
 *	Message handler class.  Takes messages out of the queue and processes them.  All of the music data should go inside of the test_data method
 *
 */
class MessageHandler implements Runnable 
{
	
/*Instance Variables*/ 
//#region /*instance variables*/
	DatagramSocket sendToProc;						//Used to send information for testing
	StreamingPlayer music = new StreamingPlayer();	//music player
	public objectData[] tracked;					//Information on the tracked objects
	public int musicTempo;							//BPM
	public CONSTANTS.key musicKey;					//Music Key
	public CONSTANTS.instrument_set musicInstrument;//Set of instruments to use
	public Vector<item> test_cases = new Vector<item>();	//Test cases/action lists
	public int top_timeSig, bottom_timeSig;			//Time signiture top and bottom
	
	public double time;								//Time that has passed since start
	
	float x, y, z;									//used for parsing new information
	File logFile;									//Log files if we are saving tracked info
	BufferedWriter writer;							//Writer for the log file.
	String script = "";								//String for the script
	FileReader inputStream = null;					//File reader for reading the script in
	
	double avg_proximity = 0.0;						//Average proximity between all objects
	
//#endregion
	
	/* Methods */
//#region methods
	/**
	 * Constructor
	 * @param datafile - the script file to be read in
	 */
	public MessageHandler(String datafile){
		script = datafile;
	}//end constructor
	
	/** Reads in the script file given to the constructor, creates a datastructure
	 * 
	 * @throws IOException if the file could not be found
	 */
	public void parseScript() throws IOException{
		//TODO: remove this snippet
		System.out.println("Attempting to parse file...");
		
		/** Open the file **/
		File f = new File(script);
	    FileInputStream fin = new FileInputStream(f);
	    byte[] buffer = new byte[(int) f.length()];
	    try {
			new DataInputStream(fin).readFully(buffer);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	    fin.close();
	    String s = new String(buffer, "UTF-8");   //s is now the string.
	    String[] items = s.split("<item>");		//Split it about the item tags
	  
	    /* For Each Item */
	    for (int i = 1; i < items.length; i++){	
	    	item this_mutator = new item(this);
	    	
	    	String current_item = items[i];
	    	System.out.println("Item "+i);
	    	/* Break into activities */
	    	String[] activities = current_item.split("<activity>");
	    	System.out.println("Number of activities... "+activities.length);
	    	
	    	/*For each activity*/
	    	for (int j = 1; j < activities.length; j++){ 
	    		activity this_activity = new activity(this_mutator);
	    		
	    		/* An activity contains a condition list and an action list... */
	    		conditionList thisConditionList = new conditionList();
	    		String current_activity = activities[j].trim();
	    		//System.out.println("\tActivity "+j);
	    		int statements_index = 0;
	    		boolean wasItOr = false;
	    		
	    		/* Search for Conditionals */
	    		if (current_activity.indexOf("<if>") != -1){	//if we have an if statement
	    			int start = current_activity.indexOf("<if>")+6;	//Starting index of the statements...
	    			
	    			int end = current_activity.indexOf("<then>") - 2;
		    		statements_index = end+10;
		    		String[] conditions = current_activity.substring(start, end).split(System.lineSeparator());
		    			
		    			//System.out.println("Conditions: " + conditions);
		    			/** For Each Condition **/
		    			for (int k = 0; k < conditions.length; k++){ 
		    				System.out.println("Condition line :~" + conditions[k] + "~");
		    				condition this_condition = null;	//This will be the condition...
		    				
		    				String[] tokens = conditions[k].split(" ");
		    				switch (tokens[0]){
		    				case "equal_to":  this_condition = new condition(CONSTANTS.string2var(tokens[1]), condition.op.equal, CONSTANTS.string2var(tokens[2])); break;
		    				case "less_than": this_condition = new condition(CONSTANTS.string2var(tokens[1]), condition.op.less, CONSTANTS.string2var(tokens[2])); break;
		    				case "greater_than": this_condition = new condition(CONSTANTS.string2var(tokens[1]), condition.op.greater, CONSTANTS.string2var(tokens[2])); break;
		    				case "not_equal_to": this_condition = new condition(CONSTANTS.string2var(tokens[1]), condition.op.not_equal, CONSTANTS.string2var(tokens[2])); break;
		    				//case "left_hand": this_condition = new condition(condition.obj.left_hand); break;
		    				//case "right_hand": this_condition = new condition(condition.obj.right_hand); break;
		    				//case "left_foot": this_condition = new condition(condition.obj.left_foot); break;
		    				//case "right_foot": this_condition = new condition(condition.obj.right_foot); break;
		    				//case "head": this_condition = new condition(condition.obj.head); break;
		    				//case "either_foot": this_condition = new condition(condition.obj.either_foot); break;
		    				//case "both_feet": this_condition = new condition(condition.obj.both_feet); break;
		    				//case "both_hand": this_condition = new condition(condition.obj.both_hands); break;
		    				//case "either_hand": this_condition = new condition(condition.obj.either_hand); break;
		    				case "<or>": 
		    					this_activity.add_conditionList(thisConditionList);
		    					wasItOr = true;
		    					thisConditionList = new conditionList();
		    					break;
		    				case "object":
		    					this_condition = new condition(tokens[1], "object");
		    					break;
		    				default:
		    					System.out.println("Unrecognized condition operator : "+tokens[0]);
		    				}//end switch statement
		    				
		    				if (!wasItOr){ //add condition and parse in numbers if needed.
		    					if(tokens.length > 1 &&CONSTANTS.string2var(tokens[1]) == CONSTANTS.var.NUMBER){//Set the numbers for LHS, RHS if needed.
			    					this_condition.setdLHS(Double.parseDouble(tokens[1]));
			    				}//end if number
			    				if(tokens.length > 2 && CONSTANTS.string2var(tokens[2]) == CONSTANTS.var.NUMBER){//Set the numbers for LHS, RHS if needed.
			    					this_condition.setdRHS(Double.parseDouble(tokens[2]));
			    				}//end if number
		    					thisConditionList.addCondition(this_condition);
		    				}
		    				wasItOr = false;
		    				System.out.println("\t\tConditions :" + conditions[k]);
		    			}//end of for each condition
	    		}//end if conditional
	    		/* Else, nonconditional */
	    		else{ //NONCONDITIONAL
	    			thisConditionList.addCondition(new condition(true));
	    			System.out.println("\t\tNonconditional...");
	    			statements_index = 0;
	    		}//end nonconditional/conditional
	    		
	    		this_activity.add_conditionList(thisConditionList); //add in the condition list for this activity
	    		
	    		/* PARSE ACTIONS */
	    		//Look for the action list...
	    		actionList thisActionList = new actionList();
	    		//find the end of the action list (will be first <)
	    		int endindex = current_activity.indexOf("</") - 2;
	    		
	    		String[] actions = current_activity.substring(statements_index, endindex).split(System.lineSeparator());
	    		
	    		/**for each action**/
	    		for (int acNum = 0; acNum < actions.length; acNum++){
	    			if (actions[acNum].equalsIgnoreCase("</if>") || actions[acNum].equalsIgnoreCase("</activity>")){ //Test ending condition
	    				break; //break out of the for loop
	    			}
	    			action this_action = null;	//action we will be adding
	    			
	    			String[] tokens = actions[acNum].split(" ");	//Break into tokens
	    			System.out.println("Tokens = " + tokens.length);
	    			
	    			String out = "";
	    			for (int blah = 0; blah < tokens.length; blah++){
	    				out = out + "~"+tokens[blah]+"~";
	    			}
	    			System.out.println(out);	//TODO remove this snippet.
	    			
	    			/** If we have zero tokens bad stuff **/
	    			if(tokens.length == 0){     //Should not happen
	    				System.out.println("Zero Tokens?");
	    			}
	    			else if(tokens.length < 2){	//Notes, instrument change, time signiture, key sig.
	    				/*test for notes*/
	    				if(tokens[0].startsWith("notes:")){ //set to play music
	    					this_action = new action(this_mutator, tokens[0].split(":"));	//create note string
	    				}
	    				else{
	    					this_action = new action(this_mutator, tokens[0]);	//will handle timesig, instrument, key 
	    					System.out.println("Less than two action tokens :"+tokens.length);
	    					System.out.println("Token_1 :" + tokens[0] );
	    			
	    				}
	    			}//end <3 tokens
	    			else{ //3+ tokens...  
		    			
	    				/** each token... 
		    			 * zero token is =  
		    			 * First token is what is going to be assigned, or a key signiture, or a time signiture, or an instrument group, or the value to be assigned
		    			 * Second token is op code or assign
		    			 * 
		    			  **/
	    			
	    				CONSTANTS.var LHS = CONSTANTS.string2var(tokens[1]);	//parse in the token we are assigning.
	    				CONSTANTS.var var1, var2;
	    				action.arithmatic_operations arith_op = null;
	    				
	    				/** If we have two values to parse in **/
	    				if (tokens.length == 5){
	    					
	    					var1 = CONSTANTS.string2var(tokens[3]);
	    					var2 = CONSTANTS.string2var(tokens[4]);
	    				}
	    				/** else we have one value to parse in **/
	    				else{
	    					var1 = CONSTANTS.string2var(tokens[3]);
	    					var2 = CONSTANTS.var.NULL;
	    				}
	    				
	    				
	    				/* Figure out the arithmatic operator */
	    				switch (tokens[2]){
	    				case "+":
	    					arith_op = action.arithmatic_operations.add;
	    					break;
	    				case "-":
	    					arith_op = action.arithmatic_operations.sub;
	    					break;
	    				case "*":
	    					arith_op = action.arithmatic_operations.mul;
	    					break;
	    				case "/":
	    					arith_op = action.arithmatic_operations.div;
	    					break;
	    				case "%":
	    					arith_op = action.arithmatic_operations.mod;
	    					break;
	    				case "^":
	    					arith_op = action.arithmatic_operations.square;
	    					break;
	    				case "abs":
	    					arith_op = action.arithmatic_operations.abs;
	    					break;
	    				case "assign":
	    					arith_op = action.arithmatic_operations.assign;
	    					break;
	    				}//end switch case
	    				
	    				//If we are assigning to internal memory
	    				if (tokens[1].length() == 1){
	    					this_action = new action(this_mutator, tokens[1].charAt(0),var1, arith_op, var2);
	    				}
	    				else{ //else, setting to external memory
	    					this_action = new action(this_mutator, tokens[1], var1, arith_op, var2);
	    				}//end if/else token variable
	    				/** Test for numerical tokens **/
	    				if(var1 == CONSTANTS.var.NUMBER){
	    					this_action.setVar1(Double.parseDouble(tokens[3]));
	    				}
	    				if(var2 == CONSTANTS.var.NUMBER){
	    					this_action.setVar2(Double.parseDouble(tokens[4]));
	    				}
	    			
	    			
	    			}//end else - four or more tokens	    		
	    			
	    			thisActionList.addAction(this_action);
	    			
	    		}//end for each action
	    		
	    		this_activity.set_actionList(thisActionList); //set the activitie's action list
	    		this_mutator.addActivity(this_activity);
	    		
	    	}//end for each activity
	    	
	    	test_cases.add(this_mutator);	//add this mutator (item) to the action list.
	    	
	    }//end for each item
	    
	}//end parse method
		
	/**
	 * Method to perform the needed operations to create musics -- This is where all testing should go, where all music should be generated
	**/
	public void test_data(int i){
	/**
	 * z is vertical
	 * x is side to side
	 * y is toward the screen
	 * rotation?  How will this show up.
	 * You can get velocity data, frequency of movement data, average velocity data	
	 */
		this.avg_proximity = 0.0;	//reset proximity
		//Set avg_proximity...
		for (int j = 0; j < CONSTANTS.objects; j++){
			if (j!=i){
				this.avg_proximity += this.tracked[i].proximity(this.tracked[j]);
			}//end if
		}//end for
		
		this.avg_proximity = this.avg_proximity/CONSTANTS.objects;
		
	}//end method test_data
	
	/**
	 * This method continuously snatches up messages that the main thread throws in the queue and parses the information into the various objects.
	 * This method will call test_data at a number of times per second specified in constants.
	 * 
	 */
	public void run() {
		
		if (CONSTANTS.sendToProcessing){
			try {
				sendToProc = new DatagramSocket(9998);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		}//end if we are sending data to processing.
		
		tracked = new objectData[CONSTANTS.objects];	//Objects we are tracking
		
		try {
			parseScript();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("\n Done parsing script... Data structures==... \n");
		for (int i = 0; i < test_cases.size(); i++){
			System.out.println(test_cases.get(i).toString());
		}
		
		//If we are logging, open the file to log, and a writer to write
		if (CONSTANTS.writeOutputFile){
			logFile = new File(CONSTANTS.outputFileName);
			try {
				writer = new BufferedWriter(new FileWriter(logFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//end if we are logging, make a file
		
		time = 0;								//Current time
		double messages_r = 0;							//How many messages recieved
		double messages_bad = 0;						//How many messages bad
		double deltaTime = 0;							//the change in time between samples in seconds.
		
		System.out.println("Message Handler running...");
		
		/*
		 * Main loop - Take a message, parse the information
		 */
		while (ViconGestureMapping.keepRunning) 
		{
            try 
            {
            	/*
            	 * Take a message, split it into sections, mark the time difference...
            	 */
                String msg = ViconGestureMapping.msgBuf.take();		//Should be blocking
                String[] data = msg.split("%");						//Split good from bad
                String[] objects = data[0].split(System.lineSeparator());				//Split objects
                CONSTANTS.DebugPrint("Message Recieved: "+msg, 200);//Debug printing
                
                time = time+ deltaTime;								
                deltaTime = 1.0/CONSTANTS.ViconSamplesPerSecond;	//Keep it constant
                //If we are logging, mark the time delta
                if (CONSTANTS.writeOutputFile){
                	writer.write(Double.toString(deltaTime));
                	writer.newLine();
                }//end if write file
                
                /* For each part of the message */
                for (int i = 0; i < CONSTANTS.objects; i++){
                	/*
                	 * Each part will be in the format of...
                	 * "Object Name", x, y, z
                	 */
	                String[] parts = objects[i].split("~");
	                messages_r+=1;	//mark that we received another message
	               
	                //Make sure we have enough parts
		            if (parts.length >= 4){
		                
		            	//If we are logging, write the object name, x, y, z
		            	if (CONSTANTS.writeOutputFile){
		            		writer.write(objects[i]);
		            		writer.newLine();
		            	}//end if we are writing a log file
		            	
		            	/* Parse in the new values */
		            	x = Float.parseFloat(parts[1]);
		                y = Float.parseFloat(parts[2]);
		                z = Float.parseFloat(parts[3]);
		                
		                tracked[i].addData(deltaTime, x, y, z, parts[0]);		//Add the data...
		                
		            }//end if good message
		            else//bad message
		            {
		            	CONSTANTS.DebugPrint("Bad Message! Parts:"+parts.toString()+" Expecting >=4", 150);
		            	messages_bad+=1;
		            	
		            	CONSTANTS.DebugPrint(msg, 150);
		            	CONSTANTS.DebugPrint("BAD RATIO: " + (messages_bad/messages_r), 150);		            
		            }//end else bad message
                }//end for each object
                
                /** Debugging code - used for sending transform information to processing
                 * enabled by having CONSTANTS.sendtoprocessing set to true.
                 *  **/
                if(messages_r > 1000 && messages_r%10 == 0 && CONSTANTS.sendToProcessing){
                	tracked[0].dft();
                	byte[] dataout = tracked[0].getBftt();
                	byte[] addr = {127,0,0,1};
                	InetAddress adr = InetAddress.getByAddress(addr);
                	DatagramPacket sendpacket = new DatagramPacket(dataout, dataout.length, adr, 9998); 
                	sendToProc.send(sendpacket);
                }//end debugging code to send message to processing...
                
            } 
            catch (InterruptedException | IOException ie) 
            {
            	
            	ie.printStackTrace();
            }//end catch
            
        }//end while the program is still running
		
		//flush and close the writer
		try {
			if(CONSTANTS.writeOutputFile){
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		music.close();			//close the music player
		sendToProc.close();		//close the send to proc datasocket
	}//end run method

//#endregion

	
}//end MessageHandler class
