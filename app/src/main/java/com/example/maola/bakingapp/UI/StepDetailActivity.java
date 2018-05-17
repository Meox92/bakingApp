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

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        setTitle("Titolo");

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        ArrayList<Step> stepList = bundle.getParcelableArrayList(Constants.STEP);
        int stepIndex = bundle.getInt(Constants.STEP_INDEX);

        if(savedInstanceState == null){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle b = new Bundle();
            b.putParcelableArrayList(Step.mString, (ArrayList<? extends Parcelable>) stepList);
            b.putInt(Constants.STEP_INDEX, stepIndex);
            stepDetailFragment.setArguments(b);

            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_fragment, stepDetailFragment)
                    .commit();
        }
    }
}
