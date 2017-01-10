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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.activities.MainActivity;
import com.mezzanine.app.stockmanagement.models.Clinic;
import com.mezzanine.app.stockmanagement.models.Inventory;
import com.mezzanine.app.stockmanagement.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class DrugAdapter extends ArrayAdapter<Inventory> {
    private static final String TAG = "DrugAdapter";
    Context context;
    List<Inventory> rowItems;
    HashMap<Inventory, Integer> mIdMap = new HashMap<>();
    DatabaseReference myClinics ;

    // View lookup cache
    private static class ViewHolder {
        Button addStockButton;
        TextView drugName;
        TextView drugItems;
    }
    public DrugAdapter(MainActivity context, List<Inventory> rowItems) {
        super(context, 0, rowItems);
        this.context = context;
        this.rowItems = rowItems;
        myClinics = StockManagement.getDatabase().getReference("inventory");
        displayLog("row items size "+rowItems.size());
        for (int i = 0; i < rowItems.size(); ++i) {
            mIdMap.put(rowItems.get(i), i);
            //displayLog("row item key "+rowItems.get(i).getId());
            //displayLog("row item name "+rowItems.get(i).getDrugName());
            //displayLog("row item items "+rowItems.get(i).getDrugItems());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DrugAdapter.ViewHolder viewHolder;

        try {
            if (convertView == null) {
                viewHolder = new DrugAdapter.ViewHolder();
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drugitem2, null);
                viewHolder.addStockButton = (Button) convertView.findViewById(R.id.btnAddStock);
                viewHolder.drugItems = (TextView) convertView.findViewById(R.id.txtDrugItems);
                viewHolder.drugName = (TextView) convertView.findViewById(R.id.txtDrugName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (DrugAdapter.ViewHolder) convertView.getTag();
            }
            try{
                final Inventory row_pos = rowItems.get(position);
                viewHolder.drugItems.setText(row_pos.getDrugItems());
                viewHolder.drugName.setText(row_pos.getDrugName());
                displayLog("row_pos key "+row_pos.getId());
                displayLog("row_pos name "+row_pos.getDrugName());
                displayLog("row_pos items "+row_pos.getDrugItems());
                /*
                try {
                    viewHolder.addStockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = viewHolder.drugItems.getText().toString().trim();
                            int currentValue;
                            if(value.equalsIgnoreCase("Out of Stock")){
                                currentValue = 0;
                            }
                            else{
                                String[] result = value.split("\\s");
                                currentValue = Integer.parseInt(result[0]);
                            }
                            Utilities utilities = new Utilities();
                            utilities.showAddStockDialogBox(context, myClinics,row_pos.getId(),null,row_pos.getDrugName(),currentValue);
                        }
                    });
                }
                catch (Exception e){
                    displayLog("Error button setonclicklistener "+e.toString());
                }
                */
            }
            catch (Exception e){
                displayLog("Error adding view holder "+e.toString());
            }
            return convertView;
        } catch (Exception e) {
            displayLog("convert view error "+e.toString());
        }
        return convertView;
    }


    private void displayToast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    private void displayLog(String s){
        //Log.i(TAG,s);
    }
}
