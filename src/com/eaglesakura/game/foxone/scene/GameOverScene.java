package com.eaglesakura.game.foxone.scene;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.input.MultiTouchInput;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

public class GameOverScene extends GameSceneBase {
    /**
     * ゲームオーバー用画像
     */
    Sprite gameoverImage = null;

    /**
     * 前のシーンを保持
     */
    SceneBase before = null;

    public GameOverScene(FoxOne game) {
        super(game);
    }

    @Override
    public void onSceneStart(SceneManager manager, SceneBase before) {

        // 前のシーンを保持しておく
        this.before = before;

        // ゲームオーバー画像を読み込む
        gameoverImage = new Sprite(loadImageDrawable(R.drawable.gameover));

        // 画像を真ん中へ移動させる
        gameoverImage.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2);

    }

    @Override
    public void onSceneExit(SceneManager manager, SceneBase next) {

    }

    @Override
    public void onFrameBegin(SceneManager manager) {

    }

    @Override
    public void onFrameDraw(SceneManager manager) {
        // 前のシーンを描画
        before.onFrameDraw(manager);

        // 半透明な色で背景を塗りつぶす
        int backgroundColor = Color.toColorRGBA(0, 0, 0, 128);
        getSpriteManager().clear(backgroundColor);

        //  ゲームオーバー用画像を描画する
        getSpriteManager().draw(gameoverImage);
    }

    @Override
    public void onFrameEnd(SceneManager manager) {
        MultiTouchInput input = game.getMultiTouchInput();
        // 画面をタップしたらシーンを切り替える
        if (input.isTouchOnce()) {
            TitleScene nextScene = new TitleScene(game);
            manager.setNextScene(nextScene);
        }
    }

    @Override
    public void onGamePause(SceneManager manager) {

    }

    @Override
    public void onGameResume(SceneManager manager) {

    }

}