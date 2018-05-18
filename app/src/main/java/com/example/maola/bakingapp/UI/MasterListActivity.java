package com.example.maola.bakingapp.UI;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.maola.bakingapp.Constants;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;

public class MasterListActivity extends AppCompatActivity implements MasterListFragment.onStepClickListener, StepDetailFragment.onNavigationStepClickListener {
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;
    private boolean mTwoPanel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);

        // Get list of ingredient and steps
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        ingredientList = bundle.getParcelableArrayList(Constants.INGREDIENT);
        stepList = bundle.getParcelableArrayList(Constants.STEP);
        setTitle(bundle.getString("TITLE"));

        if(findViewById(R.id.tablet_layout) != null) {
            mTwoPanel = true;
            if (savedInstanceState == null){
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();

                Bundle b = new Bundle();
                b.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);
                b.putInt(Constants.STEP_INDEX, 0);
                stepDetailFragment.setArguments(b);

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment, stepDetailFragment)
                        .commit();
            }
        }


        // Pass the data to the fragment to create the recyclerView
        Bundle b = new Bundle();
        b.putParcelableArrayList(Constants.INGREDIENT, (ArrayList<? extends Parcelable>) ingredientList);
        b.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);

        // Start the fragment
        if(savedInstanceState == null){
            MasterListFragment masterListFragment = new MasterListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            masterListFragment.setArguments(b);

            fragmentManager.beginTransaction()
                    .add(R.id.master_list_fragment, masterListFragment)
                    .commit();
        }
    }


    @Override
    public void onStepClicked(ArrayList<Step> stepList, int index) {
//        Toast.makeText(getApplicationContext(), "Clicked " + step.getDescription(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);
        bundle.putInt(Constants.STEP_INDEX, index);
        if(!mTwoPanel){
            final Intent i = new Intent(getApplicationContext(), StepDetailActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            stepDetailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onNavigationStepClicked(int index) {

    }
}
