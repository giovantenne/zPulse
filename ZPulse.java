import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.media.*;

//import com.siemens.mp.game.*;
import java.util.*;

public class ZPulse extends MIDlet implements CommandListener
{
	private Display display;	   
	private Form fmScelta,fmPrimo,fmInfo, fmBeep,fmMetr;    
	private Command cmZBeep,cmStop,cmZPulse,cmInfo,cmBack1,cmPulse,cmExit,cmPlay;  
	private TextField txtBeep;
	private StringItem sItem;   
	private boolean metr=false; 
	private byte i;
	private long [] pulse=new long [8];
	private long intervallo,ris;
	Timer timer;
	//private MyTask p=new MyTask();
	public void pauseApp()
	{ 
	}
	public void destroyApp(boolean unconditional)
	{ 
	}
	public void startApp()
	{
		
		i=0;
		display = Display.getDisplay(this);
	    	cmExit = new Command("Exit", Command.SCREEN, 8);
	    	cmBack1 = new Command("Back", Command.SCREEN, 7);
	    	cmPulse = new Command("Pulse", Command.STOP, 6);     
	    	cmZBeep = new Command("Click", Command.SCREEN, 1);     
	    	cmZPulse = new Command("Tap", Command.SCREEN, 2);     
	    	cmInfo = new Command("Info", Command.SCREEN, 3);     
	    	cmStop = new Command("Stop", Command.SCREEN, 4);     
	    	cmPlay = new Command("Play", Command.SCREEN, 5);     

	
		fmScelta = new Form("Claudio Benvenuti");
	 		//fmScelta.addCommand(cmExit);
	    	fmScelta.addCommand(cmZPulse);
	    	fmScelta.addCommand(cmZBeep);
	    	fmScelta.addCommand(cmInfo);
	       	fmScelta.setCommandListener(this);
			
	       	try{
	       		Image imm = Image.createImage("ZPulse.png");
	           	fmScelta.append(new ImageItem("",imm,ImageItem.LAYOUT_CENTER, "Image Cannot be shown"));       		
	       		
	        }
	        catch(java.io.IOException err) {
	            //fmScelta.append("Failed in creating the image test!");
	        	fmScelta.append(err.getMessage());
	        }	
     	
		display.setCurrent(fmScelta);
	}
	public void commandAction(Command c, Displayable s)
	{
		if (c == cmBack1)
		{
			display.setCurrent(fmScelta);
		}
		
		if (c == cmZPulse)
		{
			fmPrimo = new Form("Tap");
	 		//fmPrimo.addCommand(cmExit);
			fmPrimo.addCommand(cmPulse );
			fmPrimo.addCommand(cmBack1);
	 	 	fmPrimo.setCommandListener(this);
			display.setCurrent(fmPrimo);
		}
		
		if (c == cmZBeep)
		{
			fmBeep = new Form("Click");
	 		//fmBeep.addCommand(cmExit);
	 		fmBeep.addCommand(cmBack1);
	 		fmBeep.addCommand(cmPlay);
	    		txtBeep = new TextField("Value","60",3,TextField.NUMERIC);
	    		fmBeep.append(txtBeep);
	    		fmBeep.setCommandListener(this);
			display.setCurrent(fmBeep);
		}

		if (c == cmPlay)
		{
			fmMetr = new Form("Metronome");
			fmMetr.addCommand(cmStop);
			//fmMetr.addCommand(cmExit);
			sItem = new StringItem(null,txtBeep.getString()+" BPM"); 
			fmMetr.append(sItem);
	       		fmMetr.setCommandListener(this);
			timer =new Timer();
	       		TimerTask task  =new MyTask(fmMetr, display);			
			timer.scheduleAtFixedRate(task,0,(60000/Integer.parseInt(txtBeep.getString()))  );			//display.setCurrent(fmMetr);
			display.setCurrent(fmMetr);
		}
		if (c == cmStop)
		{
			timer.cancel();
			display.setCurrent(fmScelta);
			//Light.setLightOn();
 		}
 		if (c == cmInfo)
		{
	 		fmInfo = new Form("INFO");
	 		fmInfo.addCommand(cmBack1);
	 		//fmInfo.addCommand(cmExit);
	   		fmInfo.setCommandListener(this);
			sItem = new StringItem(null,"Midlet written by Claudio Benvenuti (info@claudiobenvenuti.it)"); 
			fmInfo.append(sItem);
			display.setCurrent(fmInfo);
		}

		if (c == cmExit)
		{
    		destroyApp(false);
    		notifyDestroyed();
		}
		else if (c == cmPulse)
		{
			sItem = new StringItem(null,"|");
			fmPrimo.append(sItem);
			pulse[i]=System.currentTimeMillis();
			i++;
			display.flashBacklight(200);
			//Sound.playTone(440, 100);
			try{
				Manager.playTone(100,100,100);
			}catch (Exception e){
				fmPrimo.append("No sound");
			}
			if (i==8)
			{
				for(byte y=0;y<7;y++)
					intervallo+=pulse[y+1]-pulse[y];
				intervallo=intervallo/7;
				sItem = new StringItem(null,"\n");
				fmPrimo.append(sItem);
				ris=(60000/intervallo);
				if(ris<100){
					sItem = new StringItem(null, "0"+ String.valueOf(ris)+"    ");
				}else{
					sItem = new StringItem(null, String.valueOf(ris)+"    ");
				}
				i=0;
				intervallo=0;
				fmPrimo.append(sItem);
			}
		}
	}	    
}

class MyTask extends TimerTask  {
        Form formBeep;
        Display md;
	int i=0;
	public MyTask(Form fr, Display d)
	{
		formBeep=fr;
		md=d;
	}
	public void run() {
		if(i%2==0){
			
			//Light.setLightOn();
		}else{
			//Light.setLightOff();
		}			
		md.flashBacklight(200);
		//Sound.playTone(440, 100);
		try{
			Manager.playTone(100,100,100);
		}catch (Exception e){
			formBeep.append("No sound");
		}
		//Sound.playTone(440, 100);

	}
}


