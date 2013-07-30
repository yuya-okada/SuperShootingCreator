package com.eaglesakura.game.foxone.bullet;

import android.graphics.Rect;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.FighterBase;
import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.game.foxone.scene.PlaySceneBase;
import com.eaglesakura.lib.android.game.graphics.ImageStub;
import com.eaglesakura.lib.android.game.graphics.Sprite;
import com.eaglesakura.lib.android.game.util.GameUtil;

public class Beam extends BulletBase {

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
     * チャージ中のエフェクト
     */
    Sprite charge = null;

    /**
     * ビーム本体
     */
    Sprite beam = null;

    /**
     * レーザーの太さ
     */
    int laserWidth = 0;

    public Beam(GameSceneBase scene, FighterBase shooter) {
        super(scene, shooter);

        // 本体のスプライトには空画像を入れておく
        sprite = new Sprite(new ImageStub(1, 1));

        {
            // ビーム画像とアニメーションを読み込む
            beam = loadSprite(R.drawable.beam_main);
            beam.getMaster().addAnimationFrames(256, 1024, 0, 4);
        }
        {
            // チャージ画像とアニメーションを読み込む
            charge = loadSprite(R.drawable.beam_charge);
            charge.getMaster().addAnimationFrames(256, 256, 0, 14);
        }
    }

    @Override
    public Rect getIntersectRect() {
        // 攻撃中か、終了中に当たり判定が発生する
        if (state != State.Attack && state != State.Finish) {
            return null;
        }

        Rect result = new Rect();
        final int centerX = (int) getPositionX();
        final int top = (int) getPositionY();

        final int hitWidth = laserWidth / 2; // 実際の当たり判定は画像よりも小さくする

        result.left = centerX - hitWidth / 2;
        result.top = top;
        result.right = result.left + hitWidth;
        result.bottom = Define.VIRTUAL_DISPLAY_HEIGHT;

        return result;
    }

    /**
     * チャージ中の動作を行う
     */
    void onUpdateCharge() {

        charge.nextFrame();
        if (charge.isAnimationFinish()) {
            // アニメが再生し終わったら、index8までアニメーションを戻す
            charge.setFrame(8);
        }

        // 指定のチャージ時間を終えたら、発射する
        if (frame > 60) {
            // 指定フレームを超えたら攻撃状態に移行する
            state = State.Attack;
            frame = 0;
        }
    }

    /**
     * 攻撃中の動作を行う
     */
    void onUpdateAttack() {
        beam.nextFrame();
        if (beam.isAnimationFinish()) {
            // アニメーションが終わったら最初に戻す
            beam.setFrame(0);
        }

        final int LASER_MAX_WIDTH = 256;

        // レーザーの太さを指定ピクセルまで上げる
        laserWidth = GameUtil.targetMove(laserWidth, 5, LASER_MAX_WIDTH);

        if (frame > 120) {
            // 指定時間レーザー攻撃を終えたら、終了状態へ移行する
            state = State.Finish;
        }
    }

    /**
     * 終了中の動作を行う
     */
    void onUpdateFinish() {

        beam.nextFrame();
        if (beam.isAnimationFinish()) {
            // アニメーションが終わったら最初に戻す
            beam.setFrame(0);
        }

        // レーザーの太さを元に戻す
        laserWidth = GameUtil.targetMove(laserWidth, 5, 0);

        if (laserWidth == 0) {
            // レーザーが太さ0になったら、無効にする
            enable = false;
        }
    }

    @Override
    public void update() {
        // 主砲位置に合わせる
        setPosition(shooter.getPositionX(), shooter.getPositionY());

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
        charge.setSpritePosition((int) getPositionX(), (int) getPositionY(), Sprite.POSITION_CENTER_X
                | Sprite.POSITION_TOP);
        getSpriteManager().draw(charge);
    }

    /**
     * 攻撃中の描画を行う
     */
    void onDrawAttack() {
        final int centerX = (int) getPositionX();
        final int top = (int) getPositionY() + 80;
        final int laserSize = laserWidth;

        beam.setSpritePosition(centerX, top, laserSize, Define.VIRTUAL_DISPLAY_HEIGHT, Sprite.POSITION_TOP
                | Sprite.POSITION_CENTER_X);
        getSpriteManager().draw(beam);
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