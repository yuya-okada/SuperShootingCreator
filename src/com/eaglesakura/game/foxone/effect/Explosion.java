package com.eaglesakura.game.foxone.effect;

import com.eaglesakura.game.foxone.GameSprite;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.scene.GameSceneBase;

public class Explosion extends EffectBase {

    public Explosion(GameSceneBase scene, GameSprite parent) {
        super(scene);
        // 64x64サイズで12コマのスプライトを生成する
        sprite = loadAnimatedSprite(R.drawable.explosion, 64, 64, 12);
        // 爆発を発生させた親の位置に合わせる
        if (parent != null) {
            setPosition(parent.getPositionX(), parent.getPositionY());
        }
    }

}