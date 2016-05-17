package com.usc.meg.stockexchangeviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by sbmeg on 20/04/16.
 */
public class StockDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    StockDetail stockDetails;
    PagerView adapter;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.stock_details_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent stockDetail = getIntent();
        stockDetails = (StockDetail) stockDetail.getSerializableExtra("details");
        setTitle(stockDetails.getName());

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        mPrefs = getSharedPreferences("favorites", MODE_PRIVATE);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Result>() {
            @Override
            public void onSuccess(Result result) {
                Toast.makeText(getApplicationContext(), "Shared Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Sharing Cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        });

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);





        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Historical"));
        tabLayout.addTab(tabLayout.newTab().setText("News"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Creating our pager adapter
        adapter = new PagerView(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, true);
                tabLayout.setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        String json = mPrefs.getString(stockDetails.getSymbol(), null);
        if ( json!= null){
            MenuItem fav = menu.findItem(R.id.favorite);
            fav.setIcon(R.drawable.star_gold);
            fav.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.fb_share:
                String name = "Current Stock price of "+stockDetails.getSymbol() +" is $"+stockDetails.getLastPrice();
                Uri picture = Uri.parse("http://chart.finance.yahoo.com/t?s="+ stockDetails.getSymbol() + "&width=150&height=150&lang=en-US");
                String caption= "LAST TRADED PRICE: $"+stockDetails.getLastPrice()+" CHANGE: "+stockDetails.getChange()+" ("+stockDetails.getChangePercent()+"%)";
                String link = "http://finance.yahoo.com/q?s="+ stockDetails.getSymbol();
                String description = "Stock Information of "+stockDetails.getName()+" ("+stockDetails.getSymbol()+")";

                Toast.makeText(StockDetailActivity.this,
                        "Sharing " + stockDetails.getName() + "!!", Toast.LENGTH_LONG).show();

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(link))
                            .setContentTitle(name)
                            .setImageUrl(picture)
                            .setContentDescription(description)
                            .setQuote(caption)
                            .build();
                    shareDialog.show(content);
                }
                return true;
            case R.id.favorite:
                if (!item.isChecked()){
                    item.setIcon(R.drawable.star_gold);
                    Toast.makeText(StockDetailActivity.this,
                            "Bookmarked " + stockDetails.getName() + "!!", Toast.LENGTH_LONG).show();
                    Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(stockDetails);
                    prefsEditor.putString(stockDetails.getSymbol(),json);
                    prefsEditor.commit();
                    item.setChecked(true);
                } else {
                    item.setIcon(R.drawable.star_outline);
                    Editor prefsEditor = mPrefs.edit();
                    prefsEditor.remove(stockDetails.getSymbol());
                    prefsEditor.apply();
                    item.setChecked(false);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
