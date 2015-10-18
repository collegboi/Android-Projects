package com.example.timbarnard.customlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    int[] imageArray = {R.drawable.motorspy, R.drawable.motorspy, R.drawable.motorspy, R.drawable.motorspy,R.drawable.motorspy,R.drawable.motorspy, R.drawable.motorspy,R.drawable.motorspy};
    String[] titleArray = {"Timothy", "Stephen", "Neil","Martin","Andy","Paul","Kennan","Owen"};
    String[] detailArray = {"Barnard", "Fox","Pelow","Quinn","Lahs","Brittain","Seno","Kane"};
    MyAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView)findViewById(R.id.list_view);
        myadapter = new MyAdapter(getApplicationContext(), R.layout.row_layout);
        listView.setAdapter(myadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent switchScreen = new Intent(MainActivity.this, Main2Activity.class);
                switchScreen.putExtra(EXTRA_MESSAGE, titleArray[position]);
                startActivity(switchScreen);
            }
        });

        for(int i = 0; i < this.imageArray.length; i++) {

            MyClass myClass;

            if (i == 2) {
                 myClass = new MyClass(R.drawable.ok, this.titleArray[i], this.detailArray[i]);

            }

            myClass = new MyClass(this.imageArray[i], this.titleArray[i], this.detailArray[i]);

            myadapter.add(myClass);
        }
    }
}
