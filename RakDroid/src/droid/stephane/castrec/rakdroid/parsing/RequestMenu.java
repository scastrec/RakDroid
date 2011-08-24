package droid.stephane.castrec.rakdroid.parsing;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import droid.stephane.castrec.util.Day;

import android.os.Environment;
import android.util.Log;

public class RequestMenu {

	final public String pathServer = "http://resel.fr:8000/services/rak/xml.php";//"http://rak.resel.fr/xml.php";//"http://resel.fr/services/rak/xml.php"
	private String _pathFile;
	private SAXParserFactory _spf;
	private SAXParser _sp;
	private XMLReader _xr;
	private HandlerMenu _HandlerMenu;
	
	
	static public List<Day> _Menus;
	
	public RequestMenu() throws Exception
	{
		Log.d("RakDroid", "create Request");
		_spf = SAXParserFactory.newInstance();
		_sp = _spf.newSAXParser();
		_xr = _sp.getXMLReader();
		_pathFile = Environment.getExternalStorageDirectory()+"/RakDroid/meal.xml";
	}
	
	
	/**
	 * 
	 * @return -1 if fail
	 */
	/*public int makeRequest()
	{
		try
		{
			String path;

			//_Url = new URL(pathServer);
			_HandlerMenu = new HandlerMenu();
			_xr.setContentHandler(_HandlerMenu);
			InputSource lIS = new InputSource(pathServer);
			_xr.parse(lIS);
			_Menus = _HandlerMenu.getMenus();
			adaptList();//be carreful 
		}
		catch (Exception e)
		{
			Log.e("RakDroid", "makeRequest initRequest exception : "+e.getMessage()+" "+e.getCause());
			return -1;
		}	
		return 1;
	}*/
	
	/**
	 * 
	 * @return -1 if fail
	 */
	public int makeRequest()
	{
		try
		{
			String path;

			//_Url = new URL(pathServer);
			_HandlerMenu = new HandlerMenu();
			_xr.setContentHandler(_HandlerMenu);
			InputSource lIS = new InputSource(_pathFile);
			
			_xr.parse("file://"+_pathFile); 
			_Menus = _HandlerMenu.getMenus();
			adaptList();//be carreful 
		}
		catch (Exception e)
		{
			Log.e("RakDroid", "makeRequest initRequest exception : "+e.getMessage()+" "+e.getCause());
			return -1;
		}	
		return 1;
	}

	public List<Day> getMenus()
	{
		return _Menus;
	}
	
	/**
	 * reduce list by suppr old date
	 */
	public void adaptList()
	{
		try
		{
			List<Day> lMenus = new ArrayList<Day>();
			Date lDate = new Date();
			int indexCurrentDate=0;
			int i=0;
			/*for (i=0; i<_Menus.size(); i++)
			{
				if(!_Menus.get(i).date.before(lDate))
				{
					indexCurrentDate = i;
					Log.d("RakDroid", "adaptList suppr "+_Menus.get(i).date);
				}
			}*/
			for (i=0; i<_Menus.size(); i++)
			{
				if(!_Menus.get(i).dinner_plats.isEmpty())
				{
					lMenus.add(_Menus.get(i));
					Log.d("RakDroid", "adaptList add "+_Menus.get(i).date);
				}
			}
			_Menus = lMenus;
		}
		catch (Exception e) {
			Log.e("RakDroid", "adaptList error "+e.getMessage());
		}
	}
	
	/**
     * getDayIndex
     * @param pDate to find
     * @return index of the list
     */
    public int getDayIndex(Date pDate)
    {
    	try
    	{
    		int i =0;
    		Log.d("RakDroid", "getDayIndex find :"+pDate.toString());   		
    			
    		for (i=0; i<_Menus.size(); i++)
    		{
    			Date lDate = _Menus.get(i).date;
    			if(lDate.getYear() == pDate.getYear() && 
    				lDate.getDate() == pDate.getDate() && 
    				lDate.getMonth()== pDate.getMonth())
    			{
    				Log.d("RakDroid", "RequestMenus getDayIndex index find : "+String.valueOf(i));
    				return i;    				
    			}
    		}
    	}
    	catch (Exception e) {
    		Log.e("RakDroid", "getIndex error : "+e.getMessage()+" "+e.getCause());
		}
    	Log.d("RakDroid", "getDayIndex NO index find ");
    	return -1;
    }
}
