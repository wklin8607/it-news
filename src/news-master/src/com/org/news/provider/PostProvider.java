package com.org.news.provider;

import com.org.news.ui.Constants;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class PostProvider extends ContentProvider {

    public static final String AUTHORITY = "com.org.news.provider.PostProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/wordpress");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.org.news.provider";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.org.news.provider";
    public static final String DEFAULT_SORT_ORDER = "score DESC";

  
    public static final String ID = BaseColumns._ID;
    public static final String CONTENT = "content";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String DATECREATED = "datecreated";
    public static final String EXCERPT = "excerpt";
    public static final String POSTID = "postid";
    public static final String BOOKMARKS = "bookmarks";
    public static final String PART = "part";
    
    private static final String DATABASE_NAME = "wordpress.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "post";
    
    private static final int WORDPRESS = 1;
    private static final int WORDPRESS_ID = 2;

    private static final UriMatcher sUriMatcher;
        
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

 
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
				db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				        + ID + " INTEGER PRIMARY KEY,"
				        + CONTENT + " TEXT,"
				        + DESCRIPTION + " TEXT,"
				        + LINK + " TEXT,"
				        + TITLE + " TEXT,"
				        + DATECREATED + " TEXT,"
				        + EXCERPT + " TEXT,"
				        + PART + " TEXT,"
				        + BOOKMARKS + " INTEGER,"
				        + POSTID + " INTEGER"
				        + ");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

  
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	Log.d(Constants.TAG,"oldVersion:"+oldVersion+" newVersion:"+newVersion);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper mDatabaseHelper;


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case WORDPRESS:
            return CONTENT_TYPE;

        case WORDPRESS_ID:
            return CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != WORDPRESS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final long id = db.insert(TABLE_NAME, null, values);
        final Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(newUri, null);

        return newUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int updateCount;
        switch (sUriMatcher.match(uri)) {
        case WORDPRESS:
            updateCount = db.update(TABLE_NAME, values, selection, selectionArgs);
            break;

        case WORDPRESS_ID:
            final long id = Long.parseLong(uri.getPathSegments().get(1));
            final String idPlusSelection = android.provider.BaseColumns._ID + "=" + Long.toString(id) + (selection == null ? "" : "AND (" + selection + ")");
            updateCount = db.update(TABLE_NAME, values, idPlusSelection, selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int updateCount;
        switch (sUriMatcher.match(uri)) {
        case WORDPRESS:
            updateCount = db.delete(TABLE_NAME, selection, selectionArgs);
            break;

        case WORDPRESS_ID:
            final long id = Long.parseLong(uri.getPathSegments().get(1));
            final String idPlusSelection = android.provider.BaseColumns._ID + "=" + Long.toString(id) + (selection == null ? "" : "AND (" + selection + ")");
            updateCount = db.delete(TABLE_NAME, idPlusSelection, selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "wordpress", WORDPRESS);
        sUriMatcher.addURI(AUTHORITY, "wordpress/#", WORDPRESS_ID);
    }
}
