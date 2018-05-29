package com.example.maola.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeDBHelper extends SQLiteOpenHelper {
    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     */
    public static final String LOG_TAG = RecipeDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " +
                RecipeContract.FoodEntry.TABLE_FOOD + "(" +
                RecipeContract.FoodEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.FoodEntry.COLUMN_FOOD_NAME +
                " TEXT NOT NULL); ";
//                RecipeContract.FoodEntry.COLUMN_FOOD_SERVINGS +
//                " INTEGER NOT NULL);";

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
                RecipeContract.IngredientEntry.TABLE_INGREDIENTS + "(" + RecipeContract.IngredientEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME +
                " TEXT NOT NULL, " +
                RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY +
                " DOUBLE NOT NULL, " +
                RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE +
                " TEXT NOT NULL, " +
                RecipeContract.IngredientEntry.COLUMNS_FOOD_NAME +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FOOD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.w(LOG_TAG, "Upgrading database from version " + i + " to " +
                i1 + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.FoodEntry.TABLE_FOOD);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                RecipeContract.FoodEntry.TABLE_FOOD + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_INGREDIENTS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                RecipeContract.IngredientEntry.TABLE_INGREDIENTS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}