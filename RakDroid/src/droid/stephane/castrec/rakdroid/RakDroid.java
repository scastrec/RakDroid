/***************************************************************************************/
/*
 * @Author Stéphane Castrec
 * @Date 2010
 * @AppName RakDroid
 * @Subject Povide an Android app which displays Rak Meals 
 * 
 */
/***************************************************************************************/
package droid.stephane.castrec.rakdroid;


import java.util.Date;
import java.util.List;

import droid.stephane.castrec.rakdroid.parsing.ManageFile;
import droid.stephane.castrec.rakdroid.parsing.RequestMenu;
import droid.stephane.castrec.util.Day;
import droid.stephane.castrec.util.FileMgt;
import droid.stephane.castrec.util.Util;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import droid.stephane.castrec.rakdroid.R;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class RakDroid extends Activity implements OnTouchListener, android.view.View.OnClickListener{
	
	/**
	 * Manage touchScreen
	 */

	final int ACTION_SWITCH = 0;
	final int ACTION_LEFT_TO_RIGHT = 1;
	final int ACTION_RIGHT_TO_LEFT = 2;
	float _XTouchDown;
	float _XTouchUp;
	
	/**
	 * Components
	 */

	private ViewFlipper _Flipper;
	View leftView, rightView;
	//private ListView _listView;
	private TextView _txtDate;
	private TextView _txtEntree;
	private TextView _txtPlat;
	//private View mRemoteView;
	private int _IndexCurrentViewDisplay;
	private Button _BtnSwitcher;
	private Button _BtnArg;
	
	//private Day _MenuOfDay;
	private List<Day> _ListMenus;
	private Date _CurrentDateDisplay;
	private int _CurrentIndexListDisplay;
	private Boolean _Lunch = false; //if not Lunch => Dinner
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main); 
        
        Log.d("RakDroid", "starting ... ");
        try
        {	        
        	main();//next of the app
        }
        catch (Exception e) {
			Log.e("RakDroid", "onCreate error "+e.getMessage());
		}
    }
    
    private void main()
    {
    	try
    	{
    		Log.d("RakDroid", "main starting");
    		_CurrentDateDisplay = new Date();
    		_ListMenus = RequestMenu._Menus;
    		//_ListMenus = ManageFile._ListDays;
	        Log.d("RakDroid", "Date "+_ListMenus.get(0).date);
        	//display infos
        	setFlipper();//and view
	        setView(-1);//setView for the current day
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "main error "+e.getMessage());
    		finish();
    	}
    }
    
    /**
     * setFlipper
     * init Flipper
     */
    private void setFlipper()
    {
    	try
    	{
    		Log.d("RakDroid", "setFlipper " + String.valueOf(_Lunch));
	    	if(_Lunch)
	    	{//set dinner
	    		setContentView(R.layout.dinner);
	    		_Lunch = false;	    		
	    		 _Flipper = (ViewFlipper)findViewById(R.id.FlipperDinner);
	    		 _Flipper.setOnTouchListener(this);
	    		 setViewDinnerComponents(0);
	    	}
	    	else
	    	{//set Lunch
	    		setContentView(R.layout.lunch);
	    		_Lunch = true;
	    		 _Flipper = (ViewFlipper)findViewById(R.id.FlipperLunch);
	 	        _Flipper.setOnTouchListener(this);
	 	        setViewLunchComponents(0);
	    	}
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "setFlipper error "+e.getMessage());
		}
    }
    
    /**
     * 
     * @param pView
     */
    private void setViewLunchComponents(int pView)
    {
    	try
    	{
    		_IndexCurrentViewDisplay=pView;
    		Log.d("RakDroid", "setViewDInnerComponents number " + String.valueOf(pView));
    		switch (pView)
    		{
    			//set Lunch Components
	    		case 0:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateLunch0);
		 	        _txtEntree = (TextView)findViewById(R.id.TxtEntreeLunch0);
		 	        _txtPlat = (TextView)findViewById(R.id.TxtPlatLunch0);
		 	       //_listView = (ListView)findViewById(R.id.ListPlatLunch0);
		 	        _BtnSwitcher = (Button)findViewById(R.id.btnGoDinner0);
		 	       _BtnArg = (Button)findViewById(R.id.btnLArg0);
		 	       break;
	    		case 1:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateLunch1);
		 	        _txtEntree = (TextView)findViewById(R.id.TxtEntreeLunch1);
		 	        _txtPlat = (TextView)findViewById(R.id.TxtPlatLunch1);
		 	      _BtnSwitcher = (Button)findViewById(R.id.btnGoDinner1);
		 	     _BtnArg = (Button)findViewById(R.id.btnLArg1);
		 	       break;
	    		case 2:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateLunch2);
		 	        _txtEntree = (TextView)findViewById(R.id.TxtEntreeLunch2);
		 	        _txtPlat = (TextView)findViewById(R.id.TxtPlatLunch2);
		 	      _BtnSwitcher = (Button)findViewById(R.id.btnGoDinner2);
		 	     _BtnArg = (Button)findViewById(R.id.btnLArg2);
		 	       break;		 	       
    		}
	    	_BtnSwitcher.setOnClickListener(this);
	    	_BtnArg.setOnClickListener(this);
	    	//_txtDate.setTextColor(color.black);

    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "setViewLunchComponents error " +e.getMessage());		}
    }
    
    /**
     * 
     * @param pView
     */
    private void setViewDinnerComponents(int pView)
    {
    	try
    	{
    		_IndexCurrentViewDisplay=pView;
    		Log.d("RakDroid", "setViewLunchComponents number " + String.valueOf(pView));
    		switch (pView)
    		{
    			//set Dinner Components
	    		case 0:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateDinner0);	    			
	    			_txtEntree = (TextView)findViewById(R.id.TxtEntreeDinner0);
		    		 _txtPlat = (TextView)findViewById(R.id.TxtPlatDinner0);
		    		 _BtnSwitcher = (Button)findViewById(R.id.btnGoLunch0);
		    		 _BtnArg = (Button)findViewById(R.id.btnDArg0);
		 	       break;
	    		case 1:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateDinner1);
	    			_txtEntree = (TextView)findViewById(R.id.TxtEntreeDinner1);
		    		 _txtPlat = (TextView)findViewById(R.id.TxtPlatDinner1);
		    		 _BtnSwitcher = (Button)findViewById(R.id.btnGoLunch1);
		    		 _BtnArg = (Button)findViewById(R.id.btnDArg1);
		 	       break;
	    		case 2:
	    			_txtDate = (TextView)findViewById(R.id.TxtDateDinner2);
	    			_txtEntree = (TextView)findViewById(R.id.TxtEntreeDinner2);
		    		 _txtPlat = (TextView)findViewById(R.id.TxtPlatDinner2);
		    		 _BtnSwitcher = (Button)findViewById(R.id.btnGoLunch2);
		    		 _BtnArg = (Button)findViewById(R.id.btnDArg2);
		 	       break;		 	       
    		}
	    	_BtnSwitcher.setOnClickListener(this);
	    	_BtnArg.setOnClickListener(this);
	    	//_txtDate.setTextColor(color.black);
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "setViewDinnerComponents error " +e.getMessage());
    	}
    }
    
    /**
     * 
     * @param pView : number of the view to init
     * 0 :first view of the flipper
     * 1 : second view of the flipper
     * 2 : third view of the flipper
     */
    private void setViewComponents(int pView)
    {
    	try
    	{    	
    		Log.d("RakDroid", "setViewComponents "+pView);
	    	if(_Lunch)
	    	{//set lunch
	    		setViewLunchComponents(pView);
	    	}
	    	else
	    	{//set Dinner
	    		setViewDinnerComponents(pView);
	    	}
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "setViewComponents error "+e.getMessage());
		}    	
    }
    
    /**
     * setView
     * @param pIndex : new index ; if pIndex=-1 never 
     * calculate the current day index
     */
    private void setView(int pIndex)
    {
    	try
    	{
    		String meal = "";
    		Log.d("RakDroid", "setView start");
    		if(pIndex==-1)
    		{
	    		Log.d("RakDroid", "setView pIndex = -1");
	    		_CurrentIndexListDisplay = Util.getDayIndex(_ListMenus,_CurrentDateDisplay);
    		}
    		else
    		{
    			_CurrentIndexListDisplay=pIndex;
    		}

    		Day lday = _ListMenus.get(_CurrentIndexListDisplay);
    		//create date
    		_CurrentDateDisplay=lday.date;
    		
    		Log.d("RakDroid", "SetView date getDay "+_CurrentDateDisplay.getDay());
    		String ldayName = Util.getDayName(_CurrentDateDisplay.getDay());
    		String ldate = String.valueOf(lday.date);
    		String lMonth = String.copyValueOf(ldate.toCharArray(), 4, 3);
    		String locday = String.copyValueOf(ldate.toCharArray(), 8,2);
    		//String lYear = String.copyValueOf(ldate.toCharArray(), 29, 5);
    		if(ldayName!=null)
    			_txtDate.setText(ldayName+"\n"+ locday  );
    		else
    			_txtDate.setText(locday + " \n" + lMonth  );
    		
    		Log.d("RakDroid", "setView textDate = "+_txtDate.getText());

    		if(_Lunch)
    		{
        		Log.d("RakDroid", "setView Lunch");
    			int lsize= lday.lunch_entrees.size();
        		//_txtEntree.setLines(lsize);
        		Log.d("RakDroid", lday.toString());
        		meal = "";
        		for(int i=0; i<lsize; i++)
        		{
        			Log.d("RakDroid", "setView : add plats "+lday.lunch_entrees.get(i));
        			if(lday.lunch_entrees.get(i).length()>32)
        			{
        				//too big to display all the line
        				Log.d("RakDroid", "setView : too many characters : "+lday.lunch_entrees.get(i).length());
        				String tmp = (String) lday.lunch_entrees.get(i).subSequence(0, 30);
        				tmp +="...";
        				meal +=tmp;
        			}
        			else
        				meal += lday.lunch_entrees.get(i);
        			meal += "\n"; 
        		}
        		_txtEntree.setText(meal);
        		
        		lsize= lday.lunch_plats.size();
        		//_txtPlat.setLines(lsize);
        		
        		meal = "";
        		for(int i=0; i<lsize; i++)
        		{
        			Log.d("RakDroid", "setView : add plats "+lday.lunch_plats.get(i));
        			if(lday.lunch_plats.get(i).length()>32)
        			{
        				//too big to display all the line
        				Log.d("RakDroid", "setView : too many characters : "+lday.lunch_plats.get(i).length());
        				String tmp = (String) lday.lunch_plats.get(i).subSequence(0, 30);
        				tmp +="...";
        				meal +=tmp;
        			}
        			else
        				meal += lday.lunch_plats.get(i);
        			meal += "\n";      			
        		} 
        		//String lmeal = meal.replace("-", "\n ");
        		_txtPlat.setText(meal);   
    		}
    		else
    		{
    			int lsize= lday.dinner_entrees.size();
        		//_txtEntree.setLines(lsize);
        		Log.d("RakDroid", lday.toString());
        		meal="";
        		for(int i=0; i<lsize; i++)
        		{
        			Log.d("RakDroid", "setView : add plats "+lday.dinner_entrees.get(i));
        			if(lday.dinner_entrees.get(i).length()>32)
        			{
        				//too big to display all the line
        				Log.d("RakDroid", "setView : too many characters : "+lday.dinner_entrees.get(i).length());
        				String tmp = (String) lday.dinner_entrees.get(i).subSequence(0, 30);
        				tmp +="...";
        				meal +=tmp;
        			}
        			else
        				meal += lday.dinner_entrees.get(i);
        			meal += "\n"; 
        			
        		}
        		_txtEntree.setText(meal);
        		
        		lsize= lday.dinner_plats.size();
        		//_txtPlat.setLines(lsize);
        		meal = "";
        		for(int i=0; i<lsize; i++)
        		{
        			Log.d("RakDroid", "setView : add plats "+lday.dinner_plats.get(i));
        			if(lday.dinner_plats.get(i).length()>32)
        			{
        				//too big to display all the line
        				Log.d("RakDroid", "setView : too many characters : "+lday.dinner_plats.get(i).length());
        				String tmp = (String) lday.dinner_plats.get(i).subSequence(0, 30);
        				tmp +="...";
        				meal +=tmp;
        			}
        			else
        				meal += lday.dinner_plats.get(i);
        			meal += "\n"; 
        		} 
        		_txtPlat.setText(meal);
    		}    		
    	}
    	catch (Exception e) {
			Log.e("RakDroid", "setView error"+e.getMessage());
		}    
    }
    
    /**
     * execute Action
     * @param pAction
     */
    private void executeAction(int pAction)
    {
    	try
    	{
    		Log.d("RakDroid", "executeAction start "+ String.valueOf(pAction));
    		String lCurrentMeal = null;
    		int indexViewToDisplay;
    		switch(pAction)
    		{
    			/*case ACTION_SWITCH : //switch to dinner/Lunch
    				setFlipper();
    				setView(-1);
    				break;*/
    			case ACTION_LEFT_TO_RIGHT :    				
    				_Flipper.setInAnimation(inFromLeftAnimation());
    		        _Flipper.setOutAnimation(outToRightAnimation());
    		        if(_CurrentIndexListDisplay-1>=0)
    		        {
    		        	setViewComponents(Util.indexViewToDisplay(_IndexCurrentViewDisplay, false));
    		        	Log.d("RakDroid", "ExecuteACtion show previous at index "+ _CurrentIndexListDisplay);
    		        	setView(_CurrentIndexListDisplay-1);
    		        	_Flipper.showPrevious();
    		        }
    		        else
    		        {
    		        	Log.d("RakDroid", "ExecuteACtion impossible show previous at index "+ _CurrentIndexListDisplay);
    		        	// TO DO : animation can't go more on this side
    		        }
    				break;
    			case ACTION_RIGHT_TO_LEFT :
    				_Flipper.setInAnimation( inFromRightAnimation());
    		        _Flipper.setOutAnimation(outToLeftAnimation());
    				if(_CurrentIndexListDisplay<_ListMenus.size()-1)
    		        {
        				setViewComponents(Util.indexViewToDisplay(_IndexCurrentViewDisplay, true));
    					Log.d("RakDroid", "show next at index "+ _CurrentIndexListDisplay);
        				setView(_CurrentIndexListDisplay+1);
        				_Flipper.showNext();
    		        }
    		        else
    		        {
    		        	// TO DO : animation can't go more on this side
    		        }

    				break;
    		}
    	}
    	catch (Exception e)
    	{
    		Log.e("RakDroid", "RakDroid DisplayInfo error "+e.getMessage());
    	}
    }
	
	@Override
	public void onClick(View v) {
			
		Log.d("RakDroid", "onTouch  touch");
		if(v.equals(_BtnSwitcher))
		{//switch view
			Log.d("RakDroid", "onTouch BtnSwitcher touch");
			setFlipper();
			setView(-1);
			//executeAction(ACTION_SWITCH);
		}
		else if(v.equals(_BtnArg))
		{
			Log.d("RakDroid", "onTouch BtnSwitcher touch");
			//start the uri to display a website 
			String url = "https://services.ard.fr/fr/espaces-clients/etablissements/enst.html";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
	
		
		switch(arg1.getAction())
		{
			case MotionEvent.ACTION_DOWN :
				_XTouchDown = arg1.getX();
				Log.d("RakDroid", "Touch down : X="+_XTouchDown );
				break;
			case MotionEvent.ACTION_UP :				
				_XTouchUp = arg1.getX();
				Log.d("RakDroid", "Touch Up : X="+_XTouchUp );
				//vertical action
				if(_XTouchUp-_XTouchDown>50)
					executeAction(ACTION_LEFT_TO_RIGHT);
				else if(_XTouchDown-_XTouchUp>50)
					executeAction(ACTION_RIGHT_TO_LEFT);

				break;
			case MotionEvent.ACTION_MOVE :
				break;		
		}
		return true;
	}

    /********************** Animation method **********************/
    private Animation inFromRightAnimation() {
    	Animation inFromRight = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT, +1.0f,
    	Animation.RELATIVE_TO_PARENT, 0.0f,
    	Animation.RELATIVE_TO_PARENT, 0.0f,
    	Animation.RELATIVE_TO_PARENT, 0.0f);
    	inFromRight.setDuration(500);
    	inFromRight.setInterpolator(new AccelerateInterpolator());
    	return inFromRight;
    	}

    private Animation outToLeftAnimation() {
    Animation outtoLeft = new TranslateAnimation(
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, -1.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoLeft.setDuration(500);
    outtoLeft.setInterpolator(new AccelerateInterpolator());
    return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
    Animation inFromLeft = new TranslateAnimation(
    Animation.RELATIVE_TO_PARENT, -1.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f);
    inFromLeft.setDuration(500);
    inFromLeft.setInterpolator(new AccelerateInterpolator());
    return inFromLeft;
    }

    private Animation outToRightAnimation() {
    Animation outtoRight = new TranslateAnimation(
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, +1.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f,
    Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoRight.setDuration(500);
    outtoRight.setInterpolator(new AccelerateInterpolator());
    return outtoRight;
    }
}