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

//public abstract class EnemyFighterBase extends FighterBase {
public class EnemyFighterBase extends FighterBase {
	/**
	 * 攻撃手段
	 */
	AttackType attackType = AttackType.Not;


	/**
	 * 生成されてからのフレームを記録する。
	 */
	protected int frameCount = 0;

	Displayable image = null;



	MoveType moveType = MoveType.Not;

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

	//public EnemyFighterBase(int x,int y) {
		//this(ImageType.Frisbee ,MoveType.Straight,AttackType.Not,x,y,null);
	//}
	public EnemyFighterBase(Displayable image,MoveType moveType,AttackType attackType,int x,int y) {
		this(0,image,moveType,attackType,x,y,null);
	}
	public EnemyFighterBase(int createFrame,Displayable image,MoveType moveType,AttackType attackType,int x,int y,GameSceneBase scene) {
		super(createFrame,image,moveType,attackType,x,y,scene);

    }

	/**
	 * 直線移動をするように初期化する
	 * @param moveSpeed
	 */
	public void initMoveStraight(float moveSpeed) {
		moveType = MoveType.Straight;
		this.moveSpeed = moveSpeed;
	}

	/**
	 * カーブ移動をするように初期化する
	 * @param moveSpeedX Xの移動量。この値が大きいほど、左右に大きく動く
	 * @param moveSpeedY Yの移動量。この値が大きいほど、上下の動きが速くなる
	 * @param sinSpeed sinθの変動量。この値が大きいほど、左右のサイクルが短くなる
	 */
	public void initMoveCurve(float moveSpeedX, float moveSpeedY, float sinSpeed) {
		moveType = MoveType.Curved;

        Log.d("","りりりりりり");
		this.moveSpeedX = moveSpeedX;
		this.moveSpeed = moveSpeedY;
		this.sinSpeed = sinSpeed;

		// 現在の中心位置を記録する
		centerX = getPositionX();
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
		Log.d("","into 'onUpdateStraight' method");

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

        Log.d("","りnextX:"+nextX);
        // 求められた移動先座標を設定する
        setPosition(getPositionX() + nextX, nextY);
    }

	@Override
	public void update() {
		switch (attackType) {
		case ShotStraight:
			updateStraight();
			break;
		case Snipe:
			updateSnipe();
			break;
		case AllDirection:
			onUpdateAllDirection();
			break;
		case Laser:
			onUpdateLaser();
			break;
		case LaserAndDirection:
			onUpdateLaserAndDirection();
			break;
		default:
			// 何もしない
			break;
		}

		switch (moveType) {
		case Straight:
			onUpdateStraignt();
			break;
		case Curved:
			onUpdateCurved();
			break;
		default:
			//動かない
			break;
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

		// 指定したフレームで処理を行わせる
		if (frameCount == 30 * 3) {
			// 150フレーム経過したら弾を撃って行動カウンターをリセットする。
			FrisbeeBullet bullet = new FrisbeeBullet(scene, this);
			((PlaySceneBase) scene).addBullet(bullet);

			resetFrameCount();
		}
	}

	/**
	 * 狙撃する場合の更新
	 */
	void updateSnipe() {
		// 指定したフレームで処理を行わせる
		if (frameCount == 30 * 3) {
			SnipeBullet bullet = new SnipeBullet(scene, this);
			bullet.setup(((PlaySceneBase) scene).getPlayer(), 20);

			((PlaySceneBase) scene).addBullet(bullet);

			resetFrameCount();
		}
	}
	/**
	 * 全方位攻撃トンガリの更新
	 */
	void onUpdateAllDirection() {
		final int ATTACK_START_FRAME = 90;
		final int ATTACK_END_FRAME = ATTACK_START_FRAME + 360;

		if (frameCount >= ATTACK_START_FRAME && frameCount <= (ATTACK_END_FRAME)) {
			// インベーダーの時よりも高い頻度で攻撃
			if (frameCount % 5 == 0) {

				// 方向弾を生成する
				DirectionBullet bullet = new DirectionBullet(scene, this);
				// 現在のフレーム数の角度へ10の速度で打ち込む
				bullet.setup(180 - ATTACK_START_FRAME + frameCount, 10);

				// シーンに弾を追加する
				((PlaySceneBase) scene).addBullet(bullet);

			}
		}
	}

	/**
	 * レーザー攻撃トンガリの更新
	 */
	void onUpdateLaser() {
		switch (frameCount) {
		case 100: {
			Laser laser = new Laser(scene, this);
			((PlaySceneBase) scene).addBullet(laser);
			resetFrameCount();
		}
		break;
		case 300: {
			resetFrameCount();
		}
		break;
		}
	}

	/**
	 * 複合攻撃トンガリの更新
	 */
	void onUpdateLaserAndDirection() {
		onUpdateAllDirection();
		onUpdateLaser();
	}



	@Override
	public void setScene(GameSceneBase scene){
		super.setScene(scene);
		sprite = loadSprite(image);
	}
	



}