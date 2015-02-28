/**
 * All information to create music should go inside of the test_data method inside of the messagehandler class.
 * 
 */

//import java.io.*;
import java.net.*;
//import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfugue.*;

public class ViconGestureMapping
{    
    static LinkedBlockingQueue<String> msgBuf = new LinkedBlockingQueue<String>();
    static volatile boolean keepRunning = true;
    static Thread handlerThread, OnExit;
    static DatagramSocket serverSocket;
    static DatagramSocket sendToProc;
    public static JFrame frame;
    
   /**
    * 
    * @param args - No args
    * @throws Exception - No exceptions... well... it may crash, who knows!? :D
    */
   public static void main(String args[]) throws Exception
      {
	     serverSocket = new DatagramSocket(CONSTANTS.port);
         System.out.println("Listening on "+CONSTANTS.port + "\nArgs: ");
	     System.out.println(args.length);
         for (int i = 0; i < args.length; i++){
	    	 System.out.println(i+" :"+args[i]);
	     }
	     
         mainLoop(args);
         
	     
	     CONSTANTS.DebugPrint("Keeprunning = false...", 1);
	     
	     
         
         
       }//end public main

   /**
    * Continuously recieves messages and pops them into the queue to be read
    */
   private static void mainLoop(String args[]){
	   
	   if (args.length < 3){
		   System.out.println("Useage: ViconGestureMapping <Script Name> <number_of_objects_to_track>");
		   System.out.println("Example:\nViconGestureMapping script.txt 4");
		   return;
	   }
	   
	   CONSTANTS.set_objects(Integer.parseInt(args[2]));
	   
	   byte[] receiveData = new byte[1024];
       //frame = new JFrame("Data");		//Window to display data
       
       MessageHandler handler = new MessageHandler(args[1]); 
       handlerThread = new Thread(handler);
       handlerThread.start();
       exitThread exitT = new exitThread();
       OnExit = new Thread(exitT);
       Runtime.getRuntime().addShutdownHook(OnExit);
       
       
	   
	   while(ViconGestureMapping.keepRunning)
       {
 
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          try{
			serverSocket.receive(receivePacket);
          } 
          catch (IOException e) {
			
			e.printStackTrace();
          }
          
          String sentence = new String( receivePacket.getData());
          //System.out.println("RECEIVED+OFFERING: " + sentence);
          
          ViconGestureMapping.msgBuf.offer(sentence);
          

       }//end while keep running
	   
	   
   }//end mainloop function

}//end class

class exitThread implements Runnable {
	
	public void run() {
		System.out.println("Exiting");
		ViconGestureMapping.serverSocket.close();
		ViconGestureMapping.keepRunning = false;
		
	}//end run exitThread
}//end class exitThread

