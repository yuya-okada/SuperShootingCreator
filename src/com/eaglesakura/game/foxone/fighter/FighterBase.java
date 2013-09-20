package com.eaglesakura.game.foxone.fighter;

import android.graphics.Rect;

import com.eaglesakura.game.bundle.Displayable;
import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.GameSprite;
import com.eaglesakura.game.foxone.bullet.BulletBase;
import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.lib.android.game.graphics.Sprite;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class FighterBase extends GameSprite {

    protected float createX;
    protected float createY;

    int lastCreateFrame;
    /**
     * 攻撃手段
     */
    AttackType attackType = AttackType.Not;


    public enum AttackType {
        /**
         * まっすぐに弾を撃つ
         */
        ShotStraight,

        /**
         * プレイヤーを狙撃する
         */
        Snipe,
        /**
         * 全方位弾を撃つ
         */
        AllDirection,

        /**
         * レーザーを撃つ
         */
        Laser,

        /**
         * 複合攻撃
         */
        LaserAndDirection,
        /**
         * 何もしない
         */
        Not,

    }
    MoveType moveType = MoveType.Not;

    /**
     * 移動手段を列挙する
     * @author TAKESHI YAMASHITA
     *
     */
    public enum MoveType {
        /**
         * 直線的に動く
         */
        Straight,

        /**
         * 曲線的に動く
         */
        Curved,

        /**
         * 動かない
         */
        Not,
    }

    Displayable image = null;

    int x;
    int y;

    /**
     * 戦闘機のヒットポイント。
     * デフォルトは1にしておく。
     */
    protected int hp = 1;

    public FighterBase(GameSceneBase scene) {
        this(scene, null);
    }

    public FighterBase(GameSceneBase scene, Displayable image) {
        this(0, image, null, null, -1, -1, scene);
    }

    public FighterBase(int createFrame,Displayable image,MoveType moveType,AttackType attackType,int x,int y,GameSceneBase scene) {
        super(scene);

        lastCreateFrame = createFrame;

        this.x = x;

        this.y = y;

        // 攻撃手段を保持する
        this.attackType = attackType;

        this.moveType = moveType;

        this.image = image;
        // 攻撃手段によって、敵の見た目を変化させる

        if(scene != null){
            sprite = loadSprite(image);
        }


        final int PLAY_AREA_WIDTH = Define.PLAY_AREA_RIGHT - Define.PLAY_AREA_LEFT;

        createX = PLAY_AREA_WIDTH / 5f * x;
        createX += PLAY_AREA_WIDTH / 5f / 2f;
        createX += Define.PLAY_AREA_LEFT;
        createY = Define.VIRTUAL_DISPLAY_HEIGHT-PLAY_AREA_WIDTH / 5 * y;
    }

    /**
     * 見た目よりも当たり判定を小さくする
     */
    @Override
    public Rect getIntersectRect() {
        Rect result = new Rect(sprite.getDstRect());
        result.inset(sprite.getDstWidth() / 4, sprite.getDstHeight() / 4);
        return result;
    }

    @Override
    protected Sprite loadSprite(Displayable displayable) {
        sprite = super.loadSprite(displayable);
        setPosition(createX, createY);
        return sprite;
    }

    /**
     * ２つのスプライトが衝突している場合、trueを返す。
     * @param other
     * @return
     */
    public boolean isIntersect(GameSprite other) {

        if (isDead()) {
            // 撃墜済みの戦闘機に当たり判定は発生しない。
            return false;
        }

        Rect mySpriteArea = getIntersectRect();
        Rect otherSpriteArea = other.getIntersectRect();

        // どちらかがnullだったら接触できない
        if (mySpriteArea == null || otherSpriteArea == null) {
            return false;
        }

        return Rect.intersects(mySpriteArea, otherSpriteArea);
    }

    /**
    * 弾が当たったらこのメソッドに通知される。
    * @param bullet
    */
    public void onDamage(BulletBase bullet) {
        --hp;
    }

    /**
    * この機体が撃墜されていたらtrueを返す。
    * @return
    */
    public boolean isDead() {
        return hp <= 0;
    }
    public JSONObject toJson(){

        JSONObject enemy = new JSONObject();

        try {
            enemy.put("attackType",attackType.toString());
            enemy.put("moveType",moveType.toString());
            enemy.put("imageType",image.toJSON());
            enemy.put("x",x);
            enemy.put("y",y);

            return enemy;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Displayable getDisplayable(){
        return image;
    }

    public float getCreateX() {
        return createX;
    }

    public float getCreateY() {
        return createY;
    }
    public AttackType getAttackType(){
        return attackType;
    }
    public MoveType getMoveType(){
        return moveType;
    }
    public int getLastCreateFrame(){
        return lastCreateFrame;
    }

    public void setLastCreateFrame(int lastCreateFrame) {
        this.lastCreateFrame = lastCreateFrame;
    }

    @Override
    public void setScene(GameSceneBase scene) {
        super.setScene(scene);
        sprite = loadSprite(image);
    }
}