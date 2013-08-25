package com.eaglesakura.game;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by okadakeiko on 13/08/24.
 */
public class App extends Application {

    private static Context context;

    public void onCreate(Bundle bundle){
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
