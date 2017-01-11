package com.mezzanine.app.stockmanagement.fragments;

/**
 * Created by ramogiochola on 1/4/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.List;

/**
 * A fragment that displays all the clinics, their details and the stock level for each clinic.
 */
public class ClinicsFragment extends Fragment {
    private static final String TAG = "ClinicsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clinics, container, false);
        try {
            ListView mListView = (ListView) rootView.findViewById(R.id.clinicListView);
            List<Clinic> clinicList = StockManagement.getClinicList();
            displayLog("clinic list size " + clinicList.size());
            mListView.setAdapter(StockManagement.getClinicAdapter());
        } catch (Exception e) {
            displayLog("clinic adapater error " + e.toString());
        }


        return rootView;
    }

    private void displayLog(String me) {
        Log.i(TAG, me);
    }
}