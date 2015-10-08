package com.example.timbarnard.helloworld;
import android.content.DialogInterface.OnClickListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends Activity {

    Button pressButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pressButton = (Button) findViewById(R.id.press_btn);
        cancelButton = (Button) findViewById(R.id.cancel_btn);

        OnClickListener pressClickListen = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        OnClickListener cancelClickListen = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        pressButton.setOnClickListener(pressClickListen);
        cancelButton.setOnClickListener(cancelClickListen);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
