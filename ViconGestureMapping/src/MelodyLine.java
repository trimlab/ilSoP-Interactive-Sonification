import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.jfugue.*;

/**
 *
 * @author Joseph Ryan
 */
public class MelodyLine extends InstrumentLine{
    
    //Test timer stuff
    private final Timer timer = new Timer();
    private final int tickRate = 4000;
    private boolean canStart = false;
    
    private String baseNote = "G2";
    private String[] playLine;
    private final String melodyVolume = "a70d70";
    private item Parent;
    private melodyThread mT = new melodyThread();
    
    private int instr = 0;
    private int mode = 0;
    private int count = 0;
    private boolean rep = true;
    private final String[] melodyInstruments = {"I31","I18","I22","I55","I86","I81","I90"};
    private final String[] melodyLines = {"G5w A5w Bb5w A5w Bb5w C6w Bb5w C6w", //
                                    "D6w C6w D6w F6w D6w F6w G6w F6w", //
                                    "G6w F6w D6w F6w D6w C6w D6w C6w", //
                                    "Bb5w C6w Bb5w A5w Bb5w A5w G5w F5w",
                                    "D6w C6w D6w F6w D6w F6w G6w F6w", // 
                                    "G4w F4w D4w G4w F4w D4w G4w F4w", //
                                    "G4w Bb4w C5w D5w C5w Bb4w G4w Bb4w", //
                                    "G4w Bb4w C5w D5w F5w Bb4w F5w C5w", //
                                    "G6w G5w F6w D6w C6w D6w F6w", //
                                    "D7w D7w C7w D7w C7w Bb6w A6w Bb6w G6w A6w Bb6w C7w D7w C7w F7w G7w", //
                                    "G6i Ri Ri G6i Ri Ri G6i Ri Ri G6i c7q A6i Bb6i Bb6i Ri Ri Bbi Ri Ri F6i Ri Ri D6i Ri Ri F6i G6i A6i Bb6i", //
                                    "G6h Bb6h D6h G6h Bb6h D6h", //
                                    "G5",
                                    "Bb5",
                                    "D5", 
                                    "G5 D5", 
                                    "D5 G5",
                                    "G6hwa120d120+D7h C7hwa120d120+G7h G6hwa120d120+D7h D6hwa120d120+A6h G6hwa120d120+D7h", //
                                    "G6w G5w F6w A5w E6w Bb5 D5w C5w",
                                    "C5w D5w Bb5w E6W A5w F6w G5w G6w",
                                    "G7q F7q D7q F7q D7q C7q D7q C7q D7q C7q Bb6q C7q Bb6q G6q Bb6q G6q",
                                    "G6h D7h G7h D7h G6h D7h G6h D7h G7h D7h"
                                    };
    
    private final String[] melodyLines2 = {"G5", "A5", "Bb5", "A5", "Bb5", "C6", "Bb5", "C6",
                                    "D6", "C6", "D6", "F6", "D6", "F6", "G6", "F6",
                                    "G6", "F6", "D6", "F6", "D6", "C6", "D6", "C6",
                                    "Bb5", "C6", "Bb5", "A5", "Bb5", "A5", "G5", "F5",
                                    "D6", "C6", "D6", "F6", "D6", "F6", "G6", "F6"
                                    };
    
    public MelodyLine(item parent, int m){
        Parent = parent;
        
        mode = m;
        if(mode == 0)
            selectMelody();
        else
            selectMelody2();
        timer.schedule(new RemindTask(), tickRate);
        mT.start();
    }
    
    public void setBaseNote(String newBase){
        if(newBase!=null){
            baseNote = newBase;
            makeBaseLine();
        }
    }
    
    private void selectMelody(){
        //Random
        double rr  = Math.random()*100;
        int n = (int)rr%melodyLines.length;

        //Not random
//        n = count;
//        count= (count+1)%melodyLines.length;
//        
//        rr  = Math.random()*100;
//        instr = (int)rr%melodyInstruments.length;
        
        Scanner s = new Scanner(melodyLines[n]);
        
        String[] tempLine = new String[4000];
        int c = 0;
        while(s.hasNext()){
            tempLine[c] = s.next();
            c++;
        }
        
        int rep = 1;        
        playLine = new String[c*rep];
        for(int re = 0; re < rep; re++){
            for(int r = 0; r < playLine.length; r++){
                playLine[r] = tempLine[r]+Parent.Parent.note_length+melodyVolume;
            }
        }
        
    }
    
    private void selectMelody2(){
        //Random
        double rr  = Math.random()*100;
        int n = (int)rr%melodyLines2.length;

        //Not random
        n = count;
        count= (count+1)%melodyLines2.length;
        
        rr  = Math.random()*100;
        instr = (int)rr%melodyInstruments.length;
        
        Scanner s = new Scanner(melodyLines2[n]);
        
        String[] tempLine = new String[4000];
        int c = 0;
        while(s.hasNext()){
            tempLine[c] = s.next();
            c++;
        }
        
        int rep = 1;        
        playLine = new String[c*rep];
        for(int re = 0; re < rep; re++){
            for(int r = 0; r < playLine.length; r++){
                playLine[r] = tempLine[r]+Parent.Parent.note_length+melodyVolume;
            }
        }
        
    }
    
    private void makeBaseLine(){
        Note base = new Note();
        for(int f = 21; f < 109;f++){
            base.setValue((byte) f);
            if(base.getMusicString().contains(baseNote)){
                base.setValue((byte) (f + KeyChanger.keyMod()));
                for(int d = 0; d < playLine.length; d++){
                    playLine[d]=base.getMusicString().substring(0, base.getMusicString().indexOf("/"))+melodyVolume;
                }
                break;
            }
        }
    }
    
    public class melodyThread extends Thread{
        
            
        public void run(){
            StreamingPlayer play = new StreamingPlayer();
            while(true){

                if(canStart && !isQuiet){
                    Pattern p = new Pattern();
                    for(int a = 0; a < playLine.length; a++){
                        p.add(playLine[a]);
                    }
                    play.streamAndWait("I88 "+p);
                                        
                    if(mode == 0)
                        selectMelody();
                    else
                        selectMelody2();
                }
                sleep(200);
                
                
            }



        }
        
        private void sleep(int dur){
            //Sleep
            try{
                Thread.sleep(dur);
            }catch(Exception e){
                System.out.println(toString()+" has input interrupted");
            }
        }
    }
    
    public class RemindTask extends TimerTask{

        //Alternate
        public void run(){
           canStart = true;
        }
        
    }
}
