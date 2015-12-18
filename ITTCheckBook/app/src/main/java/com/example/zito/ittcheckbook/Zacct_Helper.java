package com.example.zito.ittcheckbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zito on 10/13/2015 ^^ updated 10/27/2015.
 */
public class Zacct_Helper extends SQLiteOpenHelper {

    public static final String DB_NAME = "account_DB";
    private static final int DB_VERSION = 1;

    //*******Account Table ******
    public static final String TABLE_NAME = "account";
    public static final String ACCT_NUMBER = "acctNumber";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String BANK_NAME = "bankName";
    public static final String BANK_BALANCE = "bankBalance";
    public static final String ACCT_DATE = "acctDate";
    public static final String RUN_BALANCE = "runBalance";
    public static final String ACCT_NOTES = "acctNotes";

    //******** Transactions Table *******
    public static final String TRTABLE_NAME = "transactions";
    public static final String TRAN_ID = "_id";
    public static final String TRAN_ACTN = "tranAccount";
    public static final String TRAN_TYPE = "tranType";
    public static final String TRAN_AMOUNT = "tranAmount";
    public static final String TRAN_DATE = "tranDate";
    public static final String TRAN_NOTES = "tranNotes";

    //***** Calling Constructor *****
    public Zacct_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //***=================
    public static final String TABLE_ACCOUNT = "CREATE TABLE " + TABLE_NAME
            + "(" + ACCT_NUMBER + " INTEGER PRIMARY KEY,"
            + FIRST_NAME + " TEXT," + LAST_NAME + " TEXT," + BANK_NAME + " TEXT,"
            + BANK_BALANCE + " DOUBLE," + ACCT_DATE + " TEXT,"
            + RUN_BALANCE + " DOUBLE," + ACCT_NOTES + " TEXT);";

    public static final String TABLE_TRANS = "CREATE TABLE " + TRTABLE_NAME
            + "(" + TRAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRAN_ACTN + " INTEGER," + TRAN_TYPE + " TEXT,"
            + TRAN_AMOUNT + " DOUBLE," + TRAN_DATE + " TEXT," + TRAN_NOTES + " TEXT);";


    //***** Creating ******
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ACCOUNT);
        db.execSQL(TABLE_TRANS);
    }

    //**** Delete the table and create a new table - if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //     String sql = ("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        //   String sql_tr = ("DROP TABLE IF EXISTS " + TABLE_TRANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRTABLE_NAME);
        onCreate(db);
    }

    //**** Enter records
    public boolean addAccount(String actnumber, String fname, String lname, String bkname,
                              String bkbalance, String actdate, String rnbalance, String actnotes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCT_NUMBER, actnumber);
        contentValues.put(FIRST_NAME, fname);
        contentValues.put(LAST_NAME, lname);
        contentValues.put(BANK_NAME, bkname);
        contentValues.put(BANK_BALANCE, bkbalance);
        contentValues.put(ACCT_DATE, actdate);
        contentValues.put(RUN_BALANCE, rnbalance);
        contentValues.put(ACCT_NOTES, actnotes);

        db.insert(TABLE_NAME, null, contentValues);
        // db.close();
        return true;
    }

    //****** TRANSACTIONS ******
    public boolean addtrans(String ztrAcct, String ztrType, String ztrAmount, String ztrDate, String ztrNotes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRAN_ACTN, ztrAcct);
        contentValues.put(TRAN_TYPE, ztrType);
        contentValues.put(TRAN_AMOUNT, ztrAmount);
        contentValues.put(TRAN_DATE, ztrDate);
        contentValues.put(TRAN_NOTES, ztrNotes);

        db.insert(TRTABLE_NAME, null, contentValues);
        // db.close();
        return true;
    }

    public Cursor readData() {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] allCols = new String[]{
                TRAN_ID, TRAN_ACTN, TRAN_TYPE, TRAN_AMOUNT, TRAN_DATE, TRAN_NOTES};

        Cursor c = db.query(TRTABLE_NAME, allCols, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}
