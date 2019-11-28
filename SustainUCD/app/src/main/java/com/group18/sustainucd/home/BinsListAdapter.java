package com.group18.sustainucd.home;

import android.content.Context;
import android.content.res.AssetManager;
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

import com.group18.sustainucd.BinImageHelper;
import com.group18.sustainucd.database.Bin;
import com.group18.sustainucd.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter used for the list of bins showed in the home screen. It has also an interface that should
 * be implemented by who use this class. A reference of who implements the interface will be passed
 * by constructor. Who implements the interface will receive the click events.
 * The home screen activity implements this interface and instantiate an object of this class and
 * uses it with a recycle view.
 * Thanks to this interface, if the user clicks on a bin then the OnElementClick() method of the
 * home screen activity will be called.
 */
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
        Bin bin = bins.get(position);
        //If the bitmap is still in RAM then it shouldn't be loaded again
        if (bin.bitmap == null)
            new BinImageHelper.LoadPictureTask(bin, holder.binImageView, context).execute();
        else
            holder.binImageView.setImageBitmap(bin.bitmap);
        //Set distance text view
        holder.distanceTextView.setText(String.format(Locale.ENGLISH, "%d m", (int)bin.distance));
        //Shows only the right images. View.GONE because the remaining images should
        //be near each other without spaces
        if (!bin.paper)
            holder.paperImageView.setVisibility(View.GONE);
        if (!bin.plastic)
            holder.plasticImageView.setVisibility(View.GONE);
        if (!bin.food)
            holder.foodImageView.setVisibility(View.GONE);
        if (!bin.glass)
            holder.glassImageView.setVisibility(View.GONE);
        if (!bin.battery)
            holder.batteryImageView.setVisibility(View.GONE);
        if (!bin.electronic)
            holder.electronicImageView.setVisibility(View.GONE);
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

    //Custom view holder
    public static class BinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView distanceTextView;
        public ImageView binImageView;
        private ImageView paperImageView;
        private ImageView foodImageView;
        private ImageView batteryImageView;
        private ImageView glassImageView;
        private ImageView plasticImageView;
        private ImageView electronicImageView;
        public OnClickListener onClickListener;

        public BinViewHolder(View view, OnClickListener listener) {
            super(view);
            distanceTextView = (TextView) view.findViewById(R.id.binDistanceTextView);
            binImageView = (ImageView) view.findViewById(R.id.binImageView);
            paperImageView = (ImageView) view.findViewById(R.id.paperImageView);
            foodImageView = (ImageView) view.findViewById(R.id.foodImageView);
            batteryImageView = (ImageView) view.findViewById(R.id.batteryImageView);
            glassImageView = (ImageView) view.findViewById(R.id.glassImageView);
            plasticImageView = (ImageView) view.findViewById(R.id.plasticImageView);
            electronicImageView = (ImageView) view.findViewById(R.id.electronicImageView);
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
}
