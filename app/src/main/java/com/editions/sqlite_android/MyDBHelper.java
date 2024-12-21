package com.editions.sqlite_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MyDBHelper extends SQLiteOpenHelper {

    //Database Creation & Variables Declaration Here
    private static final String DATABASE_NAME = "Student.db";
    private static final int version = 2;
    private static final String TABLE_NAME = "Student_table";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_Address = "address";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";

    //Create Table Query
    private static final String CREATE_TABLE =("CREATE TABLE " + TABLE_NAME + "" +
            "(" + KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" VARCHAR(255), "+KEY_Address+" VARCHAR(255), "+KEY_AGE+" INTEGER, "+KEY_GENDER+" VARCHAR(15))" );

    //Select Query for Fetch Data
    String selectQuery = "SELECT * FROM " + TABLE_NAME;
    //Drop Table Query
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //Constructor
    private Context context;
    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
        this.context = context;
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Toast.makeText(context, "OnCreate is Called", Toast.LENGTH_SHORT).show();
            db.execSQL(CREATE_TABLE);

        }catch (Exception e){
            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    //Update Table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "OnUpgrade is Called", Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (Exception e){
            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    //Insert Data Method
    public long insertData(String name, String address, int age, String gender) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_Address, address);
        contentValues.put(KEY_AGE, age);
        contentValues.put(KEY_GENDER, gender);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);// if Successfully Insert Data return 1, else return -1 in result

        return result;
    }//

    public Cursor FetchData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Select Query for Fetch Data from Database
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        return cursor;
    }




}//Main Class
