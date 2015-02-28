import org.jfugue.*;

/**
 *
 * @author Joseph Ryan
 */
public class BassLine extends InstrumentLine{
    
    private String baseNote = "G2";
    private String instrument = "I97 ";
    private String[] bassLine = new String[4];
    private String bassLength = "www-a40d40";
    private item Parent;
    private bassThread bT = new bassThread();
    
    public BassLine(item parent){
        Parent = parent;
        makeBassLine();
        bT.start();
    }
    
    public void setInstrument(String i){
        instrument = i;
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
                base.setValue((byte) (f + KeyChanger.keyMod()-12));
                for(int d = 0; d < bassLine.length; d++){
                    bassLine[d]=base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+bassLength;
                }
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
                }
                play.stream(instrument+p);
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
