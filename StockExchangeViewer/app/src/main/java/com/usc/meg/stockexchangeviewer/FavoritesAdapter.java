package com.usc.meg.stockexchangeviewer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhaarman.listviewanimations.util.Swappable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbmeg on 23/04/16.
 */

public class FavoritesAdapter extends ArrayAdapter<StockDetail> implements Swappable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private int res;
    private LayoutInflater mLayoutInflater;
    List<StockDetail> list;

    public FavoritesAdapter(Context context, int resource, List<StockDetail> objects) {
        super(context, resource, objects);
        list = objects;
        mContext = context;
        res = resource;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(res, parent, false);
        }
        convertView = (LinearLayout) mLayoutInflater.inflate( res, null );

        final StockDetail item = (StockDetail) getItem( position );

        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GetStockDetails getDetails = new GetStockDetails();
                getDetails.execute(item.getSymbol());
            }
        });

        TextView txtName = (TextView) convertView.findViewById(R.id.txt_fav_name);
        txtName.setText(item.getName());

        TextView txtSymb = (TextView) convertView.findViewById(R.id.txt_fav_syb);
        txtSymb.setText(item.getSymbol());

        TextView txtPerc = (TextView) convertView.findViewById(R.id.txt_fav_perc);
        if (item.getChangePercent() > 0) {
            txtPerc.setBackgroundColor(0xFF70ED4E);
        } else {
            txtPerc.setBackgroundColor(0xFFFF0000);
        }
        txtPerc.setText(item.getChangePercent()+"%");

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txt_fav_price);
        txtPrice.setText("$ " + item.getLastPrice());

        TextView txtMarketCap = (TextView) convertView.findViewById(R.id.txt_fav_marketcap);
        txtMarketCap.setText("Market Cap: " + item.getMarketCap()+" "+ item.getMarketType());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();
                results.values = list;
                results.count = list.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void swapItems(int pos1, int pos2) {
        StockDetail s1 = this.getItem(pos1);
        StockDetail s2 = this.getItem(pos2);

        this.remove(s1);
        this.insert(s2, pos1);
        this.remove(s2);
        this.insert(s1, pos2);
        this.notifyDataSetChanged();
    }

    @Override
    public StockDetail getItem(int position) {
        return super.getItem(position);
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
                Gson gson = new Gson();
                details = gson.fromJson(detailsOBJ.toString(), StockDetail.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent detailsActivity = new Intent(mContext, StockDetailActivity.class);
            detailsActivity.putExtra("details", details);
            mContext.startActivity(detailsActivity);
        }
    }
}

