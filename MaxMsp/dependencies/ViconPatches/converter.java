import com.cycling74.max.*;
import java.util.*;

public class converter extends MaxObject
{


	private static final String[] INLET_ASSIST = new String[]{
		"inlet 1 help"
	};
	private static final String[] OUTLET_ASSIST = new String[]{
		"outlet 1 help"
	};
	
	public converter(Atom[] args)
	{
		declareInlets(new int[]{DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
		

		setOutletAssist(new String[] {"FootR","FootL","HandL","HandR"});

	//	setInletAssist(INLET_ASSIST);
	//	setOutletAssist(OUTLET_ASSIST);

	}
    
	public void bang()
	{
		post("finally");
	}
    
	public void inlet(int i)
	{
	}
    
	public void inlet(float f)
	{
	}
    
    
	public void list(Atom[] list)
	{
		String input = "";
		for(int l = 0; l < list.length; l++){
			input += Character.toString((char)list[l].getInt());
		}

		String name = "";
        double[] vars = new double[3];
        
        int t = 1;
        for(int i = 0; i < 4 && t < input.length()-1; i++){ //Items
            name = "";
            vars = new double[3];
            
            //Item name
            while(!input.substring(t, t+1).equals("~")){
               name += input.substring(t, t+1);
               t++;
            }
            t++;
            
            //Coords
            for(int v = 0; v < 3; v++){
                int startI = t;
                while(!input.substring(t, t+1).equals("~") && !input.substring(t, t+1).equals("%") && t < input.length()-1){
                   t++;
                }
                vars[v] = Double.parseDouble(input.substring(startI, t));
                t++;
            }
            t++;

			//OUTPUT HERE
			outlet(i,new Atom[]{Atom.newAtom(vars[0]),Atom.newAtom(vars[1]),
				   Atom.newAtom(vars[2])});
        }
	}
    

}











