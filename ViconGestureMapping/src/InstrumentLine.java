
/**
 *
 * @author Joseph Ryan
 */
public abstract class InstrumentLine {
    
    public boolean isQuiet = false;
    public boolean gotInput = false;
    
    public void isQuiet(boolean quiet){
        isQuiet = quiet;
    }
    
    public boolean isQuiet(){
        return isQuiet;
    }
    
    public void gotInput(boolean input){
        gotInput = input;
    }
    
    public boolean gotInput(){
        return gotInput;
    }
}
