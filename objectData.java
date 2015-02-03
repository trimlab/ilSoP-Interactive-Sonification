import java.util.Vector;
import java.math.*;


public class objectData {
	
	/**
	 * Variables
	 */
	int skipped = 0;	//how many entries have we skipped for bad data?
	double dTimeskipped = 0;	//extra time we need to add to dT due to skipped data
	int entries = 0;	//how many entries are valid?
	int current = 0;	//current position of the circular buffer
	float[] xPos;					//x position
	float[] yPos;					//y position
	float[] zPos;					//z position
	float xLast, yLast, zLast;
	double[] xVel;					//x velocity
	double[] yVel;					//y velocity
	double[] zVel;					//z velocity
	double[] xAcc;					//x acceleration
	double[] yAcc;					//y acceleration			
	double[] zAcc;					//z acceleration
	double[] tAcc;					//total acceleration
	double[] totalVel;				//total velocity
	double[] dTime;					//change in time for each entry
	double[] fxR;		//Real and imaginary components of ftx 
	double[] fyR;	//Real and imaginary components of fty
	double[] fzR;		//Real and imaginary components of ftz
	double[] ftR;
	byte[] bftx, bfty, bftz, bftt;	//Byte sized transform data
	String objectName = "";			//Object's Name
	double avgXvel, avgYvel, avgZvel, avgTvel;	//Average velocity over the last five seconds
	double avgXacc, avgYacc, avgZacc, avgTacc;	//average acceleration over the last five seconds
	
	/**
	 * Ideas:
	 *    TODO: proximity to other objects
	 *    
	 */
/* GETTER METHODS */ 
//#region Getters

	public double getXacc(){
		return xAcc[current-1];
	}
	public double getYacc(){
		return yAcc[current-1];
	}
	public double getZacc(){
		return zAcc[current-1];
	}
	public double getTacc(){
		return tAcc[current-1];
	}
	/**
	 * 
	 * @return current calculated fourier transform of total velocity data, be sure to call dft first.
	 */
	public double[] getftR(){
		return ftR;
	}
	/**
	 * 
	 * @return current calculated f transform of x velocity.  be sure to call dft first.
	 */
	public double[] getfxR(){
		return fxR;
	}
	/**
	 * 
	 * @return current calced f transform data for y vel.  call dft first
	 */
	public double[] getfyR(){
		return fyR;
	}
	/**
	 * 
	 * @return current calced f transform data for z vel.  call dft first.
	 */
	public double[] getfzR(){
		return fzR;
	}
	/**
	 * 
	 * @return the current x velocity
	 */
	public double getXvel(){
		return xVel[current-1];
	}
	/**
	 * 
	 * @return the current y velocity
	 */
	public double getYvel(){
		return yVel[current-1];
	}
	/**
	 * 
	 * @return the current z velocity
	 */
	public double getZvel(){
		return zVel[current-1];
	}
	/**
	 * 
	 * @return the current total velocity
	 */
	public double getTvel(){
		return totalVel[current-1];
	}
	/**
	 * 
	 * @return the current byte transform of x
	 */
	public byte[] getBftx() {
		return bftx;
	}
	/**
	 * 
	 * @return the current byte transform of y
	 */
	public byte[] getBfty() {
		return bfty;
	}
	/**
	 * 
	 * @return the current byte transform of z
	 */
	public byte[] getBftz() {
		return bftz;
	}
	/**
	 * 
	 * @return the current byte transform of total velocity
	 */
	public byte[] getBftt() {
		return bftt;
	}
	public double getCurX(){
		return xPos[current-1];
	}
	public double getCurY(){
		return yPos[current-1];
	}
	public double getCurZ(){
		return zPos[current-1];
	}	
	/**
	 * 
	 * @return this object's name (the first name it was given, and the only data it will accept)
	 */
	public String getName(){
		return this.objectName;
	}
	
