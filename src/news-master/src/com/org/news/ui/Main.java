package com.org.news.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.org.news.view.MyListView;
import com.org.news.view.Utility;
import com.org.news.view.ZoomOutPageTransformer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends BaseActivity{
    private DrawerLayout mDrawerLayout;
    private MyListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private LinearLayout mMenu_layout_left;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private List<String> mDrawerTitles = new ArrayList<String>();
    
    private MyListView mButtonList;
    private ArrayList<Map<String, Object>> mButtonData = new ArrayList<Map<String,Object>>();
    
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    
    //private TextView pointsTextView;
	//private String displayPointsText;
	
   // private FragmentViewPager mFragmentViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //AppConnect.getInstance(this);
        
        initListView();
        initButtonListView();
        mTitle = mDrawerTitle = mDrawerTitles.get(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (MyListView) findViewById(R.id.left_drawer);
        mButtonList = (MyListView)findViewById(R.id.button_list);
        mMenu_layout_left = (LinearLayout)findViewById(R.id.menu_layout_left);
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(mPagerAdapter);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setAdapter(new MyAdpater(this));
        Utility.setListViewHeightBasedOnChildren(mDrawerList);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        
        //pointsTextView = (TextView)findViewById(R.id.point);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setTitle(mTitle);
        //actionbar.setDisplayShowHomeEnabled(false);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	getSupportActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	getSupportActionBar().setTitle(getString(R.string.app_name));
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

       
        SimpleAdapter adapter = new SimpleAdapter(this, 
				 								  mButtonData, 
				 								  R.layout.button_item, 
				 								  new String[]{"icon","name"},
				 								  new int[]{R.id.icon,R.id.nametext}); 
        mButtonList.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(mButtonList);
        mButtonList.setOnItemClickListener(new SetOnItemClickListener());
        

        mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
            	getSupportActionBar().setTitle(mDrawerTitles.get(arg0));

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        //AppConnect.getInstance(this).checkUpdate(this);
		//AppConnect.getInstance(this).initAdInfo();

    }
    private class SetOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(arg2==0){
				startActivity(new Intent(Main.this,Preferences.class));
			}else if(arg2 == 1){
				//AppConnect.getInstance(Main.this).showOffers(Main.this);
			}else if(arg2 == 2){
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
				sendIntent.putExtra(Intent.EXTRA_TEXT, "http://sexy.dy2046.com/app/jqxl8-android.apk");
				sendIntent.setType("text/plain");
				startActivity(sendIntent);

			}
		}
    	
    }
    private void initButtonListView(){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("icon", R.drawable.ic_gear_40);
    	map.put("name",getString(R.string.setting));
    	mButtonData.add(map);
    	
    	Map<String, Object> offer = new HashMap<String, Object>();
    	offer.put("icon", R.drawable.ic_gear_app);
    	offer.put("name",getString(R.string.appRecommend));
    	mButtonData.add(offer);
    	
    	Map<String, Object> share = new HashMap<String, Object>();
    	share.put("icon", R.drawable.ic_gear_share);
    	share.put("name",getString(R.string.shareapp));
    	mButtonData.add(share);


    }
    private void initListView(){
    	String[] titles = getResources().getStringArray(R.array.left_title);
    	for(int i=0;i<titles.length;i++){
    		mDrawerTitles.add(titles[i]);
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    }

    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
    	mDrawerLayout.isDrawerOpen(mMenu_layout_left);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case android.R.id.home:
			handleNavigationDrawerToggle();
			break;

		default:
			break;
		}

       return super.onOptionsItemSelected(item);
    }
    private void handleNavigationDrawerToggle() {  
        if (mDrawerLayout.isDrawerOpen(mMenu_layout_left)) {  
            mDrawerLayout.closeDrawer(mMenu_layout_left);  
        } else {  
            mDrawerLayout.openDrawer(mMenu_layout_left);  
        }  
      } 

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @SuppressLint("NewApi")
	private void selectItem(int position) {
    	mPager.setCurrentItem(position);
     
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        setTitle(mDrawerTitles.get(position));
        mDrawerLayout.closeDrawer(mMenu_layout_left);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				return ReadNow.newInstance("test","http://mysite.chinacloudsites.cn/xmlrpc.php");
			case 1:
				return ReadNow.newInstance("tupian","http://sexy.dy2046.com/tupian/xmlrpc.php");
			case 2:
				return ReadNow.newInstance("wxxh","http://sexy.dy2046.com/wxxh/xmlrpc.php");
			case 3:
				return ReadNow.newInstance("rqll","http://sexy.dy2046.com/rqll/xmlrpc.php");
			case 4:
				return ReadNow.newInstance("dpxs","http://sexy.dy2046.com/dpxs/xmlrpc.php");
			case 5:
				return Bookmark.newInstance("", "");
				
			default:
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}

    	
    }
    
    private class MyAdpater extends BaseAdapter{
    	private LayoutInflater mInflater;
    	public MyAdpater(Context context) {
			// TODO Auto-generated constructor stub
    		this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDrawerTitles.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(view==null){
				holder=new ViewHolder();
				
				view = mInflater.inflate(R.layout.drawer_item, null);
				holder.title = (TextView)view.findViewById(android.R.id.text1);
				view.setTag(holder);
			}else{
				holder=(ViewHolder)view.getTag();
			}
			holder.title.setText(mDrawerTitles.get(position));
			return view;
		}
		public final class ViewHolder{
			public TextView title;
		}
    }
    
    boolean isExit;
  
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		
            exit();  
            return false;  
        } else {  
            return super.onKeyDown(keyCode, event);  
        }     }
    public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getString(R.string.exit), Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
		//AppConnect.getInstance(Main.this).getPoints(this);

    	super.onResume();
    }
    @SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };
}