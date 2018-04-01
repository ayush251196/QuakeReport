/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EarthquakeActivity extends AppCompatActivity {
    private  int res;
    private QueryUtils queryUtils;
    private  Myadapter myadapter;
    private ArrayList<Earthquake> arrayList;
    private ListView listView;
    private String url="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Earthquake earthquake=(Earthquake)adapterView.getItemAtPosition(i);
            Intent intent=createBrowserIntent(earthquake.getUrl());
            startActivity(intent);
        }
    };

//
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            res=R.layout.earthquake_list_land;
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            res=R.layout.earthquake_list;
//        }
//    }
    private class BackgroundTask extends AsyncTask<String ,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            queryUtils.clear();
            setUI();

        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.i("tag","doinbackground/////////////////////////");
            queryUtils=new QueryUtils();
            arrayList=queryUtils.extractEarthquakes(strings[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("tag","oncreated/////////////////");
        int orientation=getApplicationContext().getResources().getConfiguration().orientation;
        if(orientation==Configuration.ORIENTATION_LANDSCAPE){
                setContentView(R.layout.earthquake_activity_land);
                res=R.layout.earthquake_list_land;
        }
        else if(orientation==Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.earthquake_activity);
                res = R.layout.earthquake_list;
        }
        if (isOnline()){
            //Date c = Calendar.getInstance().getTime();
//            long millis=System.currentTimeMillis();
//           // System.out.println("Current time => " + c);
//            Date date=new Date(millis);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            df.setTimeZone(TimeZone.getTimeZone("GMT"));
     //       String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
           // String formattedDate = df.format(date);
        //    Log.i("tag---",date);
//            url+="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=1&starttime="+date;
            BackgroundTask backgroundTask=new BackgroundTask();
            backgroundTask.execute(url);
        }
       else {
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
           // Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
           // finish();
       }

    }
    Intent createBrowserIntent(String url){
        Intent intent=new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url));
        return intent;
    }
    private void setUI(){
        myadapter=new Myadapter(this,arrayList,res);
        listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(onItemClickListener);
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
           //Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myadapter!=null)
            myadapter.clear();
        if(listView!=null){
            listView=null;
        }
        Log.i("tag","paused//////////////////////");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("tag","stopped/////////////////////////");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BackgroundTask backgroundTask=new BackgroundTask();
        backgroundTask.execute(url);
        Log.i("tag","restarted/////////////////////////");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("tag","resumed/////////////////////////");


    }
}
