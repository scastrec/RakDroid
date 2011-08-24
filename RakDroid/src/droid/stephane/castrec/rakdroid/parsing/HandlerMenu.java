package droid.stephane.castrec.rakdroid.parsing;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import droid.stephane.castrec.util.Day;

import android.util.Log;



public class HandlerMenu extends DefaultHandler{
	/*
	 * list of tag
	 */
	final private String TAG_DATE = "date";
	final private String TAG_HOUR = "hour";
	final private String TAG_MEAL = "meal";
	final private String TAG_DESCRIPTION = "description";
	
	
	/*
	 * in tag boolean
	 */
	private Boolean IN_TAG_DATE = false;
	private Boolean IN_TAG_HOUR = false;//lunch , dinner
	private Boolean IN_TAG_MEAL = false;//entree, plat, dessert
	private Boolean IN_TAG_DESCRIPTION = false;
	
	private Boolean LUNCH = false;
	private Boolean DINNER = false;
	private Boolean ENTREE = false;
	private Boolean PLAT = false;
	
	public int _nbDate=0;
	private List<Day> _listDay;
	private Day _tmpDay;
	private Date _Date;
	@Override
	public void startDocument()throws SAXException 
	{	
		Log.d("RakDroid", "HandlerMenu start document ");
		_listDay = new ArrayList<Day>();
	}
	
	@Override	
	public void endDocument()throws SAXException 
	{
		Log.d("RakDroid", "HandlerMenu end document ");		

	}
	@Override	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)throws SAXException 
	{
		try
		{
			//Log.d("RakDroid", "HandlerMenu start element "+localName);
		    if(localName.equalsIgnoreCase(TAG_DATE))
		    {
		    	//Log.d("RakDroid", "date =  "+atts.getValue(0));
				IN_TAG_DATE=true;
				_tmpDay = new Day();
				String lDate = String.copyValueOf(atts.getValue(0).toCharArray(), 0, 10) ;
				String lYear = String.copyValueOf(lDate.toCharArray(), 0, 4);
				String lMonth = String.copyValueOf(lDate.toCharArray(), 5, 2);
				String lDay = String.copyValueOf(lDate.toCharArray(), 8, 2);
				//Log.d("RakDroid", lDay+":"+lMonth+":"+lYear);
				_Date = new Date(Integer.parseInt(lYear)-1900,Integer.parseInt(lMonth)-1,Integer.parseInt(lDay));
				_tmpDay.date = _Date;
				 Log.d("RakDroid", "date enter in list : "+String.valueOf(_Date));
		    }
			else if(localName.equalsIgnoreCase(TAG_HOUR))
			{
				IN_TAG_HOUR=true;
				if(atts.getValue(0).equalsIgnoreCase("lunch"))
					LUNCH=true;
				else 
					DINNER=true;
			}
			else if(localName.equalsIgnoreCase(TAG_MEAL))
			{
				IN_TAG_MEAL=true;
				if (atts.getValue(0).equalsIgnoreCase("entree"))
					ENTREE=true;
				else if (atts.getValue(0).equalsIgnoreCase("plat"))
					PLAT=true;
			}				
			else if(localName.equalsIgnoreCase(TAG_DESCRIPTION))
				IN_TAG_DESCRIPTION=true;
		}
		catch (Exception d)
		{
			Log.e("RakDroid", "HandlerMenu start element error "+ d.getMessage()+" Cause : "+d.getCause());			
		}	    
	}
	
	@Override	
	public void endElement(String namespaceURI, String localName, String  qName)throws SAXException 
	{
		try
		{
			if(localName.equalsIgnoreCase(TAG_DATE))
			{
				_listDay.add(_tmpDay);
				IN_TAG_DATE=false;
			}
			else if(localName.equalsIgnoreCase(TAG_HOUR))
			{
				IN_TAG_HOUR=false;
				LUNCH=false;
				DINNER=false;
			}
			else if(localName.equalsIgnoreCase(TAG_MEAL))
			{
				IN_TAG_MEAL=false;
				ENTREE=false;
				PLAT=false;
			}
			else if(localName.equalsIgnoreCase(TAG_DESCRIPTION))
				IN_TAG_DESCRIPTION=false;
		}
		catch (Exception e) {
			Log.e("RakDroid", "HandlerMenu end element error "+" "+localName+" "+ e.getMessage()+" Cause : "+e.getCause());
		}
	}	
	
	@Override	
	public void characters(char ch[], int start, int length) 
	{	
		try
		{
			String text = new String(ch, start, length);
			if(IN_TAG_DESCRIPTION)
			{
				//Log.d("RakDroid", "HandlerMenu characters "+text);	
				if(LUNCH)
				{
					if(ENTREE)
					{
						Log.d("RakDroid", "HandlerMenu characters Lunch entree"+text);
						_tmpDay.lunch_entrees.add(text);
					}
					else if(PLAT)
					{
						Log.d("RakDroid", "HandlerMenu characters Lunch plats"+text);
						_tmpDay.lunch_plats.add(text);
					}
				}
				else
				{
					if(ENTREE)
					{
						Log.d("RakDroid", "HandlerMenu characters dinner entree"+text);
						_tmpDay.dinner_entrees.add(text);
					}
					else if(PLAT)
					{
						Log.d("RakDroid", "HandlerMenu characters dinner plats"+text);
						_tmpDay.dinner_plats.add(text);
					}
				}
			}
		}
		catch (Exception e) {
			Log.e("RakDroid", "HandlerMenu characters error "+ e.getMessage());
		}
	}	
	
	public List<Day> getMenus()
	{
		return _listDay;
	}

}
