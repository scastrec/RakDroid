package droid.stephane.castrec.rakdroid;

import java.util.Date;
import java.util.List;

import droid.stephane.castrec.rakdroid.parsing.ManageFile;
import droid.stephane.castrec.rakdroid.parsing.RequestMenu;
import droid.stephane.castrec.util.Day;
import droid.stephane.castrec.util.FileMgt;
import droid.stephane.castrec.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import droid.stephane.castrec.rakdroid.R;
import android.util.Log;
import android.widget.ImageView;


public class SplashScreen extends Activity {

	protected int _splashTime = 5000; // time to display the splash screen in ms
	private Handler _Handler = new Handler() {
	    public void handleMessage(Message msg) {
	    	Log.d("RakDroid", "SplashScreen handleMessage");
        	createAlert("Erreur", "Problème sur le serveur. Veuillez ré-essayer ultérieurement!");
	        }
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
        Log.d("RakDroid", "SplashScreen starting");

        request();
        /*
        if(!isConnected() && FileMgt.isFileExist())
        {	//no data cnx
        	//createAlert("Erreur", "Pas de connexion internet trouvée");
        	request();        	
        }  
        else 
        {
		    request();
		    	//createAlert("Error", "Probème de connection internet");;
        }*/
	}

	
    
    /**
     * Internet is okay we download file and use it
     * @return
     */
    private void request()
    {
    	// thread for displaying the SplashScreen
	    Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {	            	
	                //sleep(Time);
	            	//FileMgt.downloadFile();
	            	FileMgt.downloadFile();
	                try {
	        			Thread.sleep(1000);
	        		} catch (InterruptedException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} 
	            	RequestMenu request = new RequestMenu();
	    	        if(request.makeRequest()!=-1)
	    	        {
	    	        	//test if we have the correct day :
	    	        	if(Util.getDayIndex(request.getMenus(), new Date())==-1)
	    	        	{
	    	        		Looper.prepare();
	    	        		Handler mHandler = new Handler();
	    	        		mHandler.post(new Runnable(){

	                            public void run(){
	                            	Log.d("RakDroid", "SplashScreen ");
	    	    	        		createAlert("Erreur", "Impossible de trouver les informations sur le serveur!");
	                            }
	                            });
	    	        		Looper.loop();
	    	        		return;
	    	        	}
	    		        //List<Day> lListMenus = request.getMenus();
	    		        Intent rakDroidIntent = new Intent(SplashScreen.this,RakDroid.class);
		                startActivity(new Intent(rakDroidIntent));
		                finish();
		                stop();	    
	    	        }
	    	        else
	    	        {
		                Log.d("RakDroid", "SplashScreen makeRequest Error");
	    	        	//Looper.prepare();
		                _Handler.sendEmptyMessage(0); 
	    	        	//finish();
		                //stop();	
	    	        }
	                Log.d("RakDroid", "SplashScreen request finish");
	                
	            } catch(Exception e) {
	                Log.e("RakDroid", "SplashScreen thread error"+e.getMessage());
	                Looper.prepare();
    	        	createAlert("Erreur", "Problème sur le serveur. Veuillez ré-essayer ultérieurement!");
    	        	finish();
	                stop();	
	            }
	        }
	    };
	    splashTread.start();
    }
    
    private void createAlert(String pTitle, String pMessage)
    {
    	try
    	{
    		AlertDialog lAlert = new AlertDialog.Builder(this).create();
        	lAlert.setTitle("Erreur");
        	lAlert.setMessage(pMessage);
        	lAlert.setButton("Quitter", new OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					return;
				}
			});
        	Log.d("RakDroid", pMessage);
        	lAlert.show();
        	return ;
    	}
    	catch(Exception e)
    	{
    		Log.e("RakDroid", "createAlert error "+e.getMessage());
    	}
    }
    
	private Boolean isConnected()
	{
		Log.d("RakDroid", "isConnected");
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(RakDroid.CONNECTIVITY_SERVICE);
		if(connManager.getNetworkInfo(0).getState() == State.CONNECTED)
			return true;
		else
			return false;
	}

}
