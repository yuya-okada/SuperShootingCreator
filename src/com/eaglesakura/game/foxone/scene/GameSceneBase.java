package com.eaglesakura.game.foxone.scene;

import java.util.HashMap;
import java.util.Map;

import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;
import com.eaglesakura.lib.android.game.input.MultiTouchInput;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.sound.SoundManager;

public abstract class GameSceneBase extends SceneBase {
    /**
     * ゲームメインクラス
     */
    protected FoxOne game = null;

    /**
     * サウンド管理クラス
     */
    protected SoundManager soundManager = null;

    /**
     * 読み込み済みの画像をキャッシュしておく
     */
    Map<Integer, ImageBase> imageCache = new HashMap<Integer, ImageBase>();

    protected GameSceneBase(FoxOne game) {
        this.game = game;
        soundManager = new SoundManager(game.getContext());
    }

    /**
     * rawリソースIDを指定して音を再生する
     * @param rawId
     */
    public void playSE(int rawId) {
        // サウンドが読み込まれていなかったら、この場で読み込む
        if (!soundManager.isLoaded(rawId)) {
            // rawからサウンドの読み込み
            soundManager.loadFromRaw(rawId, rawId);
        }

        // サウンドの再生を行う
        soundManager.play(rawId);
    }

    /**
     * スプライトマネージャを取得する
     * @return
     */
    public SpriteManager getSpriteManager() {
        return game.getSpriteManager();
    }

    /**
     * drawableのIDを指定して画像を読み込む。
     * @param drawableId
     * @return
     */
    public ImageBase loadImageDrawable(int drawableId) {
        // まずはキャッシュから画像を探す
        ImageBase result = imageCache.get(drawableId);

        // キャッシュが見つからなかったら
        if (result == null) {
            // 新たに画像を読み込む
            result = game.loadImageDrawable(drawableId);

            // キャッシュへ登録する
            imageCache.put(drawableId, result);
        }

        // リソースを返す
        return result;
    }

    /**
     * マルチタッチ解析クラスを取得する。
     * @return
     */
    public MultiTouchInput getMultiTouchInput() {
        return game.getMultiTouchInput();
    }
}