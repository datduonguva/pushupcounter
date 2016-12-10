package com.duongCompany.duong.pushupcounter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.duongCompany.duong.pushupcounter.data.PushContract.*;

/**
 * Created by duong on 12/1/2016.
 */

public class PushDbHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "pushup.db";
    private final static int DATABASE_VERSION = 1;

    public PushDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE = "CREATE TABLE " +
                PushEntry.TABLE_NAME + "(" +
                PushEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PushEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                PushEntry.COLUMN_COUNT + " INTEGER NOT NULL, "+
                PushEntry.COLUMN_CALORIES +" INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE);

        String PARAMETERS_OPEN_SQL = "CREATE TABLE "+
                Parameter.TABLE_NAME + " ("+
                Parameter.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Parameter.COLUMN_WEIGHT+ " REAL NOT NULL, "+
                Parameter.COLUMN_HEIGHT+ " INTEGER NOT NULL);";

        db.execSQL(PARAMETERS_OPEN_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
