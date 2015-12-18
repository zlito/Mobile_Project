package com.example.zito.ittcheckbook;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zito on 11/9/2015.
 * Capstone Project - ITT-Tech Westminster CO * Sep - Dec - 2105
 * ==== Julio C. =====
 */
public class TransUpdel extends AppCompatActivity implements View.OnClickListener {

    EditText jAmount, jNotes;
    Button btnUpdate, btnDelete;

    Long jz_id;
    int zbig, jbig;

    String zvAcct = "";
    String zvBalan = "";
    String jjBalan = "";
    String zvamount = "";
    String zzid = "";
    String ztipo = "";

    Zacct_Helper db;
    SQLiteDatabase zb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_updel);

        db = new Zacct_Helper(this);
        zb = db.getWritableDatabase();

        // *** Display the Transaction  *****
        TextView vid = (TextView) findViewById(R.id.jid);
        vid.setText(getIntent().getExtras().getString("xid"));
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        vAcct.setText(getIntent().getExtras().getString("xaccount"));
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        vfname.setText(getIntent().getExtras().getString("xfname"));
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        vBalan.setText(getIntent().getExtras().getString("xbalan"));

        TextView vtipo = (TextView) findViewById(R.id.jtipos);
        vtipo.setText(getIntent().getExtras().getString("xtype"));

        TextView vamount = (TextView) findViewById(R.id.jAmount);
        vamount.setText(getIntent().getExtras().getString("xamount"));

        TextView vnotes = (TextView) findViewById(R.id.jNotes);
        vnotes.setText(getIntent().getExtras().getString("xnotes"));

        zzid = vid.getText().toString().trim();
        jz_id = Long.parseLong(zzid);
        zvAcct = vAcct.getText().toString().trim();
        zvamount = vamount.getText().toString().trim();
        ztipo = vtipo.getText().toString().trim();
        zvBalan = vBalan.getText().toString().trim();
        zvBalan = zvBalan.replace(",", "");  //**** Removes commas

        Double xBalan = Double.parseDouble(zvBalan);
        DecimalFormat zcur = new DecimalFormat("$###,###.##");
        String zBal = zcur.format(xBalan);
        vBalan.setText(zBal);

        // TextView jrDate = (TextView) findViewById(R.id.jDate);
        TextView vdate = (TextView) findViewById(R.id.jDate);
        vdate.setText(getIntent().getExtras().getString("xdate"));
        //****** Sets the current date - Date Dialog **********
        vdate.setOnClickListener(new View.OnClickListener() {
            TextView vdate = (TextView) findViewById(R.id.jDate);
            String dd = vdate.getText().toString().trim();

            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat(dd);  //current date
                vdate.setText(sdf.format(new Date()));
                vdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DateDialog dialog = new DateDialog(v);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        dialog.show(ft, "Date Picker");
                    }
                });
            }
        });

        jAmount = (EditText) findViewById(R.id.jAmount);
        jNotes = (EditText) findViewById(R.id.jNotes);

        // *** Buttons Update/Delete  ********
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // *** Calling Buttons Listners  *****
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //***** UPDATE Transaction *******
        if (v == btnUpdate) {

            switch (ztipo) {
                case "CREDIT":
                    uCredit();
                    zClear();
                    break;
                case "DEBIT":
                    uDebit();
                    zClear();

                    break;
                default:
                    Toast.makeText(this, "Invalid Transaction Type !", Toast.LENGTH_SHORT).show();
            }
        }

        //***** DELETE Transaction *******
        if (v == btnDelete) {

            switch (ztipo) {
                case "CREDIT":
                    zDebit();
                    zClear();
                    break;
                case "DEBIT":
                    zCredit();
                    zClear();

                    break;
                default:
                    Toast.makeText(this, "Invalid Transaction Type !", Toast.LENGTH_SHORT).show();
            }

        }

    } //***** End on Create

    //************ UPDATES / DELETES **********
    public void uCredit() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        TextView vdate = (TextView) findViewById(R.id.jDate);
        zvBalan = vBalan.getText().toString().trim();
        String jbal = zvBalan.substring(1); //*** Removes $ sign

        if (jAmount.getText().toString().trim().length() == 0) {
            //    Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.error, null);
            //    zMessage("Error ! - Transaction Amount", "Please, Enter a Valid Amount", zicon);
            Toast.makeText(this, "Transaction Amount is INVALID !!", Toast.LENGTH_SHORT).show();
            return;
        }

        String ztrDate = vdate.getText().toString().trim();
        String ztrAmount = jAmount.getText().toString().trim();
        String ztrNotes = jNotes.getText().toString().trim();

        //  Double xAmount = Double.parseDouble(ztrAmount);  //Convert to Double
        BigDecimal b1 = new BigDecimal(jbal.replaceAll(",", ""));
        BigDecimal b2 = new BigDecimal(ztrAmount);
        BigDecimal b3 = new BigDecimal(zvamount);
        zbig = b2.compareTo(b3);
        if (zbig == 0) {      //*** b2 and b3 are equal
            //BigDecimal b5 = b1.setScale(2, BigDecimal.ROUND_UP);  //*** b2 and b3 are equal
            BigDecimal b4 = b2.subtract(b3); //*** b2 = b3
            b1 = b1.subtract(b4);
        }
        if (zbig == 1) {
            BigDecimal b4 = b2.subtract(b3); //*** b2 greater than b3
            b1 = b1.add(b4);
        }

        if (zbig == -1) {
            BigDecimal b4 = b3.subtract(b2);  //*** b3 greater than b2
            b1 = b1.subtract(b4);
        }

        Cursor z = zb.rawQuery("SELECT * FROM transactions WHERE _id ='" + jz_id + "'", null);
        if (z.moveToFirst()) {
            zb.execSQL("UPDATE transactions SET tranAmount='" + ztrAmount
                    + "',tranDate='" + ztrDate + "',tranNotes='" + ztrNotes
                    + "' WHERE _id='" + jz_id + "'");

            Toast.makeText(this, "Transaction Was UPDATED !!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Invalid Transaction !!", Toast.LENGTH_SHORT).show();

        BigDecimal b5 = b1.setScale(2, BigDecimal.ROUND_UP);

        String runBalance = String.valueOf(b5); //convert to String

        Cursor zup = zb.rawQuery("SELECT runBalance FROM account WHERE acctNumber ='" + vAcct.getText() + "'", null);
        if (zup.moveToFirst()) {
            zb.execSQL("UPDATE account SET runBalance='" + runBalance + "' WHERE acctNumber='" + vAcct.getText() + "'");
            //   Toast.makeText(this, "Run Balance Was UPDATED ! " + zBal + " " + runBalance, Toast.LENGTH_LONG).show();
        }

        //***** To Transactions List ********
        Intent dlist = new Intent(TransUpdel.this, TransLista.class);
        dlist.putExtra("xaccount", vAcct.getText().toString());
        dlist.putExtra("xfname", vfname.getText().toString());
        dlist.putExtra("xbalan", runBalance);
        startActivity(dlist);
        finish();

    }

    public void uDebit() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        TextView vdate = (TextView) findViewById(R.id.jDate);
        zvBalan = vBalan.getText().toString().trim();
        String jbal = zvBalan.substring(1); //*** Removes $ sign

        if (jAmount.getText().toString().trim().length() == 0) {
            Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.error, null);
            zMessage("Error ! - Invalid Amount", "Please, Enter a Valid Amount", zicon);
            //   Toast.makeText(this, "Transaction Amount is INVALID !!", Toast.LENGTH_SHORT).show();
            return;
        }

        String ztrDate = vdate.getText().toString().trim();
        String ztrAmount = jAmount.getText().toString().trim();
        String ztrNotes = jNotes.getText().toString().trim();

        //  Double xAmount = Double.parseDouble(ztrAmount);  //Convert to Double
        BigDecimal b1 = new BigDecimal(jbal.replaceAll(",", ""));
        BigDecimal b2 = new BigDecimal(ztrAmount);
        BigDecimal b3 = new BigDecimal(zvamount);
        jbig = b1.compareTo(b2);
        if (jbig == -1) {
            Toast.makeText(this, "Debit Amount cannot be greater than Balance", Toast.LENGTH_SHORT).show();
            //***** To Transactions List ********
            Intent dlist = new Intent(TransUpdel.this, TransLista.class);
            dlist.putExtra("xaccount", vAcct.getText().toString());
            dlist.putExtra("xfname", vfname.getText().toString());
            dlist.putExtra("xbalan", jbal);
            startActivity(dlist);
            finish();
            return;

        }
        zbig = b2.compareTo(b3);
        if (zbig == 0) {      //*** b2 and b3 are equal
            // BigDecimal b5 = b1.setScale(2, BigDecimal.ROUND_UP);
            BigDecimal b4 = b2.subtract(b3); //*** b2 equal b3
            b1 = b1.subtract(b4);
        }
        if (zbig == 1) {
            BigDecimal b4 = b2.subtract(b3); //*** b2 greater than b3
            b1 = b1.subtract(b4);
        }

        if (zbig == -1) {
            BigDecimal b4 = b3.subtract(b2);  //*** b3 greater than b2
            b1 = b1.add(b4);
        }

        BigDecimal b5 = b1.setScale(2, BigDecimal.ROUND_UP);

        String runBalance = String.valueOf(b5); //convert to String

        Cursor z = zb.rawQuery("SELECT * FROM transactions WHERE _id ='" + jz_id + "'", null);
        if (z.moveToFirst()) {
            zb.execSQL("UPDATE transactions SET tranAmount='" + ztrAmount
                    + "',tranDate='" + ztrDate + "',tranNotes='" + ztrNotes
                    + "' WHERE _id='" + jz_id + "'");

            Toast.makeText(this, "Transaction Was UPDATED !!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Invalid Transaction !!", Toast.LENGTH_SHORT).show();

        Cursor zup = zb.rawQuery("SELECT runBalance FROM account WHERE acctNumber ='" + vAcct.getText() + "'", null);
        if (zup.moveToFirst()) {
            zb.execSQL("UPDATE account SET runBalance='" + runBalance + "' WHERE acctNumber='" + vAcct.getText() + "'");
            //   Toast.makeText(this, "Run Balance Was UPDATED ! " + zBal + " " + runBalance, Toast.LENGTH_LONG).show();
        }

        //***** To Transactions List ********
        Intent dlist = new Intent(TransUpdel.this, TransLista.class);
        dlist.putExtra("xaccount", vAcct.getText().toString());
        dlist.putExtra("xfname", vfname.getText().toString());
        dlist.putExtra("xbalan", runBalance);
        startActivity(dlist);
        finish();
    }

    public void zCredit() {  //*** Delete

        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        zvBalan = vBalan.getText().toString().trim();
        final String dbal = zvBalan.substring(1); //*** Removes $ sign
        final String dvAcct = vAcct.getText().toString().trim();
        final String dvfname = vfname.getText().toString().trim();
        final String ztrAmount = jAmount.getText().toString().trim();

        Cursor z = zb.rawQuery("SELECT * FROM transactions WHERE _id ='" + jz_id + "'", null);
        if (z.moveToFirst()) {
            //****************
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //*** The Yes button was clicked

                            zb.execSQL("DELETE FROM transactions WHERE _id='" + jz_id + "'");
                            Toast.makeText(getApplicationContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
                            //**======================
                            BigDecimal b1 = new BigDecimal(dbal.replaceAll(",", ""));
                            BigDecimal b2 = new BigDecimal(ztrAmount);
                            b1 = b1.add(b2);
                            BigDecimal b3 = b1.setScale(2, BigDecimal.ROUND_UP);

                            String runBalance = String.valueOf(b3); //convert to String

                            //*** Updating Account Balance
                            Cursor zup = zb.rawQuery("SELECT runBalance FROM account WHERE acctNumber ='" + dvAcct + "'", null);
                            if (zup.moveToFirst()) {
                                zb.execSQL("UPDATE account SET runBalance='" + runBalance + "' WHERE acctNumber='" + dvAcct + "'");
                                //   Toast.makeText(this, "Run Balance Was UPDATED ! " + zBal + " " + runBalance, Toast.LENGTH_LONG).show();
                            }

                            //***** To Transactions List ********
                            Intent dlist = new Intent(TransUpdel.this, TransLista.class);
                            dlist.putExtra("xaccount", dvAcct);
                            dlist.putExtra("xfname", dvfname);
                            dlist.putExtra("xbalan", runBalance);
                            startActivity(dlist);
                            finish();

                            //**====================
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //*** The No button was clicked
                            Toast.makeText(getApplicationContext(), "NO Transaction Deleted", Toast.LENGTH_SHORT).show();
                            //***** To Transactions List ********
                            Intent jdlist = new Intent(TransUpdel.this, TransLista.class);
                            // dlist.putExtra("xaccount", vAcct.getText().toString());
                            // dlist.putExtra("xfname", vfname.getText().toString());
                            jdlist.putExtra("xaccount", dvAcct);
                            jdlist.putExtra("xfname", dvfname);
                            jdlist.putExtra("xbalan", dbal);
                            startActivity(jdlist);
                            finish();

                            break;
                    }
                }
            };

            Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.delete, null);
            android.app.AlertDialog.Builder alertD = new android.app.AlertDialog.Builder(TransUpdel.this);
            alertD.setTitle("DELETE - Alert!");
            alertD.setIcon(zicon);
            alertD.setCancelable(false);
            alertD.setMessage("Deleting this Transaction" + "\n" + "Are you sure?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            //*********************

        } else {
            //     Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.error, null);
            //   zMessage("Error ! - Account Does Not Exist", "Please, Enter a Valid ACCT #", zicon);
            Toast.makeText(this, "Transaction NOT EXIST !!", Toast.LENGTH_SHORT).show();
        }

    } //*** End CREDIT from Delete button ******

    public void zDebit() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        zvBalan = vBalan.getText().toString().trim();

        final String jvAcct = vAcct.getText().toString().trim();
        final String jvfname = vfname.getText().toString().trim();
        final String ztrAmount = jAmount.getText().toString().trim();
        //  Double xAmount = Double.parseDouble(ztrAmount);  //Convert to Double

        final String jbal = zvBalan.substring(1); //*** Removes $ sign

        Cursor z = zb.rawQuery("SELECT * FROM transactions WHERE _id ='" + jz_id + "'", null);
        if (z.moveToFirst()) {
            //****************
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //*** The Yes button was clicked

                            zb.execSQL("DELETE FROM transactions WHERE _id='" + jz_id + "'");
                            Toast.makeText(getApplicationContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
                            //**======================
                            // BigDecimal b1 = new BigDecimal(jbal);
                            BigDecimal b1 = new BigDecimal(jbal.replaceAll(",", ""));
                            BigDecimal b2 = new BigDecimal(ztrAmount);
                            b1 = b1.subtract(b2);
                            BigDecimal b3 = b1.setScale(2, BigDecimal.ROUND_UP);

                            String runBalance = String.valueOf(b3); //convert to String

                            //*** Updating Account Balance
                            Cursor zup = zb.rawQuery("SELECT runBalance FROM account WHERE acctNumber ='" + jvAcct + "'", null);
                            if (zup.moveToFirst()) {
                                zb.execSQL("UPDATE account SET runBalance='" + runBalance + "' WHERE acctNumber='" + jvAcct + "'");
                                //   Toast.makeText(this, "Run Balance Was UPDATED ! " + zBal + " " + runBalance, Toast.LENGTH_LONG).show();
                            }

                            //***** To Transactions List ********
                            Intent dlist = new Intent(TransUpdel.this, TransLista.class);
                            // dlist.putExtra("xaccount", vAcct.getText().toString());
                            // dlist.putExtra("xfname", vfname.getText().toString());
                            dlist.putExtra("xaccount", jvAcct);
                            dlist.putExtra("xfname", jvfname);
                            dlist.putExtra("xbalan", runBalance);
                            startActivity(dlist);
                            finish();

                            //**====================
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //*** The No button was clicked
                            Toast.makeText(getApplicationContext(), "NO Transaction Deleted", Toast.LENGTH_SHORT).show();
                            //***** To Transactions List ********
                            Intent jdlist = new Intent(TransUpdel.this, TransLista.class);
                            // dlist.putExtra("xaccount", vAcct.getText().toString());
                            // dlist.putExtra("xfname", vfname.getText().toString());
                            jdlist.putExtra("xaccount", jvAcct);
                            jdlist.putExtra("xfname", jvfname);
                            jdlist.putExtra("xbalan", jbal);
                            startActivity(jdlist);
                            finish();

                            break;
                    }
                }
            };

            Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.delete, null);
            android.app.AlertDialog.Builder alertD = new android.app.AlertDialog.Builder(TransUpdel.this);
            alertD.setTitle("DELETE - Alert!");
            alertD.setIcon(zicon);
            alertD.setCancelable(false);
            alertD.setMessage("Deleting this Transaction" + "\n" + "Are you sure?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            //*********************

        } else {
            //     Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.error, null);
            //   zMessage("Error ! - Account Does Not Exist", "Please, Enter a Valid ACCT #", zicon);
            Toast.makeText(this, "Transaction NOT EXIST !!", Toast.LENGTH_SHORT).show();
        }

    } //*** End DEBIT from Delete button ******

    public void zMessage(String title, String message, Drawable zicon) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //*** The Yes button was clicked

                        break;
                    //  case DialogInterface.BUTTON_NEGATIVE:
                    //*** The No button was clicked
                    //     break;
                }
            }
        };

        android.app.AlertDialog.Builder jmessa = new android.app.AlertDialog.Builder(TransUpdel.this);
        jmessa.setTitle(title);
        jmessa.setIcon(zicon);
        jmessa.setCancelable(false);
        jmessa.setMessage(message)
                .setPositiveButton("OK", dialogClickListener).show();

