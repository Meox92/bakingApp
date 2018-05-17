package com.example.maola.bakingapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;

public class NavigationStepAdapter extends RecyclerView.Adapter<NavigationStepAdapter.MyViewHolder> {

    private ArrayList<Step> stepArrayList;
    private Step step;
    private int selectedStep;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public NavigationStepAdapter(ArrayList<Step> stepArrayList, ListItemClickListener listItemClickListener, int selectedStep) {
        this.stepArrayList = stepArrayList;
        this.listItemClickListener = listItemClickListener;
        this.selectedStep = selectedStep;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.navigation_step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        step = stepArrayList.get(position);
        String text = "Intro";
        if(step.getId() != 0) {
            text = "Step ".concat(String.valueOf(step.getId()));
        }
        holder.button.setText(text);
        if(position == selectedStep){
            holder.button.setBackgroundColor(Color.parseColor("#d32f2f"));
        } else {
            holder.button.setBackgroundColor(Color.parseColor("#9e0707"));
        }

        holder.position = position;

    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private Button button;

        public MyViewHolder(View view) {
            super(view);
            button = (Button)view.findViewById(R.id.button);
            button.setOnClickListener(this);
//            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(position);
        }
    }
}