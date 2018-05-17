package com.example.maola.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    private ArrayList<Ingredient> ingredientArrayList;
    private Ingredient ingredient;


    public IngredientAdapter(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_adapter_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ingredient = ingredientArrayList.get(position);

        String ingredientString = String.valueOf(ingredient.getQuantity()).concat(ingredient.getMeasure());
        holder.quantity.setText(ingredientString);
        holder.ingredientTV.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView quantity;
        public TextView ingredientTV;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            quantity = (TextView) view.findViewById(R.id.ingredient_qnt_msr_tv);
            ingredientTV = (TextView) view.findViewById(R.id.ingredient_tv);

        }
    }
}