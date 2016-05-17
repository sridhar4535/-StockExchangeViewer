package com.usc.meg.stockexchangeviewer;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by sbmeg on 20/04/16.
 */
public class HistoricalActivity extends Fragment{
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historical_layout, container, false);
        webView = (WebView) view.findViewById(R.id.webView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        StockDetail obj = (StockDetail) getActivity().getIntent().getSerializableExtra("details");
        final String symbol = obj.getSymbol();
        webView.loadUrl("file:///android_asset/highcharts.html");
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("javascript:init('" + symbol + "');");

        webView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){

                webView.loadUrl("javascript:init('" + symbol + "');");
                String ur = "javascript:init('" + symbol + "');";
            }
        });

        return view;

    }
}
