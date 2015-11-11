package com.example.timbarnard.criminalintent.Activity;


import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
