package com.eaglesakura.game.foxone.fighter;

import android.graphics.Rect;

import com.eaglesakura.game.foxone.GameSprite;
import com.eaglesakura.game.foxone.bullet.BulletBase;
import com.eaglesakura.game.foxone.scene.GameSceneBase;

public abstract class FighterBase extends GameSprite {

    /**
     * 戦闘機のヒットポイント。
     * デフォルトは1にしておく。
     */
    protected int hp = 1;

    public FighterBase(GameSceneBase scene) {
        super(scene);

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
}