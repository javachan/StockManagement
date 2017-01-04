package com.mezzanine.app.stockmanagement.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezzanine.app.stockmanagement.R;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class InventoryFragment  extends Fragment {
    public InventoryFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        return rootView;
    }
}
