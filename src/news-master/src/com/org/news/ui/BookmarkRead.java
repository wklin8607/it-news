package com.org.news.ui;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mobstat.StatService;
import com.org.news.htmltextview.HtmlTextView;
import com.org.news.provider.PostProvider;
import com.org.news.view.DepthPageTransformer;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookmarkRead extends SherlockFragmentActivity {

	private ViewPager mPager;
	private MyAdapter mAdapter;
	private int items;
	private int position;
	//private String part_name;
	
	private Cursor cursor = null;
	private String mCurrentTitle;
	
	private String title;
	private String des;
	//private String link;
	private int bookmark;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.viewpager);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);

		Bundle bundle = getIntent().getExtras();
		items = bundle.getInt("count");
		position = bundle.getInt("position");
		//part_name = bundle.getString("part");		
		//Log.d(Constants.TAG,"num:"+items+"position:"+position+"title:"+mTitle);
		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(position);

	
		cursor = getContentResolver().query(PostProvider.CONTENT_URI, 
				 new String[]{PostProvider.ID,
							  PostProvider.TITLE,
							  PostProvider.DESCRIPTION,
							  PostProvider.POSTID,
							  PostProvider.BOOKMARKS}, 
				 PostProvider.BOOKMARKS+"=?",
				 new String[]{String.valueOf(1)},
				 PostProvider.POSTID+" DESC");
		
		cursor.moveToPosition(mPager.getCurrentItem());
		title = cursor.getString(cursor.getColumnIndex(PostProvider.TITLE));
		des = cursor.getString(cursor.getColumnIndex(PostProvider.DESCRIPTION));
		//link = cursor.getString(cursor.getColumnIndex(PostProvider.LINK));
		bookmark = cursor.getInt(cursor.getColumnIndex(PostProvider.BOOKMARKS));

		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
				
				cursor.moveToPosition(mPager.getCurrentItem());
				title = cursor.getString(cursor.getColumnIndex(PostProvider.TITLE));
				des = cursor.getString(cursor.getColumnIndex(PostProvider.DESCRIPTION));
				//link = cursor.getString(cursor.getColumnIndex(PostProvider.LINK));
				bookmark = cursor.getInt(cursor.getColumnIndex(PostProvider.BOOKMARKS));

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

	} 

	public  class MyAdapter extends FragmentStatePagerAdapter {
		
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public int getCount() {
			return items;
		}

		@Override
		public Fragment getItem(int position) {
			cursor.moveToPosition(position);
			mCurrentTitle = cursor.getString(cursor.getColumnIndex(PostProvider.TITLE));
			String descrition = cursor.getString(cursor.getColumnIndex(PostProvider.DESCRIPTION));
			//String link = cursor.getString(cursor.getColumnIndex(PostProvider.LINK));
			return ContentFragment.newInstance(mCurrentTitle,descrition,"");
		}
	}
	
	public static class ContentFragment extends Fragment {
		private String title="";
		private String description="";
		//private String link = "";
		
		private TextView tv;
		private HtmlTextView dv;
		//private Button openweb;

		
		static ContentFragment newInstance(String title,String description,String link) {
			ContentFragment f = new ContentFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putString("title", title);
			args.putString("description",description);
			//args.putString("link",link);

			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			title = getArguments() != null ? getArguments().getString("title"):getString(R.string.app_name);
			description = getArguments() != null ? getArguments().getString("description"):"";
			//link = getArguments() != null ? getArguments().getString("link"):"";

		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.content_fragment, container,
					false);
			tv=(TextView)v.findViewById(R.id.title);
			dv = (HtmlTextView)v.findViewById(R.id.content);
			//openweb = (Button)v.findViewById(R.id.openweb);
			
			String able= getResources().getConfiguration().locale.getCountry();
			//Log.d(Constants.TAG,able);
			if(able.equals("TW")||able.equals("HK")){
				try {
					JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
					tv.setText(jChineseConvertor.s2t(title));
					dv.setTextSize(Preferences.getTextSize(getActivity()));
					dv.setHtmlFromString(jChineseConvertor.s2t(description));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				tv.setText(title);
				dv.setTextSize(Preferences.getTextSize(getActivity()));
				dv.setHtmlFromString(description);
			}
			return v;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(cursor!=null){
			cursor.close();
			cursor = null;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		cursor = getContentResolver().query(PostProvider.CONTENT_URI, 
				 new String[]{PostProvider.ID,
							  PostProvider.CONTENT,
							  PostProvider.TITLE,
							  PostProvider.DESCRIPTION,
							  PostProvider.LINK,
							  PostProvider.BOOKMARKS}, 
							  PostProvider.BOOKMARKS+"=?",
				new String[]{String.valueOf(1)},
							  PostProvider.POSTID+" DESC");
		cursor.moveToPosition(mPager.getCurrentItem());
		title = cursor.getString(cursor.getColumnIndex(PostProvider.TITLE));
		des = cursor.getString(cursor.getColumnIndex(PostProvider.DESCRIPTION));
		//link = cursor.getString(cursor.getColumnIndex(PostProvider.LINK));
		bookmark = cursor.getInt(cursor.getColumnIndex(PostProvider.BOOKMARKS));

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
	        return true;
		case 0:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,title);
			sendIntent.putExtra(Intent.EXTRA_TEXT, des);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		case 1:
			ContentValues values = new ContentValues();
			if(bookmark==1){
				values.put(PostProvider.BOOKMARKS, 0);
				item.setIcon(R.drawable.btn_bookmark_before);
			}else{
				values.put(PostProvider.BOOKMARKS, 1);
				item.setIcon(R.drawable.btn_bookmark_after);
			}
			getContentResolver().update(PostProvider.CONTENT_URI, values, PostProvider.TITLE+"=?", new String[]{title});

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		menu.add(0, 0, 0, "share")
			 .setIcon(R.drawable.ic_action_share)
			 .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		if(bookmark==0){
			menu.add(0,1,0,"bookmarks")
				.setIcon(R.drawable.btn_bookmark_before)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}else{
			menu.add(0,1,0,"bookmarks")
			.setIcon(R.drawable.btn_bookmark_after)
			.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		}
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
}
