
import java.util.Timer;
import java.util.TimerTask;
import org.jfugue.*;


/**
 *
 * @author Joseph Ryan
 */
public class ChordLine extends InstrumentLine{
    
    private Timer timer = new Timer();
    private int tickRate = 1000;
    private boolean canStart = false;
    private int volLevel = 0;
    
    
    private String volume ="a0d0";
    private String baseNote = "A6";
    private item Parent;
    private chordThread cT = new chordThread();
    
    public ChordLine (item parent){
        Parent = parent;
        makeChord();
        cT.start();
        timer.schedule(new RemindTask(), tickRate);
    }
    
    public void setBaseNote(String newBase){
        if(newBase!=null){
            baseNote = newBase;
            makeChord();
        }
    }
    
    public void makeChord(){
        Note base = new Note();
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                String n1, n2, n3;
                base.setValue((byte) (f + KeyChanger.keyMod()-12));
                n1 = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f + 5 + KeyChanger.keyMod()-12));
                n2 = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f + 7 +KeyChanger.keyMod()-12));
                n3 = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                baseNote = n1 + "+" + n2 + "+" + n3;
//                baseNote = n1;
                break;
            }
        }
    }
    
    
    public class chordThread extends Thread{
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                    Pattern p = new Pattern();
                    p.add(baseNote);
                    p.add("Rww");
                    play.streamAndWait("I90 "+p);
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
           volLevel += 20;
           volume = "a"+volLevel+"d"+volLevel;
           if(volLevel  != 80){
           timer.schedule(new RemindTask(), tickRate);
           }
        }
        
    }
}
