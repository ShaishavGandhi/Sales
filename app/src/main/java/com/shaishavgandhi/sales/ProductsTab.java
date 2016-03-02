package com.shaishavgandhi.sales;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;


import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProductsTab extends Fragment {


    private static final String TAG = "Something";

    public ProductsTab() {
        // Required empty public constructor
    }

    private View rowView;
    private SwipeMenuListView listView;
    private List<Products> productsList;
    private ProductDataSource productDataSource;
    private CustomList customAdapter;
    private TextView emptyText;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    public static MyOnClickListener myOnClickListener;
    public static MyLongClickListener myLongClickListener;
    public Products bufferAnswer;
    public int bufferPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rowView = inflater.inflate(R.layout.fragment_products_tab, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Products");

        initializeEverything();
        loadData();
        loadList();

        return rowView;
    }

    public void initializeEverything()
    {

        productDataSource = new ProductDataSource(getActivity());
        myOnClickListener = new MyOnClickListener();
        myLongClickListener = new MyLongClickListener();
        emptyText = (TextView)rowView.findViewById(R.id.emptyText);
        recyclerView = (RecyclerView)rowView.findViewById(R.id.productList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton)rowView.findViewById(R.id.addproduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddProduct.class);
                startActivity(intent);
            }
        });


    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int position=recyclerView.getChildPosition(v);
            Products temp = productAdapter.getItemAt(position);
            Intent intent = new Intent(getActivity(),Details.class);

            intent.putExtra("name",temp.getName());
            intent.putExtra("description",temp.getDescription());
            intent.putExtra("id",temp.getId());
            intent.putExtra("selling_price",temp.getSelling_price());
            intent.putExtra("cost_price",temp.getCost_price());
            intent.putExtra("sold",temp.getSold());
            intent.putExtra("category",temp.getCategory());
            intent.putExtra("discount_price",temp.getDiscount_price());
            intent.putExtra("image",temp.getImage());
            intent.putExtra("created_at",temp.getCreatedAt());
            intent.putExtra("quantity",temp.getQuantity());
            startActivity(intent);
        }
    }

    class MyLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            registerForContextMenu(v);
            bufferPosition = recyclerView.getChildPosition(v);
            bufferAnswer = productAdapter.getItemAt(bufferPosition);

            return false;
        }
    }

    @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Choose action");
                String[] menuItems = {"Sell", "Delete"};
                for (int i = 0; i < menuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }


        }



    public void deleteProduct(){
        productDataSource.open();
        productDataSource.deleteProduct(bufferAnswer);
        productDataSource.close();
        productAdapter.deleteProduct(bufferAnswer);
        Toast.makeText(getActivity(),bufferAnswer.getName()+" deleted",Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        productDataSource.open();
        productsList = productDataSource.getAllComments();
        productDataSource.close();
    }

    public void loadList()
    {

        productAdapter = new ProductAdapter(getActivity(),productsList);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        if(productAdapter.getItemCount()==0)
            emptyText.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = productAdapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getTitle().toString()) {
            case "Delete":
                deleteProduct();
                break;
            case "Sell":
                sellProduct();
                break;
        }
        return super.onContextItemSelected(item);
    }


    public void refreshList(){
        loadData();
        loadList();
    }

    public void sellProduct(){
        productDataSource.open();
        productDataSource.createOrder(bufferAnswer.getId(),new Date().getTime(),bufferAnswer.getCost_price(),
                bufferAnswer.getSelling_price(),bufferAnswer.getQuantity());
        productDataSource.updateQuantity(bufferAnswer.getId(),bufferAnswer.getQuantity()-1);
        productDataSource.close();
        productAdapter.reduceQuantity(bufferPosition);
        Toast.makeText(getActivity(),"Sold 1 quantity of "+bufferAnswer.getName(),Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume(){
        super.onResume();
        refreshList();
    }


}
