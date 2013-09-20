package com.eaglesakura.game.foxone.fighter.enemy;

import android.graphics.Rect;
import android.util.Log;

import com.eaglesakura.game.bundle.Displayable;
import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.bullet.BulletBase;
import com.eaglesakura.game.foxone.bullet.DirectionBullet;
import com.eaglesakura.game.foxone.bullet.FrisbeeBullet;
import com.eaglesakura.game.foxone.bullet.Laser;
import com.eaglesakura.game.foxone.bullet.SnipeBullet;
import com.eaglesakura.game.foxone.fighter.FighterBase;
import com.eaglesakura.game.foxone.scene.GameSceneBase;
import com.eaglesakura.game.foxone.scene.PlaySceneBase;

import java.util.ArrayList;

//public abstract class EnemyFighterBase extends FighterBase {
public class BossFighterBase extends FighterBase {
    float createX;
    float createY;

    int conductNumber;

    public enum ConductType {
        Shot,
        SnipeShot,
        AllDirectionShot,
        Laser,
        MoveToUnder,
        MoveToUp,
        MoveToRight,
        MoveToLeft,
        Wait

    }

    /**
     * 敵の行動パターン
     */
    ArrayList<ConductType> conductArray = new ArrayList<ConductType>();

    ArrayList<Integer> conductFrame = new ArrayList<Integer>();

    /**
     * 生成されてからのフレームを記録する。
     */
    protected int frameCount = 0;

    Displayable image = null;


    ConductType nowConduct;

    /**
     * 移動速度
     */
    float moveSpeed = 2;

    /**
     * 移動の基準点
     */
    float centerX = 0;

    /**
     * sinの増加速度
     */
    float sinSpeed = 4f;

    /**
     * 現在のsinθの値
     */
    float theta = 0;

    /**
     * Cuve移動でのXの移動速度
     */
    float moveSpeedX = 5f;

    /**
     * 敵のX座標
     */
    int x;

    /**
     * 敵のY座標
     */
    int y;


    public BossFighterBase(Displayable image, ArrayList<ConductType> conductArray, int x, int y) {
    this(image,conductArray,x,y,null);
    }
    public BossFighterBase(Displayable image, ArrayList<ConductType> conductArray, int x, int y, GameSceneBase scene) {
        super(0, image, null, null, x, y, scene);


        this.x = x;

        this.y = y;

        // 攻撃手段を保持する
        this.conductArray = conductArray;

        for (int i = 0; i < conductArray.size(); i++) {

            if (conductArray.get(i) == ConductType.Shot) {
                conductFrame.add(30 * 3);
            } else if (conductArray.get(i) == ConductType.SnipeShot) {
                conductFrame.add(30 * 3);
            } else if (conductArray.get(i) == ConductType.AllDirectionShot) {
                conductFrame.add(360);
            } else if (conductArray.get(i) == ConductType.Laser) {
                conductFrame.add(300);
            } else if (conductArray.get(i) == ConductType.MoveToUp) {
                conductFrame.add(24);
            } else if (conductArray.get(i) == ConductType.MoveToUnder) {
                conductFrame.add(24);
            } else if (conductArray.get(i) == ConductType.MoveToRight) {
                conductFrame.add(24);
            } else if (conductArray.get(i) == ConductType.MoveToLeft) {
                conductFrame.add(24);
            } else if (conductArray.get(i) == ConductType.Wait) {
                conductFrame.add(24);
            }


        }

        this.image = image;
        // 攻撃手段によって、敵の見た目を変化させる

        if (scene != null) {
            sprite = loadSprite(image);
        }


        final int PLAY_AREA_WIDTH = Define.PLAY_AREA_RIGHT - Define.PLAY_AREA_LEFT;

        createX = PLAY_AREA_WIDTH / 5f * x;
        createX += PLAY_AREA_WIDTH / 5f / 2f;
        createX += Define.PLAY_AREA_LEFT;
        createY = Define.VIRTUAL_DISPLAY_HEIGHT - PLAY_AREA_WIDTH / 5 * y;

    }

