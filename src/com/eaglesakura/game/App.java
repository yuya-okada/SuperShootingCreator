package com.eaglesakura.game;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by okadakeiko on 13/08/24.
 */
public class App extends Application {

    private static Context context;
    public  static final String DATA_FILE_NAME = "stageData";

    public void onCreate(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String stagePath = sharedPreferences.getString("stageData",null);
        if(stagePath == null){
            sharedPreferences.edit().putString("stageData","stageData.json").commit();
        }
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
