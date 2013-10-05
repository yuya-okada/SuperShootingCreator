package com.gult.shootingcreator.foxone.fighter.enemy;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.gult.shootingcreator.bundle.Displayable;
import com.gult.shootingcreator.foxone.Define;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.bullet.BulletBase;
import com.gult.shootingcreator.foxone.bullet.DirectionBullet;
import com.gult.shootingcreator.foxone.bullet.FrisbeeBullet;
import com.gult.shootingcreator.foxone.bullet.Laser;
import com.gult.shootingcreator.foxone.bullet.SnipeBullet;
import com.gult.shootingcreator.foxone.fighter.FighterBase;
import com.gult.shootingcreator.foxone.scene.GameSceneBase;
import com.gult.shootingcreator.foxone.scene.PlaySceneBase;

import java.util.ArrayList;

//public abstract class EnemyFighterBase extends FighterBase {
public class BossFighterBase extends FighterBase {
    int conductNumber = 0;
    boolean appearance = true;

    public enum ConductType implements Parcelable {
        Shot,
        SnipeShot,
        AllDirectionShot,
        Laser,
        MoveToUnder,
        MoveToUp,
        MoveToRight,
        MoveToLeft,
        Wait;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<ConductType> CREATOR = new Creator<ConductType>() {
            @Override
            public ConductType createFromParcel(final Parcel source) {
                return ConductType.values()[source.readInt()];
            }

            @Override
            public ConductType[] newArray(final int size) {
                return new ConductType[size];
            }
        };
    }

    /**
     * 敵の行動パターン
     */
    ArrayList<ConductType> conductArray = new ArrayList<ConductType>();

    ArrayList<Integer> conductFrame = new ArrayList<Integer>();


    ConductType continueConduct;

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

    public BossFighterBase(BossFighterBase toCopy) {
        this(toCopy.getDisplayable(), toCopy.conductArray, toCopy.getX(), toCopy.getY(), toCopy.getScene());
    }


    public BossFighterBase(Displayable image, ArrayList<ConductType> conductArray, int x, int y) {
        this(image, conductArray, x, y, null);
    }

    public BossFighterBase(Displayable image, ArrayList<ConductType> conductArray, int x, int y, GameSceneBase scene) {
        super(0, image, x, y, scene);

        hp=20;

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


    @Override
    public void update() {

        if(appearance == true){
            offsetPosition(0,2);
            Log.d("","apearance");
            if(position.y > 80){
                appearance = false;
            }
        }

        //if (conductFrame.get(conductNumber) == frameCount) {
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
            case MoveToUp:
                updateMove(0,(int)moveSpeed);
                break;
            case MoveToUnder:
                updateMove(0,-1* (int)moveSpeed);
                break;
            case MoveToRight:
                updateMove((int)moveSpeed,0);
                break;
            case MoveToLeft:
                updateMove(-1* (int)moveSpeed,0);
                break;
            default:
                //動かない
                break;
        }
        //}

        if (conductNumber >= conductArray.size()) {
            conductNumber = 0;
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
        if (frameCount == 1) {
            FrisbeeBullet bullet = new FrisbeeBullet(scene, this);
            ((PlaySceneBase) scene).addBullet(bullet);
        }
        if (frameCount >= 90) {
            conductNumber++;
            resetFrameCount();
        }
    }

    /**
     * 狙撃する場合の更新
     */
    void updateSnipe() {
        Log.d("", "gorira" + frameCount);
        if (frameCount == 1) {

            SnipeBullet bullet = new SnipeBullet(scene, this);
            bullet.setup(((PlaySceneBase) scene).getPlayer(), 15);
            ((PlaySceneBase) scene).addBullet(bullet);
        }
        if (frameCount >= 90) {
            conductNumber++;
            resetFrameCount();
        }
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
        if (frameCount >= 360) {
            conductNumber++;
            resetFrameCount();
        }
    }

    /**
     * レーザー攻撃トンガリの更新
     */
    void onUpdateLaser() {
        if (frameCount == 1) {
            Laser laser = new Laser(scene, this);
            ((PlaySceneBase) scene).addBullet(laser);
        }
        if (frameCount == 200) {
            resetFrameCount();
            conductNumber++;
        }
    }

    /**
     * 下に移動
     */
    void updateMove(int moveX,int moveY) {

        offsetPosition(moveX, moveY);

        if (frameCount >= 24) {
            conductNumber++;
            resetFrameCount();
        }
    }

    @Override
    public BossFighterBase clone() {
        BossFighterBase result = (BossFighterBase) super.clone();
        result.conductArray = conductArray;
        result.conductFrame = conductFrame;
        result.conductNumber = conductNumber;
        return result;
    }


    @Override
    public String getType() {
        return "Boss";
    }
}