/*
        AlertDialog alertDialog = new AlertDialog.Builder(TransUpdel.this).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setIcon(zicon);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
*/
    }


    public void zClear() {
        jAmount.setText("");
        jNotes.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_updel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.zapp:
                zabout();
                break;
            case R.id.dlistra:
                dlistra();
                break;
            case R.id.zaccts:
                zaccts();
                break;
            case R.id.totrans:
                totrans();
                zClear();
                break;
            case R.id.barabout:
                zabout();
                break;
        }
        return true;
    }

    private void zabout() {
        Drawable zicon = ResourcesCompat.getDrawable(getResources(), R.drawable.zxitt, null);
        zMessage("Capstone Project" + "\n" + "** Sep - Dec 2015 **", "Created by:" + "\n" + "Julio Casachagua", zicon);
        return;
    }

    private void dlistra() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        zvBalan = vBalan.getText().toString().trim();
        String jbal = zvBalan.substring(1);

        Intent dlist = new Intent(TransUpdel.this, TransLista.class);
        dlist.putExtra("xaccount", vAcct.getText().toString());
        dlist.putExtra("xfname", vfname.getText().toString());
        dlist.putExtra("xbalan", jbal);
        startActivity(dlist);
        finish();
    }

    private void zaccts() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        jjBalan = vBalan.getText().toString().trim();
        jjBalan = jjBalan.substring(1); //Removes first character $ from string

        Intent iput = new Intent(TransUpdel.this, MainActivity.class);
        iput.putExtra("xaccount", vAcct.getText().toString());
        iput.putExtra("xfname", vfname.getText().toString());
        iput.putExtra("xbalan", jjBalan);

        startActivity(iput);

    }

    private void totrans() {
        TextView vAcct = (TextView) findViewById(R.id.jAcct);
        TextView vfname = (TextView) findViewById(R.id.jOwner);
        TextView vBalan = (TextView) findViewById(R.id.jBalance);
        jjBalan = vBalan.getText().toString().trim();
        jjBalan = jjBalan.substring(1); //Removes first character $ from string

        Intent iput = new Intent(TransUpdel.this, Transactions.class);
        iput.putExtra("xaccount", vAcct.getText().toString());
        iput.putExtra("xfname", vfname.getText().toString());
        iput.putExtra("xbalan", jjBalan);

        startActivity(iput);

    }

} //**** End Transaction Update/delete Class
