package com.example.maola.bakingapp.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maola on 18/02/2018.
 * https://www.journaldev.com/13639/retrofit-android-example-tutorial
 */

public class RetrofitClient {

    // This class will create a singleton of Retrofit.
    private static Retrofit retrofit = null;




    public static Retrofit getClient(String baseUrl) {
//        Gson gson = new GsonBuilder()
//                .create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
