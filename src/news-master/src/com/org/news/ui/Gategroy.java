package com.org.news.ui;

import com.android.volley.imagecache.ImageCacheManager;
import com.android.volley.toolbox.NetworkImageView;
import com.org.news.view.MyGridView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class Gategroy extends BaseFragment {
	private GridView mGridView;
	private GridAdapter mGridAdapter;
	private String[] titles;
	private String[] icons;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.gategory, null);
		mGridView = (GridView)view.findViewById(R.id.grid);
		init();
		mGridAdapter = new GridAdapter();
		mGridView.setAdapter(mGridAdapter);
		return view;
	}
	private void init(){
		titles = getResources().getStringArray(R.array.gategroy_list);
		icons = getResources().getStringArray(R.array.gategroy_icon_list);
	}
	
	private class GridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return titles[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			
			if(convertView==null){
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.gategory_item, null);
				viewHolder = new ViewHolder();
				viewHolder.icon = (NetworkImageView)convertView.findViewById(R.id.icon);
				viewHolder.title = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.icon.setImageUrl(icons[position], ImageCacheManager.getInstance().getImageLoader());
			viewHolder.title.setText(titles[position]);
			return convertView;
		}
		
	}
	private static class ViewHolder{
		NetworkImageView icon;
		TextView title;
	}
}
