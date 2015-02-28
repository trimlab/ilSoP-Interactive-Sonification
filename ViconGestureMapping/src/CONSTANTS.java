/**
 * TODO List:
 * 
 * Today is August 13th.
 * Two weeks from now (Aug 27th)
 * 		Some sonification, some visualization based on movement.
 * 		Generated via virtual instrument.
 * 		Demo 
 * 
 * Write a couple of paragraphs on what I have done, how it was developed, technical specifications, structures, etc.
 * How the mapping works...
 * Goal of the mapping
 * 
 * Goal of the paper is to introduce the format.
 * Enter in some of the structure, how it was developed, etc.
 * (By August 27th)
 * 
 * Future plans also.
 * 
 */
public final class CONSTANTS {
	
	enum var{
		cur_velocity_x, cur_velocity_y, cur_velocity_z, cur_velocity_composite,
		avg_velocity_x, avg_velocity_y, avg_velocity_z, avg_velocity_composite,
		acceleration_x, acceleration_y, acceleration_z, acceleration_composite,
		avg_proximity, x_position, y_position, z_position, elapsed_time_once,
		elapsed_time_repeatable, tempo,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,NULL, NUMBER, INSTRUMENT, note_length;
	}
	enum key{
		cmaj, gmaj, dmaj, amaj, emaj, bmaj, fsharpmaj, csharpmaj, amin, emin, bmin, fsharpmin, csharpmin, gsharpmin, dsharpmin, asharpmin
	}
	enum instrument_set{
		/** Instrument sets go here **/
		rock, electronica, orchestral 
	}
	
	public static var string2var(String strVar){
		strVar = strVar.trim();
		//Test first if it is numerical...
		if (isADouble(strVar)){
			return var.NUMBER;
		}
		
		switch (strVar){
		case "cur_velocity_x": return var.cur_velocity_x; 
		case "cur_velocity_y": return var.cur_velocity_y;
		case "cur_velocity_z": return var.cur_velocity_z;
		case "cur_velocity_composite": return var.cur_velocity_composite;
		case "avg_velocity_x": return var.avg_velocity_x;
		case "avg_velocity_y": return var.avg_velocity_y;
		case "avg_velocity_z": return var.avg_velocity_z;
		case "avg_velocity_composite": return var.avg_velocity_composite;
		case "acceleration_x": return var.acceleration_x;
		case "acceleration_y": return var.acceleration_y;
		case "acceleration_z": return var.acceleration_z;
		case "acceleration_composite": return var.acceleration_composite;
		case "avg_proximity": return var.avg_proximity;
		case "x_position": return var.x_position;
		case "y_position": return var.y_position;
		case "z_position": return var.z_position;
		case "elapsed_time_once": return var.elapsed_time_once;
		case "elapsed_time_repeatable": return var.elapsed_time_repeatable;
		case "bpm": return var.tempo;
                
                case "instr": return var.INSTRUMENT;    
                case "note_length": return var.note_length;
                    
		case "a": return var.a;
		case "b": return var.b;
		case "c": return var.c;
		case "d": return var.d;
		case "e": return var.e;
		case "f": return var.f;
		case "g": return var.g;
		case "h": return var.h;
		case "i": return var.i;
		case "j": return var.j;
		case "k": return var.k;
		case "l": return var.l;
		case "m": return var.m;
		case "n": return var.n;
		case "o": return var.o;
		case "p": return var.p;
		case "q": return var.q;
		case "r": return var.r;
		case "s": return var.s;
		case "t": return var.t;
		case "u": return var.u;
		case "v": return var.v;
		case "w": return var.w;
		case "x": return var.x;
		case "y": return var.y;
		case "z": return var.z;
		default:
			
			return var.NULL;
		}
		
	}

