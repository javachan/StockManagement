package com.mezzanine.app.stockmanagement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICCITY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICCOUNTRY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICKEY;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_CLINICNAME;
import static com.mezzanine.app.stockmanagement.utilities.Constants.COLUMN_ID;
import static com.mezzanine.app.stockmanagement.utilities.Constants.TABLE_CLINICDETAILS;

/**
 * Created by ramogiochola on 1/7/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "stockmanagement.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE_CLINIC =
            "create table " + TABLE_CLINICDETAILS + " (" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_CLINICKEY +" VARCHAR, "+
                    COLUMN_CLINICNAME +" VARCHAR, "+
                    COLUMN_CLINICCITY +" VARCHAR, "+
                    COLUMN_CLINICCOUNTRY +" VARCHAR, "+
                    "UNIQUE ( "+COLUMN_CLINICKEY +" ) ON CONFLICT REPLACE);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_CLINIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLINICDETAILS);
        onCreate(db);
    }
}
