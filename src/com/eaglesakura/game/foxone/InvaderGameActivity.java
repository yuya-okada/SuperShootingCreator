package com.eaglesakura.game.foxone;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.eaglesakura.lib.android.game.activity.GL11GameActivityBase;
import com.eaglesakura.lib.android.game.loop.GameLoopManagerBase;
import com.eaglesakura.lib.android.game.math.Vector2;

public class InvaderGameActivity extends GL11GameActivityBase {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GameLoopManagerBase createGameLoopManager() {
        return new FoxOne(this, this);
    }

    @Override
    protected Vector2 getVirtualDisplaySize() {
        return new Vector2(Define.VIRTUAL_DISPLAY_WIDTH, Define.VIRTUAL_DISPLAY_HEIGHT);
    }
    
}