package com.eaglesakura.game.foxone;

import android.graphics.Rect;

import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.lib.android.game.graphics.ImageBase;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;
import com.eaglesakura.lib.android.game.math.Vector2;
import com.eaglesakura.lib.android.game.util.GameUtil;

public abstract class GameSprite {
    /**
     * 描画に利用する画像
     */
    protected Sprite sprite = null;

    /**
     * 描画位置
     */
    protected Vector2 position = new Vector2();

    /**
     * このスプライトを保持しているシーン
     */
    protected GameSceneBase scene = null;

    protected GameSprite(GameSceneBase scene) {
        this.scene = scene;
    }

    /**
     * 描画対象のスプライトを取得する。
     * @return
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * drawableの画像からスプライトを作成する。
     * @param drawableId
     */
    protected Sprite loadSprite(int drawableId) {
        ImageBase image = scene.loadImageDrawable(drawableId);
        Sprite result = new Sprite(image);
        return result;
    }

    /**
        * アニメーションスプライトを作成する
        * @param drawableId
        * @param gridX
        * @param gridY
        * @param komaNum
        * @return
        */
    protected Sprite loadAnimatedSprite(int drawableId, int blockWidth, int blockHeight, int komaNum) {
        // 通常スプライトを作成する
        Sprite result = loadSprite(drawableId);

        // スプライトの中身をコマとして切り分ける
        result.getMaster().addAnimationFrames(blockWidth, blockHeight, 0, komaNum);

        // 切り分け終わったものを返す
        return result;
    }

    /**
     * 描画位置を指定する。
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
        // スプライトに現在位置を伝える
        sprite.setSpritePosition((int) position.x, (int) position.y);
    }

    /**
     * X座標を取得する。
     * 画面左端が0となる。
     * @return
     */
    public float getPositionX() {
        return position.x;
    }

    /**
     * Y座標を取得する。
     * 画面上端が0となる。
     * @return
     */
    public float getPositionY() {
        return position.y;
    }

    /**
     * 現在の位置から指定ピクセル数だけ位置をずらす。
     * 絶対位置ではなく、相対位置で制御したい場合に利用する。
     * @param x
     * @param y
     */
    public void offsetPosition(float x, float y) {
        setPosition(getPositionX() + x, getPositionY() + y);// 指定ピクセル位置をずらす
    }

    /**
     * スプライト描画クラスを取得する。
     * @return
     */
    protected SpriteManager getSpriteManager() {
        return scene.getSpriteManager();
    }

    /**
     * ゲームエリアからはみ出していたらfalseを返し、プレイエリア内に収まっていればtrueを返す。
     * @return
     */
    public boolean isPlayArea() {
        if (getPositionY() < 0) {
            return false; // 画面の上方向にはみ出している
        }
        if (getPositionY() > Define.VIRTUAL_DISPLAY_HEIGHT) {
            return false; // 画面の下方向にはみ出している
        }
        if (getPositionX() < Define.PLAY_AREA_LEFT) {
            return false; // 画面の左方向にはみ出している
        }
        if (getPositionX() > Define.PLAY_AREA_RIGHT) {
            return false; // 画面の右方向にはみ出している
        }

        return true;
    }

    /**
     * プレイエリアからはみ出していたら、プレイエリア内に引き戻す。
     */
    public void correctPosition() {
        if (!isPlayArea()) {
            // X座標は最小PLAY_AREA_LEFT〜最大PLAY_AREA_RIGHTに収まらなければならない。
            // Y座標は最小0〜最大VIRTUAL_DISPLAY_HEIGHTに収まらなければならない。
            setPosition(GameUtil.minmax(Define.PLAY_AREA_LEFT, Define.PLAY_AREA_RIGHT, getPositionX()),
                    GameUtil.minmax(0, Define.VIRTUAL_DISPLAY_HEIGHT, getPositionY()));
        }
    }

    /**
     * ディスプレイ範囲内に存在する場合はtrueを返す。
     * @return
     */
    public boolean isAppearedDisplay() {
        Rect mySpriteArea = sprite.getDstRect();
        return mySpriteArea.intersects(0, 0, Define.VIRTUAL_DISPLAY_WIDTH, Define.VIRTUAL_DISPLAY_HEIGHT);
    }

    /**
     * 毎フレームの更新処理を行う。
     */
    public abstract void update();

    /**
     * 描画を行う。
     */
    public void draw() {
        if (sprite != null) {
            getSpriteManager().draw(sprite);
        }
    }

    /**
     * 衝突判定用の範囲を取得する
     * @return
     */
    public Rect getIntersectRect() {
        return getSprite().getDstRect();
    }
    
    public  void setScene(GameSceneBase scene){
    	
    	this.scene=scene;
    	
    }
}