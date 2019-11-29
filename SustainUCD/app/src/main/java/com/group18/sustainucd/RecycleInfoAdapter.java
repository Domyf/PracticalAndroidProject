package com.group18.sustainucd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used for the list of recycle infos showed in the SubInfo screen. It has also an interface that should
 * be implemented by who use this class. A reference of who implements the interface will be passed
 * by constructor. Who implements the interface will receive the click events.
 * The Sub Info activity implements this interface and instantiate an object of this class and
 * uses it with a recycle view.
 */
public class RecycleInfoAdapter extends RecyclerView.Adapter<RecycleInfoAdapter.BinViewHolder> {

    private ArrayList<RecycleData> recycleData;
    private OnClickListener mainListener;

    public RecycleInfoAdapter(OnClickListener listener, List list) {
        this.recycleData = new ArrayList<RecycleData>(list);
        this.mainListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleInfoAdapter.BinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycle_card, parent, false);

        // Return a new holder instance
        BinViewHolder viewHolder = new BinViewHolder(contactView, mainListener);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BinViewHolder holder, int position) {
        RecycleData data = recycleData.get(position);
        holder.recycleTitle.setText(data.getNameID());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recycleData.size();
    }

    public RecycleData getDataAtPosition(int position) {
        if (position >= 0 && position < recycleData.size())
            return recycleData.get(position);
        return null;
    }

    //Custom view holder
    public static class BinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView recycleTitle;
        public OnClickListener onClickListener;

        public BinViewHolder(View view, OnClickListener listener) {
            super(view);
            recycleTitle = (TextView) view.findViewById(R.id.recycle_name);

            this.onClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.OnElementClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void OnElementClick(int position);
    }
}
