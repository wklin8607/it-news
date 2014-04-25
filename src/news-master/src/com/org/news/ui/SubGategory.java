package com.org.news.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.imagecache.ImageCacheManager;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.org.news.provider.FeedProvider;
import com.org.news.view.MyGridView;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SubGategory extends BaseActivity implements LoaderCallbacks<Cursor>{
	private SimpleCursorAdapter mSimpleCursorAdapter;
	private MyGridView mGV;
	private String GATEGORY;
	private String TITLE;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.sub_gategory);
		
		Bundle bundle = getIntent().getExtras();
		GATEGORY = bundle.getString("gategroy");
		TITLE = bundle.getString("title");
		
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setTitle(TITLE);
		
		init();
		
		mGV = (MyGridView)findViewById(R.id.sub_grid);

		mSimpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.sub_gategory_item, null,
				new String[]{FeedProvider.LOGO,
							 FeedProvider.TITLE,
							 FeedProvider.DESCRIPTION,
							 FeedProvider.SUBSCRIPTION}, 
				new int[]{R.id.logo,
						  R.id.title,
						  R.id.description,
						  R.id.add}, 0);
		mSimpleCursorAdapter.setViewBinder(new ListBinder());

		mGV.setAdapter(mSimpleCursorAdapter);
		getSupportLoaderManager().initLoader(0, null, this);
	}
	private class ListBinder implements ViewBinder{

		@Override
		public boolean setViewValue(View view, Cursor cursor, int position) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.logo:
				NetworkImageView logo = (NetworkImageView)view;
				String url = cursor.getString(cursor.getColumnIndex(FeedProvider.LOGO));
				logo.setImageUrl(url, ImageCacheManager.getInstance().getImageLoader());
				return true;
			case R.id.add:
				ImageButton add = (ImageButton) view;
				final int type = cursor.getInt(cursor
						.getColumnIndex(FeedProvider.SUBSCRIPTION));
				final int id = cursor.getInt(cursor
						.getColumnIndex(FeedProvider.ID));
				switch (type) {
				case 0:
					add.setImageResource(R.drawable.btn_add_on_card_before);
					break;
				case 1:
					add.setImageResource(R.drawable.btn_add_on_card_after);
					break;
				default:
					break;
				}
				add.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ContentValues values = new ContentValues();

						switch (type) {
						case 0:
							values.put(FeedProvider.SUBSCRIPTION, 1);
							break;
						case 1:
							values.put(FeedProvider.SUBSCRIPTION, 0);
							break;
						default:
							break;
						}
						getContentResolver().update(
								FeedProvider.CONTENT_URI, values,
								FeedProvider.ID + "=?",
								new String[] { String.valueOf(id) });
					}
				});
				return true;
			default:
				break;
			}
			return false;
		}
		
	}

	private void init(){
		Cursor cursor = getContentResolver().query(FeedProvider.CONTENT_URI, 
					   new String[]{FeedProvider.TITLE,
								    FeedProvider.GATEGORY}, 
					   FeedProvider.GATEGORY+"=?", 
					   new String[]{GATEGORY}, null);
		if(cursor.getCount()!=0){
			cursor.close();
			return;
		}
		cursor.close();
		
		if(GATEGORY.equals("information")){
			for(int i=0;i<Constants.INFORMATION_TITLE.length;i++){
				ContentValues values = new ContentValues();
				values.put(FeedProvider.TITLE, Constants.INFORMATION_TITLE[i]);
				values.put(FeedProvider.DESCRIPTION, Constants.INFORMATION_DESCRIPTION[i]);
				values.put(FeedProvider.LOGO, Constants.INFORMATION_LOGO[i]);
				values.put(FeedProvider.LINK, Constants.INFORMATION_LINK[i]);
				values.put(FeedProvider.SUBSCRIPTION, "0");
				values.put(FeedProvider.GATEGORY, GATEGORY);
				getContentResolver().insert(FeedProvider.CONTENT_URI, values);
				Log.d(Constants.TAG,"insert ok");
			}
		}
	}
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		CursorLoader cursorLoader = new CursorLoader(this, 
				FeedProvider.CONTENT_URI, 
				new String[]{FeedProvider.ID,
							 FeedProvider.TITLE,
							 FeedProvider.DESCRIPTION,
							 FeedProvider.LINK,
							 FeedProvider.LOGO,
							 FeedProvider.SUBSCRIPTION,
							 FeedProvider.GATEGORY
							}, 
				FeedProvider.GATEGORY+"=?",
				new String[]{GATEGORY},
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		mSimpleCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mSimpleCursorAdapter.swapCursor(null);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
