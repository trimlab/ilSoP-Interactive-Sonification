
import java.util.Timer;
import java.util.TimerTask;
import org.jfugue.*;



/**
 *
 * @author Joseph Ryan
 */
public class TechnoMelody extends InstrumentLine{
    
    //Test timer stuff
    private Timer timer = new Timer();
    private int tickRate = 1000;
    private boolean canStart = false;
    private int volLevel = 0;
    
    private String[] musicLine = new String[16];
    private String nlength = "h";
    private String volume ="a0d0";
    private String baseNote = "A6";
    private item Parent;
    private techThread mT = new techThread();
    
    public TechnoMelody(item parent){
        Parent = parent;
        makeMelodyLine();
        timer.schedule(new RemindTask(), tickRate);
        mT.start();
    }
    
    public void setBaseNote(String newBase){
        if(newBase!=null){
            baseNote = newBase;
            makeMelodyLine();
        }
    }
    
    private void makeMelodyLine(){
        Note base = new Note();
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                base.setValue((byte) (f + KeyChanger.keyMod()-13));
                musicLine[0] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-6));
                musicLine[1] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-1));
                musicLine[2] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()+3));
                musicLine[3] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                
                base.setValue((byte) (f + KeyChanger.keyMod()-9));
                musicLine[4] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-2));
                musicLine[5] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()+3));
                musicLine[6] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()+7));
                musicLine[7] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                
                
                base.setValue((byte) (f + KeyChanger.keyMod()-14));
                musicLine[8] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-7));
                musicLine[9] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-2));
                musicLine[10] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()+2));
                musicLine[11] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                
                
                base.setValue((byte) (f + KeyChanger.keyMod()-16));
                musicLine[12] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-9));
                musicLine[13] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()-4));
                musicLine[14] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                base.setValue((byte) (f + KeyChanger.keyMod()));
                musicLine[15] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+nlength+volume;
                
            }
        }
    }
    
    public class techThread extends Thread{
        
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                Pattern p = new Pattern();
                for(int a = 0; a < musicLine.length;a++){
                    p.add(musicLine[a]);
                    if(a<musicLine.length-1)
                    p.add("Rh");
                }
                play.streamAndWait("I81 "+p);
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
