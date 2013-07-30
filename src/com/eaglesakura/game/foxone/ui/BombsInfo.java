package com.eaglesakura.game.foxone.ui;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.PlayerFighter;
import com.eaglesakura.game.foxone.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;

public class BombsInfo {

    /**
    * プレイヤー機
    */
    PlayerFighter player = null;

    /**
    * シーン本体
    */
    PlaySceneBase scene = null;

    /**
    * 表示するボム画像
    */
    Sprite bombImage = null;

    public BombsInfo(PlaySceneBase scene) {

        // プレイヤー情報を取り寄せる
        this.scene = scene;
        this.player = scene.getPlayer();

        // 必要な画像を読み込む
        bombImage = new Sprite(scene.loadImageDrawable(R.drawable.bomb));
        // ボムは横向きに表示する
        bombImage.setRotateDegree(90);
    }

    public void draw() {
        SpriteManager spriteManager = scene.getSpriteManager();
        int bombCount = player.getBombCount();

        // HPバーの中心位置
        final int BOMB_CENTER = Define.VIRTUAL_DISPLAY_WIDTH - 50;

        // ボムの表示座標
        int y = 300;

        // ボムの数だけ描画する
        for (int i = 0; i < bombCount; ++i) {
            // 通常よりも大きく描画する
            bombImage.setSpritePosition(BOMB_CENTER, y, 2.0f, Sprite.POSITION_CENTER);
            spriteManager.draw(bombImage);

            // 表示座標を少し下へ下げる
            y += 15;
        }
    }
}