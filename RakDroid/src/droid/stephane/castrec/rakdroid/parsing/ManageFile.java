package droid.stephane.castrec.rakdroid.parsing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.ByteArrayBuffer;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import droid.stephane.castrec.util.Day;

import android.R.string;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class ManageFile {

	private final String PATH = "/data/data/android.stephane.castrec.RakDroid/";
	final public String pathServer = "http://rak.resel.fr/xml.php";
	static public List<Day> _ListDays;
	
	private SAXParserFactory _spf;
	private SAXParser _sp;
	private XMLReader _xr;
	private HandlerMenu _HandlerMenu;
	
	public void downloadFromUrl()
	{
		try
		{
			if(sdCardAvailable()==2)
			{
				Log.d("RakDroid", getClass().toString()+" downloadFromUrl starting DL");
				URL url = new URL(pathServer);
				File file = new File("RakDroid");
				//long startTime = System.currentTimeMillis();
				URLConnection ucon = url.openConnection();
				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				
				
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
				
				/* Convert the Bytes read to a String. */
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
			}
			
			
		}catch (Exception e) {
			Log.e("RakDroid", "ManageFile downloadFromUrl error "+e.getMessage());
		}
	}
	
	/**
	 * getListDay
	 * parse the local xml file and create the list of Days
	 * 
	 */
	public List<Day> getListDay()
	{
		try
		{
			Log.d("RakDroid", "create Request");
			_spf = SAXParserFactory.newInstance();
			_sp = _spf.newSAXParser();
			_xr = _sp.getXMLReader();

			//_Url = new URL(pathServer);
			_HandlerMenu = new HandlerMenu();
			_xr.setContentHandler(_HandlerMenu);
			InputSource lIS = new InputSource(PATH);
			_xr.parse(lIS);
			List<Day> list = _HandlerMenu.getMenus();
			Log.d("RakDroid", getClass().toString()+" getListDay nbr day ="+list.size());
			_ListDays=list;
			return list;
		}
		catch (Exception e) {
			Log.e("RakDroid", getClass().toString()+" getListDay "+e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * sdCardAvailable
	 * @return int :
	 * 				0 = no read/write
	 * 				1 = only read;
	 * 				2 = read and write
	 */
	private int sdCardAvailable()
	{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		    return 2;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		    return 1;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		    return 0;
		}
	}
}
