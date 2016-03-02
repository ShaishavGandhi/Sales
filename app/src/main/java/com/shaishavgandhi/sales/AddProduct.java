package com.shaishavgandhi.sales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;




public class AddProduct extends ActionBarActivity {



    private ProductDataSource productDataSource;
    private ImageView imageView;
    private String imgDecodableString="";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productDataSource = new ProductDataSource(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button button = (Button)findViewById(R.id.addProduct);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText)findViewById(R.id.name);
                EditText desc = (EditText)findViewById(R.id.description);
                EditText cp = (EditText)findViewById(R.id.cost_price);
                EditText sp = (EditText)findViewById(R.id.selling_price);
                EditText category = (EditText)findViewById(R.id.category);
                EditText dp = (EditText)findViewById(R.id.discount_price);
                EditText quantity = (EditText)findViewById(R.id.quantity);

                if(name.getText().length()>0 && cp.getText().length()>0 && sp.getText().length()>0 && quantity.getText().length()>0) {
                    productDataSource.open();
                    productDataSource.createComment(name.getText().toString(), desc.getText().toString(), Float.parseFloat(cp.getText().toString()), category.getText().toString(), false
                            , Float.parseFloat(sp.getText().toString()), Float.parseFloat(dp.getText().toString()), new Date().getTime(), imgDecodableString,Long.parseLong(quantity.getText().toString()));
                    productDataSource.close();

                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();

                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill out the details",Toast.LENGTH_SHORT).show();
                }


            }
        });

        imageView = (ImageView)findViewById(R.id.product_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        //Activity currentActivity = (Activity)getApplicationContext();
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //ImageView imgView = (ImageView) findViewById(R.id.product_image);
                // Set the Image in ImageView after decoding the String
                Bitmap bmp = BitmapFactory.decodeFile(imgDecodableString);
                Drawable d = new BitmapDrawable(bmp);
                imageView.setImageDrawable(d);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
