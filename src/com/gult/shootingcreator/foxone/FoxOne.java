package com.gult.shootingcreator.foxone;

import android.content.Context;

import com.gult.shootingcreator.foxone.scene.TitleScene;
import com.eaglesakura.lib.android.game.loop.SpriteGameLoopManagerBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

public class FoxOne extends SpriteGameLoopManagerBase {
    Context context;

    /**
     * シーン管理クラスを追加
     */
    SceneManager sceneManager = new SceneManager(null);

    public FoxOne(Context context, ILoopParent loopParent) {
        super(context, loopParent);

        this.context = context;
    }

    @Override
    protected void onGameInitialize() {
        super.onGameInitialize();

        // 最初のシーンを登録する
        sceneManager.setNextScene(new TitleScene(this,context));
    }

    @Override
    protected void onGameFrameBegin() {
        sceneManager.onFrameBegin();
    }

    @Override
    protected void onGameFrameDraw() {
        sceneManager.onFrameDraw();
    }

    @Override
    protected void onGameFrameEnd() {
        sceneManager.onFrameEnd();

    }

    @Override
    protected void onGamePause() {
        sceneManager.onGamePause();
    }

    @Override
    protected void onGameResume() {
        sceneManager.onGameResume();
    }
}
