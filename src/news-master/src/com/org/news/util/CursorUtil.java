package com.org.news.util;

import com.org.news.provider.PostProvider;
import com.org.news.ui.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CursorUtil {
	
	/**
	 * 检查postprovider中是否有重复项
	 * @param title
	 * @return
	 */
	public static boolean checkIsRepeat(Context context,int postid){
		Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI, 
						new String[]{PostProvider.TITLE}, 
						PostProvider.POSTID+"=?", 
						new String[]{String.valueOf(postid)}, null);
		boolean result = false;
		if(cursor.getCount()==0){
			result = false;
			//Log.d(Constants.TAG,"result:"+result);
		}else{
			result = true;
			//Log.d(Constants.TAG,"result:"+result);

		}
		cursor.close();
		return result;
	}
	public static boolean checkIsRepeat(Context context,String title){
		Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI, 
						new String[]{PostProvider.TITLE}, 
						PostProvider.TITLE+"=?", 
						new String[]{title}, null);
		boolean result = false;
		if(cursor.getCount()==0){
			result = false;
		}else{
			result = true;
		}
		cursor.close();
		return result;
	}
	public static String parserCoverUrl(String in){
		int index1;
		int index2;
		String src = in.toLowerCase().replace(" ", "");
		index1 = src.indexOf("<img");
		if(index1==-1){
			return "";
		}
		String src2 = src.substring(index1);
		index2 = src2.indexOf("src=\"");
		if(index2==-1){
			return "";
		}
		String src3 = src2.substring(index2+5);
		
		int index3 = src3.indexOf("\"");
		if(index3==-1){
			return "";
		}
		return src3.substring(0,index3);
	}
	
	/**
	 * 解析img url
	 * @param in
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String parserImageUrl(String in){
		//Log.d("xmlrpc","in"+in);
		int index1;
		int index2;
		int offset;
		index1 = in.toLowerCase().indexOf("<img src=\"");
		if(index1==-1){
			index1 = in.indexOf("<img src =\"");
			if(index1==-1){
				index1 = in.indexOf("<img src = \"");
				if(index1 == -1){
					return "";
				}else{
					offset = 12;
				}
			}else{
				offset = 11;
			}
		}else{
			offset = 10;
		}
		String str1 = in.toLowerCase().substring(index1+offset);
		index2 = str1.indexOf("\"");
		if(index2==-1){
			return "";
		}
		String result = str1.substring(0, index2);
		if(result.equals("")){
			return "";
		}
		return result;
	} 
	/**
	 * 提前摘要，长度150
	 * @param input
	 * @return
	 */
	public static String getSummary(String input){
		if(input.length()<=150){
			return input;
		}else{
			return input.substring(0, 150);
		}
	}

	public static int getFirstPostId(Context context,String part){
		Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI, 
				new String[]{PostProvider.ID,
							PostProvider.POSTID,
							PostProvider.PART}, 
							PostProvider.PART+"=?", 
				new String[]{part}, 
				PostProvider.POSTID + " DESC");
		if(cursor.moveToFirst()){
			int frastpostid =cursor.getInt(cursor.getColumnIndex(PostProvider.POSTID));
			cursor.close();
			return frastpostid;

		}else{
			return 0;
		}
	}
	public static int getLastPostId(Context context,String part){
		Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI, 
				new String[]{PostProvider.ID,
							PostProvider.POSTID,
							PostProvider.PART}, 
							PostProvider.PART+"=?", 
				new String[]{part}, 
				PostProvider.POSTID + " DESC");
		if(cursor.moveToLast()){
			int lastpostid = cursor.getInt(cursor.getColumnIndex(PostProvider.POSTID));
			cursor.close();
			return lastpostid;
		}else{
			return 0;
		}
	}
}
