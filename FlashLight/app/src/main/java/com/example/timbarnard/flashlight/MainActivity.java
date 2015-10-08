package com.example.timbarnard.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button tapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().getDecorView().setBackgroundColor(Color.GREEN);

        tapButton = (Button) findViewById(R.id.buttonLight);
        tapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent switchScreen = new Intent(MainActivity.this, MainActivity2Activity.class);
                startActivity(switchScreen);
            }
        });

    }
}
