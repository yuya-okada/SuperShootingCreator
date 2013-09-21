//package com.gult.shootingcreator.foxone.fighter.enemy;
//
//import android.graphics.Rect;
//
//import com.gult.shootingcreator.R;
//import com.gult.shootingcreator.foxone.bullet.Beam;
//import com.gult.shootingcreator.foxone.bullet.BulletBase;
//import com.gult.shootingcreator.foxone.bullet.Laser;
//import com.gult.shootingcreator.foxone.bullet.SnipeBullet;
//import com.gult.shootingcreator.foxone.effect.Explosion;
//import com.gult.shootingcreator.foxone.scene.GameSceneBase;
//import com.gult.shootingcreator.foxone.scene.GameSceneStage1;
//import com.gult.shootingcreator.foxone.scene.PlaySceneBase;
//import com.eaglesakura.lib.android.game.graphics.Sprite;
//import com.eaglesakura.lib.android.game.util.GameUtil;
//
//public class MotherShip extends EnemyFighterBase {
//
//    /**
//     * 現在の行動モード
//     * @author TAKESHI YAMASHITA
//     *
//     */
//    enum State {
//        /**
//         * 母艦が画面外から現れる
//         */
//        Encounter,
//
//        /**
//         * 通常の攻撃パターンに移る
//         */
//        NormalAttacks,
//
//        /**
//         * 発狂モードの攻撃パターンに移る
//         */
//        CrazyAttacks,
//
//        /**
//         * 撃墜状態
//         */
//        Dead,
//    }
//
//    State state = State.Encounter;
//
//    /**
//     * 最大HPを設定する
//     */
//    static final int MAX_HP = 50;
//
//    public MotherShip(GameSceneBase scene) {
//        super(scene);
//        hp = MAX_HP;
//        sprite = loadSprite(R.drawable.boss);
//    }
//
//    /**
//     * 遭遇状態。
//     * 画面の上から現れ、適当な位置で停止する
//     */
//    void onUpdateEncounter() {
//        final float TARGET_POS_Y = 150;
//
//        // 5ピクセルずつ画面に入ってくる
//        final float nextY = GameUtil.targetMove(getPositionY(), 5, TARGET_POS_Y);
//
//        setPosition(getPositionX(), nextY);
//
//        // 移動が完了したら、攻撃を開始する
//        if (nextY == TARGET_POS_Y) {
//            state = State.NormalAttacks;
//            resetFrameCount();
//        }
//    }
//
//    /**
//     * 通常攻撃を行う
//     */
//    void onUpdateNormalAttacks() {
//
//        PlaySceneBase playSceneBase = (PlaySceneBase) scene;
//
//        // 指定したフレームで行動を起こさせる
//        switch (frameCount) {
//            case 20:
//            case 30:
//            case 40: {
//                // このタイミングで弾を撃つ
//                SnipeBullet bullet = new SnipeBullet(scene, this);
//                bullet.setup(playSceneBase.getPlayer(), 15);
//                playSceneBase.addBullet(bullet);
//            }
//                break;
//            case 100:
//            case 250: {
//                // このタイミングでレーザーを撃つ
//                Laser laser = new Laser(scene, this);
//
//                // 1/3の確率で左右中央いずれかの砲門から発射する
//                if (Math.random() > (0.7)) {
//                    // 右の砲門
//                    laser.setOffsetLaserPosition(70, 110);
//                } else if (Math.random() > 0.4) {
//                    // 左の砲門
//                    laser.setOffsetLaserPosition(-70, 110);
//                } else {
//                    // 中央の砲門
//                    laser.setOffsetLaserPosition(0, 100);
//                }
//                playSceneBase.addBullet(laser);
//            }
//                break;
//            case 300:
//                // 行動リセットする
//                resetFrameCount();
//                break;
//        }
//
//        // HPが半分未満になったら発狂モードになる
//        if (hp < (MAX_HP / 2)) {
//            state = State.CrazyAttacks;
//            resetFrameCount();
//        }
//    }
//
//    /**
//     * 発狂攻撃を行う
//     */
//    void onUpdateCrazyAttacks() {
//        {
//            // 少し撤退する素振りを見せる
//            final float TARGET_POS_Y = 100;
//            // 1ピクセルずつ画面から遠ざかる
//            final float nextY = GameUtil.targetMove(getPositionY(), 1, TARGET_POS_Y);
//            setPosition(getPositionX(), nextY);
//        }
//
//        PlaySceneBase playSceneBase = (PlaySceneBase) scene;
//        // 指定したフレームで行動を起こさせる
//        switch (frameCount) {
//            case 40: {
//                // 2門同時攻撃
//                {
//                    // 右の砲門
//                    Laser laser = new Laser(scene, this);
//                    laser.setOffsetLaserPosition(70, 110);
//                    playSceneBase.addBullet(laser);
//                }
//                {
//
//                    // 左の砲門
//                    Laser laser = new Laser(scene, this);
//                    laser.setOffsetLaserPosition(-70, 110);
//                    playSceneBase.addBullet(laser);
//                }
//                break;
//            }
//            case 200: {
//                // ビームを放つ
//                Beam beam = new Beam(scene, this);
//                playSceneBase.addBullet(beam);
//            }
//                break;
//
//            case 210:
//            case 220:
//            case 230:
//            case 240:
//            case 250: {
//                // このタイミングで弾を撃つ
//                SnipeBullet bullet = new SnipeBullet(scene, this);
//                bullet.setup(playSceneBase.getPlayer(), 10);
//                playSceneBase.addBullet(bullet);
//            }
//                break;
//            case 500:
//                // 行動リセットする
//                resetFrameCount();
//                break;
//        }
//    }
//
//    /**
//     * 撃墜演出
//     */
//    void onUpdateDead() {
//        // 演出中は弾を削除し続ける
//        ((PlaySceneBase) scene).clearBullets();
//
//        // 2フレームに1回、爆炎を追加する
//        if (frameCount % 2 == 0) {
//            final float offsetX = (float) (Math.random() - 0.5) * (256);
//            final float offsetY = (float) (Math.random() - 0.5) * (256);
//
//            Explosion explosion = new Explosion(scene, this);
//            explosion.offsetPosition(offsetX, offsetY);
//            ((PlaySceneBase) scene).addEffect(explosion);
//        }
//
//        // 少しずつ小さくして、沈んでいるように見せる
//        sprite.setRotateDegree(sprite.getRotateDegree() + 0.01f);
//        sprite.setSpritePosition((int) getPositionX(), (int) getPositionY(), sprite.getScale() * 0.999f,
//                Sprite.POSITION_CENTER);
//
//        // 適当な時間が経ったらゲームクリアフラグを立てて強制的にゲームクリアとする
//        if (frameCount > 200) {
//            ((GameSceneStage1) scene).setGameClear(true);
//        }
//    }
//
//    @Override
//    public void update() {
//        super.update();
//
//        switch (state) {
//            case Encounter:
//                // 進軍中
//                onUpdateEncounter();
//                break;
//            case NormalAttacks:
//                // 通常攻撃
//                onUpdateNormalAttacks();
//                break;
//            case CrazyAttacks:
//                // 発狂攻撃
//                onUpdateCrazyAttacks();
//                break;
//
//            case Dead:
//                // 演出
//                onUpdateDead();
//                break;
//        }
//    }
//
//    @Override
//    public Rect getIntersectRect() {
//        if (state == State.Dead) {
//            // 撃墜状態なら当たり判定は存在しない
//            return null;
//        }
//        return super.getIntersectRect();
//    }
//
//    @Override
//    public void onDamage(BulletBase bullet) {
//        super.onDamage(bullet);
//
//        // HPが無くなったらDEAD状態にする
//        if (hp <= 0) {
//            state = State.Dead;
//            resetFrameCount();
//        }
//    }
//
//    /**
//    * 演出で死んでいただくため、デッド扱いにはしない
//    */
//    @Override
//    public boolean isDead() {
//        return false;
//    }
//}