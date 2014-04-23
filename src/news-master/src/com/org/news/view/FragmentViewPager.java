/*
 * Copyright 2013 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.org.news.view;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.org.news.ui.ReadNow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class FragmentViewPager extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;

    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public FragmentViewPager(SherlockFragmentActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mActionBar = activity.getSupportActionBar();

        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        mActionBar.addTab(tab);
        mTabs.add(info);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return ReadNow.newInstance("xdgq","http://xdgq.chinacloudsites.cn/xmlrpc.php");
		case 1:
			return ReadNow.newInstance("xycs","http://xycs.chinacloudsites.cn/xmlrpc.php");
		case 2:
			return ReadNow.newInstance("wxxh","http://wxxh.chinacloudsites.cn/xmlrpc.php");
		case 3:
			return ReadNow.newInstance("rqll","http://rqll.chinacloudsites.cn/xmlrpc.php");
		case 4:
			return ReadNow.newInstance("dpxs","http://dpxs.chinacloudsites.cn/xmlrpc.php");

		default:
			break;
		}
		return null;
	}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // NO-OP
    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // NO-OP
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // NO-OP
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // NO-OP
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}