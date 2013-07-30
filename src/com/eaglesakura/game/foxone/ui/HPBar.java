package com.eaglesakura.game.foxone.ui;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.PlayerFighter;
import com.eaglesakura.game.foxone.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;

public class HPBar {
    Sprite frame = null;

    /**
     * 赤いバー
     */
    Sprite barRed = null;

    /**
     * 黄色いバー
     */
    Sprite barYellow = null;

    /**
     * 緑のバー
     */
    Sprite barGreen = null;

    /**
     * プレイヤー機
     */
    PlayerFighter player = null;

    /**
     * シーン本体
     */
    PlaySceneBase scene = null;

    public HPBar(PlaySceneBase scene) {

        // プレイヤー情報を取り寄せる
        this.scene = scene;
        this.player = scene.getPlayer();

        // 必要な画像を読み込む
        frame = new Sprite(scene.loadImageDrawable(R.drawable.ui_hp_frame));
        barRed = new Sprite(scene.loadImageDrawable(R.drawable.ui_hp_r));
        barYellow = new Sprite(scene.loadImageDrawable(R.drawable.ui_hp_y));
        barGreen = new Sprite(scene.loadImageDrawable(R.drawable.ui_hp_g));
    }

    public void draw() {
        final SpriteManager spriteManager = scene.getSpriteManager();

        // HPバーの中心位置
        final int HP_BAR_CENTER = Define.VIRTUAL_DISPLAY_WIDTH - 50;

        // HPバーの上側の位置
        final int HP_BAR_TOP = 25;

        // 内側の目盛りを描画する
        {
            // 残HPが何パーセントか取得する
            final int hpPercent = player.getHPPercent();
            // 目盛りの個数を計算する。最大は12。
            int graduation = 12 * hpPercent / 100;

            if (!player.isDead()) {
                // プレイヤーが撃墜されていなかったら、最低1つは描画する
                graduation = Math.max(graduation, 1);
            }

            // 下側の位置を合わせる
            int bottom = HP_BAR_TOP + frame.getDstHeight() - 32;

            // 描画対象のスプライト
            Sprite sprite = barGreen;

            // HP残量によって表示するスプライトを変更

            if (hpPercent > 70) {
                // 残量70パーセントを超えていたら緑
                sprite = barGreen;
            } else if (hpPercent > 30) {
                // 残量30パーセントを超えていたら黄
                sprite = barYellow;
            } else {
                // 30パーセント未満なら赤
                sprite = barRed;
            }
            for (int i = 0; i < graduation; ++i) {
                sprite.setSpritePosition(HP_BAR_CENTER, bottom, Sprite.POSITION_CENTER_X | Sprite.POSITION_BOTTOM);
                spriteManager.draw(sprite);
                bottom -= sprite.getDstHeight();
            }
        }
        {
            // 外側のフレームを描画する
            frame.setSpritePosition(HP_BAR_CENTER, HP_BAR_TOP, Sprite.POSITION_CENTER_X | Sprite.POSITION_TOP);
            spriteManager.draw(frame);
        }
    }
}