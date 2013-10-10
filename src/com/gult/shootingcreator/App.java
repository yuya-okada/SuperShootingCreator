package com.gult.shootingcreator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.parse.Parse;

/**
 * Created by okadakeiko on 13/08/24.
 */
public class App extends Application {

    private static Context context;
    public static MediaPlayer mp ;
    public  static final String DATA_FILE_KEY = "stageData";

    public void onCreate(){
        //Parseを初期化
        Parse.initialize(this, "hoi1RwcRUVTdF5eIs9NQcahevCtqKWP2i0aJMhkG", "xrEp9TCVoW4vvAqLjd7Jw9CCjxltf6l9ffcGzN9o");

        mp = MediaPlayer.create(this, R.raw.bgm_main);

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
