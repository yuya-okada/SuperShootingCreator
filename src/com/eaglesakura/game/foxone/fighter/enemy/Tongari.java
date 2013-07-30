//package com.eaglesakura.game.foxone.fighter.enemy;
//
//import com.eaglesakura.game.foxone.R;
//import com.eaglesakura.game.foxone.bullet.DirectionBullet;
//import com.eaglesakura.game.foxone.bullet.Laser;
//import com.eaglesakura.game.foxone.scene.GameSceneBase;
//import com.eaglesakura.game.foxone.scene.PlaySceneBase;
//
//public class Tongari extends EnemyFighterBase {
//
//    public enum AttackType {
//        /**
//        * 全方位弾を撃つ
//        */
//        AllDirection,
//
//        /**
//        * レーザーを撃つ
//        */
//        Laser,
//
//        /**
//        * 複合攻撃	
//        */
//        LaserAndDirection,
//    }
//
//    AttackType attackType = AttackType.Laser;
//
//    public Tongari(AttackType attackType, GameSceneBase scene) {
//        super(scene);
//        hp = 1; // 雑魚キャラと同じHPにする
//
//        this.attackType = attackType;
//
//        switch (attackType) {
//            case AllDirection:
//                sprite = loadSprite(R.drawable.enemy_01); // 青トンガリ
//                hp = 3;
//                break;
//            case Laser:
//                sprite = loadSprite(R.drawable.enemy_01_p); // 紫トンガリ
//                hp = 5;
//                break;
//            case LaserAndDirection:
//                sprite = loadSprite(R.drawable.enemy_01_r); // 赤トンガリ
//                hp = 10;
//                break;
//        }
//    }
//
//    /**
//     * 全方位攻撃トンガリの更新
//     */
//    void onUpdateAllDirection() {
//        final int ATTACK_START_FRAME = 90;
//        final int ATTACK_END_FRAME = ATTACK_START_FRAME + 360;
//
//        if (frameCount >= ATTACK_START_FRAME && frameCount <= (ATTACK_END_FRAME)) {
//            // インベーダーの時よりも高い頻度で攻撃
//            if (frameCount % 5 == 0) {
//
//                // 方向弾を生成する
//                DirectionBullet bullet = new DirectionBullet(scene, this);
//                // 現在のフレーム数の角度へ10の速度で打ち込む
//                bullet.setup(180 - ATTACK_START_FRAME + frameCount, 10);
//
//                // シーンに弾を追加する
//                ((PlaySceneBase) scene).addBullet(bullet);
//
//            }
//        }
//    }
//
//    /**
//    * レーザー攻撃トンガリの更新
//    */
//    void onUpdateLaser() {
//        switch (frameCount) {
//            case 100: {
//                Laser laser = new Laser(scene, this);
//                ((PlaySceneBase) scene).addBullet(laser);
//                resetFrameCount();
//            }
//                break;
//            case 300: {
//                resetFrameCount();
//            }
//                break;
//        }
//    }
//
//    /**
//    * 複合攻撃トンガリの更新
//    */
//    void onUpdateLaserAndDirection() {
//        onUpdateAllDirection();
//        onUpdateLaser();
//    }
//
//    @Override
//    public void update() {
//        // スーパークラスの処理を行わせる
//        super.update();
//
//        switch (attackType) {
//            case AllDirection:
//                onUpdateAllDirection();
//                break;
//            case Laser:
//                onUpdateLaser();
//                break;
//            case LaserAndDirection:
//                onUpdateLaserAndDirection();
//                break;
//        }
//    }
//}