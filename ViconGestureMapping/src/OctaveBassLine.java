
import org.jfugue.*;


/**
 *
 * @author Joseph Ryan
 */
public class OctaveBassLine extends InstrumentLine{
    
    private String baseNote = "G2";
    private String[] bassLine = new String[12];
    private String bassLength = "wha80d80";
    private item Parent;
    private bassThread bT = new bassThread();
    
    public OctaveBassLine(item parent){
        Parent = parent;
        makeBassLine();
        bT.start();
    }
    
    public void setBassNote(String newBass){
        if(newBass!=null){
            baseNote = newBass;
            makeBassLine();
        }
    }
    private void makeBassLine(){
        Note base = new Note();
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                base.setValue((byte) (f + KeyChanger.keyMod() - 24));
                bassLine[0] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[1] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[2] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                base.setValue((byte) (f + KeyChanger.keyMod() - 21));
                bassLine[3] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[4] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[5] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                base.setValue((byte) (f + KeyChanger.keyMod() - 19));
                bassLine[6] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[7] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[8] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                base.setValue((byte) (f + KeyChanger.keyMod() - 28));
                bassLine[9] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[10] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                bassLine[11] = base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                break;
            }
        }
    }
    
    public class bassThread extends Thread{
        
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){
                if(!isQuiet){
                Pattern p = new Pattern();
                for(int a = 0; a < bassLine.length;a++){
                    p.add(bassLine[a]);
                    if(a < bassLine.length-1)
                    p.add("Rhq");
                }
                play.streamAndWait("I87 "+p);
                makeBassLine();
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
