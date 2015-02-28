
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Joseph Ryan
 */
public abstract class KeyChanger {
    
    //Test timer stuff
    private static Timer timer = new Timer();
    private static boolean started = false;
    private static int tickRate = 12000;
                                   
    private static int[] keyMods = {17,12,14,10}; // G = 0
    private static int modIndex = 0;
    
    public KeyChanger(){}
    
    public static int keyMod(){
        
        if(!started){
            started = true;
            timer.schedule(new RemindTask(), tickRate);
        }
        
        return keyMods[modIndex];
    }
    
    public static class RemindTask extends TimerTask{
        
        public void run(){
            
            if(modIndex+1 > keyMods.length-1){
                modIndex = 0;
            }else{
                modIndex++;
            }
            timer.schedule(new RemindTask(), tickRate);
        }
        
    }
}
