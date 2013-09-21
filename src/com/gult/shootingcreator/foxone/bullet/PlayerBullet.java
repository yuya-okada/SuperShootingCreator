package com.gult.shootingcreator.foxone.bullet;

import com.gult.shootingcreator.bundle.ResourceDisplayable;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.effect.Explosion;
import com.gult.shootingcreator.foxone.fighter.FighterBase;
import com.gult.shootingcreator.foxone.scene.GameSceneBase;
import com.gult.shootingcreator.foxone.scene.PlaySceneBase;

public class PlayerBullet extends BulletBase {

    public PlayerBullet(GameSceneBase scene, FighterBase shooter) {
        super(scene, shooter);
        sprite = loadSprite(new ResourceDisplayable(R.drawable.bullet_player)); // 弾の画像を保持する
        setPosition(shooter.getPositionX(), shooter.getPositionY()); // 位置を発射した戦闘機に合わせる

        // 音を再生する
        scene.playSE(R.raw.player_bullet);
    }

    @Override
    public void update() {
        offsetPosition(0, -10); // 上へ向かって移動させる

        // 敵機との衝突判定を行わせる
        if (((PlaySceneBase) scene).intersectsEnemy(this) != null) {
            // どれかの敵にあたったらこの弾は無効にする
            enable = false;

            // 衝突箇所に爆発を起こす
            {
                Explosion exp = new Explosion(scene, this);
                ((PlaySceneBase) scene).addEffect(exp);
            }
        }
    }
}