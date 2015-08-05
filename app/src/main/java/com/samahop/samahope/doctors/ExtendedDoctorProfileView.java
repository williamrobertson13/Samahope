package com.samahop.samahope.doctors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samahop.samahope.R;
import com.squareup.picasso.Picasso;

public class ExtendedDoctorProfileView extends Fragment {

    private DoctorProfile doctor;

    public ExtendedDoctorProfileView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extended_doctor_profile, container, false);

        //set toolbar and enable back navigation
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Doctor Overview");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name = (TextView) view.findViewById(R.id.extended_doctor_name);
        TextView biography = (TextView) view.findViewById(R.id.extended_doctor_biography);
        TextView location = (TextView) view.findViewById(R.id.extended_doctor_location);
        TextView moneyRaised = (TextView) view.findViewById(R.id.extended_money_needed);
        ImageView bannerImage = (ImageView) view.findViewById(R.id.extended_doctor_image);
        ProgressBar percentageFunded = (ProgressBar) view.findViewById(R.id.extended_doctor_progress);
        TextView treatmentName = (TextView) view.findViewById(R.id.extended_focus_text);
        ImageView treatmentImage = (ImageView) view.findViewById(R.id.extended_focus_image);

        name.setText(doctor.getName());
        biography.setText(doctor.getBiography());
        location.setText(doctor.getLocation());
        percentageFunded.setProgress(doctor.getCostPercentage());
        treatmentName.setText(doctor.getTreatmentName());

        // format html string for money raised from xml
        String text = String.format(getResources().getString(R.string.amount_raised_of_goal),
                doctor.getMoneyRaised(), doctor.getTotalCost());
        CharSequence styledText = Html.fromHtml(text);
        moneyRaised.setText(styledText);

        Picasso.with(view.getContext())
                .load(doctor.getBannerImage())
                .fit()
                .centerCrop()
                .into(bannerImage);

        Picasso.with(view.getContext())
                .load(doctor.getTreatmentImage())
                .fit()
                .centerCrop()
                .into(treatmentImage);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setDoctor(DoctorProfile doc) { doctor = doc; }

    public void onDonateClicked(View view) {

    }
}
