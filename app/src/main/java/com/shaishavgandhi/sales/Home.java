package com.shaishavgandhi.sales;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }

    private View rowView;
    private ProductDataSource productDataSource;
    private float revenue,profit,cost;
    private List<Orders> ordersList;
    private TextView revenue_textview,profit_textview,revenue_textview_half_yearly,profit_textview_half_yearly,revenue_textview_yearly
            ,profit_textview_yearly;
    private PieChart pieChart;
    private LineChart lineChart;
    private LinkedHashMap revenuedataset,profitdataset;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rowView = inflater.inflate(R.layout.fragment_home, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");
        initialize();
        setMonthlyStats();
        List<String> nameList = new ArrayList<String>();
        nameList.add("Revenue");
        nameList.add("Profit");

        List<Integer> valueList = new ArrayList<Integer>();
        valueList.add((int)revenue);
        valueList.add((int)profit);

        setPieChart(nameList,valueList);
        setLineChart();

        return rowView;
    }

    public void initialize(){
        productDataSource = new ProductDataSource(getActivity());

        Date date = new Date();
        date.setDate(1);
        date.setHours(0);
        revenue = getMonthlyRevenue(date.getTime());
        cost = getMonthlyCost(date.getTime());

        profit = revenue - cost;
        revenue_textview = (TextView)rowView.findViewById(R.id.revenue_view);
        profit_textview = (TextView)rowView.findViewById(R.id.profit_view);
        revenue_textview_half_yearly = (TextView)rowView.findViewById(R.id.revenue_view_6_months);
        profit_textview_half_yearly = (TextView)rowView.findViewById(R.id.profit_view_6_months);
        revenue_textview_yearly = (TextView)rowView.findViewById(R.id.revenue_view_year);
        profit_textview_yearly = (TextView)rowView.findViewById(R.id.profit_view_year);
        productDataSource.open();
        ordersList = productDataSource.getAllOrders();
        LinkedHashMap hm = productDataSource.getTopProducts();
        productDataSource.close();
        pieChart = (PieChart)rowView.findViewById(R.id.piechart);
        lineChart = (LineChart)rowView.findViewById(R.id.lineChart);
        lineChart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                    lineChart.animateXY(2000,2000);
            }
        });


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

    public void setMonthlyStats(){
        //for(float i=0;i<=revenue;i++)
                revenue_textview.setText("$" + revenue);
        //for(float j=0;j<=profit;j++)
                profit_textview.setText("$" + profit);

        Date date = new Date();
        date.setMonth(date.getMonth()-6);

        float half_year_revenue= getMonthlyRevenue(date.getTime());
        float half_year_cost = getMonthlyCost(date.getTime());
        revenue_textview_half_yearly.setText("$" +half_year_revenue );
        profit_textview_half_yearly.setText("$"+(half_year_revenue-half_year_cost));

        date.setMonth(date.getMonth()-6);
        float year_revenue= getMonthlyRevenue(date.getTime());
        float year_cost = getMonthlyCost(date.getTime());
        revenue_textview_yearly.setText("$"+year_revenue);
        profit_textview_yearly.setText("$"+(year_revenue-year_cost));




    }

    public void setPieChart(List<String> names,List<Integer> values ){
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Revenue v/s Profit");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);




        setDate(names, values);
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
    }

    public void setDate(List<String> names,List<Integer> values){
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for(int i=0;i<values.size();i++)
            yVals1.add(new Entry(values.get(i),i));

        ArrayList<String> xVals = new ArrayList<String>();

        for(int i=0;i<values.size();i++)
            xVals.add(names.get(i));

        PieDataSet pieDataSet = new PieDataSet(yVals1,"");


        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        PieData data = new PieData(xVals, pieDataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        pieChart.invalidate();

    }

    public float getMonthlyRevenue(long time){

        productDataSource.open();
        float temp = productDataSource.getMonthlyRevenue(time);

        productDataSource.close();

        return temp;
    }

    public float getMonthlyCost(long time){


        productDataSource.open();
        float temp = productDataSource.getMonthlyCost(time);

        productDataSource.close();

        return temp;
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


}
