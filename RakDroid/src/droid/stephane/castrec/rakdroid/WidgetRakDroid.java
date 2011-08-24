package droid.stephane.castrec.rakdroid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.client.RoutedRequest;

import droid.stephane.castrec.rakdroid.parsing.RequestMenu;
import droid.stephane.castrec.util.Day;
import droid.stephane.castrec.util.Util;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import droid.stephane.castrec.rakdroid.R;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

public class WidgetRakDroid extends AppWidgetProvider  {

	private Context _Context;
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager,
        int[] appWidgetIds) {
		 _Context = context;
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }    

	 /**
	  * Service class
	  */
	 public static class UpdateService extends Service {

		 public void onStart(Intent pIntent, int pStartId)
		 {
			 try
			 {
				 Log.d("RakDroid", "WidgetRakDroid UpdateService onStart");

				 RemoteViews lUpdateView = buildUpdate(this);
				 //set onClick
				 Intent defineIntent = new Intent(this, SplashScreen.class );
				 PendingIntent pendingIntent = PendingIntent.getActivity(this,
	                        0 /* no requestCode */, defineIntent, 0 /* no flags */);
				 lUpdateView.setOnClickPendingIntent(R.id.widgetlayout, pendingIntent);
				 
		            // Push update for this widget to the home screen
				 ComponentName thisWidget = new ComponentName(this, WidgetRakDroid.class);
		         AppWidgetManager manager = AppWidgetManager.getInstance(this);
		         manager.updateAppWidget(thisWidget, lUpdateView); 
			 }
			 catch (Exception e) {
				 Log.e("RakDroid", "UpdateService onStart error "+e.getMessage());
			 }
		 }
		 
		 
		 /**
		  * build a widget update to show the meal 
		  * Block until the online Api returns
		  * @param pContext
		  * @return
		  */
		 private RemoteViews buildUpdate(Context pContext)
		 {
			 try
			 {
				 Log.d("RakDroid", "WidgetRakDroid buildUpdate start");
				 RequestMenu lReqMenu = new RequestMenu();
				 String lMessage="\n";
				 Date lDate = new Date();
				 String sDate ="";
				 RemoteViews lUpdateView = new RemoteViews(pContext.getPackageName(),R.layout.widgetlayout);
				 
				 if(lReqMenu.makeRequest()!=-1)
				 {
					 int lDayIndex = lReqMenu.getDayIndex(lDate);
					 if(lDayIndex==-1)//not found
					 {
						 sDate = Util.getDayName(lDate.getDay());
						 lMessage = "\n\nImpossible de trouver les informations sur le serveur!";
					 }
					 else
					 {
						 //date present in the file
						 Log.d("RakDroid", "Widget UpdateService buildUpdate Request ok hour "+lDate.getHours()+" lDayindex:"+lDayIndex+" menu size:"+lReqMenu._Menus.size());
						 
						 List <String> lMealList = new ArrayList<String>();
						 String ltmp ;
						 if(lDate.getHours()<13)
						 {
							 Log.d("RakDroid", "WidgetRakDroid update hour<13");
							 sDate = Util.getDayName(lReqMenu._Menus.get(lDayIndex).date.getDay());
							 sDate += " midi";
							 lMealList = lReqMenu._Menus.get(lDayIndex).lunch_plats;
							 ltmp = lReqMenu._Menus.get(lDayIndex).date.toGMTString();
						 }
						 else if(lDate.getHours()>20 && lDayIndex<lReqMenu._Menus.size())
						 {
							 sDate = Util.getDayName(lReqMenu._Menus.get(lDayIndex+1).date.getDay());
							 sDate += " midi";
							 lMealList = lReqMenu._Menus.get(lDayIndex+1).lunch_plats;
							 ltmp = lReqMenu._Menus.get(lDayIndex+1).date.toGMTString();
						 }
						 else 
						 {	 
							 Log.d("RakDroid", "WidgetRakDroid update soir");
							 sDate = Util.getDayName(lReqMenu._Menus.get(lDayIndex).date.getDay());//getDayName(lReqMenu._Menus.get(lDayIndex).date.getDay());
							 sDate += " soir";
							 lMealList = lReqMenu._Menus.get(lDayIndex).dinner_plats;
							 ltmp = lReqMenu._Menus.get(lDayIndex).date.toGMTString();
						 }
						 
						 for (int i=0; i<lMealList.size(); i++ )
						 {
							 if(i==4 && lMealList.size()>5)
							 {//not enough space in the widget
								 lMessage +="...";
								 break;
							 }
							 else
								 lMessage +=lMealList.get(i)+"\n";
						 }
					 }
					 lUpdateView.setTextViewText(R.id.message, lMessage);
					 lUpdateView.setTextViewText(R.id.widgetDate, sDate);					 
				 }
				 else
				 {
					 //No Update
					 Log.d("RakDroid", "Widget UpdateService buildUpdate Request NOK");
					 lMessage = "Erreur du chargement";
					 sDate = "";					 
				 }
				 Log.d("RakDroid", "Widget UpdateService buildUpdate setText "+lMessage);
				 
				 
				 return lUpdateView;
			 }
			 catch (Exception e) {
				 Log.e("RakDroid", "UpdateService buildUpdate error "+e.getMessage());
			 }
			 return null;
		 }
		 
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}	
	 }
}
