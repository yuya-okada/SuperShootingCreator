package com.eaglesakura.game.foxone.bullet;

import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.FighterBase;
import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.game.foxone.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.math.Vector2;

public class DirectionBullet extends BulletBase {

    /**
     * １フレームごとの移動量を保持する。
     */
    Vector2 offset = new Vector2(0, 10);

    public DirectionBullet(GameSceneBase scene, FighterBase shooter) {
        super(scene, shooter);

        sprite = loadSprite(R.drawable.bullet_enemy); // 弾の画像を保持する
        setPosition(shooter.getPositionX(), shooter.getPositionY()); // 位置を発射した戦闘機に合わせる
    }

    /**
     * 弾の向かう方向と速度を指定する。
     * @param directionDegree
     * @param moveSpeed
     */
    public void setup(float directionDegree, float moveSpeed) {
        // radianを求める
        final double radian = Math.toRadians(directionDegree + 90);

        // x移動量 = cos(radian) * 移動速度
        // y移動量 = sin(radian) * 移動速度
        offset.set((float) Math.cos(radian) * moveSpeed, (float) Math.sin(radian) * moveSpeed);

        //! yは計算と上下が逆になる
        offset.y = -offset.y;
    }

    @Override
    public void update() {
        // setupメソッドで計算されたオフセット量だけ移動させる。
        offsetPosition(offset.x, offset.y);

        FighterBase player = ((PlaySceneBase) scene).getPlayer();
        // プレイヤーと弾が衝突していたらダメージ処理を行う
        if (player.isIntersect(this)) {
            enable = false;
            player.onDamage(this);
        }
    }
}