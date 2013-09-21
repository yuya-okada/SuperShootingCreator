package com.gult.shootingcreator.bullet;

import android.graphics.Rect;

import com.eaglesakura.shootingcreator.bundle.ResourceDisplayable;
import com.gult.shootingcreator.Define;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.fighter.FighterBase;
import com.gult.shootingcreator.scene.GameSceneBase;
import com.gult.shootingcreator.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.graphics.ImageStub;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.math.Vector2;
import com.eaglesakura.lib.android.game.util.GameUtil;

public class Laser extends BulletBase {

    /**
     * メインのレーザー画像
     */
    Sprite laserMain = null;

    /**
     * レーザーの開始部分の画像
     */
    Sprite laserStart = null;

    /**
     * 現在の攻撃状態
     * @author TAKESHI YAMASHITA
     *
     */
    enum State {
        /**
         * チャージ中
         */
        Charge,

        /**
         * 攻撃中
         */
        Attack,

        /**
         * 終了中
         */
        Finish,
    }

    int frame = 0;

    State state = State.Charge;
    /**
     * 親機からこのピクセル数分だけ位置をずらす
     */
    Vector2 offsetLaserPosition = new Vector2();
    /**
     * レーザーの太さ
     */
    int laserWidth = 0;

    public Laser(GameSceneBase scene, FighterBase shooter) {
        super(scene, shooter);

        // 本体のスプライトには空画像を入れておく
        sprite = new Sprite(new ImageStub(1, 1));

        laserMain = loadSprite(new ResourceDisplayable(R.drawable.laser_main));
        laserStart = loadSprite(new ResourceDisplayable(R.drawable.laser_start));
    }

    @Override
    public Rect getIntersectRect() {
        // 攻撃中以外は当たり判定が発生しない
        if (state != State.Attack) {
            return null;
        }

        Rect result = new Rect();
        final int centerX = (int) getPositionX();
        final int top = (int) getPositionY();

        result.left = centerX - laserWidth / 2;
        result.top = top;
        result.right = result.left + laserWidth;
        result.bottom = Define.VIRTUAL_DISPLAY_HEIGHT;

        return result;
    }

    /**
     * チャージ中の動作を行う
     */
    void onUpdateCharge() {
        // レーザーの太さを30ピクセルまで上げる
        final int LASER_MAX_WIDTH = 30;
        laserWidth = GameUtil.targetMove(laserWidth, 2, LASER_MAX_WIDTH);

        if (frame > 30) {
            // 30フレームを超えたら攻撃状態に移行する
            state = State.Attack;
            frame = 0;
        }
    }

    /**
     * 攻撃中の動作を行う
     */
    void onUpdateAttack() {

        if (frame > 60) {
            // 2秒間のレーザー攻撃を終えたら、終了状態へ移行する
            state = State.Finish;
        }
    }

    /**
     * 終了中の動作を行う
     */
    void onUpdateFinish() {
        // レーザーの太さを0に戻していく
        laserWidth = GameUtil.targetMove(laserWidth, 2, 0);

        if (laserWidth == 0) {
            // レーザーが太さ0になったら、無効にする
            enable = false;
        }
    }

    /**
     * レーザーの攻撃起点座標をずらす
     * @param x
     * @param y
     */
    public void setOffsetLaserPosition(float x, float y) {
        offsetLaserPosition.set(x, y);
    }

    @Override
    public void update() {
        // 攻撃中の機体と同じ位置に表示する。砲門の位置合わせが必要ならその分をずらす
        setPosition(shooter.getPositionX() + offsetLaserPosition.x, shooter.getPositionY() + offsetLaserPosition.y);

        switch (state) {
            case Charge:
                onUpdateCharge();
                break;
            case Attack:
                onUpdateAttack();
                break;
            case Finish:
                onUpdateFinish();
                break;
        }

        // 射手が撃墜されたか、画面外にいる場合は無効にする
        if (shooter.isDead() || !shooter.isAppearedDisplay()) {
            enable = false;
        }

        {
            FighterBase player = ((PlaySceneBase) scene).getPlayer();
            // プレイヤーと弾が衝突していたらダメージ処理を行う
            // プレイヤーと当たっただけでは、レーザーは消えないためenableは変更しない
            if (player.isIntersect(this)) {
                player.onDamage(this);
            }
        }

        ++frame;
    }

    /**
     * チャージ中の描画を行う
     */
    void onDrawCharge() {

        final int centerX = (int) getPositionX();
        final int top = (int) getPositionY();
        final int laserSize = laserWidth;
        {
            // スプライトの位置と大きさを設定する
            laserStart.setSpritePosition(centerX, top, laserSize, laserSize, Sprite.POSITION_CENTER_X
                    | Sprite.POSITION_TOP);

            // 描画する
            getSpriteManager().draw(laserStart);
        }
        {
            // スプライトの位置と大きさを設定する
            // heightに負の値を設定することで上下を反転させる
            laserStart.setSpritePosition(centerX, top + (laserSize * 2), laserSize, -laserSize,
                    Sprite.POSITION_CENTER_X | Sprite.POSITION_TOP);

            // 描画する
            getSpriteManager().draw(laserStart);
        }
    }

    /**
     * 攻撃中の描画を行う
     */
    void onDrawAttack() {

        final int centerX = (int) getPositionX();
        final int top = (int) getPositionY();
        final int laserSize = laserWidth;

        // 先端を描画する
        {
            // スプライトの位置と大きさを設定する
            laserStart.setSpritePosition(centerX, top, laserSize, laserSize, Sprite.POSITION_CENTER_X
                    | Sprite.POSITION_TOP);

            // 描画する
            getSpriteManager().draw(laserStart);
        }

        // 残りを描画する
        {
            // スプライトの位置と大きさを設定する
            laserMain.setSpritePosition(centerX, top + laserSize, laserWidth, Define.VIRTUAL_DISPLAY_HEIGHT,
                    Sprite.POSITION_CENTER_X | Sprite.POSITION_TOP);

            // 描画する
            getSpriteManager().draw(laserMain);
        }
    }

    /**
     * 終了中の描画を行う
     */
    void onDrawFinish() {
        onDrawAttack();
    }

    @Override
    public void draw() {

        switch (state) {
            case Charge:
                onDrawCharge();
                break;
            case Attack:
                onDrawAttack();
                break;
            case Finish:
                onDrawFinish();
                break;
        }
    }
}