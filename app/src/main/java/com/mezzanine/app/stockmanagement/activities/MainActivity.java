package com.mezzanine.app.stockmanagement.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.adapters.ClinicAdapter;
import com.mezzanine.app.stockmanagement.adapters.InventoryAdapter;
import com.mezzanine.app.stockmanagement.database.DataProvider;
import com.mezzanine.app.stockmanagement.fragments.ClinicsFragment;
import com.mezzanine.app.stockmanagement.fragments.InventoryFragment;
import com.mezzanine.app.stockmanagement.models.Clinic;
import com.mezzanine.app.stockmanagement.models.Inventory;
import com.mezzanine.app.stockmanagement.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mezzanine.app.stockmanagement.utilities.Constants.CLINICLIST;

public class MainActivity  extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    private static final String TAG = "MainActivity";
    private static List<Clinic> clinicList;
    private static Fragment fragmentClinic;
    private static  Fragment fragmentInventory;
    //private FirebaseDatabase database;
    private DataProvider dataProvider;
    private Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataProvider = new DataProvider(this);

        // Write a message to the database
        //database = FirebaseDatabase.getInstance();


        //clinicList = new ArrayList<>();
        StockManagement.setMainActivity(this);
        utilities = new Utilities();
        StockManagement.setClinicAdapter(new ClinicAdapter(StockManagement.getMainActivity(), StockManagement.getClinicList(), StockManagement.getDatabase()));
        StockManagement.setInventoryAdapter(new InventoryAdapter(StockManagement.getMainActivity(),StockManagement.getInventoryList()));
        fragmentClinic = new ClinicsFragment();
        fragmentInventory = new InventoryFragment();

        //final DatabaseReference myRef = database.getReference("clinics");
        //DatabaseReference myClinics = database.getReference("clinics");

        //populateDatabase();
        //final DatabaseReference myInventory = database.getReference("inventory");
        //getQueryKeys(myInventory);



        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tabcolor)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Stock Management</font>"));


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Clinics")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Inventory")
                        .setTabListener(this));

        // Read from the database
        StockManagement.getMyClinics().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                StockManagement.getClinicList().clear();
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iter.iterator();
                while (iterator.hasNext()) {

                    DataSnapshot dataSnapshot1 = iterator.next();
                    //displayLog(""+dataSnapshot1.toString());
                    Clinic clinic = dataSnapshot1.getValue(Clinic.class);
                    clinic.setId(dataSnapshot1.getKey());
                    StockManagement.getMyInventory().child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            Iterable<DataSnapshot> iter = dataSnapshot2.getChildren();
                            Iterator<DataSnapshot> iterator = iter.iterator();
                            while (iterator.hasNext()) {

                                DataSnapshot dataSnapshot4 = iterator.next();
                                displayLog(dataSnapshot4.getKey()+" inventory results "+dataSnapshot4.toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //displayLog("id "+clinic.getId());
                    //displayLog("name "+clinic.getName());
                    //displayLog("country "+clinic.getCountry());
                    //displayLog("city "+clinic.getCity());
                    StockManagement.getClinicList().add(clinic);
                    try{
                        long result = dataProvider.insertClinic(clinic);
                        displayLog("clinic inserted "+result);
                    }
                    catch (Exception e){
                        displayLog("clinic insert error "+e.toString());
                    }
                    try {
                        StockManagement.getClinicAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e){
                        displayLog("stock management error "+e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        getQueryValues(StockManagement.getMyInventory());
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        if(tab.getPosition() == 1){
            StockManagement.getMainActivity().getQueryValues(StockManagement.getMyInventory());
        }

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.

                    //Bundle args = new Bundle(CLINICLIST,getClinicList());


                    return getFragmentClinic();

                default:
                    // The other sections of the app are dummy placeholders.

                    return getFragmentInventory();
            }
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    public void getQueryValues(final DatabaseReference databaseReference){

        final List<Clinic> clinicList = dataProvider.getAllClinics();
        final HashMap<String, Clinic> hashMapClinics = new HashMap<>();
        StockManagement.getInventoryList().clear();
        StockManagement.getClinicInventoryHashMap().clear();
        final List<String> messages = new ArrayList<>();
        try{
            StockManagement.getNotificationMessages().clear();
            //StockManagement.getNotificationManager().cancelAll();
        }
        catch (Exception e){
            displayLog("Error clearing notifications "+e.toString());
        }
        for(final Clinic clinic : clinicList){
            final String key = clinic.getId();
            Query query = databaseReference.child(key).orderByValue().endAt(5);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = iter.iterator();
                    List<Inventory> inventoryList = new ArrayList<>();

                    while (iterator.hasNext()) {
                        DataSnapshot dataSnapshot1 = iterator.next();
                        Inventory inventory = new Inventory();
                        inventory.setId(key);
                        inventory.setDrugName(dataSnapshot1.getKey().toString());
                        inventory.setDrugItems(dataSnapshot1.getValue().toString());
                        inventoryList.add(inventory);
                        messages.add(utilities.toTitleCase(clinic.getName())+" is low on "+inventory.getDrugName().toUpperCase());
                        if(StockManagement.getClinicInventoryHashMap().containsKey(key)){
                            StockManagement.getClinicInventoryHashMap().remove(key);
                        }
                        else {
                            hashMapClinics.put(key,clinic);
                        }
                    }
                    StockManagement.getInventoryList().clear();
                    for(Map.Entry<String, Clinic> entry : hashMapClinics.entrySet()){
                        StockManagement.getInventoryList().add(entry.getValue());
                    }
                    StockManagement.getClinicInventoryHashMap().put(key,inventoryList);
                    StockManagement.getInventoryAdapter().notifyDataSetChanged();
                    try {
                        if (StockManagement.getInventoryList().size() > 0) {
                            StockManagement.getClinicsToBeStockedTextView().setText(getResources().getString(R.string.clinicstobestocked));
                        } else {
                            StockManagement.getClinicsToBeStockedTextView().setText(getResources().getString(R.string.clinicsfullystocked));
                        }
                    }
                    catch (Exception e){
                        displayLog("Error updating getClinicsToBeStockedTextView "+e.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private Clinic getClinicDetails(String key){
        Clinic clinic = new Clinic();
        try{
            clinic = dataProvider.getClinicDetails(key);
        }
        catch (Exception e){
            displayLog("Get clinic details error "+e.toString());
        }
        return clinic;
    }

    private void populateDatabase(){
        DatabaseReference myRef = StockManagement.getDatabase().getReference("clinics");
        //String text = editText.getText().toString().trim();
        //myRef.setValue(text);
        Clinic clinic = new Clinic();
        clinic.setCountry("Kenya");
        clinic.setCity("Eldoret");
        clinic.setName("Eldyclinic");
        //clinic.setNevirapine(6);
        //clinic.setStavudine(8);
        //clinic.setZidotabine(12);
        //myRef.setValue(clinic);
        myRef.child("A").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Uganda");
        clinic.setCity("Tororo");
        clinic.setName("Toroclinic");
        //clinic.setNevirapine(17);
        //clinic.setStavudine(12);
        //clinic.setZidotabine(32);
        //myRef.setValue(clinic);
        myRef.child("B").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Tanzania");
        clinic.setCity("Moshi");
        clinic.setName("Moshiclinic");
        //clinic.setNevirapine(13);
        //clinic.setStavudine(8);
        //clinic.setZidotabine(24);
        //myRef.setValue(clinic);
        myRef.child("C").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Ethiopia");
        clinic.setCity("Addis Ababa");
        clinic.setName("Addisclinic");
        //clinic.setNevirapine(9);
        //clinic.setStavudine(23);
        //clinic.setZidotabine(15);
        //myRef.setValue(clinic);
        myRef.child("D").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Sudan");
        clinic.setCity("Khartoum");
        clinic.setName("Sudaclinic");
        //clinic.setNevirapine(18);
        //clinic.setStavudine(12);
        //clinic.setZidotabine(18);
        //myRef.setValue(clinic);
        myRef.child("E").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("South Sudan");
        clinic.setCity("Juba");
        clinic.setName("Jubaclinic");
        //clinic.setNevirapine(12);
        //clinic.setStavudine(31);
        //clinic.setZidotabine(1);
        //myRef.setValue(clinic);
        myRef.child("F").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("DRC");
        clinic.setCity("Kinshasa");
        clinic.setName("Kliniki");
        //clinic.setNevirapine(21);
        //clinic.setStavudine(31);
        //clinic.setZidotabine(12);
        //myRef.setValue(clinic);
        myRef.child("G").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Zimbabwe");
        clinic.setCity("Harare");
        clinic.setName("Haraclinic");
        //clinic.setNevirapine(20);
        //clinic.setStavudine(19);
        //clinic.setZidotabine(40);
        //myRef.setValue(clinic);
        myRef.child("H").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Mozambique");
        clinic.setCity("Maputo");
        clinic.setName("Mapuclinic");
        //clinic.setNevirapine(20);
        //clinic.setStavudine(10);
        //clinic.setZidotabine(27);
        //myRef.setValue(clinic);
        myRef.child("I").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Malawi");
        clinic.setCity("Lilongwe");
        clinic.setName("Liclinic");
        //clinic.setNevirapine(21);
        //clinic.setStavudine(31);
        //clinic.setZidotabine(10);
        //myRef.setValue(clinic);
        myRef.child("J").setValue(clinic);

        DatabaseReference myRef2 = StockManagement.getDatabase().getReference("inventory");

        Clinic clinic1 = new Clinic();

        clinic1.setNevirapine(6);
        clinic1.setStavudine(8);
        clinic1.setZidotabine(12);
        myRef2.child("A").setValue(clinic1);

        clinic1.setNevirapine(17);
        clinic1.setStavudine(12);
        clinic1.setZidotabine(32);
        myRef2.child("B").setValue(clinic1);

        clinic1.setNevirapine(13);
        clinic1.setStavudine(8);
        clinic1.setZidotabine(24);
        myRef2.child("C").setValue(clinic1);

        clinic1.setNevirapine(9);
        clinic1.setStavudine(23);
        clinic1.setZidotabine(15);
        myRef2.child("D").setValue(clinic1);

        clinic1.setNevirapine(18);
        clinic1.setStavudine(12);
        clinic1.setZidotabine(18);
        myRef2.child("E").setValue(clinic1);

        clinic.setNevirapine(12);
        clinic.setStavudine(31);
        clinic.setZidotabine(1);
        myRef2.child("F").setValue(clinic1);

        clinic1.setNevirapine(21);
        clinic1.setStavudine(31);
        clinic1.setZidotabine(12);
        myRef2.child("G").setValue(clinic1);

        clinic1.setNevirapine(20);
        clinic1.setStavudine(19);
        clinic1.setZidotabine(40);
        myRef2.child("H").setValue(clinic1);

        clinic1.setNevirapine(20);
        clinic1.setStavudine(10);
        clinic1.setZidotabine(27);
        myRef2.child("I").setValue(clinic1);

        clinic1.setNevirapine(21);
        clinic1.setStavudine(31);
        clinic1.setZidotabine(10);
        myRef2.child("J").setValue(clinic1);
    }
    public static List<Clinic> getClinicList() {
        return clinicList;
    }

    public static void setClinicList(List<Clinic> clinicList) {
        MainActivity.clinicList = clinicList;
    }

    public static Fragment getFragmentClinic() {
        return fragmentClinic;
    }

    public static void setFragmentClinic(Fragment fragmentClinic) {
        MainActivity.fragmentClinic = fragmentClinic;
    }

    public static Fragment getFragmentInventory() {
        return fragmentInventory;
    }

    public static void setFragmentInventory(Fragment fragmentInventory) {
        MainActivity.fragmentInventory = fragmentInventory;
    }

    private void displayLog(String s){
        Log.i(TAG,s);
    }
}