	/**
	 * 
	 * @param s - String to test to see if it is a double
	 * @return true if it can be parsed into a double, false otherwise.
	 */
	public static boolean isADouble(String s){
		
		try{
			Double.parseDouble(s);
		}
		catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	
	
	public static double token_to_var(var token, item Parent){
		objectData tracked = Parent.Parent.tracked[Parent.object_number];
		double value = 0;	//will be the value of var1
		switch (token){
			case cur_velocity_x: value = tracked.getXvel(); break;
			case cur_velocity_y: value = tracked.getYvel(); break;
			case cur_velocity_z: value = tracked.getZvel(); break;
			case cur_velocity_composite: value = tracked.getTvel(); break;
			case avg_velocity_x: value = tracked.getAvgXvel();
				break;
			case avg_velocity_y: value = tracked.getAvgYvel();
				break;
			case avg_velocity_z: value = tracked.getAvgZvel();
				break;
			case avg_velocity_composite: value = tracked.getAvgTvel();
				break;
			case acceleration_x: value = tracked.getXacc();
				break;
			case acceleration_y: value = tracked.getYacc();
				break;
			case acceleration_z: value = tracked.getZacc();
				break;
			case acceleration_composite: value = tracked.getTacc();
				break;
			case avg_proximity: value = Parent.Parent.avg_proximity;
				break;
			case x_position: value = tracked.getCurX();
				break;
			case y_position: value = tracked.getCurY();
				break;
			case z_position: value = tracked.getCurZ();
				break;
			case elapsed_time_once: value = Parent.Parent.time;
				break;
			case elapsed_time_repeatable: value = Parent.Parent.time;
				break;                            
		case a: value = Parent.vars[0];
			break;
		case b: value = Parent.vars[1];
			break;
		case c: value = Parent.vars[2];
			break;
		case d: value = Parent.vars[3];
			break;
		case e: value = Parent.vars[4];
			break;
		case f: value = Parent.vars[5];
			break;
		case g: value = Parent.vars[6];
			break;
		case h: value = Parent.vars[7];
			break;
		case i: value = Parent.vars[8];
			break;
		case j: value = Parent.vars[9];
			break;
		case k: value = Parent.vars[10];
			break;
		case l: value = Parent.vars[11];
			break;
		case m: value = Parent.vars[12];
			break;
		case n: value = Parent.vars[13];
			break;
		case o: value = Parent.vars[14];
			break;
		case p: value = Parent.vars[15];
			break;
		case q: value = Parent.vars[16];
			break;
		case r: value = Parent.vars[17];
			break;
		case s: value = Parent.vars[18];
			break;
		case t: value = Parent.vars[19];
			break;
		case tempo: value = Parent.Parent.musicTempo;
			break;
		case u: value = Parent.vars[20];
			break;
		case v: value = Parent.vars[21];
			break;
		case w: value = Parent.vars[22];
			break;
		case x: value = Parent.vars[23];
			break;
		case y: value = Parent.vars[24];
			break;
		case z: value = Parent.vars[25];
			break;
		default:
			break;
			
		}
		return value;
	}//end token to value
	
	public static final boolean sendToProcessing = false;				//Are we trying to send data to processing?
	public static final int SecondsOfRawData = 2; // 5
	public static final int ViconSamplesPerSecond = 100;				//How many vicon samples per second are we getting?
	public static final int dftLength = ViconSamplesPerSecond*SecondsOfRawData;//How many samples we want to use in a DFT
	public static final int resolution = 10;							//clump together 10 items when doing ffts
	public static final boolean writeOutputFile = false;					//Are we going to write to an output file?
	public static final String outputFileName = "Vicon_output.txt";		//What is the name of the output file
	public static final int verbose = 201;								//Amount of internal data to print (lower is less)
	public static final int port = 9875;								//port to connect on
	public static final float scale = (float) 0.1;						
	public static final float panScale = 4.0f;							
	public static final float zPlay = 0.2f;								//Location of play barrier
	public static int objects = 4;								        //Number of objects we are tracking
	public static final int minVolume = 5;								//Minimum volume of a strike
	public static final int instrument = 0;								//Grand piano
	/*  Instrument Types
	 * 0-7 Piano
	 * 8-15 chromatic percussion
	 * 16-23 organ
	 * 24-31 Guitar
	 * 32-39 bass
	 * 40-47 strings
	 * 48-55 ensemble
	 * 56-63 brass
	 * 64-71 reed
	 * 72-79 pipe
	 * 80-87 synth lead
	 * 88-95 synth pad
	 * 96-103 synth effects
	 * 104-111 ethnic
	 * 112-119 Percussive
	 * 120-127 sound effects
	 */

	
	public static final double longFallOff = 0.99;			//For rolling tapered average
	public static final double longFallOffDivide = 99;		//Since E(all entries*.99^n) = 99
	
	public static final double shortFallOff = .98;			//For rolling tapered short average
	public static final double shortFallOffDivide = 49;		//Since E(all entries*.98^n) = 49
	/* TODO: Change this to two octaves, see if it is easier to play
	 * 
	 * 
	 */
	/* maximum volume calcs...
	 * Free fall from 1m... v=4.43m/s
	 * free fall from 1/2m..v=3.126m/s
	 * free fall from 1/3m..v=2.558m/s */ 
	public static final float velMaxVol = 1.00f;		//Velocity of maximum loudness (m/s)
	
	public static final float firstKeyX = -3.17f;		//location of first key in X
	public static final float lastKeyX = 2.3f;			//location of last key in X
	public static final int whiteKeys = 21;				//How many white keys on keyboard
	public static final float range = lastKeyX - firstKeyX; 	//Range in m of keyboard
	public static final float keySize = range/whiteKeys;//Size of one key
	public static final int firstKey = 60;				//Middle C
		
	/* Might need to do this in half strike zones... */
	/*
	 *   1  3     6  8  10
	 * x|# |# |xx|# |# |# |x
	 * |c |d |e |f |g |a |b |
	 *  0  2  4  5  7  9  11
	 */
	public static final int[] wKeyIndex = {0, 2, 4, 5, 7, 9, 11};	//From middle c
	public static final int[] bKeyIndex = {1, 3, 6, 8, 10};			//from middle c
	public static final int[] bKeyIndexHalf = {-1,1,1,3,3,-1,-1,6,6,8,8,10,10,-1};
	//Had conflicting data on what first key was... what is first key?  Have as C right now
	
	public static final String[] wKeyName = {"c", "d", "e", "f", "g", "a", "b"};
	public static final String[] bKeyName = {"X", "c#","c#", "d#","d#","X","X", "f#","f#", "g#","g#","a#","a#","X"};
	
	public static final float yBlackKeys = 0.0f;//y> this, black keys y<this, white keys
	/**y=2.4 at display wall		      
	 * |			    4
	 * |			  ^
	 * |______________x,y = 0,0 at center
	 * |			  v
	 * |      <-y->
	 * |			   -2
	 * 
	 * ^Display Wall
	 */
	//
	public static void DebugPrint(String str, int v){
		if (verbose >= v){
			
			System.out.println(str);
			
		}//end if verbose
		
	}//end DebugPrint
	
	public static void set_objects(int i){
		objects = i;
	}
	
}//end CONSTANTS
