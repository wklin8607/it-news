package com.org.news.util;

import com.org.news.ui.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtil {
	
	  /**
     * Àı∑≈Õº∆¨
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {  
        int width = drawable.getIntrinsicWidth();  
        int height = drawable.getIntrinsicHeight();  
        Bitmap oldbmp = drawableToBitmap(drawable);  
        Matrix matrix = new Matrix();  
        float scaleWidth = ((float) w / width);  
        float scaleHeight = ((float) h / height);  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,  
                matrix, true);  
        return new BitmapDrawable(null, newbmp);  
    }
    
    public static float getScale(Drawable drawable,int w){
    	float scale;
    	scale = (float)w/drawable.getMinimumWidth();
    	if(scale<1){
    		return 1;
    	}
    	return scale;
    }
    public static Drawable zoomDrawable(Drawable drawable, int w) throws Exception {  
        int width = drawable.getIntrinsicWidth();  
        int height = drawable.getIntrinsicHeight();  
        if(width<0||height<0){
        	return null;
        }
        Bitmap oldbmp = drawableToBitmap(drawable); 
        if(oldbmp==null){
        	return null;
        }
        Matrix matrix = new Matrix();  
        float scaleWidth = ((float) w / width); 

        //Log.d(Constants.TAG,"width:"+width+" height:"+height+" scale:"+scaleWidth+" w:"+w);
        matrix.postScale(scaleWidth, scaleWidth);  
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,  matrix, true);  
        if(!oldbmp.isRecycled()){
        	oldbmp.recycle();
        	oldbmp = null;
        }
        Drawable result = new BitmapDrawable(newbmp);

        return result;  
    } 
    private static Bitmap drawableToBitmap(Drawable drawable) {  
        int width = drawable.getIntrinsicWidth();  
        int height = drawable.getIntrinsicHeight();  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        if(width<0||height<0){
        	return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, width, height);  
        drawable.draw(canvas);  
        return bitmap;  
    } 
}
