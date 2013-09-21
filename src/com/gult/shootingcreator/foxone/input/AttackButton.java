package com.gult.shootingcreator.foxone.input;

import com.gult.shootingcreator.bundle.ResourceDisplayable;
import com.gult.shootingcreator.foxone.Define;
import com.gult.shootingcreator.foxone.GameSprite;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.scene.GameSceneBase;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.input.MultiTouchInput.TouchPoint;

public class AttackButton extends GameSprite {
    /**
     * 押していない時のボタン画像
     */
    Sprite release = null;

    /**
     * 押している時のボタン画像
     */
    Sprite press = null;

    /**
     * 攻撃する瞬間の場合、true
     */
    boolean attack = false;

    public AttackButton(GameSceneBase scene) {
        super(scene);

        release = loadSprite(new ResourceDisplayable(R.drawable.ui_shot)); // 離している時の画像
        press = loadSprite(new ResourceDisplayable(R.drawable.ui_shot_p)); // 押している時の画像

        sprite = release; // まずは離している時の画像を表示したい

        // 適当な位置で表示する
        setPosition(Define.VIRTUAL_DISPLAY_WIDTH - 50, Define.VIRTUAL_DISPLAY_HEIGHT - 120);

    }

    public void initBombButton() {
        release = loadSprite(new ResourceDisplayable(R.drawable.ui_bomb)); // 離してる時の画像
        press = loadSprite(new ResourceDisplayable(R.drawable.ui_bomb_p)); // 押してる時の画像

        sprite = release; // まずは押してる時の画像を表示したい

        // 適当な位置で表示する
        setPosition(Define.VIRTUAL_DISPLAY_WIDTH - 50, Define.VIRTUAL_DISPLAY_HEIGHT - 120 - 150);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        // release/pressのスプライト位置も更新する
        release.setSpritePosition((int) x, (int) y);
        press.setSpritePosition((int) x, (int) y);
    }

    /**
     * 攻撃する場合、trueを返す。
     * @return
     */
    public boolean isAttack() {
        return attack;
    }

    @Override
    public void update() {
        // 触れているか、離した瞬間のタッチ座標があるか確認する
        TouchPoint touchPoint = sprite.findIntersectTouchOrReleaseOnce(scene.getMultiTouchInput());

        if (touchPoint != null) {
            // 該当ポイントがあったので、「離した瞬間」ならば攻撃タイミングとして認識する
            if (touchPoint.isReleaseOnce()) {
                sprite = release; // 離している時の画像にする
                attack = true; // 攻撃するタイミングとして認識する
            } else {
                sprite = press; // 押している時の画像を表示する
                attack = false; // 攻撃するタイミングではない
            }
        } else {
            // 該当ポイントがなかったので、ボタンは押されていないし攻撃タイミングでもない
            sprite = release; // 離している時の画像を表示する
            attack = false; // 攻撃するタイミングではない
        }
    }
}