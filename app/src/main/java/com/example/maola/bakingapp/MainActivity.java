package com.example.maola.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.maola.bakingapp.Adapter.RecipeAdapter;
import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.Retrofit.APIUtils;
import com.example.maola.bakingapp.Retrofit.BakingRecipeAPI;
import com.example.maola.bakingapp.Retrofit.RetrofitClient;
import com.example.maola.bakingapp.UI.MasterListActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BakingRecipeAPI mService;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> recipeArrayList;
    private static String TAG = "MAIN_ACTIVITY";
    private int savedRecipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        savedRecipeID = prefs.getInt(Constants.SAVED_RECIPE_ID, -1);
        savedRecipeID = savedRecipeID-1;

        mRecyclerView = (RecyclerView)findViewById(R.id.main_recipe_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mService = APIUtils.getResults();
        mService.getRecipeResults().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Log.i("[MainActivity]", response.toString());

                if(response.isSuccessful()){
                    recipeArrayList = response.body();
                    mRecipeAdapter = new RecipeAdapter(recipeArrayList);
                    mRecyclerView.setAdapter(mRecipeAdapter);
                    Intent i = getIntent();
                    boolean isFromWidget = i.getBooleanExtra(Constants.WIDGET, false);
                    if(isFromWidget){
                        if(savedRecipeID == -2){
                            Toast.makeText(getApplicationContext(), R.string.no_favorite, Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constants.RECIPE, recipeArrayList.get(savedRecipeID));

                            Intent j = new Intent(getApplicationContext(), MasterListActivity.class);
                            j.putExtras(bundle);
                            startActivity(j);
                        }
                    }

                }
                if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error on getting response", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error on getting response " + t, Toast.LENGTH_LONG).show();
                Log.i("[MainActivity]", call.toString() + "throwable" + t);
            }
        });

    }
}
