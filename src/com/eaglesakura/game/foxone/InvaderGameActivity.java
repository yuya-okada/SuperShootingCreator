package com.eaglesakura.game.foxone;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.eaglesakura.game.App;
import com.eaglesakura.lib.android.game.activity.GL11GameActivityBase;
import com.eaglesakura.lib.android.game.loop.GameLoopManagerBase;
import com.eaglesakura.lib.android.game.math.Vector2;

public class InvaderGameActivity extends GL11GameActivityBase {
    MediaPlayer mp = new MediaPlayer();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.mp.isPlaying()){
            //一時停止
            App.mp.pause();
        }
        App.mp = MediaPlayer.create(this, R.raw.buttle_22);
        App.mp .setLooping(true);
        App.mp.start();
    }

    @Override
    protected GameLoopManagerBase createGameLoopManager() {
        return new FoxOne(this, this);
    }

    @Override
    protected Vector2 getVirtualDisplaySize() {
        return new Vector2(Define.VIRTUAL_DISPLAY_WIDTH, Define.VIRTUAL_DISPLAY_HEIGHT);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        if (App.mp.isPlaying()){
            //一時停止
            App.mp.pause();
        }
        App.mp = MediaPlayer.create(this, R.raw.bgm_main);
        App.mp .setLooping(true);
        App.mp.start();
    }
}