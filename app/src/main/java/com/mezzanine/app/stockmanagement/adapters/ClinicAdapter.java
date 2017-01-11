package com.mezzanine.app.stockmanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.models.Clinic;
import com.mezzanine.app.stockmanagement.utilities.Utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ramogiochola on 1/4/17.
 *
 * The clinic adapter displays all clinics. The details of the clinic, the stock in each clinic.
 *
 *
 */

public class ClinicAdapter extends ArrayAdapter<Clinic> {
    private static final String TAG = "ClinicAdapter";
    Context context;
    List<Clinic> rowItems;
    HashMap<Clinic, Integer> mIdMap = new HashMap<>();
    FirebaseDatabase database;
    DatabaseReference myClinics;
    Utilities utilities;

    public ClinicAdapter(MainActivity context, List<Clinic> rowItems, FirebaseDatabase database) {
        super(context, 0, rowItems);
        this.context = context;
        this.rowItems = rowItems;
        this.database = database;
        utilities = new Utilities();
        myClinics = database.getReference("inventory");
        displayLog("row items size " + rowItems);
        for (int i = 0; i < rowItems.size(); ++i) {
            mIdMap.put(rowItems.get(i), i);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Clinic row_pos = rowItems.get(position);
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
                viewHolder.location = (TextView) convertView.findViewById(R.id.location);
                viewHolder.clinic = (TextView) convertView.findViewById(R.id.clinicName);
                viewHolder.nevarapineItemsTextView = (TextView) convertView.findViewById(R.id.itemsNevarapine);
                viewHolder.stavudineItemsTextView = (TextView) convertView.findViewById(R.id.itemsStavudine);
                viewHolder.zidotabineItemsTextView = (TextView) convertView.findViewById(R.id.itemsZidotabine);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                viewHolder.key = row_pos.getId();
                viewHolder.clinicItem = row_pos;

                viewHolder.clinic.setText(row_pos.getName());
                viewHolder.location.setText(row_pos.getCity() + ", " + row_pos.getCountry());

                try {
                    viewHolder.nevarapineDispenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String value = viewHolder.nevarapineItemsTextView.getText().toString().trim();
                            if (value.equalsIgnoreCase("Out of stock")) {
                                displayToast("Cannot dispense, please add stock");
                            } else {
                                int valBefore;
                                int valAfter;
                                String[] result = value.split("\\s");
                                for (int x = 0; x < result.length; x++) {
                                    displayLog("result " + result[x]);
                                }
                                valBefore = Integer.parseInt(result[0]);
                                valAfter = valBefore - 1;
                                row_pos.setNevirapine(valAfter);
                                try {
                                    StockManagement.getNotificationMessages().clear();
                                } catch (Exception e) {
                                    displayLog("Error clearing notification messages " + e.toString());
                                }
                                if (valAfter == 1) {
                                    viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine().toString() + " item");
                                    viewHolder.nevarapineDispenseButton.setEnabled(true);
                                } else if (valAfter == 0) {
                                    viewHolder.nevarapineItemsTextView.setText("Out of stock");
                                    viewHolder.nevarapineDispenseButton.setEnabled(false);
                                } else {
                                    viewHolder.nevarapineItemsTextView.setText(row_pos.getNevirapine().toString() + " items");
                                    viewHolder.nevarapineDispenseButton.setEnabled(true);
                                }
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("nevirapine", row_pos.getNevirapine());
                                StockManagement.getMyInventory().child(row_pos.getId()).updateChildren(childUpdates);
                            }
                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button setonclicklistener " + e.toString());
                }

                try {
                    viewHolder.nevarapineAddStockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.nevarapineItemsTextView.getText().toString().trim();
                            int currentValue;
                            if (value.equalsIgnoreCase("Out of Stock")) {
                                currentValue = 0;
                            } else {
                                String[] result = value.split("\\s");
                                currentValue = Integer.parseInt(result[0]);
                            }
                            utilities.showAddStockDialogBox(context, StockManagement.getMyInventory(), row_pos.getId(), row_pos.getName(), "nevirapine", currentValue);
                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button setonclicklistener " + e.toString());
                }

                try {
                    viewHolder.stavudineDispenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.stavudineItemsTextView.getText().toString().trim();
                            if (value.equalsIgnoreCase("Out of stock")) {
                                displayToast("Cannot dispense Stavudine, please add stock");
                            } else {
                                int valBefore;
                                int valAfter;
                                String[] result = value.split("\\s");
                                for (int x = 0; x < result.length; x++) {
                                    displayLog("result " + result[x]);
                                }
                                valBefore = Integer.parseInt(result[0]);
                                valAfter = valBefore - 1;
                                row_pos.setStavudine(valAfter);
                                try {
                                    StockManagement.getNotificationMessages().clear();
                                } catch (Exception e) {
                                    displayLog("Error clearing notification messages " + e.toString());
                                }
                                if (valAfter == 1) {
                                    viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine().toString() + " item");
                                    viewHolder.stavudineDispenseButton.setEnabled(true);
                                } else if (valAfter == 0) {
                                    viewHolder.stavudineItemsTextView.setText("Out of stock");
                                    viewHolder.stavudineDispenseButton.setEnabled(false);
                                } else {
                                    viewHolder.stavudineItemsTextView.setText(row_pos.getStavudine().toString() + " items");
                                    viewHolder.stavudineDispenseButton.setEnabled(true);
                                }
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("stavudine", row_pos.getStavudine());
                                StockManagement.getMyInventory().child(row_pos.getId()).updateChildren(childUpdates);
                            }
                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button stavudine setonclicklistener " + e.toString());
                }

