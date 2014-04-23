package com.org.news.ui;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.VolleyError;
import com.android.volley.imagecache.ImageCacheManager;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.baidu.mobstat.StatService;
import com.org.news.htmltextview.HtmlTextView;
import com.org.news.htmltextview.SystemInfoUtils;
import com.org.news.provider.PostProvider;
import com.org.news.ui.ReadPager.ContentFragment;
import com.org.news.ui.ReadPager.MyAdapter;
import com.org.news.view.DepthPageTransformer;

public class ReadPage extends SherlockFragmentActivity{

	private int items;
	private int position;
	private String part_name;
	
	private Cursor cursor = null;
	private String mCurrentTitle;
	
	private String title;
	private String des;
	private String link;
	private int bookmark;
	private TextView tv;
	private TextView dv;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.readpage);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);

		//AppConnect.getInstance(this).initPopAd(this);
		//AppConnect.getInstance(this).showPopAd(this);
		
		Bundle bundle = getIntent().getExtras();
		items = bundle.getInt("count");
		position = bundle.getInt("position");
		part_name = bundle.getString("part");		
		//Log.d(Constants.TAG,"num:"+items+"position:"+position+"title:"+mTitle);

	
		cursor = getContentResolver().query(PostProvider.CONTENT_URI, 
				 new String[]{PostProvider.ID,
							  PostProvider.CONTENT,
							  PostProvider.TITLE,
							  PostProvider.DESCRIPTION,
							  PostProvider.LINK,
							  PostProvider.POSTID,
							  PostProvider.BOOKMARKS}, 
				 PostProvider.PART+"=?",
				 new String[]{part_name},
				 PostProvider.POSTID+" DESC");
		
		cursor.moveToPosition(position);
		title = cursor.getString(cursor.getColumnIndex(PostProvider.TITLE));
		des = cursor.getString(cursor.getColumnIndex(PostProvider.DESCRIPTION));
		link = cursor.getString(cursor.getColumnIndex(PostProvider.LINK));
		bookmark = cursor.getInt(cursor.getColumnIndex(PostProvider.BOOKMARKS));

		tv=(TextView)findViewById(R.id.title);
		dv = (TextView)findViewById(R.id.content);
		dv.setMovementMethod(ScrollingMovementMethod.getInstance());
		//openweb = (Button)v.findViewById(R.id.openweb);
		
		String able= getResources().getConfiguration().locale.getCountry();
		//Log.d(Constants.TAG,able);
		if(able.equals("TW")||able.equals("HK")){
			try {
				JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
				tv.setText(jChineseConvertor.s2t(title));
				dv.setTextSize(Preferences.getTextSize(this));
				//dv.setHtmlFromString(jChineseConvertor.s2t(description));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			tv.setText(title);
			dv.setTextSize(Preferences.getTextSize(this));
			//dv.setHtmlFromString(des);
			//Log.d(Constants.TAG,des);
			//String html="tesdfsdfdf<div><img alt=\"传雅虎将改版Flickr：以照片为中心\" src='http://i3.sinaimg.cn/IT/2014/0317/U10330P2DT20140317115552.jpg' title=\"传雅虎将改版Flickr：以照片为中心\" /><span>传雅虎将改版Flickr：以照片为中心</span></div>  <p>　　新浪科技讯 北京时间3月17日上午消息，据美国科技博客Re/code报道，知情人士透露，<span><a href=http://stock.finance.sina.com.cn/usstock/quotes/YHOO.html target=_blank>雅虎</a></span><span></span>即将对Flickr进行最新一次改版。</p>";

			Spanned text = Html.fromHtml(des, new UrlImageGetter(dv, this), null);
			dv.setText(text);
		}
		

	} 
	private class UrlImageGetter implements Html.ImageGetter {
		private TextView t;
		private Context c;

		public UrlImageGetter(TextView textview, Context context) {
			// TODO Auto-generated constructor stub
			this.t = textview;
			this.c = context;
		}

		@Override
		public Drawable getDrawable(String source) {
	        if (TextUtils.isEmpty(source))
	            return null;

	        // images in reader comments may skip "http:" (no idea why) so make sure to add protocol here
	        if (source.startsWith("//"))
	           source = "http:" + source;

	        // use Photon if a max size is requested (otherwise the full-sized image will be downloaded
	        // and then resized)
	        //if (mMaxSize > 0)
	        //    source = PhotonUtils.getPhotonImageUrl(source, mMaxSize, 0);

	        final RemoteDrawable remote = new RemoteDrawable(c);
	        ImageCacheManager.getInstance().getImage(source, new ImageLoader.ImageListener(){
	            @Override
	            public void onErrorResponse(VolleyError error){
	                remote.displayFailed();
	                if (t != null){
	                	t.invalidate();
	                	
	                }
	                Log.d(com.org.news.ui.Constants.TAG,"onErrorResponse");
	            }
	            @Override
	            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate){
	                if (response.getBitmap() != null) {
	                    // make sure view is still valid

	                    Drawable drawable = new BitmapDrawable(c.getResources(), response.getBitmap());
	                    final int oldHeight = remote.getBounds().height();
	                    int maxWidth = SystemInfoUtils.getDefaultDisplayWidth(c) - t.getPaddingLeft() - t.getPaddingRight();

	                    int mMaxSize=SystemInfoUtils.getDefaultDisplayWidth(c);
	                    if (mMaxSize > 0 && (maxWidth > mMaxSize || maxWidth == 0))
	                        maxWidth = mMaxSize;

	                    remote.setRemoteDrawable(drawable, maxWidth);

	                    //image is from cache? don't need to modify view height
	                    if (isImmediate){
	                       return;
	                    }

	                    int newHeight = remote.getBounds().height();
	                    // For ICS
	                    //重新設置高度 8是行距
	                    t.setHeight(t.getHeight() + newHeight-oldHeight+8);
	                    // Pre ICS
	                    t.setEllipsize(null);
	                    t.invalidate();

	                }
	            }
	        });
	        return remote;
	    }

	    private class RemoteDrawable extends BitmapDrawable {
	        protected Drawable mRemoteDrawable;
	        private boolean mDidFail=false;
	        public RemoteDrawable(Context context){
	        	//mRemoteDrawable = context.getResources().getDrawable(R.drawable.ic_empty);
	        	//setBounds(SystemInfoUtils.getDefaultImageBounds(context));
	        	//setBounds(0, 0, mRemoteDrawable.getIntrinsicWidth(), mRemoteDrawable.getIntrinsicHeight());
	        }
	        public void displayFailed(){
	            mDidFail = true;
	        }
	        public void setBounds(int x, int y, int width, int height){
	            super.setBounds(x, y, width, height);
	            if (mRemoteDrawable != null) {
	                mRemoteDrawable.setBounds(x, y, width, height);
	            }
	        }
	        public void setRemoteDrawable(Drawable remote){
	            mRemoteDrawable = remote;
	            setBounds(0, 0, mRemoteDrawable.getIntrinsicWidth(), mRemoteDrawable.getIntrinsicHeight());
	        }
	        public void setRemoteDrawable(Drawable remote, int maxWidth){
				if (remote == null) {
					// throw error
					return;
				}
				this.mRemoteDrawable = remote;
				// determine if we need to scale the image to fit in view
				int imgWidth = remote.getIntrinsicWidth();
				int imgHeight = remote.getIntrinsicHeight();
				float imgScale = (float) imgWidth / (float) imgHeight;

				float xScale = (float) imgWidth / (float) maxWidth;
				if (xScale > 1.0f) {
					setBounds(0, 0, Math.round(imgWidth / xScale),
							Math.round(imgHeight / xScale));
				} else {
					if(imgScale>1.0f){
						setBounds(0, 0, maxWidth, Math.round(imgHeight * imgScale));
					}else{
						setBounds(0, 0, maxWidth, Math.round(imgHeight / imgScale));
					}
				}

			}
	        public boolean didFail(){
	            return mDidFail;
	        }
	        public void draw(Canvas canvas){
	            if (mRemoteDrawable != null) {
	                mRemoteDrawable.draw(canvas);
	            }
	        }
	    }
		private class UrlDrawable extends BitmapDrawable {
			protected Drawable drawable;

			@Override
			public void draw(Canvas canvas) {
				// TODO Auto-generated method stub
				// super.draw(canvas);
				if (drawable != null) {
					drawable.draw(canvas);
				}
			}

			public void setDrawable(Drawable drawable, int maxWidth) {
				if (drawable == null) {
					// throw error
					return;
				}
				this.drawable = drawable;
				// determine if we need to scale the image to fit in view
				int imgWidth = drawable.getIntrinsicWidth();
				int imgHeight = drawable.getIntrinsicHeight();
				float imgScale = (float) imgWidth / (float) imgHeight;

				float xScale = (float) imgWidth / (float) maxWidth;
				if (xScale > 1.0f) {
					setBounds(0, 0, Math.round(imgWidth / xScale),
							Math.round(imgHeight / xScale));
				} else {
					setBounds(0, 0, maxWidth, Math.round(imgHeight * imgScale));
				}

			}

			@Override
			public void setBounds(int left, int top, int right, int bottom) {
				// TODO Auto-generated method stub
				super.setBounds(left, top, right, bottom);
				if (drawable != null) {
					drawable.setBounds(left, top, right, bottom);
				}
			}

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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//UILApplication.requestQueue.cancelAll(this);
		Log.d(Constants.TAG,"cancelAll");
	}
}
