package com.usc.meg.stockexchangeviewer;

import android.content.Intent;

import java.text.ParseException;
import java.util.Date;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import com.google.gson.Gson;

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
import java.util.zip.DataFormatException;

/**
 * Created by sbmeg on 20/04/16.
 */
public class NewsActivity extends Fragment{

    ListView list_details;
    ImageView chartImage;
    NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout, container, false);
        StockDetail obj = (StockDetail) getActivity().getIntent().getSerializableExtra("details");
        final String symbol = obj.getSymbol();
        List<NewsClass> newsList = new ArrayList<NewsClass>();
        newsAdapter = new NewsAdapter(getContext(),R.layout.news_list_item,newsList);
        list_details = (ListView) view.findViewById(R.id.list_news);
        list_details.setAdapter(newsAdapter);
        list_details.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsClass news = (NewsClass) parent.getAdapter().getItem(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUnescapedUrl()));
                startActivity(browserIntent);
            }
        });

        GetNewsTask getNews = new GetNewsTask();
        getNews.execute(symbol);

        return view;
    }

    private class GetNewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://stocksearch-android.appspot.com/?bingSymbol=" + params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine + "\n");
                }
                in.close();

                //print result
                System.out.println(response.toString());

                return response.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            NewsClass news = new NewsClass();
            List<NewsClass> newsResult = new ArrayList<NewsClass>();
            try {
            JSONArray jsonArray = new JSONArray(result);
                for(int j = 0; j < 4; j++){

                    JSONObject oneObject = jsonArray.getJSONObject(j);
                    String title = oneObject.getString("Title");
                    String content = oneObject.getString("Description");
                    String publisher = oneObject.getString("Source");
                    String publishedDate = oneObject.getString("Date");
                    DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = format.parse(publishedDate);
                    }
                    catch (ParseException e){

                    }
                    String reportDate = df.format(date);
                    String unescapedUrl = oneObject.getString("Url");
                    newsResult.add(new NewsClass(title, content, publisher, reportDate, unescapedUrl));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (newsResult != null) {
                newsAdapter.clear();
                newsAdapter.addAll(newsResult);
                newsAdapter.notifyDataSetChanged();
            }
        }
    }
}
