package com.gult.shootingcreator.scene;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.eaglesakura.shootingcreator.edit.Stage;
import com.eaglesakura.shootingcreator.edit.StageContainer;
import com.gult.shootingcreator.Define;
import com.gult.shootingcreator.FoxOne;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.bg.ScrollBackground;
import com.gult.shootingcreator.bullet.BulletBase;
import com.gult.shootingcreator.effect.EffectBase;
import com.gult.shootingcreator.fighter.FighterBase;
import com.gult.shootingcreator.input.AttackButton;
import com.gult.shootingcreator.ui.BombsInfo;
import com.gult.shootingcreator.ui.HPBar;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

import java.util.ArrayList;
import java.util.Iterator;

public class GameSceneStage1 extends PlaySceneBase {
    Context context;

    /**
     * 背景素材
     */
    ScrollBackground background = null;

    /**
     * ボム発射ボタン
     */
    AttackButton bombButton = null;

    /**
     * HPバー
     */
    HPBar hpBar = null;

    /**
     * ボム残弾
     */
    BombsInfo bomsInfo = null;

    /**
     * 強制ゲームクリアフラグ
     */
    boolean gameClear = false;

    public GameSceneStage1(FoxOne game, Context context) {

        this(game, context, StageContainer.getInstance().getCurrentStageEdit());
    }

    public GameSceneStage1(FoxOne game, Context context, Stage stage) {
        super(game);

        this.context = context;

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        stageNumber = sharedPreferences.getInt("nowStage",0);
        this.stage = stage;

    }

    ArrayList<FighterBase> fighterBaseList = new ArrayList<FighterBase>();

    /**
     * 配置情報を最初に登録する
     */
    @Override
    protected void initializeEnemy() {

        int createFrame = 0;

        ArrayList<FighterBase> enemies = stage.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            FighterBase enemy = enemies.get(i);
            fighterBaseList.add(enemy);
            enemy.setLastCreateFrame(createFrame);
        }
        // 1ラインにつき30フレーム後に生成する
        createFrame += 30;
    }

        @Override
        public void onSceneStart (SceneManager manager, SceneBase before){
            super.onSceneStart(manager, before);

            // ボムボタンを生成して、ボム用に初期化する
            bombButton = new AttackButton(this);
            bombButton.initBombButton();
            player.setBombButton(bombButton);

            // 背景素材を読み込む
            background = new ScrollBackground(game, new int[]{
                    R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_2
            });

            // HPバーを生成する
            hpBar = new HPBar(this);

            // ボムの残弾数
            bomsInfo = new BombsInfo(this);
        }

        /**
         * 敵の種類を示すenum
         *
         */
        public enum ImageType {
            Frisbee,
            FrisbeeYellow,
            FrisbeeGreen,
            Tongari,
            TongariPink,
            TongariRed,
            Custom,
            Boss;

            public int getResource() {
                Drawable drawable;
                switch (this) {
                    case Frisbee:
                        return R.drawable.enemy_00; // 赤フリスビーを読み込む
                    case FrisbeeYellow:
                        return R.drawable.enemy_00_y; // 黄色フリスビーを読み込む
                    case FrisbeeGreen:
                        return R.drawable.enemy_00_g; // 緑フリスビーを読み込む
                    case Tongari:
                        return R.drawable.enemy_01; // 青とんがりを読み込む
                    case TongariPink:
                        return R.drawable.enemy_01_p; // ピンクとんがりを読み込む
                    case TongariRed:
                        return R.drawable.enemy_01_r; // 赤とんがりを読み込む
                    case Boss:
                        return R.drawable.boss; // ボスを読み込む
                    default:
                        return R.drawable.enemy_00; // 赤フリスビーを読み込む
                }
            }
        }

    protected void addEnemy(FighterBase fighterBase) {
        fighterBase.setScene(this);
        enemies.add(fighterBase.clone());
    }
        /**
         * 敵の侵攻状態を更新する
         */

    protected void updateEnemyInvasion() {
        Iterator<FighterBase> iterator = fighterBaseList.iterator();
        // 全データをチェックする
        while (iterator.hasNext()) {
            FighterBase enemy = iterator.next();
            // 生成に成功したら、リストから排除する
            if (frameCount >= enemy.getLastCreateFrame()) {
                addEnemy(enemy);
                iterator.remove();
            }
        }
    }

    @Override
    public void onFrameBegin(SceneManager manager) {
        super.onFrameBegin(manager);

        // ボムボタンを更新する
        //bombButton.update();

        // 敵の侵略状態を更新する
        updateEnemyInvasion();

        background.scroll(10);
    }

    @Override
    public void onFrameDraw(SceneManager manager) {
        // 背景を描画する
        {
            background.draw();
        }

        // プレイヤーが死んでいなければ描画処理を行う
        if (!player.isDead()) {
            player.draw(); // プレイヤーを描画する
        }

        // 敵を全て描画する
        for (FighterBase enemy : enemies) {
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

        hpBar.draw(); // HPバーを描画する
        //bomsInfo.draw(); // ボム情報を描画する

        //shotButton.draw(); // 攻撃ボタンを描画する
        //bombButton.draw(); // ボムボタンを描画する
    }

    /**
     * 強制的なゲームクリアフラグを立てる
     *
     * @param gameClear
     */
    public void setGameClear(boolean gameClear) {
        this.gameClear = gameClear;
    }

    /**
     * 全ての敵が出現済みで、敵が居なくなったらゲームクリア
     */
    @Override
    public boolean isGameclear() {
        if (gameClear) {
            return true;
        }
        return enemies.isEmpty() && fighterBaseList.isEmpty();
    }

    /**
     * ゲームオーバー条件は自機の撃墜のみ
     */
    @Override
    public boolean isGameover() {
        return player.isDead(); // プレイヤーが撃墜されたらゲームオーバー
    }
}