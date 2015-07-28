package com.samahop.samahope.doctors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.samahop.samahope.R;

public class ExtendedDoctorProfileView extends Fragment {

    public ExtendedDoctorProfileView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // ((MainActivity)getActivity()).getDataAdapter().getDoctors().get(0).getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.extended_doctor_profile_view, container, false);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.include);

        //set toolbar and enable back navigation
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Doctor Overview");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.onBackPressed();
                Log.e("FFF", "ffffff");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
