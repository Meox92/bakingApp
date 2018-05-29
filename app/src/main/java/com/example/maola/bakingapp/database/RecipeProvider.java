package com.example.maola.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RecipeProvider extends ContentProvider {
    /**
     * PROJECT BASED ON UDACITY'S CONTENT PROVIDER PROJECT
     * https://github.com/udacity/android-content-provider
     **/
    private static final String LOG_TAG = RecipeProvider.class.getSimpleName();
    //  private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDBHelper mOpenHelper;

    private static final int FOOD = 50;
    private static final int INGREDIENT = 60;
    private UriMatcher uriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, RecipeContract.FoodEntry.TABLE_FOOD, FOOD);
        matcher.addURI(authority, RecipeContract.IngredientEntry.TABLE_INGREDIENTS, INGREDIENT);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FOOD: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.FoodEntry.TABLE_FOOD,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            case INGREDIENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.IngredientEntry.TABLE_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                return retCursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                return RecipeContract.IngredientEntry.CONTENT_ITEM_INGREDIENT;
            }
            case INGREDIENT: {
                return RecipeContract.FoodEntry.CONTENT_ITEM_FOOD;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;
        long _id = 0;

        final int match = uriMatcher.match(uri);
        switch (match) {
            case FOOD: {
                _id = db.insert(RecipeContract.FoodEntry.TABLE_FOOD, null, values);

                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = RecipeContract.FoodEntry.buildFoodUriWithId(_id);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnUri;
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
            }
            case INGREDIENT: {
                _id = db.insert(RecipeContract.IngredientEntry.TABLE_INGREDIENTS, null, values);
                if (_id > 0) {
                    returnUri = RecipeContract.IngredientEntry.buildIngredientUriWithId(_id);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnUri;
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //    final int match = sUriMatcher.match(uri);
        int numDeleted;
        final int match = uriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                numDeleted = db.delete(
                        RecipeContract.FoodEntry.TABLE_FOOD, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        RecipeContract.FoodEntry.TABLE_FOOD + "'");
                return numDeleted;
            }
            case INGREDIENT: {
                numDeleted = db.delete(
                        RecipeContract.IngredientEntry.TABLE_INGREDIENTS, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        RecipeContract.IngredientEntry.TABLE_INGREDIENTS + "'");
                return numDeleted;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        final int match = uriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                numUpdated = db.update(RecipeContract.FoodEntry.TABLE_FOOD,
                        contentValues,
                        selection,
                        selectionArgs);
                if (numUpdated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numUpdated;
            }
            case INGREDIENT: {

                numUpdated = db.update(RecipeContract.IngredientEntry.TABLE_INGREDIENTS,
                        contentValues,
                        selection,
                        selectionArgs);
                if (numUpdated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numUpdated;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

    }
}
