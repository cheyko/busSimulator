/*Version 1.0 completed*/
package com.busey.bussimulator;

import android.Manifest;
import android.content.Intent;
import android.content.Context;
//import android.location.LocationListener;
import android.os.Handler;
import android.os.SystemClock;
//import android.support.v4.app.ActivityCompat;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.math.BigDecimal;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartRecordActivity extends AppCompatActivity {

    private static final long interval = 20; // time period to send each coordinate
    public TextView clock,iLocation;

    public Button stopButton, busStopButton;

    public long MillisecondTime, StartTime, TimeBuff, UpdateTime, wrapNum1, wrapNum2 = 0L ;

    public Handler handler;

    int Seconds, Minutes, MilliSeconds,timeThus, busNumber ;
    public int val = 0;

    private Context context;
    public Date currentTime;

    ListView listView ;
    String[] ListElements = new String[] {  };
    List<String> ListElementsArrayList ;
    ArrayAdapter<String> adapter ;

    ArrayList<Coordinates> bCoordinates,finalCoordinates ;

    DatabaseReference theDatabase, routeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_record);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView numberofbus = findViewById(R.id.busInput);
        numberofbus.setText(message);

        busNumber = Integer.parseInt(message);
        context = this;

        handler = new Handler() ;
        listView = (ListView)findViewById(R.id.listview1);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(StartRecordActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);

        clock = (TextView)findViewById(R.id.theClock);
        stopButton = (Button)findViewById(R.id.button3);
        busStopButton = (Button)findViewById(R.id.button2);

        iLocation = (TextView)findViewById(R.id.locationView);

        StartTime = SystemClock.uptimeMillis();
        currentTime = Calendar.getInstance().getTime();
        handler.postDelayed(runnable, 0);

        //stopButton.setEnabled(false)


        routeRef = FirebaseDatabase.getInstance().getReference("routes");

        finalCoordinates = new ArrayList<>(Arrays.asList(new Coordinates("18.0121653036","-76.7980285616"),
                new Coordinates("18.020930","-76.770731"),  new Coordinates("18.015291","-76.742382"),
                new Coordinates("18.005855","-76.741891") ));

        Displacements start = new Displacements(73, currentTime, finalCoordinates.get(0),0);
        Displacements chkpnt1 = new Displacements(73, currentTime, finalCoordinates.get(1),1);
        Displacements chkpnt2 = new Displacements(73, currentTime, finalCoordinates.get(2),2);
        Displacements end = new Displacements(73, currentTime, finalCoordinates.get(3),3);

        String id_1 = routeRef.push().getKey();
        routeRef.child(id_1).setValue(start);

        String id_2 = routeRef.push().getKey();
        routeRef.child(id_2).setValue(chkpnt1);

        String id_3 = routeRef.push().getKey();
        routeRef.child(id_3).setValue(chkpnt2);

        String id_4 = routeRef.push().getKey();
        routeRef.child(id_4).setValue(end);


        ////////////////////////////////////////////////////////////////////
        theDatabase = FirebaseDatabase.getInstance().getReference("displacements");

        bCoordinates = new ArrayList<>(Arrays.asList(new Coordinates("18.01216530360","-76.79802856160"),
                new Coordinates("18.01206804350","-76.79654350890"), new Coordinates("18.01128875180","-76.79566363610"),
                new Coordinates("18.01220177300","-76.79321459750"), new Coordinates("18.01384000000","-76.79000000000"),
                new Coordinates("18.0155625","-76.787816"), new Coordinates("18.0168195","-76.7853059"),
                new Coordinates("18.017934","-76.7827221"), new Coordinates("18.0192581","-76.7802501"),
                new Coordinates("18.020354","-76.775664"), new Coordinates("18.021000","-76.770996"),
                new Coordinates("18.019861","-76.766441"), new Coordinates("18.018968","-76.761806"),
                new Coordinates("18.018059","-76.757175"), new Coordinates("18.017187","-76.752546"),
                new Coordinates("18.016325","-76.747911"),new Coordinates("18.015462","-76.743282"),
                new Coordinates("18.014171","-76.742510"), new Coordinates("18.011079","-76.742613"),
                new Coordinates("18.006656","-76.742029"),new Coordinates("18.005855","-76.741891") ));


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
            timeThus = (int) (UpdateTime / 1000);

            Minutes = timeThus / 60;
            Seconds = timeThus % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            if ((Seconds - wrapNum1 == interval) || (Minutes > wrapNum2)){
                Toast.makeText(context,"20 seconds pass ",Toast.LENGTH_SHORT).show();
                wrapNum1 = Seconds;
                wrapNum2 = Minutes;


                Coordinates currentLocs = bCoordinates.get(val);
                Displacements displacement = new Displacements(busNumber,currentTime,currentLocs,timeThus);

                //iLocation.setText("latti = " + currentLocs.getLatti() + " & longi = " + currentLocs.getLongi());
                iLocation.setText(" "+timeThus+" ");


                if (val < bCoordinates.size()) {
                    val += 1;
                }
                String id = theDatabase.push().getKey();
                theDatabase.child(id).setValue(displacement);

            }

            clock.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };


}
