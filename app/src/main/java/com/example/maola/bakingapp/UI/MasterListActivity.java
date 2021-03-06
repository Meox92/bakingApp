package com.example.maola.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.maola.bakingapp.Constants;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class MasterListActivity extends AppCompatActivity implements MasterListFragment.onStepClickListener, StepDetailFragment.onNavigationStepClickListener {
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private Recipe recipe;
    private boolean mTwoPanel = false;
    public static Context contextOfApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);

        // Get list of ingredient and steps
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        contextOfApplication = getApplicationContext();

//        if(bundle == null){
//            Log.i("MM", "opened from widget");
//
//            return;
//        }

        // Get info
        if (savedInstanceState == null){
            recipe = bundle.getParcelable("RECIPE");
        } else {
            recipe = savedInstanceState.getParcelable(Constants.RECIPE);
        }
        ingredientList = recipe.getIngredients();
        stepList = recipe.getSteps();
        setTitle(recipe.getName());

        // Tablet layout
        if(findViewById(R.id.tablet_layout) != null) {
            mTwoPanel = true;
            if (savedInstanceState == null){
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();

                Bundle b = new Bundle();
                b.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);
                b.putInt(Constants.STEP_INDEX, 0);
                b.putBoolean("MTWOPANEL", mTwoPanel);
                stepDetailFragment.setArguments(b);

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment, stepDetailFragment)
                        .addToBackStack("backStack")
                        .commit();
            }
        }


        // Pass the data to the fragment to create the recyclerView
        Bundle b = new Bundle();
        b.putParcelable(Constants.RECIPE, recipe);

        // Start the fragment
        if(savedInstanceState == null){
            MasterListFragment masterListFragment = new MasterListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            masterListFragment.setArguments(b);

            fragmentManager.beginTransaction()
                    .add(R.id.master_list_fragment, masterListFragment)
                    .addToBackStack("backStack")
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECIPE, recipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(Constants.RECIPE);
    }


    @Override
    public void onStepClicked(List<Step> stepList, int index) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);
        bundle.putInt(Constants.STEP_INDEX, index);
        bundle.putBoolean("MTWOPANEL", mTwoPanel);
        if(!mTwoPanel){ // phone
            final Intent i = new Intent(getApplicationContext(), StepDetailActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        } else { // tablet
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            stepDetailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment, stepDetailFragment)
                    .addToBackStack("backStack")
                    .commit();
        }
    }

    @Override
    public void onNavigationStepClicked(int index) {

    }

}
