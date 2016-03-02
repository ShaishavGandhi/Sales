package com.shaishavgandhi.sales;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Shaishav on 25-06-2015.
 */
public class CustomList extends ArrayAdapter<Products> {

    List<Products> answers;
    private final Activity context;
    private int lastPosition=-1;
    private SparseBooleanArray mSelectedItemsIds;
    private View rowView;
    private String image="";
    private ImageView imageView;

    public CustomList(Activity context,
                      List<Products> values) {
        super(context, R.layout.list_single, values);
        this.answers = values;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();


    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.product_row, null, true);
        //rowView.setBackgroundColor(Color.parseColor("#fff3f3f3"));
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.txt1);
        imageView = (ImageView)rowView.findViewById(R.id.productImage);
        txtTitle.setText(Html.fromHtml(answers.get(position).getName()));
        txtTitle1.setText(String.valueOf(answers.get(position).getSelling_price()));

        if(answers.get(position).getSold()){
            TextView sold = (TextView)rowView.findViewById(R.id.sold);
            sold.setText("Sold");
        }

        image = answers.get(position).getImage();
        if(image!=null){
            /*Bitmap bmp = BitmapFactory.decodeFile(image);
            Drawable d = new BitmapDrawable(context.getResources(),bmp);
            imageView.setImageDrawable(d);*/
            new ImageDownloaderTask(imageView).execute(image);
        }

        return rowView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
            //rowView.setBackgroundColor(Color.RED);
            
        }
        else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();

    }
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;

    }

    class ImageDownloaderTask extends AsyncTask<String,Void,Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;

        public ImageDownloaderTask(ImageView imageView1) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView1);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = BitmapFactory.decodeFile(params[0]);
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            if (imageViewWeakReference != null) {
                ImageView imageView2 = imageViewWeakReference.get();
                if (imageView2 != null) {
                    if (bmp != null) {
                        imageView2.setImageBitmap(bmp);
                    }
                }
            }
        }
    }
}
