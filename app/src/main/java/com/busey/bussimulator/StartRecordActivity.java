/*Version 1.0 completed*/
package com.busey.bussimulator;

import android.Manifest;
import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
//import android.location.LocationProvider;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class StartRecordActivity extends AppCompatActivity {

    public TextView clock,ilocation;

    public Button stopButton, busStopButton;

    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    public Handler handler;

    int Seconds, Minutes, MilliSeconds ;

    ListView listView ;

    String[] ListElements = new String[] {  };

    List<String> ListElementsArrayList ;

    ArrayAdapter<String> adapter ;

    //private LocationManager locationManager = null;
    //private LocationListener locationListener = null;
    //private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_record);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView numberofbus = findViewById(R.id.busInput);
        numberofbus.setText(message);
        handler = new Handler() ;
        listView = (ListView)findViewById(R.id.listview1);

        handler = new Handler() ;

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(StartRecordActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);

        clock = (TextView)findViewById(R.id.theClock);
        stopButton = (Button)findViewById(R.id.button3);
        busStopButton = (Button)findViewById(R.id.button2);
        ilocation = (TextView)findViewById(R.id.locationView);

        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        //getLocation();
        //stopButton.setEnabled(false);

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListElementsArrayList.add(clock.getText().toString());
                adapter.notifyDataSetChanged();

                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                clock.setText("00:00:00");
            }
        });

        busStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListElementsArrayList.add(clock.getText().toString());
                adapter.notifyDataSetChanged();

            }
        });

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            clock.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };


}
