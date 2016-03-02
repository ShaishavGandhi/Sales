package com.shaishavgandhi.sales;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Shaishav on 25-09-2015.
 */
public class OrdersSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;


    public OrdersSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


}
