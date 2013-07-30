package com.eaglesakura.game.foxone.scene;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.input.MultiTouchInput;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;
import com.eaglesakura.lib.android.game.util.GameUtil;

public class GameClearScene extends GameSceneBase {

    /**
     * ゲームクリア用画像
     */
    Sprite gameclearImage = null;

    /**
     * クリアの一枚絵
     */
    Sprite eventImage = null;

    /**
    * 前のシーンを保持
    */
    SceneBase before = null;

    /**
     * 少しずつ画面の透過度を下げて、暗くしていく
     */
    int alpha = 0;

    public GameClearScene(FoxOne game) {
        super(game);
    }

    @Override
    public void onSceneStart(SceneManager manager, SceneBase before) {
        // 前のシーンを保持しておく
        this.before = before;

        // ゲームクリア画像を読み込む
        gameclearImage = new Sprite(loadImageDrawable(R.drawable.gameclear));

        // 画像を真ん中へ移動させる
        gameclearImage.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2);

        {
            // ゲームクリア画像を読み込む
            gameclearImage = new Sprite(loadImageDrawable(R.drawable.gameclear));
            // 画像を真ん中へ移動させる
            gameclearImage.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2);
        }
        {
            // イベント一枚絵を読み込む
            eventImage = new Sprite(loadImageDrawable(R.drawable.gameclear_boss));
            // 画像を真ん中へ移動させる
            eventImage.setSpritePosition(Define.VIRTUAL_DISPLAY_WIDTH / 2, 0, Sprite.POSITION_CENTER_X
                    | Sprite.POSITION_TOP);
        }
    }

    @Override
    public void onSceneExit(SceneManager manager, SceneBase next) {

    }

    @Override
    public void onFrameBegin(SceneManager manager) {
        // 透明度を下げていく
        alpha = GameUtil.minmax(0, 255, alpha + 10);
    }

    @Override
    public void onFrameDraw(SceneManager manager) {
        // 前のシーンを描画
        before.onFrameDraw(manager);

        // 半透明な色で背景を塗りつぶす
        int backgroundColor = Color.toColorRGBA(0, 0, 0, alpha);
        getSpriteManager().clear(backgroundColor);

        //  ゲームクリア用画像を描画する
        {
            int imageColor = Color.toColorRGBA(255, 255, 255, alpha);
            gameclearImage.setColorRGBA(imageColor);
            eventImage.setColorRGBA(imageColor);
            getSpriteManager().draw(gameclearImage);
            getSpriteManager().draw(eventImage);
        }
    }

    @Override
    public void onFrameEnd(SceneManager manager) {
        MultiTouchInput input = game.getMultiTouchInput();

        // 画面をタップしたらシーンを切り替える
        if (alpha == 255) {
            if (input.isTouchOnce()) {
                // タイトル画面へ戻す
                TitleScene nextScene = new TitleScene(game);
                manager.setNextScene(nextScene);
            }
        }
    }

    @Override
    public void onGamePause(SceneManager manager) {

    }

    @Override
    public void onGameResume(SceneManager manager) {

    }

}