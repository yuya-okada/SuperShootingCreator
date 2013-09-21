//package com.gult.shootingcreator.foxone.fighter.enemy;
//
//import com.gult.shootingcreator.R;
//import com.gult.shootingcreator.foxone.bullet.FrisbeeBullet;
//import com.gult.shootingcreator.foxone.bullet.SnipeBullet;
//import com.gult.shootingcreator.foxone.scene.GameSceneBase;
//import com.gult.shootingcreator.foxone.scene.PlaySceneBase;
//
//public class Frisbee extends EnemyFighterBase {
//  
//
//   
//    public Frisbee(AttackType attackType, GameSceneBase scene) {
//        super(scene);
//
//       
//    }
//
//    /**
//     * まっすぐ弾を撃つ場合の更新
//     */
//    void updateStraight() {
//
//        // 指定したフレームで処理を行わせる
//        if (frameCount == 30 * 3) {
//            // 150フレーム経過したら弾を撃って行動カウンターをリセットする。
//            FrisbeeBullet bullet = new FrisbeeBullet(scene, this);
//            ((PlaySceneBase) scene).addBullet(bullet);
//
//            resetFrameCount();
//        }
//    }
//
//    /**
//    * 狙撃する場合の更新
//    */
//    void updateSnipe() {
//        // 指定したフレームで処理を行わせる
//        if (frameCount == 30 * 3) {
//            SnipeBullet bullet = new SnipeBullet(scene, this);
//            bullet.setup(((PlaySceneBase) scene).getPlayer(), 20);
//
//            ((PlaySceneBase) scene).addBullet(bullet);
//
//            resetFrameCount();
//        }
//    }
//
//    @Override
//    public void update() {
//        // スーパークラスの処理を行わせる
//        super.update();
//
//        switch (attackType) {
//            case ShotStraight:
//                updateStraight();
//                break;
//            case Snipe:
//                updateSnipe();
//                break;
//            default:
//                // 何もしないパターン
//                break;
//        }
//    }
//    
//    
//}