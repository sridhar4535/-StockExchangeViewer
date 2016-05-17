package com.usc.meg.stockexchangeviewer;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbmeg on 23/04/16.
 */

public class StockDetailAdapter extends ArrayAdapter<StockSummary> {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private int res;
    private LayoutInflater mLayoutInflater;
    List<StockSummary> list;

    public StockDetailAdapter(Context context, int resource, List<StockSummary> objects) {
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

        StockSummary item = (StockSummary) getItem(position);
        ImageView arrow = (ImageView) convertView.findViewById(R.id.imageView2);
        TextView txtName = (TextView) convertView.findViewById(R.id.text_stock_detail);

        txtName.setText(item.getName());
        if(item.getName().equals("CHANGE")||item.getName().equals("CHANGEYTD") ){
            if (item.getSymbol().indexOf("-")>=0) {
                arrow.setImageDrawable(convertView.getResources().getDrawable(R.drawable.down));
            } else {
                arrow.setImageDrawable(convertView.getResources().getDrawable(R.drawable.up));
            }
        }



        TextView txtDesc = (TextView) convertView.findViewById(R.id.text_stock_value);
        txtDesc.setText(item.getSymbol());

        return convertView;
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
}

