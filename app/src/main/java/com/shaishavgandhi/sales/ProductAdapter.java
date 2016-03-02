package com.shaishavgandhi.sales;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shaishav on 20-09-2015.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<Products> productsList;
    private LayoutInflater inflater;
    private int position;
    public  RecyclerView.ViewHolder temp;



    public ProductAdapter(Context context,List<Products> productsList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.productsList = productsList;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(productsList.get(position).getName());
        holder.price.setText("$"+String.valueOf(productsList.get(position).getSelling_price()));
        holder.sold.setText(String.valueOf(productsList.get(position).getQuantity())+" left in stock");
        Date date = new Date(productsList.get(position).getCreatedAt());
        PrettyTime pt = new PrettyTime();
        holder.created_at.setText(pt.format(date));
        if(productsList.get(position).getImage()!=null) {

            Picasso.with(context).load("file:///"+productsList.get(position).getImage()).resize(getWidth(),getWidth()).centerCrop().into(holder.product_image);
        }
        else{
            Picasso.with(context).load(R.drawable.no_product_image).resize(getWidth(),getWidth()).centerCrop().into(holder.product_image);
        }

        /*temp = holder;
        temp.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(temp.getPosition());
                return false;
            }
        });*/



    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Products getItemAt(int position){
        return productsList.get(position);
    }

    public void deleteProduct(Products buffer){
        productsList.remove(buffer);
        notifyDataSetChanged();
    }


    public int getWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public void reduceQuantity(int position){
        Products temp = productsList.get(position);
        temp.setQuantity(temp.getQuantity()-1);
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,price,sold,created_at;
        CircleImageView product_image;
        private float x1,x2;
        static final int MIN_DISTANCE = 150;



        public MyViewHolder(View itemView) {



            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt);
            created_at = (TextView) itemView.findViewById(R.id.created_at);
            price = (TextView) itemView.findViewById(R.id.txt1);
            sold = (TextView) itemView.findViewById(R.id.quantity);
            product_image = (CircleImageView)itemView.findViewById(R.id.productImage);
            itemView.setOnClickListener(ProductsTab.myOnClickListener);
            itemView.setOnLongClickListener(ProductsTab.myLongClickListener);
        }


    }


}
