package com.mezzanine.app.stockmanagement;

import android.app.Application;
import android.app.NotificationManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.adapters.ClinicAdapter;
import com.mezzanine.app.stockmanagement.adapters.InventoryAdapter;
import com.mezzanine.app.stockmanagement.models.Clinic;
import com.mezzanine.app.stockmanagement.models.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ramogiochola on 1/4/17.
 *
 * A class that extends the application class to provide various global variables and methods to the app
 *
 */

public class StockManagement extends Application {
    public static List<Clinic> clinicList;
    public static ClinicAdapter clinicAdapter;
    public static List<Clinic> inventoryList;
    public static InventoryAdapter inventoryAdapter;
    public static MainActivity mainActivity;
    public static HashMap<String, List<Inventory>> clinicInventoryHashMap;
    public static DatabaseReference myInventory;
    public static FirebaseDatabase database;
    public static DatabaseReference myClinics;
    public static TextView clinicsToBeStockedTextView;
    public static NotificationManager notificationManager;
    public static List<String> notificationMessages;

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

    public static List<Clinic> getInventoryList() {
        return inventoryList;
    }

    public static void setInventoryList(List<Clinic> inventoryList) {
        StockManagement.inventoryList.clear();
        StockManagement.inventoryList = inventoryList;
    }

    public static InventoryAdapter getInventoryAdapter() {
        return inventoryAdapter;
    }

    public static void setInventoryAdapter(InventoryAdapter inventoryAdapter) {
        StockManagement.inventoryAdapter = inventoryAdapter;
    }

    public static HashMap<String, List<Inventory>> getClinicInventoryHashMap() {
        return clinicInventoryHashMap;
    }

    public static void setClinicInventoryHashMap(HashMap<String, List<Inventory>> clinicInventoryHashMap) {
        StockManagement.clinicInventoryHashMap.clear();
        StockManagement.clinicInventoryHashMap = clinicInventoryHashMap;
    }

    public static DatabaseReference getMyInventory() {
        return myInventory;
    }

    public static void setMyInventory(DatabaseReference myInventory) {
        StockManagement.myInventory = myInventory;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(FirebaseDatabase database) {
        StockManagement.database = database;
    }

    public static DatabaseReference getMyClinics() {
        return myClinics;
    }

    public static void setMyClinics(DatabaseReference myClinics) {
        StockManagement.myClinics = myClinics;
    }

    public static TextView getClinicsToBeStockedTextView() {
        return clinicsToBeStockedTextView;
    }

    public static void setClinicsToBeStockedTextView(TextView clinicsToBeStockedTextView) {
        StockManagement.clinicsToBeStockedTextView = clinicsToBeStockedTextView;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static void setNotificationManager(NotificationManager notificationManager) {
        StockManagement.notificationManager = notificationManager;
    }

    public static List<String> getNotificationMessages() {
        return notificationMessages;
    }

    public static void setNotificationMessages(List<String> notificationMessages) {
        StockManagement.notificationMessages = notificationMessages;
    }

    public void onCreate() {
        super.onCreate();
        clinicList = new ArrayList<>();
        inventoryList = new ArrayList<>();
        clinicInventoryHashMap = new HashMap<>();

        //connect to the realtime database
        database = FirebaseDatabase.getInstance();

        //connect to the inventory table in the realtime database
        myInventory = database.getReference("inventory");

        //connect to the clinics table in the realtime database
        myClinics = database.getReference("clinics");
        notificationMessages = new ArrayList<>();
    }
}







