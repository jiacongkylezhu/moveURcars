package com.kylezhudev.moveurcars.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;
    private static final String DATABASE_NAME =  "Date_address.db";
    private static final String TABLE_NAME =  "date_and_address_table";
    private static final String COL_1 =  "id";
    private static final String COL_2 =  "year";
    private static final String COL_3 =  "month";
    private static final String COL_4 =  "day_of_month";
    private static final String COL_5 =  "date_of_week_in_month";
    private static final String COL_6 =  "day_of_week";
    private static final String COL_7 =  "hour";
    private static final String COL_8 =  "minute";
    private static final String COL_9 =  "street";
    private static final String COL_10 =  "side_of_street";
    private static final String COL_11 =  "item_index";

    public static DatabaseHelper getInstance(Context context){
        if(mInstance==null){
            return mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_2+" INTEGER, "+COL_3+" INTEGER, "+COL_4+" INTEGER, "+COL_5+" INTEGER, "
                +COL_6+" INTEGER, "+COL_7+" INTEGER, "+COL_8+" INTEGER, "+COL_9+" TEXT, "
                +COL_10+ " TEXT, " +COL_11+" INTEGER)");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isDataBaseExist(){
        File file = new File(DATABASE_NAME);
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public void closeDB(){
        if(mInstance != null){
            SQLiteDatabase db = this.getWritableDatabase();
            db.close();
        }
    }

    public void updateStreet(String streetName, String sideOfStreet, int id){
        String strFilter = "item_index =" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_9, streetName);
        contentValues.put(COL_10, sideOfStreet);
        db.update(TABLE_NAME, contentValues, strFilter, null);
    }

    public void updateNextCal(int year, int month, int id){
        String strFilter = "item_index=" + Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, year);
        contentValues.put(COL_3, month);
        db.update(TABLE_NAME, contentValues, strFilter, null);
    }

    public void updateNextMonth(int month, int id){
        String strFilter = "item_index=" + Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, month + 1);
        db.update(TABLE_NAME, contentValues, strFilter, null);
    }

    public boolean insertData(int year, int month, int dayOfMonth, int dowim, int dayOfWeek, int hour, int minute, String street, String sideOfStreet, int itemIndex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, year);
        contentValues.put(COL_3, month);
        contentValues.put(COL_4, dayOfMonth);
        contentValues.put(COL_5, dowim);
        contentValues.put(COL_6, dayOfWeek);
        contentValues.put(COL_7, hour);
        contentValues.put(COL_8, minute);
        contentValues.put(COL_9, street);
        contentValues.put(COL_10, sideOfStreet);
        contentValues.put(COL_11, itemIndex);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public void updateCal(int year, int month, int dayOfMonth, int dowim, int dayOfWeek, int hour, int minute, int itemIndex){
        String strFilter = "item_index =" + itemIndex;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, year);
        contentValues.put(COL_3, month);
        contentValues.put(COL_4, dayOfMonth);
        contentValues.put(COL_5, dowim);
        contentValues.put(COL_6, dayOfWeek);
        contentValues.put(COL_7, hour);
        contentValues.put(COL_8, minute);
        db.update(TABLE_NAME, contentValues, strFilter, null);

    }

    public boolean insertData(String street, String sideOfStreet, int itemIndex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_9, street);
        contentValues.put(COL_10, sideOfStreet);
        contentValues.put(COL_11, itemIndex);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getIntData(String displayColumnName, String columnName, int target ){
        SQLiteDatabase db = this.getWritableDatabase();
        String targetItemIndex = Integer.toString(target);
        Cursor result = db.rawQuery("SELECT " +displayColumnName+ " FROM " + TABLE_NAME + " WHERE " + columnName + " = " + targetItemIndex + ";", null);
        result.moveToFirst();
        if(result.getCount() >0){
            return result;
        }else{
            Log.i("Empty_cursor: ","the cursor is empty");
            return result;
        }
    }

    public Cursor getStringData(String displayColumnName, String columnName, int target ){
        SQLiteDatabase db = this.getWritableDatabase();
        String targetItemIndex = Integer.toString(target);
        Cursor result = db.rawQuery("SELECT " + displayColumnName + " FROM " + TABLE_NAME + " WHERE " + columnName + " = " + targetItemIndex + ";", null);
        result.moveToFirst();
        if(result.getCount() >0){
            return result;
        }else{
            Log.i("Empty_cursor: ","the cursor is empty");
            return result;
        }
    }

    public int getItemIndex(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + ";", null );
        result.moveToFirst();
        int itemCounter = result.getInt(0);
        return itemCounter;
    }

    public void deleteItem(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_11 + " = " + position + ";");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_11 + " = " + COL_11 + " -1 WHERE " + COL_11 + " > " + position + ";");
        db.close();
    }

    public long getLatestID(){
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_NAME, null,null);
        return id;
    }

    public int getYear(int id){
        Log.i("CheckItemIndexRec","Item Index = " + id);
        int result = getIntData(COL_2, COL_11, id).getInt(0);
        return result;
    }

    public int getMonth(int id){
        int result = getIntData(COL_3, COL_11, id).getInt(0);
        return result;
    }

    public int getDayOfMonth(int id){
        int result = getIntData(COL_4, COL_11, id).getInt(0);
        return result;
    }

    public int getDowim(int id){
        int result = getIntData(COL_5, COL_11, id).getInt(0);
        return result;
    }

    public int getDayOfWeek(int id){
        int result = getIntData(COL_6, COL_11, id).getInt(0);
        return result;
    }

    public int getHour(int id){
        int result = getIntData(COL_7, COL_11, id).getInt(0);
        return result;
    }

    public int getMinute(int id){
        int result = getIntData(COL_8, COL_11, id).getInt(0);
        return result;
    }

    public String getStreet(int id){
        String result = getStringData(COL_9, COL_11, id).getString(0);
        return result;
    }

    public String getSide(int id){
        String result = getStringData(COL_10, COL_11, id).getString(0);
        return result;
    }


}
