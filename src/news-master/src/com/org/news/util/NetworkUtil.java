package com.org.news.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
	
	public static boolean getNetworkInfo(Context context){
		ConnectivityManager con=(ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);  
		return con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting(); 
	}
	 

}
