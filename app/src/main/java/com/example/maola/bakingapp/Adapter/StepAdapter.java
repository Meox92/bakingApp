package com.example.maola.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;
import com.example.maola.bakingapp.UI.MasterListFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {

    private List<Step> stepArrayList;
    private Step step;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public StepAdapter(List<Step> stepArrayList, ListItemClickListener listItemClickListener) {
        this.stepArrayList = stepArrayList;
        this.listItemClickListener = listItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_adapter_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        step = stepArrayList.get(position);

        holder.title.setText(step.getShortDescription());
        holder.long_description.setText(step.getDescription());

        String videoURL = step.getVideoURL();
        if(videoURL != null && !videoURL.isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView long_description;
        public ImageView imageView;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.step_adpt_tv_short_description);
            long_description = (TextView) view.findViewById(R.id.step_adpt_tv_long_description);
            imageView = (ImageView) view.findViewById(R.id.step_adpt_iv_video_button);
            position = getAdapterPosition();

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(position);
        }
    }
}