package com.example.android.sunshine.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by john on 3/29/2015.
 */
public class ViewHolder
{

    public final ImageView iconView;
    public final TextView dateView;
    public final TextView forecastView;
    public final TextView highView;
    public final TextView lowView;

    public ViewHolder(View view)
    {
        iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
    }
}
