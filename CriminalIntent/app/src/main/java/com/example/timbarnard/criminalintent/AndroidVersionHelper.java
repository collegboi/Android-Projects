package com.example.timbarnard.criminalintent;

/**
 * Created by Timbarnard on 23/10/2015.
 */
import android.os.Build;

public class AndroidVersionHelper {
    public AndroidVersionHelper() {
    }

    static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    static boolean isGingerbreadOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
}
