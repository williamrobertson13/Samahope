package com.samahop.samahope.doctors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samahop.samahope.R;
import com.samahop.samahope.doctors.DoctorProfile;
import com.squareup.picasso.Picasso;


/**
 * Provides a simple data binder for list items in a recycler view.
 */
public class DoctorProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    protected TextView name;
    protected TextView location;
    protected TextView treatmentName;
    protected TextView moneyNeeded;
    protected TextView percentageFunded;
    protected ProgressBar progressFunded;
    protected ImageView treatmentImage;
    protected ImageView bannerImage;

    /**
     * Initializes the widgets found on a doctor card.
     * @param v the view of the card being initialized
     */
    public DoctorProfileViewHolder(View v) {
        super(v);

        name = (TextView) v.findViewById(R.id.doctor_name);
        location = (TextView) v.findViewById(R.id.location);
        treatmentName = (TextView) v.findViewById(R.id.treatment_name);
        moneyNeeded = (TextView) v.findViewById(R.id.money_needed);
        percentageFunded = (TextView) v.findViewById(R.id.percentage_funded);
        progressFunded = (ProgressBar) v.findViewById(R.id.progressFunded);
        treatmentImage = (ImageView) v.findViewById(R.id.focus_image);
        bannerImage = (ImageView) v.findViewById(R.id.doctor_bgImage);

        v.setOnClickListener(this);
    }

    /**
     * Sets all of widgets in the card view to the data in a parse object.
     * @param object the profile of the doctor
     */
    public void bindParseData(DoctorProfile object) {

        name.setText(object.getName());
        location.setText(object.getLocation());
        treatmentName.setText(object.getTreatmentName());
        moneyNeeded.setText("$" + String.valueOf(object.getMoneyNeeded()));
        percentageFunded.setText(String.valueOf((int) object.getCostPercentage()) + "%");
        progressFunded.setProgress((int) object.getCostPercentage());

        Picasso.with(itemView.getContext())
                .load(object.getTreatmentImage())
                .fit()
                .centerCrop()
                .into(treatmentImage);

        Picasso.with(itemView.getContext())
                .load(object.getBannerImage())
                .fit()
                .centerCrop()
                .into(bannerImage);
    }

    @Override
    public void onClick(View view) {

        // let's pass the name to the fragment so we can access the profile
        // instead of having to query parse again for the specific entry
        Bundle bundle = new Bundle();
        bundle.putString("docName", name.getText().toString());

        Fragment profile = new ExtendedDoctorProfileView();
        profile.setArguments(bundle)
;
        FragmentTransaction transaction = ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        transaction.replace(android.R.id.content, profile);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

