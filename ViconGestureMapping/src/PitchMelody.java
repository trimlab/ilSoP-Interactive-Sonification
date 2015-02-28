import java.util.Timer;
import java.util.TimerTask;
import org.jfugue.*;

/**
 *
 * @author Joseph Ryan
 */
public class PitchMelody extends InstrumentLine{
    
    //Test timer stuff
    private Timer timer = new Timer();
    private int tickRate = 1000;
    private int volLevel = 0;

    private String volume ="a0d0";
    private String baseNote = "C4";
    private item Parent;
    private pitchThread pT = new pitchThread();
    
    public PitchMelody(item parent){
        Parent = parent;
        correctNote();
        timer.schedule(new RemindTask(), tickRate);
        pT.start();
    }

    public void setBaseNote(String newBase){
        if(newBase!=null){
            baseNote = newBase;
            correctNote();
        }
    }
    private void correctNote(){
        Note base = new Note();
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                base.setValue((byte) (f + KeyChanger.keyMod()));
                baseNote = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                break;
            }
        }
    }

    
    public class pitchThread extends Thread{
        
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                    Pattern p = new Pattern();
                    p.add(baseNote);
                    play.streamAndWait("I88 "+p);
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
    
    public class RemindTask extends TimerTask{
        
        public void run(){
           volLevel += 10;
           volume = "a"+volLevel+"d"+volLevel;
           if(volLevel  != 100){
           timer.schedule(new RemindTask(), tickRate);
           }
        }
    }
   
}
