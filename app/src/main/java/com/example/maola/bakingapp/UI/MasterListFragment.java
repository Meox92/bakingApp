package com.example.maola.bakingapp.UI;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.bakingapp.Adapter.IngredientAdapter;
import com.example.maola.bakingapp.Adapter.StepAdapter;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterListFragment extends Fragment implements StepAdapter.ListItemClickListener {
    private RecyclerView mStepRecyclerView;
    private RecyclerView mIngredientRecyclerView;
    private ScrollView mScrollView;
    private TextView mIngredientsTextView;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;
    private onStepClickListener onStepClickListener;

    public MasterListFragment() {
        // Required empty public constructor
    }

    public interface onStepClickListener{
        void onStepClicked(Step step, int index);
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

//        mIngredientsTextView = (TextView) rootView.findViewById(R.id.steps_list_ingredients);
        mStepRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps_list_recycler_view);
        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps_list_ingredients_recycler_view);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            // Get steps and ingredients
            ingredientList = bundle.getParcelableArrayList(Ingredient.mIngredientString);
            stepList = bundle.getParcelableArrayList(Step.mStepString);

            mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            StepAdapter stepAdapter = new StepAdapter(stepList, this);
            mStepRecyclerView.setAdapter(stepAdapter);

            mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientList);
            mIngredientRecyclerView.setAdapter(ingredientAdapter);

        }

        if(savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
//       Toast.makeText(getContext(), "Clicked " + clickedItemIndex, Toast.LENGTH_SHORT).show();
       onStepClickListener.onStepClicked(stepList.get(clickedItemIndex), clickedItemIndex);
    }
}
