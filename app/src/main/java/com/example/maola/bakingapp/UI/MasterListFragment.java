package com.example.maola.bakingapp.UI;


import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
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
            Recipe recipe = bundle.getParcelable(Constants.RECIPE);
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
        int savedRecipeID = prefs.getInt(Constants.SAVED_RECIPE_ID, -1);
        isSaved = false;
        if(savedRecipeID != recipeID){
            mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_off,0);
        } else {
            mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_on,0);
            isSaved = true;
        }

        mIngredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSaved){
                    mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_off,0);
                    // Set default saved recipe
                    prefs.edit().putInt(Constants.SAVED_RECIPE_ID, 1).apply();
                    isSaved = false;

                } else {
                    mIngredientsTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.btn_star_big_on,0);
                    prefs.edit().putInt(Constants.SAVED_RECIPE_ID, recipeID).apply();
                    Toast.makeText(getActivity(), R.string.added_favorite, Toast.LENGTH_SHORT).show();
                    isSaved = true;
                }
            }
        });

        return rootView;
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