	public double getAvgXvel() {
		return avgXvel;
	}
	public double getAvgYvel() {
		return avgYvel;
	}
	public double getAvgZvel() {
		return avgZvel;
	}
	public double getAvgTvel() {
		return avgTvel;
	}
	public double getAvgXacc() {
		return avgXacc;
	}
	public double getAvgYacc() {
		return avgYacc;
	}
	public double getAvgZacc() {
		return avgZacc;
	}
	public double getAvgTacc() {
		return avgTacc;
	}
//#endregion
	
	
/*CONSTRUCTOR*/
	public objectData(){
		bftx = bfty = bftz = bftt = new byte[CONSTANTS.dftLength/CONSTANTS.resolution];
		xPos = new float[CONSTANTS.dftLength];
		yPos = new float[CONSTANTS.dftLength];
		zPos = new float[CONSTANTS.dftLength];
		xVel = new double[CONSTANTS.dftLength];
		yVel = new double[CONSTANTS.dftLength];
		zVel = new double[CONSTANTS.dftLength];
		totalVel = new double[CONSTANTS.dftLength];
		dTime = new double[CONSTANTS.dftLength];
		fxR = new double[CONSTANTS.dftLength];
		fyR = new double[CONSTANTS.dftLength];
		fzR = new double[CONSTANTS.dftLength];
		ftR = new double[CONSTANTS.dftLength];
		xAcc = new double[CONSTANTS.dftLength];					//x acceleration
		yAcc= new double[CONSTANTS.dftLength];					//y acceleration			
		zAcc= new double[CONSTANTS.dftLength];					//z acceleration
		tAcc= new double[CONSTANTS.dftLength];					//total acceleration
		
		
	}
	

/* METHODS */
	
	/** Returns the proximity between this object and the object passed
	 * 
	 * @param obj - The object to test the proximity to
	 * @return a representation in meters of how close the object is to this object
	 */
	public double proximity(objectData obj){
		double ret_val = 0.0;
		
		double other_x = obj.getCurX();
		double other_y = obj.getCurY();
		double other_z = obj.getCurZ();
		
		ret_val = Math.sqrt((xLast-other_x)*(xLast-other_x)+(yLast-other_y)*(yLast-other_y)+(zLast-other_z)*(zLast-other_z));
		return ret_val;
	}
	
	/** Adds a data point into this structure, so long as the point is valid.
	 * 
	 * @param deltaT: time passed
	 * @param x: x value
	 * @param y: y value
	 * @param z: z value
	 * @param objectname: the name of the object this data belongs to
	 */
	public void addData(double deltaT, float x, float y, float z, String objectname){
		
		//Check to see if the data is valid...
		if(this.objectName.equalsIgnoreCase("")){
			this.objectName = objectname;
		}
		else if(!this.objectName.equalsIgnoreCase(objectname)){
			System.out.println("Out of order data in message handler!");
			return;
		}
		
		if(x == 0.0f && y == 0.0f && z == 0.0f)
		{
			skipped = skipped+1;				//mark that we skipped a data point
			dTimeskipped = dTimeskipped+deltaT;	//mark the difference in time due to skipping the data point
			return; //if the data is invalid
		}
		//If the data is good... check to see if we skipped any entries.
		if (skipped != 0) //if blah
		{ /*if we skipped something */  //ififififif
			deltaT = deltaT + dTimeskipped;
			
			skipped = 0;
		}
		xPos[current] = x;
		yPos[current] = y;
		zPos[current] = z;
		dTime[current] = deltaT;
		if (entries == 0){
			xVel[0] = 0;
			yVel[0] = 0;
			zVel[0] = 0;
			totalVel[0] = 0.0;
			xAcc[0] = 0;
			yAcc[0] = 0;
			zAcc[0] = 0;
			tAcc[0] = 0;
		}
		else{
			/* calculate velocities */
			int last_index = current - 1;
			if (current == 0){
				last_index = 499;
			}
			xVel[current] = Math.abs(x - xLast)/deltaT;
			yVel[current] = Math.abs(y - yLast)/deltaT;
			zVel[current] = Math.abs(z - zLast)/deltaT;
			totalVel[current] = (Math.sqrt(Math.pow(xVel[current],2)+Math.pow(yVel[current],2)+Math.pow(zVel[current],2)));
			xAcc[current] = Math.abs(xVel[current] - xVel[last_index])/deltaT;
			yAcc[current] = Math.abs(yVel[current] - yVel[last_index])/deltaT;
			zAcc[current] = Math.abs(zVel[current] - zVel[last_index])/deltaT;         
			tAcc[current] = Math.abs(totalVel[current] - totalVel[last_index])/deltaT;
		}//end calculate velocities
		
		//Update last values
		int max_entries = CONSTANTS.dftLength;
		int last_index = current + 1;
		if (current == 500){
			last_index = 0;
		}
		/* Calculate averages over the amount of data that we hold */
		avgXvel = avgXvel - xVel[last_index]/(double)max_entries + xVel[current]/(double)max_entries;
		avgYvel = avgYvel - yVel[last_index]/(double)max_entries + yVel[current]/(double)max_entries;
		avgZvel = avgZvel - zVel[last_index]/(double)max_entries + zVel[current]/(double)max_entries;
		avgTvel = avgTvel - totalVel[last_index]/(double)max_entries + totalVel[current]/(double)max_entries;
		avgXacc = avgXacc - xAcc[last_index]/(double)max_entries + xAcc[current]/(double)max_entries;
		avgYacc = avgYacc - yAcc[last_index]/(double)max_entries + yAcc[current]/(double)max_entries;
		avgZacc = avgZacc - zAcc[last_index]/(double)max_entries + zAcc[current]/(double)max_entries;
		avgTacc = avgTacc - tAcc[last_index]/(double)max_entries + tAcc[current]/(double)max_entries;
		
		xLast = xPos[current];
		yLast = yPos[current];
		zLast = zPos[current];
		current = (current+1)%CONSTANTS.dftLength;
		entries = entries+1;
		;
	}//end add data point
	
