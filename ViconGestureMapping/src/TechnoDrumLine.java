
import org.jfugue.*;

/**
 *
 * @author Joseph Ryan
 */
public class TechnoDrumLine extends InstrumentLine{
    
    private String drumLine;
    private item Parent;
    private drumThread dT = new drumThread();
    
    
    public TechnoDrumLine(item parent){
        Parent = parent;
        makeDrumLine();
        dT.start();
    }
    
    private void makeDrumLine(){
        drumLine = "V9 [Hand_Clap]ww [Bass_Drum]ww [Hand_Clap]ww [Bass_Drum]ww "
                    + "[Hand_Clap]ww [Bass_Drum]ww [Hand_Clap]ww [Bass_Drum]ww "
                    + "[Hand_Clap]ww [Bass_Drum]ww [Hand_Clap]ww [Bass_Drum]ww "
                    + "[Hand_Clap]ww [Bass_Drum]ww [Hand_Clap]ww [Bass_Drum]ww "
                    + "[Hand_Clap]ww [Bass_Drum]ww [Hand_Clap]ww [Bass_Drum]ww ";
    }
    
    public class drumThread extends Thread{
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                    Pattern p = new Pattern();
                    p.add(drumLine);
//                    play.streamAndWait(p);
                }
                //Sleep
                try{
                    Thread.sleep(200);
                }catch(Exception e){
                    System.out.println(toString()+" has input interrupted");
                }
            }



        }
    }
}
