package com.eaglesakura.game.foxone.effect;

import com.eaglesakura.game.foxone.GameSprite;
import com.eaglesakura.game.foxone.scene.GameSceneBase;

public abstract class EffectBase extends GameSprite {

    public EffectBase(GameSceneBase scene) {
        super(scene);
    }

    /**
     * このエフェクトが有効ならtrueを返す。
     * falseを返した場合、このエフェクトはフィールドから取り除かれる。
     * @return
     */
    public boolean isEnable() {
        return !sprite.isAnimationFinish();
    }

    @Override
    public void update() {
        // スプライトのアニメーションを更新する
        sprite.nextFrame();
    }
}