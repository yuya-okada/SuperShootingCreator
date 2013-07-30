package com.eaglesakura.game.foxone.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.game.foxone.bullet.BulletBase;
import com.eaglesakura.game.foxone.effect.EffectBase;
import com.eaglesakura.game.foxone.fighter.PlayerFighter;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import com.eaglesakura.game.foxone.input.AttackButton;
import com.eaglesakura.game.foxone.input.JoyStick;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

public abstract class PlaySceneBase extends GameSceneBase {

    PlayerFighter player = null;

    protected abstract void initializeEnemy();

    JoyStick joyStick = null;
    AttackButton shotButton = null;
    protected List<EnemyFighterBase> enemies = new ArrayList<EnemyFighterBase>();
    List<BulletBase> bullets = new ArrayList<BulletBase>();
    List<EffectBase> effects = new ArrayList<EffectBase>();

    /**
     * ステージ開始からの経過フレーム
     */
    int frameCount = 0;

    public PlaySceneBase(FoxOne game) {
        super(game);
    }

    public PlayerFighter getPlayer() {
        return player;
    }

    public void addBullet(BulletBase bullet) {
        bullets.add(bullet);
    }

    public void addEffect(EffectBase effect) {
        effects.add(effect);
    }

    public EnemyFighterBase intersectsEnemy(BulletBase bullet) {
        for (EnemyFighterBase enemy : enemies) {
            if (enemy.isIntersect(bullet)) {
                enemy.onDamage(bullet);
                return enemy;
            }
        }
        return null;
    }

    /**
     * 弾と接触している敵を全て列挙する
     * @param bullet
     * @return
     */
    public List<EnemyFighterBase> getIntersectEnemies(BulletBase bullet) {
        List<EnemyFighterBase> result = new ArrayList<EnemyFighterBase>();

        for (EnemyFighterBase enemy : enemies) {
            if (enemy.isIntersect(bullet)) {
                // 衝突していたら衝突リストに追加する
                result.add(enemy);
            }
        }
        return result;
    }

    @Override
    public void onSceneStart(SceneManager manager, SceneBase before) {
        joyStick = new JoyStick(this); // ジョイスティックを生成する
        shotButton = new AttackButton(this); // 攻撃ボタンを生成する
        player = new PlayerFighter(this, joyStick, shotButton); // プレイヤーを生成する
        // 敵を全て生成させる
        initializeEnemy();
    }

    @Override
    public void onSceneExit(SceneManager manager, SceneBase next) {
    }

    @Override
    public void onFrameBegin(SceneManager manager) {

        // プレイヤーが死んでいなければ更新処理を行う
        if (!player.isDead()) {
            player.update(); // プレイヤーの位置を更新する
        }

        shotButton.update(); // 攻撃ボタンの状態を更新する
        joyStick.update(); // ジョイスティックの位置を更新する

        // 敵を全て更新する
        {
            Iterator<EnemyFighterBase> iterator = enemies.iterator();
            while (iterator.hasNext()) {
                EnemyFighterBase enemy = iterator.next();
                enemy.update();

                // 敵が撃墜されるか、ディスプレイから見えなくなったら消去
                if (enemy.isDead() || !enemy.isAppearedDisplay()) {
                    iterator.remove();
                }
            }
        }

        // 弾を全て更新する
        {
            Iterator<BulletBase> iterator = bullets.iterator();
            while (iterator.hasNext()) {
                BulletBase bullet = iterator.next();
                bullet.update();

                // 弾が無効になるか、ディスプレイから見えなくなったら消去
                if (!bullet.isEnable() || !bullet.isAppearedDisplay()) {
                    iterator.remove();
                }
            }
        }

        // エフェクトを全て更新する
        {
            Iterator<EffectBase> iterator = effects.iterator();
            while (iterator.hasNext()) {
                EffectBase effect = iterator.next();
                effect.update();
                if (!effect.isEnable()) {
                    // エフェクトが無効になったから排除する
                    iterator.remove();
                }
            }
        }
        ++frameCount;
    }

    @Override
    public void onFrameDraw(SceneManager manager) {
        getSpriteManager().clear(0, 0, 64, 255); // 宇宙の色に染める

        // プレイヤーが死んでいなければ描画処理を行う
        if (!player.isDead()) {
            player.draw(); // プレイヤーを描画する
        }
        // 敵を全て描画する
        for (EnemyFighterBase enemy : enemies) {
            enemy.draw();
        }

        // 弾を全て描画する
        for (BulletBase bullet : bullets) {
            bullet.draw();
        }

        // エフェクトを全て描画する
        for (EffectBase effect : effects) {
            effect.draw();
        }

        // 画面のプレイエリア外を塗りつぶす
        {
            // 左側を塗りつぶす
            getSpriteManager().fillRect(
            // 起点のXY座標
                    0, 0,
                    // 幅・高さ
                    Define.PLAY_AREA_LEFT, Define.VIRTUAL_DISPLAY_HEIGHT,
                    // 描画色
                    Color.toColorRGBA(255, 255, 255, 255));

            // 右側を塗りつぶす
            getSpriteManager().fillRect(
            // 起点のXY座標
                    Define.PLAY_AREA_RIGHT, 0,
                    // 幅・高さ
                    Define.VIRTUAL_DISPLAY_WIDTH - Define.PLAY_AREA_RIGHT, Define.VIRTUAL_DISPLAY_HEIGHT,
                    // 描画色
                    Color.toColorRGBA(255, 255, 255, 255));
        }
        joyStick.draw(); // ジョイスティックを描画する
        shotButton.draw(); // 攻撃ボタンを描画する
    }

    /**
    * ゲームオーバー条件を満たしたらtrueを返す。
    * @return
    */
    public boolean isGameover() {
        // 全ての敵をチェックする
        for (EnemyFighterBase enemy : enemies) {
            // 敵のY座標がプレイヤーのY座標を超えていたら=プレイヤーよりも下にいる=前線を突破された
            if (enemy.getPositionY() > player.getPositionY()) {
                // ゲームオーバー
                return true;
            }
        }
        return player.isDead(); // プレイヤーが撃墜されたらゲームオーバー
    }

    public boolean isGameclear() {
        return enemies.isEmpty(); // 敵が全滅状態になったらゲームクリア
    }

    @Override
    public void onFrameEnd(SceneManager manager) {
        if (isGameover()) {
            // ゲームオーバー条件を満たしたから、ゲームオーバーシーンへ切り替える
            GameOverScene nextScene = new GameOverScene(game);
            manager.setNextScene(nextScene);
        } else if (isGameclear()) {
            // ゲームクリア条件を満たしたら、ゲームクリアシーンへ切り替える
            GameClearScene nextScene = new GameClearScene(game);
            manager.setNextScene(nextScene);
        }
    }

    @Override
    public void onGamePause(SceneManager manager) {
    }

    @Override
    public void onGameResume(SceneManager manager) {
    }

    /**
     * 画面上の弾を全て排除する
     */
    public void clearBullets() {
        bullets.clear();
    }
}