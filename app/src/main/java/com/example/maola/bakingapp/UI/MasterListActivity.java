package com.example.maola.bakingapp.UI;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;

public class MasterListActivity extends AppCompatActivity implements MasterListFragment.onStepClickListener {
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);


        // Get list of ingredient and steps
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        ingredientList = bundle.getParcelableArrayList(Ingredient.mIngredientString);
        stepList = bundle.getParcelableArrayList(Step.mStepString);
        setTitle(bundle.getString("TITLE"));


        // Pass the data to the fragment to create the recyclerView
        Bundle b = new Bundle();
        b.putParcelableArrayList(Ingredient.mIngredientString, (ArrayList<? extends Parcelable>) ingredientList);
        b.putParcelableArrayList(Step.mStepString, (ArrayList<? extends Parcelable>) stepList);

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
    public void onStepClicked(Step step, int index) {
//        Toast.makeText(getApplicationContext(), "Clicked " + step.getDescription(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Step.mStepString, step);
        final Intent i = new Intent(getApplicationContext(), StepDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}
