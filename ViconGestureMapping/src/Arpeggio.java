
import java.util.Timer;
import java.util.TimerTask;
import org.jfugue.*;

/**
 *
 * @author Joseph Ryan
 */
public class Arpeggio extends InstrumentLine{
    
    //Test timer stuff
    private Timer timer = new Timer();
    private int tickRate = 1000;
    private boolean canStart = false;
    private int volLevel = 0;
    
    private String[] arpeggio;
    private String arpeggioLength = "wa120d120";
    private String volume ="a0d0";
    private String baseNote = "A6";
    private item Parent;
    private arpThread aT = new arpThread();
    
    public Arpeggio(item parent){
        Parent = parent;
        makeArpeggio();
        timer.schedule(new RemindTask(), tickRate);
        aT.start();
    }
    
    public void setBaseNote(String newBase){
        if(newBase!=null){
            baseNote = newBase;
            makeArpeggio();
        }
    }
    private void makeArpeggio(){
        Note base = new Note();
        arpeggio = new String[8];
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                base.setValue((byte) (f + KeyChanger.keyMod()));
                arpeggio[0] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f+4 + KeyChanger.keyMod()));
                arpeggio[1] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f+7 + KeyChanger.keyMod()));
                arpeggio[2] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f+12 + KeyChanger.keyMod()));
                arpeggio[3] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f+7 + KeyChanger.keyMod()));
                arpeggio[4] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f+4 + KeyChanger.keyMod()));
                arpeggio[5] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()));
                arpeggio[6] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                base.setValue((byte) (f-5 + KeyChanger.keyMod()));
                arpeggio[7] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+Parent.Parent.note_length+volume;
                break;
            }
        }
    }

    
    public class arpThread extends Thread{
        
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                    Pattern p = new Pattern();
                    for(int a = 0; a < arpeggio.length;a++){
                        p.add(arpeggio[a]);
                    }
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
           volLevel += 20;
           volume = "a"+volLevel+"d"+volLevel;
           if(volLevel  != 100){
           timer.schedule(new RemindTask(), tickRate);
           }
        }
        
    }
   
}
