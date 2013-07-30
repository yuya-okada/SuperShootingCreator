package com.eaglesakura.game.foxone.effect;

import com.eaglesakura.game.foxone.GameSprite;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.graphics.Sprite;

public class BombExplosion extends Explosion {

    public BombExplosion(GameSceneBase scene, GameSprite parent) {
        super(scene, parent);

        scene.playSE(R.raw.dead);
    }

    @Override
    public void update() {
        super.update();

        // 爆発エフェクトを大きくする
        final float nextScale = sprite.getScale() * 1.25f;
        sprite.setSpritePosition((int) getPositionX(), (int) getPositionY(), nextScale, Sprite.POSITION_CENTER);
    }

    @Override
    public void draw() {
        super.draw();

        if (sprite.getFrame() % 2 == 0) {
            // 偶数フレームは画面を白飛びさせる
            getSpriteManager().clear(Color.toColorRGBA(255, 255, 255, 128));
        }
    }
}