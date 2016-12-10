package com.duongCompany.duong.pushupcounter;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Date;

import static com.duongCompany.duong.pushupcounter.data.PushContract.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LoaderManager.LoaderCallbacks<Cursor> {
    private int counter = 0;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView textView;
    private Button startButton;
    private Button doneButton;
    public static boolean insue = false;
    private double mass = 60.0;
    private int height = 170;
    private int armLength;
    ListView listView;
    PushAdapter pushAdapter = null;
    private int calories = 0;
    private long time1;
    private long time2;
    private Cursor parameterCursor = null;

    private AdView mAdView;


    private TextView curCalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        textView = (TextView) findViewById(R.id.text);
        startButton = (Button) findViewById(R.id.start_button);
        curCalText = (TextView) findViewById(R.id.cur_calories);
        doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setEnabled(false);
        listView = (ListView) findViewById(R.id.list_view);
        armLength = (int) (height- 0.45)/2;

        pushAdapter = new PushAdapter(this, null);
        listView.setAdapter(pushAdapter);

        getLoaderManager().initLoader(1, null, this);
        getLoaderManager().initLoader(2, null, this);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                counter = 0;
                calories = 0;
                time1 = date.getTime();
                startButton.setEnabled(false);
                doneButton.setEnabled(true);
                insue = true;
                listView.setEnabled(false);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setEnabled(true);
                listView.setEnabled(true);
                insue = false;
                textView.setText("0");
                textView.setTextColor(Color.parseColor("#000000"));
                curCalText.setText("0");
                curCalText.setTextColor(Color.parseColor("#000000"));
                Date date = new Date();
                ContentValues contentValues = new ContentValues();
                contentValues.put(PushEntry.COLUMN_DATE, date.getTime());
                contentValues.put(PushEntry.COLUMN_COUNT, counter);
                contentValues.put(PushEntry.COLUMN_CALORIES, calories);
                Uri uri = getContentResolver().insert(PushEntry.CONTENT_URI, contentValues);
                Toast.makeText(getApplicationContext(), "Excellent! Keep pushing up!", Toast.LENGTH_SHORT).show();
                doneButton.setEnabled(false);



            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView!= null) mAdView.resume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        if(mAdView!= null) mAdView.pause();
        super.onPause();


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (insue && event.values[0] < mSensor.getMaximumRange()) {
            counter += 1;
            setCalories();
            textView.setText("" + counter);

            int caloriesKcal = calories / 4200;
            curCalText.setText("" + caloriesKcal);
            if(counter<20){
                textView.setTextColor(Color.parseColor("#EF9A9A"));
                curCalText.setTextColor(Color.parseColor("#EF9A9A"));
            } else if( counter <40){
                textView.setTextColor(Color.parseColor("#EF5350"));
                curCalText.setTextColor(Color.parseColor("#EF5350"));
            } else if( counter <60){
                textView.setTextColor(Color.parseColor("#E53935"));
                curCalText.setTextColor(Color.parseColor("#E53935"));
            } else {
                textView.setTextColor(Color.parseColor("#B71C1C"));
                curCalText.setTextColor(Color.parseColor("#B71C1C"));
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id ==1 ) return new CursorLoader(this, PushEntry.CONTENT_URI, null, null, null, null);
        else return new CursorLoader(this,Parameter.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == 1)
        pushAdapter.swapCursor(data);
        else {
            if(data.getCount()==0){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Parameter.COLUMN_WEIGHT, mass);
                contentValues.put(Parameter.COLUMN_HEIGHT, height);
                //Toast.makeText(this, "parameter done", Toast.LENGTH_SHORT).show();
                this.getContentResolver().insert(Parameter.CONTENT_URI,contentValues);
            } else{
                data.moveToFirst();
                mass = data.getDouble(data.getColumnIndex(Parameter.COLUMN_WEIGHT));
                height = data.getInt(data.getColumnIndex(Parameter.COLUMN_HEIGHT));
                //Toast.makeText(this, "parameter coppied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pushAdapter.swapCursor(null);
    }

    public void setCalories() {
        Date date = new Date();
        double mechangicalEnergy = mass * armLength * 0.25 * 9.81/100;
        time2 = date.getTime();
        double heat = time2-time1> 5000? 211*1 : 211.0 * (time2 - time1) *0.001;
        //Toast.makeText(this,""+time1 +" "+ time2, Toast.LENGTH_SHORT).show();
        calories += (int) (mechangicalEnergy + heat) / 0.2;
        time1 = time2;
    }

    @Override
    protected void onDestroy() {
        if(mAdView!= null ) mAdView.destroy();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weight_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.parameter_option:
                Intent intent = new Intent(getApplicationContext(),ParametersActivity.class);
                intent.setData(ContentUris.withAppendedId(Parameter.CONTENT_URI,1));
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

/** done creating the cursor loader, create databaseHelper*/
/*need to extract information from the parameter cursor and use that data to calculate calories */