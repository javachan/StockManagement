package com.mezzanine.app.stockmanagement.utilities;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.mezzanine.app.stockmanagement.R;
import com.mezzanine.app.stockmanagement.StockManagement;
import com.mezzanine.app.stockmanagement.activities.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ramogiochola on 1/4/17.
 *
 * Provides common methods to the entire class. Local method library of sorts for the app.
 *
 */

public class Utilities {
    private static final String TAG = "Utilities";
    private static int value = 0;
    Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
    private int NOTIFICATION_ID = 1;
    private NotificationCompat.Builder mCopat;

    public Utilities() {
    }

    //Converts the first letter of each word in a string to capital letter
    public static String toTitleCase(String givenString) {

        if ((givenString.equalsIgnoreCase("null")) || (givenString == null)) {
            givenString = "";
        }
        String current = givenString.trim();
        StringTokenizer st = new StringTokenizer(current);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            String now = st.nextToken().trim();
            sb.append(now.substring(0, 1).toUpperCase() + now.substring(1).toLowerCase() + " ");
        }
        return sb.toString().trim();
    }

    //shows a dialog box where the user can add inventory and updates the database with this value.
    public void showAddStockDialogBox(Context context, final DatabaseReference myClinics,
                                      final String key, String clinicnName, final String drug,
                                      final int currentValue) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setDimAmount(1.0f);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addstockpopup);

        final EditText addStockEditText = (EditText) dialog.findViewById(R.id.itemsEditText);

        TextView textView = (TextView) dialog.findViewById(R.id.textView2);
        if (clinicnName.length() > 0) {
            textView.setText("Enter the number of items to be added to " + drug + " for " + clinicnName);
        } else {
            textView.setText("Enter the number of items to be added to " + drug);
        }

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
                if (value.length() > 0) {
                    try {
                        Integer valAdd = Integer.parseInt(value);
                        Integer valAfter = valAdd + currentValue;
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(drug.toLowerCase(), valAfter);
                        try {
                            StockManagement.getNotificationMessages().clear();
                        } catch (Exception e) {
                            displayLog("Error clearing notification messages " + e.toString());
                        }
                        //update the realtime database
                        myClinics.child(key).updateChildren(childUpdates);
                    } catch (Exception e) {
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //sends the notification to the android subsystem for clinics with low inventory levels
    public void notifyDriver(List<String> messages) {
        int msgs = messages.size();
        displayLog("message size " + msgs);
        if (msgs > 0) {
            inboxStyle = new Notification.InboxStyle();
            NotificationManager nManager = (NotificationManager) StockManagement.getMainActivity().getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(StockManagement.getMainActivity());
            builder.setContentTitle("Stock Management");
            String longMessage = "";
            for (String value : messages) {
                longMessage = value+" "+System.getProperty("line.separator");
            }
            builder.setContentText(longMessage);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setAutoCancel(true);
            inboxStyle.setBigContentTitle("Warning");
            for (String value : messages) {
                inboxStyle.addLine(value);
            }

            Intent notificationIntent = new Intent(StockManagement.getMainActivity(), MainActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(StockManagement.getMainActivity(), 0, notificationIntent, 0);
            //builder.setDeleteIntent(pendingIntent);
            builder.setContentIntent(pendingIntent);

            builder.setStyle(inboxStyle);
            nManager.notify("Stock Management", NOTIFICATION_ID, builder.build());
            StockManagement.setNotificationManager(nManager);
        } else {
            try {
                //StockManagement.getNotificationManager().cancelAll();
            } catch (Exception e) {
                displayLog("Error canceling notification " + e.toString());
            }
        }

    }

    private void displayLog(String s) {
        Log.i(TAG, s);
    }

}
