package com.example.maola.bakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;
import com.example.maola.bakingapp.UI.MasterListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private ArrayList<Recipe> recipeArrayList;
    private Recipe recipe;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public RecipeAdapter(ArrayList<Recipe> recipeArrayList) {
        this.recipeArrayList = recipeArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_adapter_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        recipe = recipeArrayList.get(position);
        final Context context = holder.imageView.getContext();
        String title = recipe.getName();
        holder.title.setText(title);
        // Create string "Servings: n of servings  "
        String servings = context.getResources().getString(R.string.servings).concat(recipe.getServings().toString());
        holder.servings.setText(servings);

        String imageUL = recipe.getImage();
        if(imageUL != null && imageUL != ""){
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(imageUL)
                    .into(holder.imageView);

        }

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView servings;
        public ImageView imageView;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.recipe_adpt_name);
            servings = (TextView) view.findViewById(R.id.recipe_adpt_servings);

            imageView = (ImageView) view.findViewById(R.id.recipe_adpt_imageview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Ingredient> ingredientList = recipeArrayList.get(position).getIngredients();
                    List<Step> stepList = recipeArrayList.get(position).getSteps();
                    String title = recipeArrayList.get(position).getName();
                    Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Ingredient.mIngredientString, (ArrayList<? extends Parcelable>) ingredientList);
                    bundle.putParcelableArrayList(Step.mStepString, (ArrayList<? extends Parcelable>) stepList);
                    bundle.putString("TITLE", title);
                    final Intent i = new Intent(v.getContext(), MasterListActivity.class);
                    i.putExtras(bundle);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}