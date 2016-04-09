package com.hobbythai.android.ksrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ks on 4/9/16 AD.
 */
public class MyOpenHelper extends SQLiteOpenHelper{

    //explicit
    public static final String database_name = "Restaurant.db";
    private static final int database_version = 1;

    private static final String create_user_table = "create table userTABLE (" +
            "_id integer primary key, " +
            "User text, " +
            "Password text, " +
            "Name text);";

    private static final String create_food_table = "create table foodTABLE (" +
            "_id integer primary key, " +
            "Food text, " +
            "Price text," +
            "Source text);";


    public MyOpenHelper(Context context) {
        super(context, database_name, null, database_version);
    }//constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_food_table);
        sqLiteDatabase.execSQL(create_user_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}//main class
