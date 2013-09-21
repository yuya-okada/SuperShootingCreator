package com.gult.shootingcreator.foxone.effect;

import com.gult.shootingcreator.bundle.ResourceDisplayable;
import com.gult.shootingcreator.foxone.GameSprite;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.scene.GameSceneBase;

public class Explosion extends EffectBase {

    public Explosion(GameSceneBase scene, GameSprite parent) {
        super(scene);
        // 64x64サイズで12コマのスプライトを生成する
        sprite = loadAnimatedSprite(new ResourceDisplayable(R.drawable.explosion), 64, 64, 12);
        // 爆発を発生させた親の位置に合わせる
        if (parent != null) {
            setPosition(parent.getPositionX(), parent.getPositionY());
        }
    }

}