package com.samahop.samahope;

import com.parse.Parse;
import com.parse.ParseObject;
import com.samahop.samahope.doctors.DoctorProfile;

import android.app.Application;

/**
 * Initializes the Parse API upon application start-up.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(DoctorProfile.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "iFLGCz6UMGVrXLtJRUQMEY3T6flnhxF7GehtSbpo", "PXkQtnrCbjimLyndXjohfHVhr2r92XNMPq4YHYdS");
    }
}
