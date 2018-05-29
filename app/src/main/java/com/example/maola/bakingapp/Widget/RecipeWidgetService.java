package com.example.maola.bakingapp.Widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.maola.bakingapp.R;
import com.example.maola.bakingapp.database.RecipeContract;


public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public class WidgetRemoteViewsFactory implements RemoteViewsFactory {


        private Context mContext;
        private Cursor mCursor;
        private int mAppWidgetId;



        public WidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            initCursor();
        }

        @Override
        public void onDataSetChanged() {
            initCursor();
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }


        @Override
        public RemoteViews getViewAt(int position) {
           RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_adapter_item);

            if (mCursor.getCount() != 0) {
                mCursor.moveToPosition(position);
                String ingredient_name = mCursor.getString(1);
                String quantity = mCursor.getString(2);
                String measure = mCursor.getString(3);
                rv.setTextViewText(R.id.ingredient_tv, ingredient_name);
                rv.setTextViewText(R.id.ingredient_qnt_msr_tv,  quantity + measure);
            }

            return  rv;
        }

        private void initCursor() {
            mCursor = mContext.getContentResolver()
                    .query(RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT_TABLE,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
