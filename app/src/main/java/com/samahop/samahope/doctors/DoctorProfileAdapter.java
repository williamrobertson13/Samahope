package com.samahop.samahope.doctors;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.samahop.samahope.R;

import java.util.List;

/**
 * Created by William on 7/16/2015.
 */
public class DoctorProfileAdapter extends RecyclerView.Adapter<DoctorProfileViewHolder> {

    private ViewGroup parseParent;
    List<DoctorProfile> doctors;
    private ParseQueryAdapter<DoctorProfile> parseAdapter;

    public DoctorProfileAdapter(Context context, ViewGroup parentIn) {
        super();

        parseParent = parentIn;
        parseAdapter = new ParseQueryAdapter<DoctorProfile>(context, getDoctorQueryFactory()) {

            @Override
            public View getItemView(DoctorProfile object, View v, ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_profile_card, parent, false);
                }
                super.getItemView(object, v, parent);

                // set the appropriate fields for cardview using a simple data binder
                // these fields are taken from the parse backend
                DoctorProfileViewHolder dataBinder = new DoctorProfileViewHolder(v);
                dataBinder.bindParseData(object);

                return v;
            }
        };
        loadDoctors();

        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.loadObjects();
    }

    private ParseQueryAdapter.QueryFactory<DoctorProfile> getDoctorQueryFactory() {
        return new ParseQueryAdapter.QueryFactory<DoctorProfile>() {
            public ParseQuery<DoctorProfile> create() {
                ParseQuery<DoctorProfile> query = DoctorProfile.getQuery();
                return query;
            }
        };
    }

    public void loadDoctors() {
        ParseQuery<DoctorProfile> query = DoctorProfile.getQuery();
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<DoctorProfile>() {
            @Override
            public void done(List<DoctorProfile> l, ParseException e) {
                if (e == null) {
                   doctors = l;
                    notifyDataSetChanged();
                } else {
                    Log.e("DoctorProfileAdapter", "Error while querying in background: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DoctorProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_profile_card, viewGroup, false);
        DoctorProfileViewHolder dvh = new DoctorProfileViewHolder(v);

        return dvh;
    }

    @Override
    public void onBindViewHolder(DoctorProfileViewHolder doctorViewHolder, int i) {
        parseAdapter.getView(i, doctorViewHolder.vCardView, parseParent);
    }

    @Override
    public int getItemCount() {
        return parseAdapter.getCount();
    }

    public List<DoctorProfile> getDoctors() { return doctors; }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<DoctorProfile> {

        public void onLoading() {
        }

        public void onLoaded(List<DoctorProfile> objects, Exception e) {
            parseAdapter.notifyDataSetChanged();
        }
    }
}

