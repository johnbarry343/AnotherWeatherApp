package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private ShareActionProvider mShareActionProvider;
    private static final int DETAIL_LOADER_ID = 0;
    private static String mForecast;
    private Uri mForecastUri;

    static final String DETAIL_URI = "URI";

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_CONDITION_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_HUMIDITY = 5;
    static final int COL_WIND_SPEED = 6;
    static final int COL_DEGREES = 7;
    static final int COL_PRESSURE = 8;
    static final int COL_WEATHER_TYPE_ID = 9;

    private ImageView iconView;
    private TextView dayView;
    private TextView dateView;
    private TextView forecastView;
    private TextView highView;
    private TextView lowView;
    private TextView humidityView;
    private TextView windView;
    private TextView pressureView;

    public DetailFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mForecast != null)
        {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + " #SunshineApp");
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        Bundle arguments = getArguments();
        if (arguments != null)
        {
            mForecastUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        iconView = (ImageView) rootView.findViewById(R.id.icon);
        dayView = (TextView) rootView.findViewById(R.id.day_textview);
        dateView = (TextView) rootView.findViewById(R.id.date_textview);
        forecastView = (TextView) rootView.findViewById(R.id.forecast_textview);
        highView = (TextView) rootView.findViewById(R.id.high_textview);
        lowView = (TextView) rootView.findViewById(R.id.low_textview);
        humidityView = (TextView) rootView.findViewById(R.id.humidity_textview);
        windView = (TextView) rootView.findViewById(R.id.wind_textview);
        pressureView = (TextView) rootView.findViewById(R.id.pressure_textview);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if (mForecastUri != null)
        {
            return new CursorLoader(getActivity(), mForecastUri, FORECAST_COLUMNS, null, null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        if (cursor != null && cursor.moveToFirst())
        {
            String dayString = Utility.getDayName(getActivity(), cursor.getLong(COL_WEATHER_DATE));
            dayView.setText(dayString);

            String dateString = Utility
                    .getFormattedMonthDay(getActivity(), cursor.getLong(COL_WEATHER_DATE));
            dateView.setText(dateString);

            boolean isMetric = Utility.isMetric(getActivity());
            String high = Utility
                    .formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MAX_TEMP),
                            isMetric);
            highView.setText(high);

            String low = Utility
                    .formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MIN_TEMP),
                            isMetric);
            lowView.setText(low);

            int weatherId = cursor.getInt(COL_WEATHER_TYPE_ID);
            iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

            String weatherDescription = cursor.getString(COL_WEATHER_DESC);
            forecastView.setText(weatherDescription);

            float humidity = cursor.getFloat(COL_HUMIDITY);
            humidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            String wind = Utility.getFormattedWind(getActivity(), cursor.getFloat(COL_WIND_SPEED),
                    cursor.getFloat(COL_DEGREES));
            windView.setText(wind);

            float pressure = cursor.getFloat(COL_PRESSURE);
            pressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            if (mShareActionProvider != null)
            {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    void onLocationChanged(String newLocation)
    {
        // replace the uri, since the location has changed
        Uri uri = mForecastUri;
        if (null != uri)
        {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry
                    .buildWeatherLocationWithDate(newLocation, date);
            mForecastUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER_ID, null, this);
        }
    }
}