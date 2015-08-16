package com.samahop.samahope.doctors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samahop.samahope.R;
import com.samahop.samahope.widgets.PreCachingLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DoctorProfileAdapter dataAdapter;

    public DoctorFragment() {
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_doctor, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.doctors_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        // setup a linear view layout for the list of doctor profiles
        PreCachingLayoutManager llm = new PreCachingLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        // initialize the adapter now that the parse data is set up
        dataAdapter = new DoctorProfileAdapter(view.getContext(), mRecyclerView);
        mRecyclerView.setAdapter(dataAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.sama_blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataAdapter.loadDoctors();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public DoctorProfileAdapter getDataAdapter() {
        return dataAdapter;
    }
}
