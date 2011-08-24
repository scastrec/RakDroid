package droid.stephane.castrec.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Environment;
import android.util.Log;

public class FileMgt {
	
	final private static  String _Url = "http://resel.fr/services/rak/xml.php";//"http://rak.resel.fr/xml.php"; "http://resel.fr:8000/services/rak/xml.php";//
	final private static String _FilePath = "/RakDroid/";
	final private static String _FileName = "meal.xml";
	
	/**
	 * download file into the sdcard 
	 */
	static public void downloadFile()
	{
		try
		{
			Log.d("RakDroid", "FileMgt downloadFile starting");
			Log.d("RakDroid", "FileMgt downloadFile as :"+Environment.getExternalStorageDirectory()+_FilePath+_FileName);
			
			File file = new File(Environment.getExternalStorageDirectory()+_FilePath);
			Log.d("RakDroid", "FileMgt downloadFile file created");
			if(!file.mkdirs())
				Log.d("RakDroid", "FileMgt downloadFile dir already exist or can't create");
			file = new File(Environment.getExternalStorageDirectory()+_FilePath+_FileName);
			if(!file.createNewFile())
				Log.d("RakDroid", "downloadFile : File already exist");
			if(!file.canWrite())
				Log.d("RakDroid", "downloadFile : can't write");
			Log.d("RakDroid", "FileMgt downloadFile createNewFile");
			
			URL url = new URL(_Url);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			/*
			* Read bytes to the Buffer until there is nothing more to read(-1).
			*/
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
		catch (Exception e) {
			Log.e("RakDroid", "FileMgt DownloadFile error "+e.getMessage());
		}	
	}
	
	
	/**
	 * Check if the RakDroid file exist on sdCard 
	 * @return true if exist
	 * 	else return false 
	 */
	static public Boolean isFileExist()
	{
		try
		{
			Log.d("RakDroid", "FileMgt isFileExist starting");
			File f = new File(Environment.getExternalStorageDirectory() + _FilePath);
			if(f.exists())
				return true;
			else return false;
		}
		catch (Exception e) {
			Log.e("RakDroid", "FileMgt isFileExist error :"+e.getMessage());
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if the file is correct
	 * else : false if download is needed
	 */
	static public Boolean updateFileNotNeed()
	{
		try
		{
			//TO DO
			File f = new File(Environment.getExternalStorageDirectory() + _FilePath);
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			return false;
	}
	
	/**
	 * canReadFileOnSdCard
	 * @return true if can read file
	 */
	static public Boolean canReadFileOnSdCard()
	{
		try
		{
			File f = new File(Environment.getExternalStorageDirectory() + _FilePath);
			if(f.canRead())
				return true;
		}
		catch (Exception e) {
			Log.e("RakDroid", "FileMgt canReadFileOnSdCard error :"+e.getMessage());
		}
		return false;
	}
	
	
	/**
	 * canWriteFile
	 * @return true if can write file
	 * else return false
	 */
	static public Boolean canWriteFile()
	{
		try
		{
			File f = new File(Environment.getExternalStorageDirectory() + _FilePath);
			if(f.canWrite())
				return true;
		}
		catch (Exception e) {
			Log.e("RakDroid", "FileMgt canWriteFile error :"+e.getMessage());
			}
		return false;
	}
}
