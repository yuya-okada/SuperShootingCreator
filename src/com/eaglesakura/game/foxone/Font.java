package com.eaglesakura.game.foxone;

import com.eaglesakura.game.foxone.R;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.graphics.gl11.SpriteManager;

public class Font {
    /**
     * 表示するフォントの内容と配置を示したテーブル
     */
    private static final String FONT_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789:-.・()/?<>@`";

    /**
    * 画像１文字の幅
    */
    private static final int FONT_WIDTH = 32;
    /**
     * 画像１文字の高さ
     */
    private static final int FONT_HEIGHT = 64;

    /**
     * フォント描画用スプライト
     */
    Sprite fontSprite = null;

    /**
     * ゲーム本体
     */
    FoxOne game;

    public Font(FoxOne game) {
        this.game = game;
        fontSprite = new Sprite(game.loadImageDrawable(R.drawable.fonts));
    }

    /**
     * 中央座標を指定して描画を行う
     * @param text 描画するテキスト
     * @param x X座標
     * @param y Y座標
     * @param scale フォントの倍率
     */
    public void drawCenter(String text, int x, int y, float scale) {
        // 描画する全体の幅
        int textWidth = (int) ((text.length() * FONT_WIDTH) * scale);

        // 描画する全体の高さ
        int textHeight = (int) (FONT_HEIGHT * scale);

        drawLT(text, x - (textWidth / 2), y - (textHeight / 2), scale);
    }

    /**
     * 画像の左上座標を基点として描画を行う。
     * @param text
     * @param x
     * @param y
     */
    public void drawLT(String text, int x, int y, float scale) {
        text = text.toUpperCase(); // テキストを大文字に揃える
        char[] moji = text.toCharArray(); // 文字列を文字単位に分割する
        SpriteManager spriteManager = game.getSpriteManager();

        // フォントの起点を設定する
        fontSprite.setSliceGrid(FONT_WIDTH, FONT_HEIGHT, 0);

        // スケーリングを指定してスプライト位置を設定する
        fontSprite.setSpritePosition(x, y, scale, scale, Sprite.POSITION_LEFT | Sprite.POSITION_TOP);

        for (int i = 0; i < moji.length; ++i) {
            // 文字の場所を画像テーブルから探す
            int index = FONT_TABLE.indexOf(moji[i]);

            if (index >= 0) {
                // 文字が見つかったから、描画を行う
                fontSprite.setSliceGrid(FONT_WIDTH, FONT_HEIGHT, index);
                // スプライトの描画を行う
                spriteManager.draw(fontSprite);
            }

            // スプライトの位置を１文字分右へずらす
            fontSprite.offsetSpritePosition((int) (FONT_WIDTH * scale), 0);
        }
    }
}