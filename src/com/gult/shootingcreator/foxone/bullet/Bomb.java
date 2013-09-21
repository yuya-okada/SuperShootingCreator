package com.gult.shootingcreator.foxone.bullet;

import android.graphics.Rect;

import com.gult.shootingcreator.bundle.ResourceDisplayable;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.Define;
import com.gult.shootingcreator.foxone.effect.BombExplosion;
import com.gult.shootingcreator.foxone.fighter.FighterBase;
import com.gult.shootingcreator.foxone.scene.GameSceneBase;
import com.gult.shootingcreator.foxone.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.math.Vector2;

import java.util.List;

public class Bomb extends BulletBase {

    /**
    * ボムの移動ベクトル
    */
    Vector2 moveVector = new Vector2();

    /**
    * 発射から着弾までの時間
    */
    static final int EXPLOSION_FRAME = 30;

    /**
    * ボムが発射されてからのフレーム数
    */
    int frameCount = 0;

    public Bomb(GameSceneBase scene, FighterBase shooter) {
        super(scene, shooter);

        sprite = loadSprite(new ResourceDisplayable(R.drawable.bomb)); // ボム画像を読み込む
        setPosition(shooter.getPositionX(), shooter.getPositionY());

        // 画面の中心に着弾させる
        final Vector2 IMPACT_POS = new Vector2(Define.VIRTUAL_DISPLAY_WIDTH / 2, Define.VIRTUAL_DISPLAY_HEIGHT / 2);

        // 指定した位置まで、指定したフレーム数で移動させる
        moveVector.x = (IMPACT_POS.x - getPositionX()) / EXPLOSION_FRAME;
        moveVector.y = (IMPACT_POS.y - getPositionY()) / EXPLOSION_FRAME;

        // 弾をその方向へ向ける
       // float rotate = GameUtil.getAngleDegree2D(position, IMPACT_POS);
        float rotate = 1.0f;
        sprite.setRotateDegree(rotate);
    }

    /**
     * 当たり判定の矩形を生成する。
     * 起爆フレームに達していない場合、nullを返す。
     */
    @Override
    public Rect getIntersectRect() {
        if (frameCount < EXPLOSION_FRAME) {
            // 起爆フレームに達していない
            return null;
        }

        Rect result = new Rect();
        result.left = 0;
        result.top = 0;
        result.right = Define.VIRTUAL_DISPLAY_WIDTH;
        result.bottom = Define.VIRTUAL_DISPLAY_HEIGHT;

        return result;
    }

    /**
     * 着弾時の処理を行う
     */
    void onExplosion() {
        // 着弾したため、弾を無効にする
        enable = false;

        PlaySceneBase playSceneBase = (PlaySceneBase) scene;
        // ボムのダメージ量
        final int BOMB_DAMAGE = 10;

        List<FighterBase> intersectenemies = playSceneBase.getIntersectEnemies(this);
        for (FighterBase enemy : intersectenemies) {

            // 必要な回数だけダメージを与える
            for (int i = 0; i < BOMB_DAMAGE; ++i) {

                // まだ撃墜されていないならダメージを与える
                if (!enemy.isDead()) {
                    enemy.onDamage(this);
                }
            }
        }

        // ボムのエフェクトを追加する
        playSceneBase.addEffect(new BombExplosion(scene, this));
    }

    @Override
    public void update() {
        // 指定の値だけ移動させる
        offsetPosition(moveVector.x, moveVector.y);

        if (frameCount >= EXPLOSION_FRAME) {
            onExplosion();
        }

        ++frameCount;
    }
}