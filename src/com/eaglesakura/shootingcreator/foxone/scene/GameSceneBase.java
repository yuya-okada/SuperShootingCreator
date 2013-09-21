package com.gult.shootingcreator.scene;

import com.eaglesakura.shootingcreator.bundle.Displayable;
import com.gult.shootingcreator.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;
import com.eaglesakura.lib.android.game.input.MultiTouchInput;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.sound.SoundManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    //Map<Integer, ImageBase> imageCache = new HashMap<Integer, ImageBase>();
    Map<UUID, ImageBase> imageCache = new HashMap<UUID, ImageBase>();


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
     * @param displayable
     * @return
     */
    public ImageBase loadImageDrawable(Displayable displayable) {
        // まずはキャッシュから画像を探す
        ImageBase result = imageCache.get(displayable.getUUId());

        // キャッシュが見つからなかったら
        if (result == null) {
            result = displayable.getImageBase(game);
            imageCache.put(displayable.getUUId(), result);
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