                try {
                    viewHolder.stavudineAddStockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.stavudineItemsTextView.getText().toString().trim();
                            int currentValue;
                            if (value.equalsIgnoreCase("Out of Stock")) {
                                currentValue = 0;
                            } else {
                                String[] result = value.split("\\s");
                                currentValue = Integer.parseInt(result[0]);
                            }
                            utilities.showAddStockDialogBox(context, StockManagement.getMyInventory(), row_pos.getId(), row_pos.getName(), "stavudine", currentValue);

                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button setonclicklistener " + e.toString());
                }

                try {
                    viewHolder.zidotabineDispenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.zidotabineItemsTextView.getText().toString().trim();
                            if (value.equalsIgnoreCase("Out of stock")) {
                                displayToast("Cannot dispense zidotabine, please add stock");
                            } else {
                                int valBefore;
                                int valAfter;
                                String[] result = value.split("\\s");
                                for (int x = 0; x < result.length; x++) {
                                    displayLog("result " + result[x]);
                                }
                                valBefore = Integer.parseInt(result[0]);
                                valAfter = valBefore - 1;
                                row_pos.setZidotabine(valAfter);
                                try {
                                    StockManagement.getNotificationMessages().clear();
                                } catch (Exception e) {
                                    displayLog("Error clearing notification messages " + e.toString());
                                }
                                if (valAfter == 1) {
                                    viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine().toString() + " item");
                                    viewHolder.zidotabineDispenseButton.setEnabled(true);
                                } else if (valAfter == 0) {
                                    viewHolder.zidotabineItemsTextView.setText("Out of stock");
                                    viewHolder.zidotabineDispenseButton.setEnabled(false);
                                } else {
                                    viewHolder.zidotabineItemsTextView.setText(row_pos.getZidotabine().toString() + " items");
                                    viewHolder.zidotabineDispenseButton.setEnabled(true);
                                }
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("zidotabine", row_pos.getZidotabine());
                                StockManagement.getMyInventory().child(row_pos.getId()).updateChildren(childUpdates);
                            }
                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button setonclicklistener " + e.toString());
                }

                try {
                    viewHolder.zidotabineAddStockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.zidotabineItemsTextView.getText().toString().trim();
                            int currentValue;
                            if (value.equalsIgnoreCase("Out of Stock")) {
                                currentValue = 0;
                            } else {
                                String[] result = value.split("\\s");
                                currentValue = Integer.parseInt(result[0]);
                            }
                            utilities.showAddStockDialogBox(context, StockManagement.getMyInventory(), row_pos.getId(), row_pos.getName(), "zidotabine", currentValue);
                        }
                    });
                } catch (Exception e) {
                    displayLog("Error button setonclicklistener " + e.toString());
                }

                try {
                    //this is to update the stock level of each drug in realtime.
                    myClinics.child(viewHolder.clinicItem.getId()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                            Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                            Iterator<DataSnapshot> iterator = iter.iterator();
                            while (iterator.hasNext()) {
                                //displayLog("viewholder key " + viewHolder.key + " row_pos key " + row_pos.getId());

                                DataSnapshot dataSnapshot1 = iterator.next();
                                displayLog(row_pos.getId() + " " + dataSnapshot1.toString());
                                String drug = dataSnapshot1.getKey().toString();
                                Integer value = Integer.parseInt(dataSnapshot1.getValue().toString());
                                switch (drug) {
                                    case "nevirapine":
                                        viewHolder.clinicItem.setNevirapine(value);
                                        if (value == 1) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.nevarapineItemsTextView.setText(viewHolder.clinicItem.getNevirapine().toString() + " item");
                                                viewHolder.nevarapineDispenseButton.setEnabled(true);
                                            }
                                        } else if (value == 0) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.nevarapineItemsTextView.setText("Out of stock");
                                                viewHolder.nevarapineDispenseButton.setEnabled(false);
                                            }
                                        } else {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.nevarapineItemsTextView.setText(viewHolder.clinicItem.getNevirapine().toString() + " items");
                                                viewHolder.nevarapineDispenseButton.setEnabled(true);
                                            }
                                        }

                                        break;
                                    case "zidotabine":
                                        viewHolder.clinicItem.setZidotabine(value);
                                        if (value == 1) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.zidotabineItemsTextView.setText(viewHolder.clinicItem.getZidotabine().toString() + " item");
                                                viewHolder.zidotabineDispenseButton.setEnabled(true);
                                            }
                                        } else if (value == 0) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.zidotabineItemsTextView.setText("Out of stock");
                                                viewHolder.zidotabineDispenseButton.setEnabled(false);
                                            }
                                        } else {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.zidotabineItemsTextView.setText(viewHolder.clinicItem.getZidotabine().toString() + " items");
                                                viewHolder.zidotabineDispenseButton.setEnabled(true);
                                            }
                                        }

                                        break;
                                    case "stavudine":
                                        viewHolder.clinicItem.setStavudine(value);

                                        if (value == 1) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.stavudineItemsTextView.setText(viewHolder.clinicItem.getStavudine().toString() + " item");
                                                viewHolder.stavudineDispenseButton.setEnabled(true);
                                            }
                                        } else if (value == 0) {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.stavudineItemsTextView.setText("Out of stock");
                                                viewHolder.stavudineDispenseButton.setEnabled(false);
                                            }
                                        } else {
                                            if (viewHolder.key.equalsIgnoreCase(row_pos.getId())) {
                                                viewHolder.stavudineItemsTextView.setText(viewHolder.clinicItem.getStavudine().toString() + " items");
                                                viewHolder.stavudineDispenseButton.setEnabled(true);
                                            }
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

                } catch (Exception e) {
                    displayLog("error items " + e.toString());
                }
            } catch (Exception e) {
                displayLog("Error rowItems.get " + e.toString());
            }

        } catch (Exception e) {
            displayLog("convert view error " + e.toString());
        }
        return convertView;
    }

    private void displayToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void displayLog(String s) {
        Log.i(TAG, s);
    }

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
        String key;
        Clinic clinicItem;
    }
}
