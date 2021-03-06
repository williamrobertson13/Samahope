package com.samahop.samahope.doctors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samahop.samahope.MainActivity;
import com.samahop.samahope.R;
import com.samahop.samahope.payments.PaymentFragment;
import com.squareup.picasso.Picasso;

/**
 * This fragment is opened whenever a user clicks on a doctor item from the recycler view
 * list. Essentially, the user can scroll through the more specific details pertaining to
 * the doctor and click a button to donate.
 */
public class ExtendedDoctorFragment extends Fragment {

    private DoctorProfile doctor;

    // create an onClick listener here instead of the MainActivity for better organization
    // when the donate button is clicked, it will bring up the payment form
    View.OnClickListener donateOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
            transaction.replace(R.id.frame_layout, new PaymentFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    public ExtendedDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extended_doctor_profile, container, false);

        // set toolbar and enable back navigation
        MainActivity activity = (MainActivity) getActivity();
        activity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        activity.getSupportActionBar().setTitle("Doctor Overview");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name = (TextView) view.findViewById(R.id.extended_doctor_name);
        TextView biography = (TextView) view.findViewById(R.id.extended_doctor_biography);
        TextView location = (TextView) view.findViewById(R.id.extended_doctor_location);
        TextView moneyRaised = (TextView) view.findViewById(R.id.extended_money_needed);
        TextView percentageText = (TextView) view.findViewById(R.id.extended_percentage_funded);
        ImageView bannerImage = (ImageView) view.findViewById(R.id.extended_doctor_image);
        ProgressBar percentageFunded = (ProgressBar) view.findViewById(R.id.extended_doctor_progress);
        TextView treatmentName = (TextView) view.findViewById(R.id.extended_focus_text);
        TextView treatmentInfo = (TextView) view.findViewById(R.id.treatment_info);
        ImageView treatmentImage = (ImageView) view.findViewById(R.id.extended_focus_image);
        Button donateButton = (Button) view.findViewById(R.id.donate_button);
        donateButton.setOnClickListener(donateOnClick);

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

        // format html string for percentage raised from xml
        text = String.format(getResources().getString(R.string.percentage_funded_text_2),
                doctor.getCostPercentage());
        styledText = Html.fromHtml(text);
        percentageText.setText(styledText);

        // format html string for treatments funded from xml
        text = String.format(getResources().getString(R.string.treatments_funded_text),
                doctor.getTreatmentsFunded());
        styledText = Html.fromHtml(text);
        treatmentInfo.setText(styledText);

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

    public void setDoctor(DoctorProfile doc) { doctor = doc; }
}
