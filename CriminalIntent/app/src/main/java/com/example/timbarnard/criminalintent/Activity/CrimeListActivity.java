package com.example.timbarnard.criminalintent.Activity;

import android.support.v4.app.Fragment;

/**
 * Created by Timbarnard on 23/10/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