    /**
     * 直線移動をするように初期化する
     *
     * @param moveSpeed
     */
    public void initMoveStraight(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * フレーム数のカウンタを0に戻す。
     */
    protected void resetFrameCount() {
        frameCount = 0;
    }

    @Override
    public void onDamage(BulletBase bullet) {
        super.onDamage(bullet);

        // ダメージを受けた結果、撃墜されたら、撃墜音を鳴らす
        if (isDead()) {
            scene.playSE(R.raw.dead);
        }
    }

    /**
     * 直線移動を行う
     */
    void onUpdateStraignt() {
        Log.d("", "into 'onUpdateStraight' method");

        offsetPosition(0, moveSpeed);
    }

    /**
     * 左右のジグザグ移動を行う
     */
    void onUpdateCurved() {

        sinSpeed = 0.1f;

        // 移動先のY座標は単純な加算でよい
        float nextY = getPositionY() + moveSpeed;

        // 移動先のX座標はsinから計算を行う
        float nextX = 0;
        {
            float xMove = (float) Math.sin(theta) * 5f;
            nextX = xMove;
            // sinの値をすすめる
            theta += sinSpeed;
        }

        Log.d("", "りnextX:" + nextX);
        // 求められた移動先座標を設定する
        setPosition(getPositionX() + nextX, nextY);
    }

    @Override
    public void update() {

        if (conductFrame.get(conductNumber) == frameCount) {

            switch (conductArray.get(conductNumber)) {
                case Shot:
                    updateStraight();
                    break;
                case SnipeShot:
                    updateSnipe();
                    break;
                case AllDirectionShot:
                    onUpdateAllDirection();
                    break;
                case Laser:
                    onUpdateLaser();
                    break;
                default:
                    //動かない
                    break;
            }
        }
        ++frameCount;
    }

    @Override
    public void draw() {
        // 撃墜されている場合は描画を行わない
        if (isDead()) {
            return;
        }
        super.draw();
    }

    @Override
    public boolean isAppearedDisplay() {
        Rect spriteArea = sprite.getDstRect();

        if (spriteArea.right < 0) {
            // スプライトの右端が画面よりも左にある場合、画面外となる
            return false;
        }

        if (spriteArea.left > Define.VIRTUAL_DISPLAY_WIDTH) {
            // スプライトの左端が画面よりも右にある場合、画面外となる
            return false;
        }

        if (spriteArea.top > Define.VIRTUAL_DISPLAY_HEIGHT) {
            // スプライトの上端が画面よりも下にある場合、画面外となる
            return false;
        }

        // どれにも該当しない場合、画面内（もしくは画面上部）にいる
        return true;
    }

    /**
     * まっすぐ弾を撃つ場合の更新
     */
    void updateStraight() {
            FrisbeeBullet bullet = new FrisbeeBullet(scene, this);
            ((PlaySceneBase) scene).addBullet(bullet);

            resetFrameCount();
    }

    /**
     * 狙撃する場合の更新
     */
    void updateSnipe() {
            SnipeBullet bullet = new SnipeBullet(scene, this);
            bullet.setup(((PlaySceneBase) scene).getPlayer(), 20);

            ((PlaySceneBase) scene).addBullet(bullet);

            resetFrameCount();
    }

    /**
     * 全方位攻撃トンガリの更新
     */
    void onUpdateAllDirection() {
            if (frameCount % 5 == 0) {

                // 方向弾を生成する
                DirectionBullet bullet = new DirectionBullet(scene, this);
                // 現在のフレーム数の角度へ10の速度で打ち込む
                bullet.setup(90 + frameCount, 10);

                // シーンに弾を追加する
                ((PlaySceneBase) scene).addBullet(bullet);

        }
        resetFrameCount();
    }

    /**
     * レーザー攻撃トンガリの更新
     */
    void onUpdateLaser() {
        switch (frameCount) {
            case 0: {
                Laser laser = new Laser(scene, this);
                ((PlaySceneBase) scene).addBullet(laser);
                resetFrameCount();
            }
            break;
            case 200: {
                resetFrameCount();
            }
            break;
        }
        resetFrameCount();
    }

    /**
     * 複合攻撃トンガリの更新
     */
    void onUpdateLaserAndDirection() {
        onUpdateAllDirection();
        onUpdateLaser();
    }
}