	/**
	 * Calculates dfts for x y z and total velocity data.  Must have collected CONSTANTS.dftmax samples already. 
	 * After calculation, you can call the getter methods to recieve the dft data.
	 * @returns true if it worked, false if it did not
	 */
	public boolean dft(){

		/* Do I care about the x y and z fourier transforms?  or do I just care about the total... */
		/**
		 * Lets take this down a notch and convert it to bytes so we can send it and analyze it...
		 */
		int max = CONSTANTS.dftLength;
		if (entries < max){
			return false;
		}
		//First, lump pieces together.
		
		int i = 0;
		int j = 0;
		int res = CONSTANTS.resolution;
		int size = CONSTANTS.dftLength;
		int bins = size/res;
		/* To calculate values we will do ffts on*/
		float[] xx = new float[bins];
		float[] yy = new float[bins];
		float[] zz = new float[bins];
		float[] tt = new float[bins];
		
		float maxX, maxY, maxZ, maxT;
		maxX = maxY = maxZ = maxT = 0;
		
		for (i = 0; i < bins; i++){
			xx[i] = yy[i] = zz[i] = tt[i] = 0;
			for (j = 0; j< res; j++){
				xx[i] += xVel[(i*res+current+1+j)%size];
				yy[i] += yVel[(i*res+current+1+j)%size];
				zz[i] += zVel[(i*res+current+1+j)%size];
				tt[i] += totalVel[(i*res+current+1+j)%size];
			}//end bin 10 elements
		}//end for each bin
		
		/* Now the data is in the correct spot, meaning the oldest data is in 0, the newest data is in bins*/
		
		int sumrealx = 0, sumrealy = 0, sumrealz = 0, sumtotal = 0;
		
		
		i = 0;		//Loop variable
		j = 0;		//Loop variable
		//compute dfts
		for (i = 0; i < bins; i++){
			sumrealx = 0;
			sumrealy = 0;
			sumrealz = 0;
			sumtotal = 0;
			for (j = 0; j < bins; j++){
				sumrealx +=  xx[j]*Math.cos(2*Math.PI * i * j / bins);
				sumrealy +=  yy[j]*Math.cos(2*Math.PI * i * j / bins);
				sumrealz +=  zz[j]*Math.cos(2*Math.PI * i * j / bins);
				sumtotal +=  tt[j]*Math.cos(2*Math.PI * i * j / bins);
				
			}//end inner for loop
			maxX = Math.max(sumrealx, maxX);
			maxY = Math.max(sumrealy, maxY);
			maxZ = Math.max(sumrealz, maxZ);
			maxT = Math.max(sumtotal, maxT);
			fxR[i] = sumrealx;
			fyR[i] = sumrealy;
			fzR[i] = sumrealz;
			ftR[i] = sumtotal;
		}//end outer for loop
		
		//Normalize ffts to byte sized...
		for (i = 0; i < bins; i++){
			bftx[i] = (byte)((fxR[i]/maxX)*127.0);
			bfty[i] = (byte)((fyR[i]/maxY)*127.0);
			bftz[i] = (byte)((fzR[i]/maxZ)*127.0);
			bftt[i] = (byte)((ftR[i]/maxT)*127.0);
		}
		
		return true;
	}//End fourier
}
