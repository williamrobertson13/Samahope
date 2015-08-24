package com.samahop.samahope;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.samahop.samahope.doctors.DoctorProfile;

/**
 * Initializes the Parse API upon application start-up.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(DoctorProfile.class);
        Parse.enableLocalDatastore(this);

        // FOR PRODUCTION: replace these keys with live keys (be sure to hide them!)
        Parse.initialize(this, "iFLGCz6UMGVrXLtJRUQMEY3T6flnhxF7GehtSbpo", "PXkQtnrCbjimLyndXjohfHVhr2r92XNMPq4YHYdS");
    }
}
