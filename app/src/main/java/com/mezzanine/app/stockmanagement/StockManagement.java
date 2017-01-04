package com.mezzanine.app.stockmanagement;

import android.app.Application;

import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.adapters.ClinicAdapter;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class StockManagement extends Application {
    public static List<Clinic> clinicList;
    public static ClinicAdapter clinicAdapter;
    public static MainActivity mainActivity;
    public void onCreate() {
        super.onCreate();
        clinicList = new ArrayList<>();
        //clinicAdapter = new ClinicAdapter(getApplicationContext(), clinicList);
    }


    public static List<Clinic> getClinicList() {
        return clinicList;
    }

    public static void setClinicList(List<Clinic> clinicList) {
        StockManagement.clinicList.clear();
        StockManagement.clinicList = clinicList;
    }

    public static ClinicAdapter getClinicAdapter() {
        return clinicAdapter;
    }

    public static void setClinicAdapter(ClinicAdapter clinicAdapter) {
        StockManagement.clinicAdapter = clinicAdapter;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        StockManagement.mainActivity = mainActivity;
    }
}
