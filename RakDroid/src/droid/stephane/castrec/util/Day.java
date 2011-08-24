package droid.stephane.castrec.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Day {
	public Date date;

	public List<String> lunch_entrees;
	public List<String> lunch_plats;
	
	public List<String> dinner_entrees;
	public List<String> dinner_plats;
	
	public Day()
	{
		lunch_entrees=new ArrayList<String>();
		lunch_plats=new ArrayList<String>();
		dinner_entrees=new ArrayList<String>();
		dinner_plats=new ArrayList<String>();
	}
	
	public String toString()
	{
		String lDayString;
		lDayString = "date: "+date.toGMTString();
		lDayString += " nbr lunch entree: "+lunch_entrees.size();
		lDayString += " nbr lunch plats: "+lunch_plats.size();
		lDayString += " nbr dinner entree: "+dinner_entrees.size();
		lDayString += " nbr dinner plats: "+dinner_plats.size();
		return lDayString;
	}
}
