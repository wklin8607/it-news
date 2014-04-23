package com.org.news.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.org.news.provider.PostProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.KeyEvent;
import android.widget.Toast;

public class Preferences extends SherlockPreferenceActivity implements OnPreferenceClickListener,OnPreferenceChangeListener,OnSharedPreferenceChangeListener{

	private ListPreference lp_ts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(getString(R.string.setting));
		lp_ts = (ListPreference)findPreference("text_size");
		lp_ts.setSummary(lp_ts.getEntry());
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		
		Preference mClearCache = (Preference)findPreference("clear_cache");
		mClearCache.setOnPreferenceClickListener(new CCOnPreferenceClickListener());
	}
	
	private class CCOnPreferenceClickListener implements OnPreferenceClickListener{

		@Override
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
			new AsyncTask<Void, Void, Void>() {
				ProgressDialog mProgressDialog;
				protected void onPreExecute() {
					mProgressDialog = new ProgressDialog(Preferences.this);
					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mProgressDialog.setCancelable(false);
					mProgressDialog.setMessage("Loading...");
					mProgressDialog.show();
				};
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					//imageLoader.clearDiscCache();
					//imageLoader.clearMemoryCache();
					getContentResolver().delete(PostProvider.CONTENT_URI, PostProvider.BOOKMARKS+"=?", new String[]{String.valueOf(0)});
					return null;
				}
				protected void onPostExecute(Void result) {
					mProgressDialog.dismiss();
					Toast.makeText(getApplicationContext(), getString(R.string.clearfinish), Toast.LENGTH_SHORT).show();
				};
			}.execute();
			return true;
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO 自动生成的方法存根
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		if(key.equals("text_size")){
			lp_ts.setSummary(lp_ts.getEntry());
		}
	}
	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static int getTextSize(Context context) {
		String result = PreferenceManager.getDefaultSharedPreferences(context).getString("text_size", "15");
		return Integer.parseInt(result);
	}

}
