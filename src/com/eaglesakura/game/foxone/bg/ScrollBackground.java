package com.eaglesakura.game.foxone.bg;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;

public class ScrollBackground {
    /**
     * 背景スクロールする画像。
     * 上下がつながっている必要がある。
     */
    List<ImageBase> backgroundImages = new ArrayList<ImageBase>();

    /**
     *
     */
    SpriteManager spriteManager;

    /**
     * スクロールの最大ピクセル数
     */
    int scrollHeight = 0;

    /**
     * 描画座標(TOP)を示す
     */
    int posY = 0;

    /**
     *
     * @param game ゲームメインクラス
     * @param imageIds 読み込む画像のdrawableIdリスト
     */
    public ScrollBackground(FoxOne game, int[] imageIds) {
        spriteManager = game.getSpriteManager();
        for (int i = 0; i < imageIds.length; ++i) {
            ImageBase image = game.loadImageDrawable(imageIds[i]);
            scrollHeight += image.getHeight();
            backgroundImages.add(image);
        }
    }

    /**
     * 画面をスクローリングさせる
     * @param y
     */
    public void scroll(int y) {
        posY += y;
        posY %= scrollHeight;
    }

    /**
     * 指定した位置へ背景画像を描画する
     * @param y 表示したい位置
     */
    private void drawImages(int y) {
        int top = y;
        for (ImageBase image : backgroundImages) {
            spriteManager.drawImage(image, 0, top);
            top += image.getHeight();
        }
    }

    /**
     * 描画を行わせる
     */
    public void draw() {
        drawImages(posY); // 下側の背景画像を描画する
        drawImages(posY - scrollHeight); // 上側の背景画像を描画する
    }

}