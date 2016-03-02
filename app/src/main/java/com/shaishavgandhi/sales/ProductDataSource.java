package com.shaishavgandhi.sales;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shaishav on 14-09-2015.
 */
public class ProductDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;


    public ProductDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Orders createOrder(long prod_id,long sold_at,float cost_price,float selling_price,long quantity){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PROD_ID,prod_id);
        values.put(MySQLiteHelper.COLUMN_SOLD_AT,sold_at);
        values.put(MySQLiteHelper.COLUMN_COST_PRICE,cost_price);
        values.put(MySQLiteHelper.COLUMN_SELLING_PRICE,selling_price);
        values.put(MySQLiteHelper.COLUMN_QUANTITY,quantity);
        long insertId = database.insert(MySQLiteHelper.TABLE_ORDERS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                null, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, MySQLiteHelper.COLUMN_ID + " desc");
        cursor.moveToFirst();
        Orders newComment = cursorToOrder(cursor);
        cursor.close();
        return newComment;
    }

    public void updateQuantity(long id,long newQuantity){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_QUANTITY, newQuantity);
        String where = MySQLiteHelper.COLUMN_ID+"= ?";
        String selectionArgs[] = {String.valueOf(id)};
        database.update(MySQLiteHelper.TABLE_PRODUCTS,values,where,selectionArgs);
    }

    public Products createComment(String name, String description, float cost_price, String category, boolean sold, float selling_price, float discount_price
    ,long created_at,String image,long quantity) {
        ContentValues values = new ContentValues();
        if(name.trim().length()>0)
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        if(description.trim().length()>0)
        values.put(MySQLiteHelper.COLUMN_DESC, description);
        if(cost_price>0)
        values.put(MySQLiteHelper.COLUMN_COST_PRICE, cost_price);
        if(category.trim().length()>0)
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        if(selling_price>0)
        values.put(MySQLiteHelper.COLUMN_SELLING_PRICE, selling_price);
        values.put(MySQLiteHelper.COLUMN_SOLD, sold);
        if(discount_price>0)
        values.put(MySQLiteHelper.COLUMN_DISCOUNT_PRICE, discount_price);
        if(created_at>0)
        values.put(MySQLiteHelper.COLUMN_CREATED_AT,created_at);
        if(image.trim().length()>0)
        values.put(MySQLiteHelper.COLUMN_IMAGE,image);
        values.put(MySQLiteHelper.COLUMN_QUANTITY,quantity);
        long insertId = database.insert(MySQLiteHelper.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                null, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, MySQLiteHelper.COLUMN_ID + " desc");
        cursor.moveToFirst();
        Products newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public float getMonthlyCost(long thresholdDate){


        Cursor cp =database.rawQuery("SELECT Sum("+MySQLiteHelper.COLUMN_COST_PRICE+") AS cp_total from " +
                MySQLiteHelper.TABLE_ORDERS+" WHERE "
                +MySQLiteHelper.COLUMN_SOLD_AT+" >= "+thresholdDate ,null);
        cp.moveToFirst();

        return cp.getFloat(0);


    }

    public float getMonthlyRevenue(long thresholdDate){
        Cursor sp =database.rawQuery("SELECT Sum("+MySQLiteHelper.COLUMN_SELLING_PRICE+") AS sp_total from " +
                MySQLiteHelper.TABLE_ORDERS+" WHERE "
                +MySQLiteHelper.COLUMN_SOLD_AT+" >= "+thresholdDate ,null);
        sp.moveToFirst();


        return sp.getFloat(0);


    }

    private Products cursorToComment(Cursor cursor) {
        Products comment = new Products();
        comment.setId(cursor.getLong(0));
        comment.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME)));
        comment.setCategory(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CATEGORY)));
        comment.setDescription(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DESC)));
        comment.setCost_price(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COST_PRICE)));
        comment.setSelling_price(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SELLING_PRICE)));

        boolean sold = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SOLD))>0;

        comment.setSold(sold);
        comment.setDiscount_price(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DISCOUNT_PRICE)));
        comment.setObjectId(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_OBJECTID)));
        comment.setCreated_at(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CREATED_AT)));
        comment.setSold_at(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SOLD_AT)));
        comment.setImage(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_IMAGE)));
        comment.setQuantity(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_QUANTITY)));

        return comment;
    }

    private Orders cursorToOrder(Cursor cursor){
        Orders orders = new Orders();
        orders.setId(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        orders.setCost_price(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COST_PRICE)));
        orders.setProd_id(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PROD_ID)));
        orders.setQuantity(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_QUANTITY)));
        orders.setSelling_price(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SELLING_PRICE)));
        orders.setSold_at(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SOLD_AT)));
        return orders;
    }

    public List<Orders> getAllOrders(){
        List<Orders> comments = new ArrayList<Orders>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                null, null, null, null, null, MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Orders comment = cursorToOrder(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public List<Orders> getProductOrder(long id){
        List<Orders> comments = new ArrayList<Orders>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                null, MySQLiteHelper.COLUMN_PROD_ID+" = "+id, null, null, null, MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Orders comment = cursorToOrder(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public LinkedHashMap<String,Float> getTopProducts(){



        Cursor sp =database.rawQuery("SELECT Sum("+MySQLiteHelper.TABLE_ORDERS+"."+MySQLiteHelper.COLUMN_SELLING_PRICE+") AS sp_total,"
                +MySQLiteHelper.TABLE_PRODUCTS+"."+MySQLiteHelper.COLUMN_NAME+
                " from " +
                MySQLiteHelper.TABLE_ORDERS+" LEFT JOIN "+MySQLiteHelper.TABLE_PRODUCTS+" WHERE "
                +MySQLiteHelper.TABLE_ORDERS+"."+MySQLiteHelper.COLUMN_PROD_ID+" = "+MySQLiteHelper.TABLE_PRODUCTS+"."
                +MySQLiteHelper.COLUMN_ID ,null);
        sp.moveToFirst();
        LinkedHashMap hm = new LinkedHashMap();
        while(!sp.isAfterLast()){
        hm.put(sp.getString(1),sp.getFloat(0));
        sp.moveToNext();
        }

        return hm;

    }

    public List<Products> getAllComments() {
        List<Products> comments = new ArrayList<Products>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                null, null, null, null, null, MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Products comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public void deleteProduct(Products tasks){
        long id = tasks.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PRODUCTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateProduct(long id,String description, float cost_price, String category, float selling_price, float discount_price
            ,long quantity){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DESC, description);
        values.put(MySQLiteHelper.COLUMN_COST_PRICE,cost_price);
        values.put(MySQLiteHelper.COLUMN_SELLING_PRICE,selling_price);
        values.put(MySQLiteHelper.COLUMN_DISCOUNT_PRICE,discount_price);
        values.put(MySQLiteHelper.COLUMN_QUANTITY,quantity);
        if(category.trim().length()>0)
        values.put(MySQLiteHelper.COLUMN_CATEGORY,category);
        String where = MySQLiteHelper.COLUMN_ID+"= ?";
        String selectionArgs[] = {String.valueOf(id)};
        database.update(MySQLiteHelper.TABLE_PRODUCTS,values,where,selectionArgs);
    }


}