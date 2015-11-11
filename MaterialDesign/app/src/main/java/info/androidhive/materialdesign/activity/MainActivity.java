package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.MotorDatabase;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Context mContext;
    MotorDatabase mMotorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mContext = getApplicationContext();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        Log.w("Menu","inflated..");
//        if(currentPosition == 2) {
//            //getMenuInflater().inflate(R.menu.account_menu, menu);
//
//            return true;
//        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search) {
//            SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//            Log.w("User", "Already Logged In");
//            preferences.edit().clear().apply();
//
//            currentPosition = 0;
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.container_body, new SearchFragment()).commit();
//            getSupportActionBar().setTitle("Search");
//
//            item.setVisible(false);

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = "Motor Spy";
        switch (position) {
            case 0:
                fragment = new SearchFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new NewsFragment();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new Account_page();
                title = "Account";
                break;
            case 3:
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                Log.w("User", "Already Logged In");
                preferences.edit().clear().apply();
                mMotorDatabase = new MotorDatabase(mContext);
                mMotorDatabase.open();
                mMotorDatabase.removeVehicles();
                fragment = new SearchFragment();
                title = getString(R.string.title_home);
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}

