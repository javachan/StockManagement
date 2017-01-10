package com.mezzanine.app.stockmanagement.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.List;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class InventoryFragment  extends Fragment {
    private static final String TAG = "InventoryFragment";
    public InventoryFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        ListView mListView = (ListView) rootView.findViewById(R.id.clinicsToBeStockedListView);
        TextView clinicsToBeStockedMessage = (TextView) rootView.findViewById(R.id.txtClinicsToBeStocked);
        StockManagement.setClinicsToBeStockedTextView(clinicsToBeStockedMessage);
        List<Clinic> inventoryList = StockManagement.getInventoryList();
        mListView.setAdapter(StockManagement.getInventoryAdapter());
        int size = inventoryList.size();
        displayLog("clinic list size "+size);
        if(size > 0){
            //mListView.setVisibility(View.VISIBLE);
            clinicsToBeStockedMessage.setText(getResources().getString(R.string.clinicstobestocked));


        }
        else {
            clinicsToBeStockedMessage.setText(getResources().getString(R.string.clinicsfullystocked));
            //mListView.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }
    private void displayLog(String me){
        Log.i(TAG,me);
    }
}
