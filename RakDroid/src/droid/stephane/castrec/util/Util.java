package droid.stephane.castrec.util;

import java.util.Date;
import java.util.List;


import android.util.Log;

public class Util {

	/**
     * 
     * @param pNumber 0 to 6
     * 0=dimanche
     * 6=Samedi
     * @return nom du jour
     */
    static public String getDayName(int pNumber)
    {
    	switch (pNumber) {
			case 0: return "Dimanche";
			case 1: return "Lundi";	
			case 2: return "Mardi";	
			case 3: return "Mercredi";	
			case 4: return "Jeudi";	
			case 5: return "Vendredi";	
			case 6: return "Samedi";	
			default:return null;
		}
    }
    
     /**
     * getDayIndex
     * @param pDate to find
     * @return index of the list
     */
    static public int getDayIndex(List<Day> pList, Date pDate)
    {
    	try
    	{
    		int i =0;
    		Log.d("RakDroid", "getDayIndex find :"+pDate.toString());   		
    			
    		for (i=0; i<pList.size(); i++)
    		{
    			Date lDate = pList.get(i).date;
    			if(lDate.getYear() == pDate.getYear() && 
    				lDate.getDate() == pDate.getDate() && 
    				lDate.getMonth()== pDate.getMonth())
    			{
    				Log.d("RakDroid", "getDayIndex index found : "+String.valueOf(i));
    				return i;    				
    			}
    		}
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "getIndex error : "+e.getMessage()+" "+e.getCause());
		}
    	Log.d("RakDroid", "getDayIndex NO index found ");
    	return -1;
    }
    
     /**
     * 
     * @param pCurrentIndex index of the current view 0 to 2
     * @param pNext true if date +1; false either
     * @return index : the next index View to display 
     */
    static public int indexViewToDisplay(int pCurrentIndex, boolean pNext)
    {
    	try
    	{
    		switch(pCurrentIndex)
    		{
    			case 0:
    				if (pNext)return 1;
    				else return 2;
    			case 1:
    				if (pNext)return 2;
    				else return 0;
    			case 2:
    				if (pNext)return 0;
    				else return 1;
    		}
    		return -1;
    	}
    	catch (Exception e) {
			Log.d("RakDroid", "indexViewToDisplay error "+e.getMessage());
		}
    	return -1;
    } 
    
	/**
	 * getDay
	 * find a meal in the list by date
	 * @param pDate
	 * @return day : contains day which contains lunch, dinner.
	 */
    static public Day getDay(List<Day> plist,Date pDate)
    {
    	try
    	{
    		for (int i=0; i<plist.size(); i++)
    		{
    			if(((Day)(plist.get(i))).date.equals(pDate))
    				return plist.get(i);
    		}
    	}
    	catch (Exception e) 
    	{
    		Log.e("RakDroid", "getDay error : "+e.getMessage());
    	}
    	return null;    	
    }    
 

}
