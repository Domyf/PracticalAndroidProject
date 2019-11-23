package com.group18.sustainucd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group18.sustainucd.Database.Bin;

import java.util.ArrayList;
import java.util.List;

public class BinsListAdapter extends RecyclerView.Adapter<BinsListAdapter.BinViewHolder> {

    private List<Bin> bins;
    private OnClickListener mainListener;
    private Context context;
    //Location values
    private double currentLatitude;
    private double currentLongitude;

    public BinsListAdapter(OnClickListener listener, Context context) {
        this.bins = new ArrayList<>();
        this.mainListener = listener;
        this.context = context;
    }

    public void SetList(List<Bin> bins) {
        this.bins = bins;
    }

    public void SetLocation(double latitude, double longitude) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BinsListAdapter.BinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.bin_card, parent, false);

        // Return a new holder instance
        BinViewHolder viewHolder = new BinViewHolder(contactView, mainListener);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BinViewHolder holder, int position) {
        // Get the data model based on position
        Bin bin = bins.get(position);
        //bin.distance = Utils.CalculateDistance(currentLatitude, currentLongitude, bin.latitude, bin.longitude);
        Log.d("BinsListAdapter", currentLatitude+" current longitude: "+currentLongitude);
        // Set item views based on views and data model
        ImageView im = holder.binImageView;
        new LoadPictureTask(bin, im, BinImageHelper.GetBinImagePath(context, bin.pictureFileName)).execute();

        holder.distanceTextView.setText(String.format("%.2f m", bin.distance));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bins.size();
    }

    public Bin getBinAtPosition(int position) {
        if (position >= 0 && position < bins.size())
            return bins.get(position);
        return null;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView distanceTextView;
        public ImageView binImageView;
        public OnClickListener onClickListener;

        public BinViewHolder(View view, OnClickListener listener) {
            super(view);
            distanceTextView = (TextView) view.findViewById(R.id.binDistanceTextView);
            binImageView = (ImageView) view.findViewById(R.id.binImageView);
            this.onClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.OnBinClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void OnBinClick(int position);
    }

    private class LoadPictureTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageView;
        private String path;
        private Bin bin;
        public LoadPictureTask(Bin bin, ImageView imageView, String path) {
            this.imageView = imageView;
            this.path = path;
            this.bin = bin;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            bin.bitmap = bitmap;
        }
    }
}
