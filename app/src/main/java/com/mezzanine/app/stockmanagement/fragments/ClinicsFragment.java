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
import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.adapters.ClinicAdapter;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.List;

/**
 * A fragment that launches other parts of the demo application.
 */
public class ClinicsFragment extends Fragment {
    private static final String TAG = "ClinicsFragment";
    //private static ClinicAdapter clinicAdapter;
    //public ClinicsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clinics, container, false);
        try{

            ListView mListView = (ListView) rootView.findViewById(R.id.clinicListView);
            List<Clinic> clinicList = StockManagement.getClinicList();
            displayLog("clinic list size "+clinicList.size());
            //StockManagement.setClinicAdapter(new ClinicAdapter(StockManagement.getMainActivity(), clinicList));
            //displayLog("context "+getContext().toString());
            mListView.setAdapter(StockManagement.getClinicAdapter());


        }
        catch (Exception e){
            displayLog("clinic adapater error "+e.toString());
        }


        return rootView;
    }

    private void displayLog(String me){
        Log.i(TAG,me);
    }
}