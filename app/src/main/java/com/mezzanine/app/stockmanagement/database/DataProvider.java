package com.mezzanine.app.stockmanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mezzanine.app.stockmanagement.models.Clinic;

import java.util.ArrayList;
import java.util.List;

import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICCITY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICCOUNTRY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICKEY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICNAME;
import static com.mezzanine.app.stockmanagement.utilities.Constants.TABLE_CLINICDETAILS;

/**
 * Created by ramogiochola on 1/7/17.
 *
 * Interacts with the local database, all queries to the database come through here
 *
 */

public class DataProvider {
    private static final String TAG = "DataProvider";
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public DataProvider(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void openRead() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void openWrite() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertClinic(Clinic clinic) {
        displayLog("insert clinic called");
        long result = 0;
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLINICKEY, clinic.getId());
        values.put(COLUMN_CLINICNAME, clinic.getName());
        values.put(COLUMN_CLINICCITY, clinic.getCity());
        values.put(COLUMN_CLINICCOUNTRY, clinic.getCountry());

        try {
            openWrite();
            result = database.insert(TABLE_CLINICDETAILS, null, values);
            displayLog("insert clinic executed " + result);
        } catch (Exception e) {
            displayLog("Insert clinic error " + e.toString());
        } finally {
            close();
        }
        return result;
    }

    public Clinic getClinicDetails(String key) {
        displayLog("get clinic details called");
        Clinic clinic = new Clinic();
        String[] columns = {COLUMN_CLINICKEY, COLUMN_CLINICNAME, COLUMN_CLINICCITY, COLUMN_CLINICCOUNTRY};
        String selection = COLUMN_CLINICKEY + " = ? ";
        String[] selectionArgs = {key};
        try {
            openRead();
            Cursor cursor = database.query(TABLE_CLINICDETAILS, columns, selection, selectionArgs, null, null, null);
            displayLog("get clinic details executed");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                clinic = cursorToClinic(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            displayLog("Error getting clinic details " + e.toString());
        }
        return clinic;
    }

    public void deleteAllClinics() {
        displayLog("deleteAllClinics method called");
        try {
            openWrite();
            database.delete(TABLE_CLINICDETAILS, null, null);
            displayLog("deleteAllClinics() method executed");

        } catch (SQLException sqle) {
            displayLog("deleteAllClinics() error " + sqle.toString());

        } finally {
            close();
        }
    }

    public List<Clinic> getAllClinics() {
        displayLog("get all clinics called");
        List<Clinic> clinicList = new ArrayList<>();
        Clinic clinic = new Clinic();
        String[] columns = {COLUMN_CLINICKEY, COLUMN_CLINICNAME, COLUMN_CLINICCITY, COLUMN_CLINICCOUNTRY};
        try {
            openRead();
            Cursor cursor = database.query(TABLE_CLINICDETAILS, columns, null, null, null, null, null);
            displayLog("get all clinics executed");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                clinic = cursorToClinic(cursor);
                clinicList.add(clinic);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            displayLog("Error getting clinic details " + e.toString());
        }
        return clinicList;
    }

    private Clinic cursorToClinic(Cursor cursor) {
        Clinic clinic = new Clinic();
        clinic.setId(cursor.getString(0));
        clinic.setName(cursor.getString(1));
        clinic.setCity(cursor.getString(2));
        clinic.setCountry(cursor.getString(3));
        return clinic;
    }

    private void displayLog(String s) {
        Log.i(TAG,s);
    }
}
