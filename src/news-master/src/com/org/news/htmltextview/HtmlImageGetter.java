package com.org.news.htmltextview;
import java.lang.ref.WeakReference;

import com.android.volley.VolleyError;
import com.android.volley.imagecache.ImageCacheManager;
import com.android.volley.toolbox.ImageLoader;
import com.org.news.ui.UILApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HtmlImageGetter implements ImageGetter {
    private WeakReference<TextView> mWeakView;
    private int mMaxSize;
    private Context context;
    public HtmlImageGetter(TextView view ,Context context) {
		// TODO Auto-generated constructor stub
    	this(view, 0);
    	this.context = context;
	}
    public HtmlImageGetter(TextView view) {
        this(view, 0);
    }

    public HtmlImageGetter(TextView view, int maxSize) {
        mWeakView = new WeakReference<TextView>(view);
        mMaxSize = maxSize;
    }

    private TextView getView() {
        return mWeakView.get();
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

        TextView view = getView();
        final RemoteDrawable remote = new RemoteDrawable(context);

      
        
        ImageCacheManager.getInstance().getImage(source, new ImageLoader.ImageListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                remote.displayFailed();
                TextView view = getView();
                if (view != null){
                	view.invalidate();
                	
                }
                Log.d(com.org.news.ui.Constants.TAG,"onErrorResponse");
            }
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate){
                if (response.getBitmap() != null) {
                    // make sure view is still valid
                    TextView view = getView();
                    if (view == null) {
                        return;
                    }

                    Drawable drawable = new BitmapDrawable(context.getResources(), response.getBitmap());
                    final int oldHeight = remote.getBounds().height();
                    int maxWidth = SystemInfoUtils.getDefaultDisplayWidth(context) - view.getPaddingLeft() - view.getPaddingRight();

                   // int maxWidth=drawable.getIntrinsicWidth();
                    if (mMaxSize > 0 && (maxWidth > mMaxSize || maxWidth == 0))
                        maxWidth = mMaxSize;

                    remote.setRemoteDrawable(drawable, maxWidth);

                    //image is from cache? don't need to modify view height
                    if (isImmediate){
                       return;
                    }

                    int newHeight = remote.getBounds().height();
                    // For ICS
                    view.invalidate();

                    //重新設置高度 8是行距
                    view.setHeight(view.getHeight() + newHeight-oldHeight+8);
                    // Pre ICS
                    view.setEllipsize(null);

                }
            }
        });
        return remote;
    }

    private static class RemoteDrawable extends BitmapDrawable {
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
}
