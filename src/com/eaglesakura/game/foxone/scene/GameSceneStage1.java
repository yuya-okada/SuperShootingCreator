package com.eaglesakura.game.foxone.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.eaglesakura.game.bundle.Displayable;
import com.eaglesakura.game.foxone.Define;
import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.bg.ScrollBackground;
import com.eaglesakura.game.foxone.bullet.BulletBase;
import com.eaglesakura.game.foxone.effect.EffectBase;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.AttackType;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.MoveType;
import com.eaglesakura.game.foxone.input.AttackButton;
import com.eaglesakura.game.foxone.ui.BombsInfo;
import com.eaglesakura.game.foxone.ui.HPBar;
import com.eaglesakura.lib.android.game.graphics.Color;
import com.eaglesakura.lib.android.game.scene.SceneBase;
import com.eaglesakura.lib.android.game.scene.SceneManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	public GameSceneStage1(FoxOne game,Context context) {
		super(game);

        this.context = context;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        stageName = sharedPreferences.getString("nowStage",null);

	}

	/**
	 * 敵の生成情報を管理するクラス
	 * @author
	 *
	 */
	class StageEnemyData {
		/**
		 * 出現フレーム
		 */
		int createFrame;

		/**
		 * 出現させる敵タイプ
		 */
		Displayable enemyImage;

		/**
		 * 攻撃タイプ
		 */
		AttackType attackType;

		/**
		 * 移動タイプ
		 */
		MoveType moveType;

		/**
		 * 出現X位置
		 */
		float createX;

		/**
		 * 出現Y位置
		 */
		float createY;

		/**
		 * 初期値を設定して敵情報を作成する
		 * @param createFrame
		 * @param enemy
		 * @param moveType
		 * @param createX
		 * @param createY
		 */
		public StageEnemyData(int createFrame, Displayable enemy, MoveType moveType, AttackType attackType,float createX, float createY) {
			this.createFrame = createFrame;
			this.enemyImage = enemy;
			this.moveType = moveType;
			this.attackType = attackType;
			this.createX = createX;
			this.createY = createY;
		}

		/**
		 * 指定フレームに達していたら敵を作成し、trueを返す。
		 * @return
		 */
		public boolean create() {
			if (frameCount < createFrame) {
				return false;
			}

			// 敵を作成する
			addEnemy(enemyImage, moveType,attackType, (int)createX, (int)createY);

			return true;
		}
	}

	List<StageEnemyData> stageEnemyDataList = new ArrayList<GameSceneStage1.StageEnemyData>();

	/**
	 * 配置情報を最初に登録する
	 */
	@Override
	protected void initializeEnemy() { // 出現させるY座標

			int createFrame = 0;

			ArrayList<EnemyFighterBase> enemies = stage.getEnemies();
			for(int i=0;i<enemies.size();i++){
		        EnemyFighterBase enemy = enemies.get(i);

                int x = (int)enemy.getCreateX();
                int y = (int)enemy.getCreateY();

                AttackType attackType = enemy.getAttackType();
                MoveType moveType = enemy.getMoveType();
                Displayable displayable = enemy.getDisplayable();

                stageEnemyDataList.add(new StageEnemyData(createFrame, displayable, moveType,attackType, x, y));

			}
			// 1ラインにつき30フレーム後に生成する
			createFrame += 30;
	}

	@Override
	public void onSceneStart(SceneManager manager, SceneBase before) {
		super.onSceneStart(manager, before);

		// ボムボタンを生成して、ボム用に初期化する
		bombButton = new AttackButton(this);
		bombButton.initBombButton();
		player.setBombButton(bombButton);

		// 背景素材を読み込む
		background = new ScrollBackground(game, new int[] {
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
		public int getResource(){
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

	protected void addEnemy(Displayable image, MoveType moveType,AttackType attackType, int x, int y) {
		EnemyFighterBase enemy = null;
		enemy = new EnemyFighterBase(image,moveType,attackType,x,y, this);

		enemy.setPosition(x, y);

		{

		}
		enemies.add(enemy);
	}

	/**
	 * 敵の侵攻状態を更新する
	 */
	protected void updateEnemyInvasion() {

		Iterator<StageEnemyData> iterator = stageEnemyDataList.iterator();
		// 全データをチェックする
		while (iterator.hasNext()) {
			StageEnemyData stageEnemyData = iterator.next();

			// 生成に成功したら、リストから排除する
			if (stageEnemyData.create()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void onFrameBegin(SceneManager manager) {
		super.onFrameBegin(manager);

		// ボムボタンを更新する
		bombButton.update();

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

		hpBar.draw(); // HPバーを描画する
		bomsInfo.draw(); // ボム情報を描画する

		shotButton.draw(); // 攻撃ボタンを描画する
		bombButton.draw(); // ボムボタンを描画する
	}

	/**
	 * 強制的なゲームクリアフラグを立てる
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
		return enemies.isEmpty() && stageEnemyDataList.isEmpty();
	}

	/**
	 * ゲームオーバー条件は自機の撃墜のみ
	 */
	@Override
	public boolean isGameover() {
		return player.isDead(); // プレイヤーが撃墜されたらゲームオーバー
	}
}