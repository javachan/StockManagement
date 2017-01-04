package com.mezzanine.app.stockmanagement.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.google.firebase.database.ValueEventListener;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.adapters.ClinicAdapter;
import com.mezzanine.app.stockmanagement.fragments.ClinicsFragment;
import com.mezzanine.app.stockmanagement.fragments.InventoryFragment;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        //clinicList = new ArrayList<>();
        StockManagement.setMainActivity(this);
        StockManagement.setClinicAdapter(new ClinicAdapter(StockManagement.getMainActivity(), StockManagement.getClinicList(), database));

        fragmentClinic = new ClinicsFragment();
        fragmentInventory = new InventoryFragment();



        //final DatabaseReference myRef = database.getReference("clinics");
        DatabaseReference myClinics = database.getReference("clinics");

        //populateDatabase();

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
        myClinics.addValueEventListener(new ValueEventListener() {
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
                    displayLog(""+dataSnapshot1.toString());
                    Clinic clinic = dataSnapshot1.getValue(Clinic.class);
                    clinic.setId(dataSnapshot1.getKey());
                    displayLog("id "+clinic.getId());
                    displayLog("name "+clinic.getName());
                    displayLog("country "+clinic.getCountry());
                    displayLog("city "+clinic.getCity());
                    StockManagement.getClinicList().add(clinic);

                }
                try {
                    //StockManagement.setClinicList(clinicList);
                    displayLog("clinic list size "+StockManagement.getClinicList().size());
                    StockManagement.getClinicAdapter().notifyDataSetChanged();
                }
                catch (Exception e){
                    displayLog("stock management error "+e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
/*
        // Read from the database
        myRef.child("A").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iter.iterator();
                while (iterator.hasNext()) {
                    //displayLog("one "+iterator.toString());
                    DataSnapshot dataSnapshot1 = iterator.next();
                    displayLog("A "+dataSnapshot1.toString());

                    //iterator.next();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iter.iterator();
                while (iterator.hasNext()) {
                    //displayLog("one "+iterator.toString());
                    DataSnapshot dataSnapshot1 = iterator.next();
                    //displayLog(""+dataSnapshot1.toString());
                    displayLog("key "+dataSnapshot1.getKey().toString());
                    displayLog("value "+dataSnapshot1.getValue().toString());

                    //iterator.next();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        */
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
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

    private void populateDatabase(){
        DatabaseReference myRef = database.getReference("clinics");
        //String text = editText.getText().toString().trim();
        //myRef.setValue(text);
        Clinic clinic = new Clinic();
        clinic.setCountry("Kenya");
        clinic.setCity("Eldoret");
        clinic.setName("A");
        //clinic.setNevirapine(6.0);
        //clinic.setStavudine(8.0);
        //clinic.setZidotabine(12.0);
        //myRef.setValue(clinic);
        myRef.child("A").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Uganda");
        clinic.setCity("Tororo");
        clinic.setName("B");
        //clinic.setNevirapine(17.0);
        //clinic.setStavudine(12.0);
        //clinic.setZidotabine(32.0);
        //myRef.setValue(clinic);
        myRef.child("B").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Tanzania");
        clinic.setCity("Moshi");
        clinic.setName("C");
        //clinic.setNevirapine(13.0);
        //clinic.setStavudine(8.0);
        //clinic.setZidotabine(24.0);
        //myRef.setValue(clinic);
        myRef.child("C").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Ethiopia");
        clinic.setCity("Addis Ababa");
        clinic.setName("D");
        //clinic.setNevirapine(9.0);
        //clinic.setStavudine(23.0);
        //clinic.setZidotabine(15.0);
        //myRef.setValue(clinic);
        myRef.child("D").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Sudan");
        clinic.setCity("Khartoum");
        clinic.setName("E");
        //clinic.setNevirapine(18.0);
        //clinic.setStavudine(12.0);
        //clinic.setZidotabine(18.0);
        //myRef.setValue(clinic);
        myRef.child("E").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("South Sudan");
        clinic.setCity("Juba");
        clinic.setName("F");
        //clinic.setNevirapine(12.0);
        //clinic.setStavudine(31.0);
        //clinic.setZidotabine(1.0);
        //myRef.setValue(clinic);
        myRef.child("F").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("DRC");
        clinic.setCity("Kinshasa");
        clinic.setName("G");
        //clinic.setNevirapine(21.0);
        //clinic.setStavudine(31.0);
        //clinic.setZidotabine(12.0);
        //myRef.setValue(clinic);
        myRef.child("G").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Zimbabwe");
        clinic.setCity("Harare");
        clinic.setName("H");
        //clinic.setNevirapine(20.0);
        //clinic.setStavudine(19.0);
        //clinic.setZidotabine(40.0);
        //myRef.setValue(clinic);
        myRef.child("H").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Mozambique");
        clinic.setCity("Maputo");
        clinic.setName("I");
        //clinic.setNevirapine(20.0);
        //clinic.setStavudine(10.0);
        //clinic.setZidotabine(27.0);
        //myRef.setValue(clinic);
        myRef.child("I").setValue(clinic);

        //Clinic clinic = new Clinic();
        clinic.setCountry("Malawi");
        clinic.setCity("Lilongwe");
        clinic.setName("J");
        //clinic.setNevirapine(21.0);
        //clinic.setStavudine(31.0);
        //clinic.setZidotabine(10.0);
        //myRef.setValue(clinic);
        myRef.child("J").setValue(clinic);

        DatabaseReference myRef2 = database.getReference("inventory");

        Clinic clinic1 = new Clinic();

        clinic1.setNevirapine(6.0);
        clinic1.setStavudine(8.0);
        clinic1.setZidotabine(12.0);
        myRef2.child("A").setValue(clinic1);

        clinic1.setNevirapine(17.0);
        clinic1.setStavudine(12.0);
        clinic1.setZidotabine(32.0);
        myRef2.child("B").setValue(clinic1);

        clinic1.setNevirapine(13.0);
        clinic1.setStavudine(8.0);
        clinic1.setZidotabine(24.0);
        myRef2.child("C").setValue(clinic1);

        clinic1.setNevirapine(9.0);
        clinic1.setStavudine(23.0);
        clinic1.setZidotabine(15.0);
        myRef2.child("D").setValue(clinic1);

        clinic1.setNevirapine(18.0);
        clinic1.setStavudine(12.0);
        clinic1.setZidotabine(18.0);
        myRef2.child("E").setValue(clinic1);

        clinic.setNevirapine(12.0);
        clinic.setStavudine(31.0);
        clinic.setZidotabine(1.0);
        myRef2.child("F").setValue(clinic1);

        clinic1.setNevirapine(21.0);
        clinic1.setStavudine(31.0);
        clinic1.setZidotabine(12.0);
        myRef2.child("G").setValue(clinic1);

        clinic1.setNevirapine(20.0);
        clinic1.setStavudine(19.0);
        clinic1.setZidotabine(40.0);
        myRef2.child("H").setValue(clinic1);

        clinic1.setNevirapine(20.0);
        clinic1.setStavudine(10.0);
        clinic1.setZidotabine(27.0);
        myRef2.child("I").setValue(clinic1);

        clinic1.setNevirapine(21.0);
        clinic1.setStavudine(31.0);
        clinic1.setZidotabine(10.0);
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
