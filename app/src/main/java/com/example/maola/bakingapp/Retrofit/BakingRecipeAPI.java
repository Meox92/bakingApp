package com.example.maola.bakingapp.Retrofit;


import com.example.maola.bakingapp.Model.Ingredient;
import com.example.maola.bakingapp.Model.Recipe;
import com.example.maola.bakingapp.Model.Step;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Maola on 17/02/2018.
 */

public interface BakingRecipeAPI {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipeResults();
}
