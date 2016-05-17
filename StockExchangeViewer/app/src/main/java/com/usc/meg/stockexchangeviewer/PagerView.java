package com.usc.meg.stockexchangeviewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sbmeg on 20/04/16.
 */
public class PagerView extends FragmentStatePagerAdapter {

    //number of tabs
    int tabCount;

    //Constructor
    public PagerView(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    //Overriding getItem
    @Override
    public Fragment getItem(int position) {
        //Returns the current tab
        switch (position) {
            case 0:
                CurrentActivity current_activity = new CurrentActivity();
                return current_activity;
            case 1:
                HistoricalActivity historical_activity = new HistoricalActivity();
                return historical_activity;
            case 2:
                NewsActivity news_activity = new NewsActivity();
                return news_activity;
            default:
                return null;
        }
    }

    //Gives number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
