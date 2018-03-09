package com.busey.bussimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.busey.bussimulator.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   /** @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }**/

    /** Called when the user taps the Send button */
    public void startRecord(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, StartRecordActivity.class);
        EditText busNumber = (EditText) findViewById(R.id.busnumber);
        String message = busNumber.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }


}
