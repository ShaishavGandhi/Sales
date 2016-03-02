package com.shaishavgandhi.sales;

/**
 * Created by Shaishav on 25-06-2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PROD_ID = "prod_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_COST_PRICE="cost_price";
    public static final String COLUMN_SELLING_PRICE = "selling_price";
    public static final String COLUMN_SOLD="sold";
    public static final String COLUMN_CATEGORY="category";
    public static final String COLUMN_OBJECTID="objectId";
    public static final String COLUMN_DISCOUNT_PRICE="discount_price";
    public static final String COLUMN_CREATED_AT ="created_at";
    public static final String COLUMN_SOLD_AT ="sold_at";
    public static final String COLUMN_IMAGE ="image";
    public static final String COLUMN_QUANTITY="quantity";

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null,"+COLUMN_DESC+" text default 'There are no words to describe this product!', "+COLUMN_COST_PRICE+" real not null, "+COLUMN_SELLING_PRICE+" real not null" +
            ", "+COLUMN_CATEGORY+" text default 'No category',"+COLUMN_SOLD+" boolean not null default false,"+COLUMN_OBJECTID+" text default 'not set',"+COLUMN_DISCOUNT_PRICE+" real" +
            ","+COLUMN_CREATED_AT+" long not null,"+COLUMN_SOLD_AT+" long,"+COLUMN_IMAGE+" text default 'No image set',"+COLUMN_QUANTITY+" integer not null default 0);";

    private static final String ORDER_DATABASE_CREATE = "create table "+TABLE_ORDERS+
            "(" + COLUMN_ID
            + " integer primary key autoincrement, "+COLUMN_PROD_ID+" integer not null, "+COLUMN_SOLD_AT+" long not null,"
            +COLUMN_SELLING_PRICE+" real not null,"+COLUMN_COST_PRICE+" real not null,"+COLUMN_QUANTITY+" integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<newVersion){
            //final String ALTER_TBL = "ALTER TABLE "+MySQLiteHelper.TABLE_PRODUCTS+" ADD COLUMN "+MySQLiteHelper.COLUMN_QUANTITY+" integer not null default 0";
            //db.execSQL(ALTER_TBL);
            db.execSQL(ORDER_DATABASE_CREATE);
        }
    }

}

