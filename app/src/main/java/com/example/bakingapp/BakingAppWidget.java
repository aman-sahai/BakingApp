package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        SharedPreferences sharedPreferences=context.getSharedPreferences(KeyConstants.filename,Context.MODE_PRIVATE);
        String quantity=context.getResources().getString(R.string.in)+"\n"+sharedPreferences.getString(context.getResources().getString(R.string.q),"")+"\n";
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, quantity);

        Intent intent=new Intent(context,HomeScreen.class);
        sharedPreferences.edit().clear();
        PendingIntent pendingIntent=PendingIntent
                    .getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

