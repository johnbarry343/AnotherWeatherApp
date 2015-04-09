package com.example.android.sunshine.app.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by john on 4/7/2015.
 */
public class SunshineService extends IntentService
{
    private final String LOG_TAG = SunshineService.class.getSimpleName();

    public SunshineService()
    {
        super("SunshineService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

    }

    static public class AlarmReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Intent sendIntent = new Intent(context, SunshineService.class);
            sendIntent.putExtra("location", intent.getStringExtra("location"));
            context.startService(sendIntent);
        }
    }
}
