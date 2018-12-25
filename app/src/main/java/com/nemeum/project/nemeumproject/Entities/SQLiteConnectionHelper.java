package com.nemeum.project.nemeumproject.Entities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nemeum.project.nemeumproject.Utilities.Utilities;

public class SQLiteConnectionHelper extends SQLiteOpenHelper {



    public SQLiteConnectionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(Utilities.CREATE_SCENARIOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scenarios");
        onCreate(db);
    }
}
