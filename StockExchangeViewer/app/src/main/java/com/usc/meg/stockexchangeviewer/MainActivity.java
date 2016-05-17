package com.usc.meg.stockexchangeviewer;

import android.content.Intent;
import android.os.Handler;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.StringReader;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.DialogInterface;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;


import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.widget.Switch;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity{

    Button getQuote, clear;
    Switch mySwitch;
    StocksListAdapter adapter;
    FavoritesAdapter favsAdapter;
    AutoCompleteTextView searchView;
    List<StockSummary> stocksList;
    List<StockDetail> favorites;
    Timer searchtimer;
    ImageButton image;
    ProgressBar progressBar;
    ProgressBar favprogressBar;
    DynamicListView favoritesList;
    SharedPreferences mPrefs;
    int progressStatus = 0;
    boolean isFavActivityRunning = false;

    private final static int INTERVAL = 1000 * 10; //10 seconds
    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            if(!isFavActivityRunning) {
                isFavActivityRunning = true;
                new GetFavoritesPopulateTask().execute();
            }
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };
    void startRepeatingTask(){
        mHandlerTask.run();
    }
    void stopRepeatingTask(){
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    protected void onResume() {
        handleFavorites();
        if(!isFavActivityRunning && mySwitch.isChecked()) {
            startRepeatingTask();
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        stopRepeatingTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.startimage);

        favprogressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        favprogressBar.setVisibility(View.GONE);



        AppEventsLogger.activateApp(this);

        getQuote = (Button) findViewById(R.id.but_get_quote);
        mySwitch = (Switch) findViewById(R.id.switch_auto_refresh);
        clear = (Button) findViewById(R.id.but_clear);
        image = (ImageButton) findViewById(R.id.img_refresh );
        searchView = (AutoCompleteTextView) findViewById(R.id.text_search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        favoritesList = (DynamicListView) findViewById(R.id.dynamic_stock_listview);
        progressBar.setVisibility(View.GONE);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    startRepeatingTask();

                }
                else{
                    stopRepeatingTask();
                }
            }
        });

        stocksList = new ArrayList<StockSummary>();
        adapter = new StocksListAdapter(this,R.layout.search_list_row,stocksList);

        searchView.setAdapter(adapter);
        searchView.setThreshold(3);

        getQuote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStockDetails getDetails = new GetStockDetails();
                if(searchView.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,
                            "Please enter a Stock Name/Symbol", Toast.LENGTH_LONG).show();
                    return;
                }
                getDetails.execute(searchView.getText().toString());
            }
        });

        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
            }
        });

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetFavoritesPopulateTask().execute();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchtimer != null) {
                    searchtimer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchtimer = new Timer();
                searchtimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        GetStockListTask getList = new GetStockListTask();
                        getList.execute(String.valueOf(searchView.getText()));
                    }
                }, 600);
            }
        });

        handleFavorites();
    }

    public void handleFavorites(){
        //Setup favorites list
        favorites = new ArrayList<StockDetail>();
        mPrefs = getSharedPreferences("favorites", MODE_PRIVATE);
        Gson gson = new Gson();
        Map<String, ?> favs = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : favs.entrySet()) {
            String value = entry.getValue().toString();
            StockDetail detail = gson.fromJson(value, StockDetail.class);
            favorites.add(detail);
        }

        favsAdapter = new FavoritesAdapter(this,R.layout.favorites_list_row,favorites);
        favoritesList.setAdapter(favsAdapter);


        favoritesList.enableDragAndDrop();

        favoritesList.enableSwipeToDismiss(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
                final int[] array = reverseSortedPositions;

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                for (int position : array) {
                                    Editor prefsEditor = mPrefs.edit();
                                    prefsEditor.remove(favsAdapter.getItem(position).getSymbol());
                                    prefsEditor.apply();
                                    favorites.remove(position);
                                    favsAdapter.notifyDataSetChanged();
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getStockSymbol() {
        return "";
    }

    private class GetStockListTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://androidapp-1301.appspot.com/?compname="+params[0]+"&Type=1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine+"\n");
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
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressBar.setVisibility(View.GONE);
                }
            });
            try {
                JSONArray jArray = new JSONArray(result);
                List<StockSummary> summaryObjects = new ArrayList<StockSummary>();
                for (int i=0;i<jArray.length();i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String symbol = oneObject.get("Symbol").toString();
                    String name = oneObject.get("Name").toString();
                    String exchange = oneObject.get("Exchange").toString();
                    summaryObjects.add(new StockSummary(name, symbol, exchange));
                }
                stocksList = summaryObjects;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (stocksList != null) {
                adapter.clear();
                adapter.addAll(stocksList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class GetFavoritesPopulateTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            isFavActivityRunning = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        favprogressBar.setVisibility(View.VISIBLE);
                }
            });

            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {

            SharedPreferences favorites =  getSharedPreferences("favorites", MODE_PRIVATE);
            Map<String, ?> allEntries = favorites.getAll();
            ArrayList<String> favData = new ArrayList<>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                StringBuffer response = new StringBuffer();
                try {
                    URL url = new URL("http://androidapp-1301.appspot.com/?InputFixed="+entry.getKey()+"&Type=0");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine+"\n");
                    }
                    in.close();

                    //print result
                    System.out.println(response.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }

                favData.add(response.toString());
            }
            return favData;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            ArrayList<StockDetail> favList = new ArrayList<>();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favprogressBar.setVisibility(View.GONE);
                }
            });
            try {
                for (String result : results) {
                        JSONObject jobj = new JSONObject(result);
                        Gson gson = new Gson();
                        StockDetail detail = gson.fromJson(jobj.toString(), StockDetail.class);
                        favList.add(detail);
                    }
                }
             catch (JSONException e) {
                e.printStackTrace();
            }

            if (favList != null) {
                favsAdapter.clear();
                favsAdapter.addAll(favList);
                favsAdapter.notifyDataSetChanged();
            }
            isFavActivityRunning = false;
        }
    }

    private class GetStockDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://androidapp-1301.appspot.com/?InputFixed="+params[0]+"&Type=0");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine+"\n");
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
            StockDetail details = new StockDetail();
            JSONObject detailsOBJ = new JSONObject();
            try {
                detailsOBJ = new JSONObject(result);
                if(detailsOBJ.isNull("Name")){
                    Toast.makeText(MainActivity.this,
                            "Invalid Symbol", Toast.LENGTH_LONG).show();
                    return;
                }
                if(detailsOBJ.get("Symbol").equals("GRNREG")){
                    Toast.makeText(MainActivity.this,
                            "Stock Not Available", Toast.LENGTH_LONG).show();
                    return;
                }
                Gson gson = new Gson();
                details = gson.fromJson(detailsOBJ.toString(), StockDetail.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent detailsActivity = new Intent(getApplicationContext(), StockDetailActivity.class);
            detailsActivity.putExtra("details", details);
            startActivity(detailsActivity);
        }
    }

}
