/*
 * Copyright (C) 2013 Antarix Tandon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.org.news.htmltextview;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.org.news.ui.Constants;
import com.org.news.ui.R;
import com.org.news.util.ImageUtil;

public class UrlImageGetter implements ImageGetter {
	Context context;
	TextView textView;

	public UrlImageGetter(Context context, TextView textView) {
		this.context = context;
		this.textView = textView;
	}

	@Override
	public Drawable getDrawable(String paramString) {
		final URLDrawable urlDrawable = new URLDrawable(context);

		ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
		getterTask.execute(paramString);
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable drawable) {
			this.urlDrawable = drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			if (result != null) {
				urlDrawable.drawable = result;

				UrlImageGetter.this.textView.requestLayout();
				UrlImageGetter.this.textView.invalidate();
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		public Drawable fetchDrawable(String url) {
			try {
				InputStream is = fetch(url);

				Rect bounds = SystemInfoUtils.getDefaultImageBounds(context);
				Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
				Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg,
						bounds.right, bounds.bottom, true);

				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				drawable.setBounds(bounds);

				return drawable;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		private InputStream fetch(String url) throws ClientProtocolException,
				IOException {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);

			HttpResponse response = client.execute(request);
			return response.getEntity().getContent();
		}
	}

	public class URLDrawable extends BitmapDrawable {
		protected Drawable drawable;

		public URLDrawable(Context context) {
			this.setBounds(SystemInfoUtils.getDefaultImageBounds(context));

			drawable = context.getResources().getDrawable(R.drawable.ic_empty);
			drawable.setBounds(SystemInfoUtils.getDefaultImageBounds(context));
		}

		@Override
		public void draw(Canvas canvas) {
			Log.d("test", "this=" + this.getBounds());
			if (drawable != null) {
				Log.d("test", "draw=" + drawable.getBounds());
				drawable.draw(canvas);
			}
		}

	}
}