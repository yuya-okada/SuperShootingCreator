package com.eaglesakura.game.edit;

import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.R.layout;
import com.eaglesakura.game.foxone.R.menu;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.AttackType;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.MoveType;
import com.eaglesakura.game.foxone.scene.GameSceneStage1.ImageType;
import com.eaglesakura.lib.android.game.graphics.ImageBase;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ConfigurationEnemyActivity extends Activity {
	Intent baseIntent;
	MoveType moveType = MoveType.Not;
	AttackType attackType = AttackType.Not;
	ImageType imageType = ImageType.Frisbee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration_enemy);

		baseIntent = getIntent();
		
		Spinner spinnerMove = (Spinner)findViewById(R.id.spinner1);
		Spinner spinnerAttack = (Spinner)findViewById(R.id.spinner2);
		Spinner spinnerImage = (Spinner)findViewById(R.id.spinner3);

		spinnerMove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedItem = parent.getItemAtPosition(position).toString();
				if (selectedItem.equals("直進")){
					moveType = MoveType.Straight; 
				}else if (selectedItem.equals("波線")){
					moveType = MoveType.Curved; 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}
				);

		spinnerAttack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedItem = parent.getItemAtPosition(position).toString();
				if (selectedItem .equals( "通常")){
					attackType = AttackType.ShotStraight; 
				}else if (selectedItem .equals( "狙撃")){
					attackType = AttackType.Snipe;
				}else if (selectedItem .equals( "全方位弾")){
					attackType = AttackType.AllDirection;
				}else if (selectedItem .equals( "レーザー")){
					attackType = AttackType.Laser;
				}else if (selectedItem .equals( "全方位弾＆レーザー")){
					attackType = AttackType.Snipe;
				}else if (selectedItem .equals( "何もしない")){
					attackType = AttackType.Not;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}
				);

		spinnerImage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedItem = parent.getItemAtPosition(position).toString();
				if (selectedItem .equals( "円盤A")){
					imageType = ImageType.Frisbee;
				}else if (selectedItem .equals( "円盤B")){
					imageType = ImageType.FrisbeeYellow;
				}else if (selectedItem .equals( "円盤C")){
					imageType = ImageType.FrisbeeGreen;
				}else if (selectedItem .equals( "戦艦A")){
					imageType = ImageType.Tongari;
				}else if (selectedItem .equals( "戦艦B")){
					imageType = ImageType.TongariPink;
				}else if (selectedItem .equals( "戦艦C")){
					imageType = ImageType.TongariRed;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}
				);
	}
	public void Done(View v){

		Intent intent = new Intent(this,MainActivity.class);
		Log.d("","move"+moveType);
		intent.putExtra("MoveType", moveType.toString());
		intent.putExtra("AttackType", attackType.toString());
		intent.putExtra("ImageType", imageType.toString());
		intent.putExtra("x", baseIntent.getIntExtra("x",0));
		intent.putExtra("y", baseIntent.getIntExtra("y", 0));
		setResult(RESULT_OK,intent);
		finish();

	} 

}