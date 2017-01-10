package com.mezzanine.app.stockmanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class InventoryAdapter  extends ArrayAdapter<Clinic> {
    private static final String TAG = "InventoryAdapter";
    Context context;
    List<Clinic> rowItems;
    HashMap<Clinic, Integer> mIdMap = new HashMap<>();
    DatabaseReference myClinics ;
    Utilities utilities ;

    // View lookup cache
    private static class ViewHolder {
        TextView clinic;
        TextView location;
        LinearLayout relativeLayoutDrugList;
        //ListView listViewDrugList;
        //DrugAdapter drugAdapter;
    }
    public InventoryAdapter(MainActivity context,List<Clinic> rowItems) {
        super(context, 0, rowItems);
        this.context = context;
        this.rowItems = rowItems;
        utilities = new Utilities();
        myClinics = StockManagement.getDatabase().getReference("inventory");
        displayLog("row items size "+rowItems.size());
        for (int i = 0; i < rowItems.size(); ++i) {
            mIdMap.put(rowItems.get(i), i);
        }
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InventoryAdapter.ViewHolder viewHolder;

        try {
            if (convertView == null) {
                viewHolder = new InventoryAdapter.ViewHolder();
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.clinictobestocked, null);
                viewHolder.relativeLayoutDrugList = (LinearLayout) convertView.findViewById(R.id.rlDrugList);
                //viewHolder.listViewDrugList = (ListView) convertView.findViewById(R.id.drugItemsListView);
                viewHolder.location = (TextView) convertView.findViewById(R.id.location);
                viewHolder.clinic = (TextView) convertView.findViewById(R.id.clinicName);
                //viewHolder.drugList = StockManagement.getClinicInventoryHashMap().get(row_pos.getId());
                //viewHolder.drugAdapter = new DrugAdapter(StockManagement.getMainActivity(),viewHolder.drugList);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (InventoryAdapter.ViewHolder) convertView.getTag();
            }
            try{
                final Clinic row_pos = rowItems.get(position);
                viewHolder.clinic.setText(row_pos.getName());
                viewHolder.location.setText(row_pos.getCity()+", "+row_pos.getCountry());
                try{
                    List<Inventory> drugList = StockManagement.getClinicInventoryHashMap().get(row_pos.getId());
                    displayLog("drug list size "+drugList.size());
                    if(drugList.size() == 0){
                        Clinic clinic  = getItem(position);
                        List<Clinic> clinicList1 = StockManagement.getInventoryList();
                        for(int y = 0;y<clinicList1.size();y++){
                            Clinic clinic1 = clinicList1.get(y);
                            if(clinic1.getId() == clinic.getId()){
                                StockManagement.getInventoryList().remove(y);
                                //StockManagement.getInventoryAdapter().remove(clinic);
                            }
                        }

                        StockManagement.getInventoryAdapter().notifyDataSetChanged();
                        StockManagement.getClinicAdapter().notifyDataSetChanged();

                    }
                    viewHolder.relativeLayoutDrugList.removeAllViews();
                    for(Inventory inventory : drugList){
                        View vi = StockManagement.getMainActivity().getLayoutInflater().inflate(R.layout.drugitemslist, null);
                        final TextView items = (TextView) vi.findViewById(R.id.txtDrugItems);
                        int val = Integer.parseInt(inventory.getDrugItems());
                        if(val == 1){
                            items.setText(val+" item");
                        }
                        else if(val == 0){
                            items.setText("Out of stock");
                        }
                        else{
                            items.setText(val+" items");
                        }
                        final TextView name = (TextView) vi.findViewById(R.id.txtDrugName);
                        name.setText(utilities.toTitleCase(inventory.getDrugName()));
                        Button addStock = (Button) vi.findViewById(R.id.btnAddStock);
                        addStock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                displayLog("add stock clicked");
                                String value = items.getText().toString().trim();
                                int currentValue;
                                if(value.equalsIgnoreCase("Out of Stock")){
                                    currentValue = 0;
                                }
                                else {
                                    String[] result = value.split("\\s");
                                    currentValue = Integer.parseInt(result[0]);
                                }
                                utilities.showAddStockDialogBox(StockManagement.getMainActivity(),
                                        myClinics,row_pos.getId(),row_pos.getName(),name.getText().toString().trim(),currentValue);
                            }
                        });

                            displayLog(utilities.toTitleCase(row_pos.getName())+" is low on "+inventory.getDrugName().toUpperCase());
                            StockManagement.getNotificationMessages().add(utilities.toTitleCase(row_pos.getName())+" is low on "+inventory.getDrugName().toUpperCase());
                            utilities.notifyDriver(StockManagement.getNotificationMessages());

                        viewHolder.relativeLayoutDrugList.addView(vi);
                    }

                }
                catch (Exception e){
                    displayLog("Error inflating view "+e.toString());
                }
            }
            catch (Exception e){
                displayLog("clinic to be stocked error "+e.toString());
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

        Log.i(TAG,s);
    }
}
