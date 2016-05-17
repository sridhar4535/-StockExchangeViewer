package com.usc.meg.stockexchangeviewer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sbmeg on 20/04/16.
 */
public class CurrentActivity extends Fragment {

    ListView list_details;
    ImageView chartImage;
    StockDetailAdapter detailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_layout, container, false);
        list_details = (ListView) view.findViewById(R.id.list_stock_details);
        List<StockSummary> arr = new ArrayList<StockSummary>();
        StockDetail obj = (StockDetail) getActivity().getIntent().getSerializableExtra("details");

        View headerView = View.inflate(getContext(), R.layout.detail_list_header, null);
        list_details.addHeaderView(headerView);
        View footerView = View.inflate(getContext(), R.layout.detail_list_footer, null);
        list_details.addFooterView(footerView);

        chartImage = (ImageView) view.findViewById(R.id.img_current_chart);
        chartImage.setOnClickListener(mGlobal_OnClickListener);

        arr.add(new StockSummary("NAME", obj.getName(),""));
        arr.add(new StockSummary("SYMBOL", obj.getSymbol(),""));
        arr.add(new StockSummary("LASTPRICE", "$ "+String.valueOf(obj.getLastPrice()),""));
        if(obj.getChangePercent() < 0 ) {
            arr.add(new StockSummary("CHANGE", obj.getChange() + "(" + obj.getChangePercent() + "%)",""));
        }
        else{
            arr.add(new StockSummary("CHANGE", obj.getChange() + "(+" + obj.getChangePercent() + "%)",""));
        }
        arr.add(new StockSummary("TIMESTAMP", obj.getTimestamp(),""));
        arr.add(new StockSummary("MARKETCAP", obj.getMarketCap()+" "+ obj.getMarketType(),""));
        arr.add(new StockSummary("VOLUME", String.valueOf(obj.getVolume()),""));
        if(obj.getChangePercentYTD() < 0 ) {
            arr.add(new StockSummary("CHANGEYTD", obj.getChangeYTD() + "(" + obj.getChangePercentYTD() + "%)",""));
        }
        else{
            arr.add(new StockSummary("CHANGEYTD", obj.getChangeYTD() + "(+" + obj.getChangePercentYTD() + "%)",""));
        }
        arr.add(new StockSummary("HIGH", "$ "+String.valueOf(obj.getHigh()),""));
        arr.add(new StockSummary("LOW", "$ "+String.valueOf(obj.getLow()),""));
        arr.add(new StockSummary("OPEN", "$ "+String.valueOf(obj.getOpen()),""));

        detailsAdapter = new StockDetailAdapter(getActivity().getApplicationContext(), R.layout.detail_list_row, arr);
        list_details.setAdapter(detailsAdapter);
        String URL = "http://chart.finance.yahoo.com/t?s=" + obj.getSymbol() + "&width=1024&height=768&lang=en-US";
        new ImageLoadTask(URL, chartImage).execute();
        return view;
    }

    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            StockDetail obj = (StockDetail) getActivity().getIntent().getSerializableExtra("details");
            String URL = "http://chart.finance.yahoo.com/t?s=" + obj.getSymbol() + "&width=1024&height=768&lang=en-US";
            Context mContext = getActivity() ;
            Dialog d = new Dialog(mContext,android.R.style.Theme);
            d.setCancelable(true);
            WebView wv = new WebView(mContext);
            wv.setLayoutParams(new ViewGroup.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.MATCH_PARENT));
            wv.loadUrl(URL);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setSupportZoom(true);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            d.setContentView(wv);
            d.show();
            Window window = d.getWindow();
            window.setLayout(600, 500);
        }
    };


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}

