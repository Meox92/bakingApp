package com.example.maola.bakingapp.UI;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.bakingapp.Adapter.IngredientAdapter;
import com.example.maola.bakingapp.Adapter.StepAdapter;
import com.example.maola.bakingapp.Constants;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;
import com.example.maola.bakingapp.database.RecipeContract;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterListFragment extends Fragment implements StepAdapter.ListItemClickListener {
    private RecyclerView mStepRecyclerView;
    private RecyclerView mIngredientRecyclerView;
    private ScrollView mScrollView;
    private TextView mIngredientsTextView;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private onStepClickListener onStepClickListener;
    private Recipe recipe;
    private int recipeID;
    private boolean isSaved;


    public MasterListFragment() {
        // Required empty public constructor
    }

    public interface onStepClickListener{
        void onStepClicked(List<Step> stepList, int index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            onStepClickListener = (onStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        mIngredientsTextView = (TextView) rootView.findViewById(R.id.steps_list_ingredients);
        mStepRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps_list_recycler_view);
        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps_list_ingredients_recycler_view);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            // Get steps and ingredients
            recipe = bundle.getParcelable(Constants.RECIPE);
            recipeID = recipe.getId();
            ingredientList = recipe.getIngredients();
            stepList = recipe.getSteps();

            mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            StepAdapter stepAdapter = new StepAdapter(stepList, this);
            mStepRecyclerView.setAdapter(stepAdapter);
            mIngredientRecyclerView.setNestedScrollingEnabled(false);


            mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientList);
            mIngredientRecyclerView.setAdapter(ingredientAdapter);
            mIngredientRecyclerView.setNestedScrollingEnabled(false);

        }

        if(savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(Constants.ARTICLE_SCROLL_POSITION);
            if (position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });
        }


        final SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        isSaved = isFavourite();
        if(!isSaved){
            mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_off,0);
        } else {
            mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_on,0);
        }

        mIngredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSaved){
                    mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_off,0);
                    isSaved = false;
                    removeData();
                    prefs.edit().putString(Constants.SAVED_RECIPE_STRING, "No recipe saved").apply();


                } else {
                    mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_on,0);
                    Toast.makeText(getActivity(), R.string.added_favorite, Toast.LENGTH_SHORT).show();
                    isSaved = true;
                    insertData();
                    prefs.edit().putString(Constants.SAVED_RECIPE_STRING, recipe.getName()).apply();

                }
            }
        });

        return rootView;
    }

    public void insertData() {
        insertDataIngredients();
    }

    void insertDataIngredients() {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ContentValues foodValues = new ContentValues();
            foodValues.put(RecipeContract.IngredientEntry.COLUMNS_FOOD_NAME, recipe.getName().replace(" ", "_"));
            foodValues.put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            foodValues.put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
            foodValues.put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
            getActivity().getApplicationContext().getContentResolver().insert(RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                    foodValues);
        }
    }

    //remove favourite from DB by using content provider
    void removeData() {
        removeIngredients();
    }

    void removeIngredients() {
        String newName = recipe.getName().replace(" ", "_");
        String[] selections = {newName};
        getContext().getContentResolver().delete(
                RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                RecipeContract.IngredientEntry.COLUMNS_FOOD_NAME + " =? ",
                selections);
    }


    boolean isFavourite() {
        String newName = recipe.getName().replace(" ", "_");
        String[] selections = {newName};
        Cursor c = getContext().getContentResolver().query(
                RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                null,
                RecipeContract.FoodEntry.COLUMN_FOOD_NAME + " =? ",
                selections,
                null);

        return c.getCount() > 0;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(Constants.ARTICLE_SCROLL_POSITION,
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
//       Toast.makeText(getContext(), "Clicked " + clickedItemIndex, Toast.LENGTH_SHORT).show();
       onStepClickListener.onStepClicked(stepList, clickedItemIndex);
    }
}
