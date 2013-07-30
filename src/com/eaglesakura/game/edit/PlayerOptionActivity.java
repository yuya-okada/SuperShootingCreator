package com.eaglesakura.game.edit;

import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.R.id;
import com.eaglesakura.game.foxone.R.layout;
import com.eaglesakura.game.foxone.R.menu;
import com.eaglesakura.game.foxone.fighter.PlayerFighter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class PlayerOptionActivity extends Activity {
	/**
	 *プレイヤーの横幅（百分率）
	 */
	int playerWidth;
	/**
	 * プレイヤーの高さ（百分率）
	 */
	int playerHeight;
	/**
	 * 自機弾の速度（百分率）
	 */
	int playerbulletSpeed;
	/**
	 * 自機弾の高さ（百分率）
	 */
	int playerbulletHeight;
	/**
	 * 自機弾の横幅（百分率）
	 */
	int playerbulletWidth;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_option);

		setSeekValue();

	}

	public int getSeekbar(int sId , int eId){

		final SeekBar sb = (SeekBar)findViewById(sId);
		final EditText edit = (EditText)findViewById(eId);

		// シークバーの初期値をTextViewに表示
		edit.setText(String.valueOf(sb.getProgress()));

		sb.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser) {
						// ツマミをドラッグしたときに呼ばれる
						edit.setText(String.valueOf(sb.getProgress()));
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
						// ツマミに触れたときに呼ばれる
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						// ツマミを離したときに呼ばれる
						
					    return;

					}
				}
				);

		return 	sb.getProgress();

	}

	public void setSeekValue(){

		playerWidth=getSeekbar(R.id.seekBar1,R.id.editText1);
		getSeekbar(R.id.seekBar2,R.id.editText2);
		getSeekbar(R.id.seekBar3,R.id.editText3);
		getSeekbar(R.id.seekBar4,R.id.editText4);
		getSeekbar(R.id.seekBar5,R.id.editText5);
		
		

	}

	public void done(View v){

		Intent intent = new Intent(this,MainActivity.class);
		intent.putExtra("playerWidth",((ProgressBar) findViewById(R.id.seekBar1)).getProgress());
		intent.putExtra("playerHeight",((ProgressBar) findViewById(R.id.seekBar2)).getProgress());
		intent.putExtra("playerbulletSpeed", ((ProgressBar) findViewById(R.id.seekBar3)).getProgress());
		intent.putExtra("playerbulletHeight", ((ProgressBar) findViewById(R.id.seekBar4)).getProgress());
		intent.putExtra("playerbulletWidth", ((ProgressBar) findViewById(R.id.seekBar5)).getProgress());
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);

	}

}
