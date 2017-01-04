package com.mezzanine.app.stockmanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static android.app.Activity.*;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class ClinicAdapter extends ArrayAdapter<Clinic> {
    private static final String TAG = "ClinicAdapter";
    Context context;
    List<Clinic> rowItems;
    HashMap<Clinic, Integer> mIdMap = new HashMap<>();
    FirebaseDatabase database;
    DatabaseReference myClinics ;

    // View lookup cache
    private static class ViewHolder {
        Button nevarapineDispenseButton;
        Button nevarapineAddStockButton;
        Button stavudineAddStockButton;
        Button stavudineDispenseButton;
        Button zidotabineAddStockButton;
        Button zidotabineDispenseButton;
        TextView nevarapineItemsTextView;
        TextView stavudineItemsTextView;
        TextView zidotabineItemsTextView;
        TextView clinic;
        TextView location;
    }

    public ClinicAdapter(MainActivity context, List<Clinic> rowItems,FirebaseDatabase database) {
        super(context, 0, rowItems);
        this.context = context;
        this.rowItems = rowItems;
        this.database = database;
        myClinics = database.getReference("inventory");
        displayLog("row items size "+rowItems);
        for (int i = 0; i < rowItems.size(); ++i) {
            mIdMap.put(rowItems.get(i), i);
        }
    }
/*
    @Override
    public long getItemId(int position) {
        Clinic item = getItem(position);
        return mIdMap.get(item).longValue();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        try {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.clinicitem, null);
                viewHolder.zidotabineAddStockButton = (Button) convertView.findViewById(R.id.btnZidotabineAddStock);
                viewHolder.nevarapineAddStockButton = (Button) convertView.findViewById(R.id.btnNevarapineAddStock);
                viewHolder.stavudineAddStockButton = (Button) convertView.findViewById(R.id.btnStavudineAddStock);
                viewHolder.zidotabineDispenseButton = (Button) convertView.findViewById(R.id.btnZidotabineDispense);
                viewHolder.nevarapineDispenseButton = (Button) convertView.findViewById(R.id.btnNevarapineDispense);
                viewHolder.stavudineDispenseButton = (Button) convertView.findViewById(R.id.btnStavudineDispense);
                viewHolder.location = (TextView)convertView.findViewById(R.id.location);
                viewHolder.clinic = (TextView) convertView.findViewById(R.id.clinicName);
                viewHolder.nevarapineItemsTextView = (TextView) convertView.findViewById(R.id.itemsNevarapine);
                viewHolder.stavudineItemsTextView = (TextView) convertView.findViewById(R.id.itemsStavudine);
                viewHolder.zidotabineItemsTextView = (TextView) convertView.findViewById(R.id.itemsZidotabine);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try{
                final Clinic row_pos = rowItems.get(position);
                viewHolder.clinic.setText(row_pos.getId());
                viewHolder.location.setText(row_pos.getCity()+", "+row_pos.getCountry());
/*
                if(row_pos.getNevirapine() == 1){
                    viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine()+" item");
                }
                else if(row_pos.getNevirapine() == 0){
                    viewHolder.nevarapineItemsTextView.setText("Out of stock");
                }
                else {
                    viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine() + " items");
                }
                if(row_pos.getStavudine() == 1){
                    viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine()+" item");
                }
                else if(row_pos.getStavudine() == 0){
                    viewHolder.stavudineItemsTextView.setText("Out of stock");
                }
                else {
                    viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine() + " items");
                }
                if(row_pos.getZidotabine() == 1){
                    viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine()+" item");
                }
                else if(row_pos.getZidotabine() == 0){
                    viewHolder.zidotabineItemsTextView.setText("Out of stock");
                }
                else {
                    viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine() + " items");
                }

*/
                    try{

                    myClinics.child(row_pos.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                            Iterator<DataSnapshot> iterator = iter.iterator();
                            while (iterator.hasNext()) {
                                DataSnapshot dataSnapshot1 = iterator.next();
                                displayLog(row_pos.getId()+" "+dataSnapshot1.toString());
                                String drug = dataSnapshot1.getKey().toString();
                                Double value =  ((Long)dataSnapshot1.getValue()).doubleValue();
                                switch (drug){
                                    case "nevirapine":
                                        row_pos.setNevirapine(value);
                                        if(value == 1){
                                            viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine()+" item");
                                        }
                                        else if(value == 0){
                                            viewHolder.nevarapineItemsTextView.setText("Out of stock");
                                        }
                                        else{
                                            viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine()+" items");
                                        }
                                        break;
                                    case "zidotabine":
                                        row_pos.setZidotabine(value);
                                        if(value == 1){
                                            viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine()+" item");
                                        }
                                        else if(value == 0){
                                            viewHolder.zidotabineItemsTextView.setText("Out of stock");
                                        }
                                        else{
                                            viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine()+" items");
                                        }
                                        break;
                                    case "stavudine":
                                        row_pos.setStavudine(value);
                                        if(value == 1){
                                            viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine()+" item");
                                        }
                                        else if(value == 0){
                                            viewHolder.stavudineItemsTextView.setText("Out of stock");
                                        }
                                        else{
                                            viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine()+" items");
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
                catch (Exception e){
                    displayLog("error items "+e.toString());
                }

                try {
                    viewHolder.nevarapineDispenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.nevarapineItemsTextView.getText().toString().trim();
                            StringTokenizer 
                        }
                    });
                }
                catch (Exception e){
                    displayLog("Error button setonclicklistener "+e.toString());
                }

            }
            catch (Exception e){
                displayLog("Error rowItems.get "+e.toString());
            }

        } catch (Exception e) {
            displayLog("convert view error "+e.toString());
        }
        return convertView;
    }

    private void displayLog(String me){
        Log.i(TAG,me);
    }
}
