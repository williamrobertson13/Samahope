package com.samahop.samahope.doctors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.samahop.samahope.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 7/16/2015.
 */
public class DoctorProfileAdapter extends RecyclerView.Adapter<DoctorProfileViewHolder> {

    private ViewGroup parseParent;
    private Map<String, DoctorProfile> doctors;
    private ParseQueryAdapter<DoctorProfile> parseAdapter;
    private DoctorProfileAdapter recyclerAdapter = this;

    public DoctorProfileAdapter(Context context, ViewGroup parentIn) {
        super();

        // performance optimization for recycler view
        setHasStableIds(false);

        doctors = new HashMap<>();
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

                doctors.put(object.getName(), object);
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
                return DoctorProfile.getQuery();
            }
        };
    }

    public void loadDoctors() {
        ParseQuery<DoctorProfile> query = DoctorProfile.getQuery();
        // query.fromLocalDatastore();
        query.findInBackground(new FindCallback<DoctorProfile>() {
            @Override
            public void done(List<DoctorProfile> l, ParseException e) {
                if (e == null) {
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
        return new DoctorProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DoctorProfileViewHolder doctorViewHolder, int i) {
        parseAdapter.getView(i, doctorViewHolder.itemView, parseParent);
    }

    @Override
    public int getItemCount() {
        return parseAdapter.getCount();
    }

    public Map<String, DoctorProfile> getDoctors() { return doctors; }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<DoctorProfile> {

        public void onLoading() {
        }

        public void onLoaded(List<DoctorProfile> objects, Exception e) {
            parseAdapter.notifyDataSetChanged();
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}

