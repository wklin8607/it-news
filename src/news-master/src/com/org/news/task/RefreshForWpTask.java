package com.org.news.task;

import java.net.MalformedURLException;
import java.util.List;

import com.org.news.actionbarpulltorefresh.PullToRefreshLayout;
import com.org.news.provider.PostProvider;
import com.org.news.ui.Constants;
import com.org.news.util.CursorUtil;
import com.org.news.util.DateUtil;
import com.org.news.util.NetworkUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class RefreshForWpTask extends AsyncTask<Void, Void, Void> {
	private final static int PULL_DOWN = 0;
	private final static int PULL_UP = 1;

	PullToRefreshLayout mPullToRefreshLayout;
	Context context;
	String url;
	int type;
	String part;
	//Wordpress wp;
	private int MAX_NUM = 20;
	
	public RefreshForWpTask(Context context,PullToRefreshLayout mPullToRefreshLayout,String url,int type,String part) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mPullToRefreshLayout = mPullToRefreshLayout;
		this.url = url;
		this.type = type;
		this.part = part;
	}
	@Override
	protected Void doInBackground(Void... params) {
		return null;
		
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(mPullToRefreshLayout!=null)
			mPullToRefreshLayout.setRefreshComplete();
	}
}
