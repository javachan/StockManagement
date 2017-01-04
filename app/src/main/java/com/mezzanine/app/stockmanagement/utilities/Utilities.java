package com.mezzanine.app.stockmanagement.utilities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.mezzanine.app.stockmanagement.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class Utilities {
    public Utilities(){}
  public void showAddStockDialogBox(Context context, final DatabaseReference myClinics, final String key, String clinicnName, final String drug, final int currentValue){
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
      dialog.getWindow().setDimAmount(1.0f);
      dialog.setCancelable(false);
      dialog.setContentView(R.layout.addstockpopup);

      final EditText addStockEditText = (EditText)dialog.findViewById(R.id.itemsEditText) ;

      TextView textView = (TextView) dialog.findViewById(R.id.textView2);
      textView.setText("Enter the number of items to be added to "+drug+" for Clinic "+clinicnName);

      Button cancelBtn = (Button) dialog.findViewById(R.id.btnCancel);
      cancelBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dialog.dismiss();
          }
      });
      final Button addStockBtn = (Button) dialog.findViewById(R.id.btnAddItems);
      addStockBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String value = addStockEditText.getText().toString().trim();
              if(value.length() > 0){
                  Integer valAdd = Integer.parseInt(value);
                  Integer valAfter = valAdd + currentValue;

                  Map<String, Object> childUpdates = new HashMap<>();
                  childUpdates.put(drug,valAfter);
                  myClinics.child(key).updateChildren(childUpdates);
              }
              dialog.dismiss();
          }
      });
      dialog.show();
  }
}
