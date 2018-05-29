package com.example.maola.bakingapp.Retrofit;

/**
 * Created by Maola on 18/02/2018.
 */

final public class APIUtils {
    static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
//    static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";


    public static BakingRecipeAPI getResults(){
        return RetrofitClient.getClient(BASE_URL).create(BakingRecipeAPI.class);
    }


}
