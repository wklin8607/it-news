package com.org.news.ui;


import com.org.news.provider.PostProvider;
import com.org.news.util.DateUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class Splash extends Activity{
	private TextView mVersion;
	
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.splash);
		
		mVersion = (TextView)findViewById(R.id.version);
		mVersion.setText(getTitle()+" for android V"+getAppVersionName(this));
		new AsyncTask<Integer, Integer, Boolean>() {

			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				Splash.this.finish();
			}

			protected Boolean doInBackground(Integer... params) {
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//delect7DayAgoData();
				
				return false;
			}

			protected void onPostExecute(Boolean result) {
				// Toast.makeText(Splashing.this, getString(R.string.finded),
				// Toast.LENGTH_LONG).show();
				startActivity(new Intent(Splash.this,Main.class));
				super.onPostExecute(result);
			}
		}.execute(0);

	}
	

	/**
	 * 删除七天前数�?	 */
    private void delect7DayAgoData(){

		try {
			Cursor cursor = getContentResolver().query(PostProvider.CONTENT_URI,
					new String[]{PostProvider.ID,PostProvider.DATECREATED}, 
					null, null, null);
			if(cursor.moveToFirst()){
				do{
					int id = cursor.getInt(cursor.getColumnIndex(PostProvider.ID));
					String date = cursor.getString(cursor.getColumnIndex(PostProvider.DATECREATED));
					String newDate = DateUtil.getStringDateShort();
					
					Log.d(Constants.TAG,"date:"+date+" newDate:"+newDate+" twoday:"+DateUtil.getTwoDay(newDate, date));
					
					int interval = Integer.valueOf(DateUtil.getTwoDay(newDate, date));
					if(interval>5){
						getContentResolver().delete(PostProvider.CONTENT_URI, 
								PostProvider.ID+" =? "+ " and "+
								PostProvider.BOOKMARKS+" =? ", 
								new String[]{String.valueOf(id),String.valueOf(0)});
					}
				}while(cursor.moveToNext());
			}
			cursor.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**   
    * 返回当前程序版本�?  
    */    
    public static String getAppVersionName(Context context) {    
        String versionName = "";    
        try {    
            // ---get the package info---    
            PackageManager pm = context.getPackageManager();    
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);    
            versionName = pi.versionName;    
            if (versionName == null || versionName.length() <= 0) {    
                return "";    
            }    
        } catch (Exception e) {    
            Log.e("VersionInfo", "Exception", e);    
        }    
        return versionName;    
    }  
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
