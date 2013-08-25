package com.eaglesakura.game.foxone.scene;

import android.content.Context;

import com.eaglesakura.game.bundle.ResourceDisplayable;
import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.input.MultiTouchInput;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

public class TitleScene extends GameSceneBase {
    Context context;
    /**
     * ロゴ画像
     */
    //Sprite logo = null;

    /**
     * EXIT画像
     */
    Sprite exit = null;

    /**
     * START画像
     */
    Sprite start = null;

    public TitleScene(FoxOne game,Context context) {
        super(game);

        this.context = context;
    }


    public TitleScene(FoxOne game) {
        super(game);
    }
    @Override
    public void onSceneStart(SceneManager manager, SceneBase before) {
        // ロゴ画像の読み込みと、位置設定
        {
        //    logo = new Sprite(loadImageDrawable(R.drawable.danmaku_logo));
         //   logo.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2);
        }

        // START画像の読み込みと、位置設定
        {
            start = new Sprite(loadImageDrawable(new ResourceDisplayable(R.drawable.danmaku_start)));
            start.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2 + 200);
        }

        // EXIT画像の読み込みと、位置設定
        {
            exit = new Sprite(loadImageDrawable(new ResourceDisplayable(R.drawable.danmaku_exit)));
            exit.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2 + 300);
        }
    }

    @Override
    public void onSceneExit(SceneManager manager, SceneBase next) {
    }

    @Override
    public void onFrameBegin(SceneManager manager) {
    }

    @Override
    public void onFrameDraw(SceneManager manager) {
        // 背景を宇宙色に染める
        getSpriteManager().clear(0, 0, 64, 255);

        // 各画像の描画
        {
           // getSpriteManager().draw(logo);
            getSpriteManager().draw(start);
            getSpriteManager().draw(exit);
        }
    }

    @Override
    public void onFrameEnd(SceneManager manager) {

        MultiTouchInput input = game.getMultiTouchInput();
        if (start.isIntersectTouchOnce(input) != null) {
            // startに触れていたら
            manager.setNextScene(new GameSceneStage1(game,context));
        } else if (exit.isIntersectTouchOnce(input) != null) {
            // exitに触れたらゲームを終了する
            game.exit();
        }
    }

    @Override
    public void onGamePause(SceneManager manager) {
    }

    @Override
    public void onGameResume(SceneManager manager) {
    }

}