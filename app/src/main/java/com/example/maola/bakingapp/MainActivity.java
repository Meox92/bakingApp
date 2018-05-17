package com.example.maola.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.maola.bakingapp.Adapter.RecipeAdapter;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Retrofit.APIUtils;
import com.example.maola.bakingapp.Retrofit.BakingRecipeAPI;
import com.example.maola.bakingapp.Retrofit.RetrofitClient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
