package com.shaishavgandhi.sales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Details extends ActionBarActivity {

    private TextView created_at_view;
    private EditText selling_price_view,discount_price_view,cost_price_view,quantity_view;
    private EditText description;
    private Intent intent;
    private String product_name,product_description,product_category,image;
    private float product_cp,product_sp,product_dp;
    private boolean sold;
    private long id,created_at,quantity;
    private Toolbar toolbar;
    private List<Orders> ordersList;
    private CollapsingToolbarLayout collapsingToolbar;
    private ProductDataSource productDataSource;
    private FloatingActionButton addbutton;
    public String state;
    private LineChart lineChart;
    private LinkedHashMap revenuedataset,profitdataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_details);
        initializeEverything();
        loadData();
        setData();

    }



    public void setData(){


        collapsingToolbar.setTitle(product_name);
        final ImageView imag1e = (ImageView) findViewById(R.id.image);
        if(image!=null)
        Picasso.with(this).load("file:///"+image).resize(getWidth(),getWidth()).centerCrop().into(imag1e);
        description.setText(product_description);
        selling_price_view.setText(String.valueOf(product_sp));
        discount_price_view.setText(String.valueOf(product_dp));
        Date date = new Date(created_at);
        PrettyTime pt = new PrettyTime();
        created_at_view.setText(pt.format(date));
        cost_price_view.setText(String.valueOf(product_cp));
        quantity_view.setText(String.valueOf(quantity));

        setLineChart();


    }

    public void setLineChart(){
        lineChart.setHighlightEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDescription("Revenue in $");
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);

        setLineChartData();

        lineChart.animateXY(2000,2000);

        lineChart.invalidate();


    }


    public void setLineChartData(){
        sortData();

        ArrayList<String> xVals = new ArrayList<String>();
        Set set = revenuedataset.entrySet();
        Set profitSet = profitdataset.entrySet();
        Iterator i = set.iterator();

        while(i.hasNext()){
            Map.Entry me = (Map.Entry)i.next();
            if((float)me.getValue()>0.0)
            xVals.add(convertToMonth((int)me.getKey()));
        }

        ArrayList<Entry> vals1 = new ArrayList<>();
        ArrayList<Entry> vals2 = new ArrayList<>();

        int j=0;

        Iterator k = set.iterator();
        while(k.hasNext()){
            Map.Entry me = (Map.Entry)k.next();
            if((float)me.getValue()>0.0) {
                vals1.add(new Entry((float) me.getValue(), j));

                j++;
            }
        }

        Iterator profitIterator = profitSet.iterator();

        j=0;
        while(profitIterator.hasNext()){
            Map.Entry me = (Map.Entry)profitIterator.next();
            if((float)me.getValue()>0.0) {
                vals2.add(new Entry((float) me.getValue(), j));
                j++;
            }
        }

        LineDataSet lineDataSet = new LineDataSet(vals1,"Revenue");
        //lineDataSet.setDrawCubic(true);
        //lineDataSet.setCubicIntensity(0.2f);
        //lineDataSet.setDrawFilled(true);
        //lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(2f);
        //lineDataSet.setCircleSize(5f);
        lineDataSet.setHighLightColor(Color.parseColor("#2d636e"));
        lineDataSet.setColor(Color.parseColor("#336E7B"));
        lineDataSet.setFillColor(Color.parseColor("#336E7B"));
        lineDataSet.setDrawHorizontalHighlightIndicator(false);

        LineDataSet profitDataSet = new LineDataSet(vals2,"Profit");
        //profitDataSet.setDrawCubic(true);
        //profitDataSet.setCubicIntensity(0.2f);
        //profitDataSet.setDrawFilled(true);
        //profitDataSet.setDrawCircles(false);
        profitDataSet.setLineWidth(2f);
        //profitDataSet.setCircleSize(10f);
        profitDataSet.setHighLightColor(Color.parseColor("#f39c12"));
        profitDataSet.setColor(Color.parseColor("#f1c40f"));
        profitDataSet.setFillColor(Color.parseColor("#f1c40f"));
        profitDataSet.setDrawHorizontalHighlightIndicator(false);

        ArrayList<LineDataSet> finalData = new ArrayList<>();
        finalData.add(lineDataSet);
        finalData.add(profitDataSet);

        LineData lineData = new LineData(xVals,finalData);
        lineData.setValueTextSize(9f);
        lineData.setDrawValues(false);

        lineChart.setData(lineData);


    }

    public void sortData(){
        revenuedataset= new LinkedHashMap();
        profitdataset = new LinkedHashMap();
        revenuedataset= initializeDataset(revenuedataset);
        profitdataset = initializeDataset(profitdataset);

        for(int i=0;i<ordersList.size();i++)
        {
            Orders temp = ordersList.get(i);
            Date soldDate = new Date(temp.getSold_at());
            revenuedataset.put(soldDate.getMonth(),(float)(revenuedataset.get(soldDate.getMonth()))+temp.getSelling_price());
            profitdataset.put(soldDate.getMonth(),(float)(profitdataset.get(soldDate.getMonth()))+(temp.getSelling_price()-temp.getCost_price()));
        }


    }

    public LinkedHashMap initializeDataset(LinkedHashMap hm){

        for(int i=0;i<12;i++){
            hm.put(i,new Float(0.0));
        }

        return hm;
    }



    public void setStatusBarColor(int status_color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(status_color);

        }
    }

    public int getWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public void loadData(){
        intent = getIntent();
        product_name = intent.getStringExtra("name");
        product_description = intent.getStringExtra("description");
        product_category = intent.getStringExtra("category");
        image = intent.getStringExtra("image");
        product_sp = intent.getFloatExtra("selling_price", 0);
        product_cp = intent.getFloatExtra("cost_price",0);
        product_dp = intent.getFloatExtra("discount_price",0);
        created_at = intent.getLongExtra("created_at",0);
        quantity = intent.getLongExtra("quantity",0);

        sold = intent.getBooleanExtra("sold",false);
        id = intent.getLongExtra("id",0);

        productDataSource.open();
        ordersList = productDataSource.getProductOrder(id);
        productDataSource.close();



    }

    public void initializeEverything(){

        description = (EditText)findViewById(R.id.description);
        selling_price_view = (EditText)findViewById(R.id.selling_price);
        discount_price_view = (EditText)findViewById(R.id.discount_price);
        cost_price_view = (EditText)findViewById(R.id.cost_price);
        created_at_view = (TextView)findViewById(R.id.created_at);
        quantity_view = (EditText)findViewById(R.id.quantity);
        intent = getIntent();
        product_name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        addbutton = (FloatingActionButton)findViewById(R.id.addproduct);
        state="edit";
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                toggleEdit(state,v);
            }
        });

        productDataSource = new ProductDataSource(this);
        lineChart = (LineChart)findViewById(R.id.lineChart);


        }

        public void toggleEdit(String currentState,View v){

            if(currentState.equals("edit")){
                FloatingActionButton mainButton  = (FloatingActionButton)v;
                mainButton.setImageResource(R.drawable.ic_save_white);
                description.setEnabled(true);
                cost_price_view.setEnabled(true);
                selling_price_view.setEnabled(true);
                discount_price_view.setEnabled(true);
                quantity_view.setEnabled(true);
                state="save";
            }
            else{
                if(checkForInput()){
                    productDataSource.open();
                    productDataSource.updateProduct(id,description.getText().toString(),Float.parseFloat(cost_price_view.getText().toString())
                            ,"",Float.parseFloat(selling_price_view.getText().toString()),Float.parseFloat(discount_price_view.getText().toString()),
                            Long.parseLong(quantity_view.getText().toString()));
                    productDataSource.close();
                    Toast.makeText(getApplicationContext(),"Product updated",Toast.LENGTH_SHORT).show();
                    FloatingActionButton mainButton  = (FloatingActionButton)v;
                    mainButton.setImageResource(R.drawable.ic_create_white);
                    state="edit";
                    description.setEnabled(false);
                    cost_price_view.setEnabled(false);
                    selling_price_view.setEnabled(false);
                    discount_price_view.setEnabled(false);
                    quantity_view.setEnabled(false);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please check your input",Toast.LENGTH_SHORT).show();
                }
            }
        }

    public boolean checkForInput(){
        if(isEmpty(description) && isEmpty(cost_price_view) && isEmpty(selling_price_view) && isEmpty(discount_price_view)
                && isEmpty(quantity_view))
            return true;
        else
            return false;
    }

    public boolean isEmpty(EditText et){
        if(et.getText().toString().trim().length()>0)
            return true;
        else
            return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_details, menu);
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

    public String convertToMonth(int monthNumber){

        switch(monthNumber){
            case 0:
                return "January";

            case 1:
                return "February";

            case 2:
                return "March";

            case 3:
                return "April";

            case 4:
                return "May";

            case 5:
                return "June";

            case 6:
                return "July";

            case 7:
                return "August";

            case 8:
                return "September";

            case 9:
                return "October";

            case 10:
                return "November";

            case 11:
                return "December";

            default:
                return "";
        }
    }

}
