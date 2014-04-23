package com.org.news.htmltextview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Display;

public class SystemInfoUtils {
	public static Rect getDefaultImageBounds(Context context) {
	    Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
	    int width = display.getWidth();
	    int height = (int) (width * 9 / 16);
	                
	    Rect bounds = new Rect(0, 0, width, height);
	    return bounds;
	}
	public static int getDefaultDisplayWidth(Context context){
	    Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
	    return display.getWidth();
	}
	
	/**
	 * Ó‹ËãÆÁÄ»éLŒ’±È
	 * @param context
	 * @return
	 */
	public static float getDisplayScale(Context context){
	    Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
	    int width = display.getWidth();
	    int height = display.getHeight();
	    return (float)width/(float)height;
	}
}
