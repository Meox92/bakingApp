package com.example.maola.bakingapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.maola.bakingapp.Constants;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.onNavigationStepClickListener{
    private int stepIndex;
    private ArrayList<Step> stepList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        stepList = bundle.getParcelableArrayList(Constants.STEP);
        stepIndex = bundle.getInt(Constants.STEP_INDEX);

        setTitle(stepList.get(stepIndex).getShortDescription());

        if(savedInstanceState == null){
            startStepDetailFragment();
        }
    }

    @Override
    public void onNavigationStepClicked(int index) {
        stepIndex = index;
        startStepDetailFragment();
    }

    private void startStepDetailFragment(){
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle b = new Bundle();
        b.putParcelableArrayList(Constants.STEP, (ArrayList<? extends Parcelable>) stepList);
        b.putInt(Constants.STEP_INDEX, stepIndex);
        stepDetailFragment.setArguments(b);

        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_fragment, stepDetailFragment)
                .commit();
    }
}
