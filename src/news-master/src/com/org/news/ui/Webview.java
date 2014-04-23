package com.org.news.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class Webview extends BaseActivity {

	private WebView webview;
	private ProgressBar pb;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);

		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("url");

		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new MWebViewClient());

		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		webview.loadUrl(url);
		// webview.setWebViewClient(new HelloWebViewClient());
	}

	private class MWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
