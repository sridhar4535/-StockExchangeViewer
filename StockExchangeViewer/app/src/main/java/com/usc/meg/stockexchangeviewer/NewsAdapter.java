package com.usc.meg.stockexchangeviewer;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbmeg on 23/04/16.
 */

public class NewsAdapter extends ArrayAdapter<NewsClass> {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private int res;
    private LayoutInflater mLayoutInflater;
    List<NewsClass> list;

    public NewsAdapter(Context context, int resource, List<NewsClass> objects) {
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

        NewsClass item = (NewsClass) getItem( position );

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_heading);
        txtTitle.setText(Html.fromHtml("<u>" +item.getTitle()+"</u>"));

        TextView txtContent = (TextView) convertView.findViewById(R.id.txt_content);
        txtContent.setText(Html.fromHtml(item.getContent()));

        TextView txtPublisher = (TextView) convertView.findViewById(R.id.txt_publisher);
        txtPublisher.setText("Publisher: "+item.getPublisher());

        TextView txtDate = (TextView) convertView.findViewById(R.id.txt_date);
        txtDate.setText("Date:" + item.getPublishedDate());

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

