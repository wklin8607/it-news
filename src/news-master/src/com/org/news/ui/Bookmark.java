package com.org.news.ui;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;
import com.org.news.provider.PostProvider;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Bookmark extends BaseFragment implements LoaderCallbacks<Cursor>{
	private SimpleCursorAdapter mSimpleCursorAdapter;
	private ListView mListView;

	//private String Part_name;
	private View mFooterView;
	private TextView emptyView;
	
	public static Bookmark newInstance(String Part,String url){
		Bookmark fragment = new Bookmark();
		Bundle args = new Bundle();
		//args.putString("Part", Part);
		//args.putString("url", url);
		fragment.setArguments(args);

		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Part_name = getArguments() != null ? getArguments().getString("Part"):getString(R.string.app_name);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		//Toast.makeText(getActivity(), "onCreateView:"+Part_name, Toast.LENGTH_SHORT).show();
		View view = inflater.inflate(R.layout.post_list, null);
	    mFooterView = inflater.inflate(R.layout.food_view, null);
	                
		mListView = (ListView)view.findViewById(R.id.list);


		mSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), 
				   R.layout.post_list_item, null,
				   new String[]{PostProvider.TITLE,
								PostProvider.EXCERPT,
								PostProvider.DATECREATED,
								PostProvider.ID,
								}, 
				   new int[]{R.id.title,R.id.summary,R.id.updateTime},0);
		
		mSimpleCursorAdapter.setViewBinder(new ListBinder());

		mListView.addFooterView(mFooterView,null,false);
		mFooterView.setVisibility(View.GONE);
		mListView.setAdapter(mSimpleCursorAdapter);
		getLoaderManager().initLoader(0, null, this);

		mListView.setOnItemClickListener(new MyOnItemClickListener());
		mListView.setOnScrollListener(new OnScrollListenerImple());
		
		emptyView = (TextView)view.findViewById(R.id.empty);  

		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}
	private class ListBinder implements ViewBinder{

		@Override
		public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
			// TODO Auto-generated method stub
			String able= getResources().getConfiguration().locale.getCountry();

			switch (arg0.getId()) {
			case R.id.title:
				TextView title = (TextView)arg0;
				String mtitle = arg1.getString(arg1.getColumnIndex(PostProvider.TITLE));
				if(able.equals("TW")||able.equals("HK")){
					try {
						JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
						title.setText(jChineseConvertor.s2t(mtitle));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					title.setText(mtitle);
				}
				return true;
			case R.id.summary:
				TextView summary = (TextView)arg0;
				String mSummary = arg1.getString(arg1.getColumnIndex(PostProvider.EXCERPT));
				if(able.equals("TW")||able.equals("HK")){
					try {
						JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
						summary.setText(jChineseConvertor.s2t(mSummary));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					summary.setText(mSummary);
				}

				return true;
			default:
				break;
			}
			return false;
		}
		
	}
	
	private class OnScrollListenerImple implements OnScrollListener{
		private int lastItemIndex;
		@Override
		public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			lastItemIndex = firstVisibleItem + visibleItemCount - 1 -1;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE  
                    && lastItemIndex == mSimpleCursorAdapter.getCount() - 1) {  
				mFooterView.setVisibility(View.VISIBLE);
                Log.i(Constants.TAG, "onScrollStateChanged");  
                //加载数据代码，此处省略了  
/*                if(CursorUtil.getLastPostId(getActivity(), Part_name)==1){
                	Toast.makeText(getActivity(), getString(R.string.thatall), Toast.LENGTH_SHORT).show();
                	mFooterView.setVisibility(View.GONE);
                	return;
                }
*/               
                
            }else{
				mFooterView.setVisibility(View.GONE);

            }
		}
		
	}
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),BookmarkRead.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position",arg2);
				bundle.putInt("count", mSimpleCursorAdapter.getCount());
				intent.putExtras(bundle);
				startActivity(intent);
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		
	
		CursorLoader cursorLoader = new CursorLoader(getActivity(), 
				PostProvider.CONTENT_URI, 
				new String[]{PostProvider.ID,
							 PostProvider.TITLE,
							 PostProvider.POSTID,
							 PostProvider.EXCERPT,
							 PostProvider.DATECREATED,
							 PostProvider.BOOKMARKS
							}, 
							 PostProvider.BOOKMARKS+"=?",
				new String[]{String.valueOf(1)},
							 PostProvider.POSTID+" DESC");
		return cursorLoader;
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		if(cursor.getCount()==0){
			emptyView.setVisibility(View.VISIBLE);
			
		}else{
			emptyView.setVisibility(View.GONE);
		}
		mSimpleCursorAdapter.swapCursor(cursor);

	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mSimpleCursorAdapter.swapCursor(null);

	}
}
