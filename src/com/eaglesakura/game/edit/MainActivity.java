package com.eaglesakura.game.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.eaglesakura.game.bundle.Displayable;
import com.eaglesakura.game.foxone.InvaderGameActivity;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.AttackType;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.MoveType;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{
	ArrayList<EnemyFighterBase> enemyBaseArray = new ArrayList<EnemyFighterBase>();
	ScrollView scrollView;
	SharedPreferences pref;
	Editor editor;
	Intent intent;

	Button buttonint;
	/**
	 * ステージ名
	 */
	String stageName="stage1";

	int a;
	int playerWidth;
	int playerHeight;
	int playerbulletWidtht;
	int playerbulletHeight;
	int playerbulletSpeed;
	int returnValue;
	float dp_w;
	float dp_h;
	Intent intentEnemy;
	ArrayList<ArrayList<ImageButton>>  button= new ArrayList<ArrayList<ImageButton>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		scrollView= (ScrollView)findViewById(R.id.scrollView1);
		intentEnemy = getIntent();

		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

		dp_w = dispSize(true);
		dp_h = dispSize(false);
		for(int j=0;j<100;j++){
			LinearLayout line = new LinearLayout(this);  
			linearLayout.addView(line);
			ArrayList<ImageButton> buttonRow = new ArrayList<ImageButton>();
			button.add(buttonRow);

			for(int i=0;i<5;i++){
				buttonRow.add(addButton(j,i));
				line.addView(buttonRow.get(i));
				buttonRow.get(i).setOnClickListener(this);
			}
		}
		buttonint = new Button(this);
		buttonint.setText("テストプレイ");
		buttonint.setOnClickListener(new OnEndButton());
		linearLayout.addView(buttonint);
		intent=getIntent();

		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);

			}
		});

		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);  

		// Editor の設定  
		editor = pref.edit();  
		// Editor に値を代入  
		if(intent != null){
			editor.putInt("a",getIntentValue("playerWidth"));  
		}
		// データの保存  
		editor.commit(); 
		//		Toast.makeText(this, String.valueOf(pref.getInt("a", 0)), Toast.LENGTH_LONG).show();
	}


	/**
	 * インテントの値を取得する
	 * @param name
	 * @return
	 */
	public Integer getIntentValue(String name){
		if(!(intent.getSerializableExtra(name)==null)){

			return (Integer) intent.getSerializableExtra(name);
		}
		return 0;
	}

	/**
	 * プレイヤーの設定画面に遷移
	 * @param v
	 */
	public void IntentPlayerOption(View v){

		Intent intent = new Intent(this,PlayerOptionActivity.class);
		startActivity(intent);


	}
	/**
	 * テストプレイに遷移
	 */
	public void IntentTestPlay(){


		Intent intent = new Intent(this,InvaderGameActivity.class);
		startActivity(intent);


	}
	/**
	 * 全ての設定値を保存する
	 * @param v
	 */
	public void save(View v){

		if(intent != null){
			editor.putInt("playerWidth",getIntentValue("playerWidth"));  
			editor.putInt("playerHight",getIntentValue("playerHight"));  
			editor.putInt("playerbulletWidth",getIntentValue("playerbulletWidth"));  
			editor.putInt("playerbulletHeight",getIntentValue("playerbulletHeight"));  
			editor.putInt("playerbulletSpeed",getIntentValue("playerbulletSpeed"));  

		}

	}

	public void Initialization(){

	}

	/**
	 * 画面サイズを返す。
	 * @param type
	 * @return
	 */
	public int dispSize(boolean type){

		WindowManager wm=(WindowManager)getSystemService(WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();



		if(type){
			return disp.getWidth() ;
		}
		return disp.getHeight() ;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public ImageButton addButton(int y,int x){

		ImageButton but=new ImageButton(this);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN){
			but.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
		}else{
			but.setBackground(getResources().getDrawable(R.drawable.border));
		}

		but.setTag(R.id.x,x);
		but.setTag(R.id.y,y);
		but.setMinimumWidth((int) (dp_w/5));
		but.setMinimumHeight((int) (dp_w/5));

		return but;

	}


	public void onClick(View v) {
		final int x = (Integer)v.getTag(R.id.x);
		final int y = 100-(Integer)v.getTag(R.id.y);

		//100-(Integer)v.getTag(R.id.x))*5+(Integer)v.getTag(R.id.y)-5
		Log.d("","x="+x + ",y="+y);

		final View.OnClickListener context = this;

		 final ImageButton imageButton = (ImageButton)v;
		 imageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				imageButton.setImageDrawable(null);
				imageButton.setOnClickListener(context);
				for(int i = 0; i < enemyBaseArray.size();i++){
					EnemyFighterBase enemyFighterBase = enemyBaseArray.get(i);
					if(enemyFighterBase.getX() == x && enemyFighterBase.getY() == y){
						enemyBaseArray.remove(i);
						break;
					}

				}
			}
		});



		Intent intent = new Intent(this,ConfigurationEnemyActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("Point",Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putInt("x",x);
        edit.putInt("y",y);
        edit.commit();

		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestcode,int resultcode ,Intent data){
		if(requestcode == 1){
			if(resultcode == RESULT_OK){
				MoveType moveType = MoveType.valueOf((String) data.getExtras().get("MoveType"));
				AttackType attackType = AttackType.valueOf((String) data.getExtras().get("AttackType"));
				//ImageType imageType = ImageType.valueOf((String)data.getExtras().get("ImageType"));
                SharedPreferences sharedPreferences = getSharedPreferences("Point",MODE_PRIVATE);
				int x = sharedPreferences.getInt("x", 0);
				int y = sharedPreferences.getInt("y", 0);
                Log.d("","resourceDisplayable1"+data.getExtras().getParcelable("ImageType"));

                Displayable resourceDisplayable = data.getExtras().getParcelable("ImageType");

                button.get(100-y).get(x).setImageDrawable(resourceDisplayable.getDrawable(this));
                EnemyFighterBase enemyFighterBase = new EnemyFighterBase(resourceDisplayable,moveType,attackType,x, y);
				enemyBaseArray.add(enemyFighterBase);


            }
		}			
	}


	public AttackType makeAlertList(){

		final CharSequence[] items = {"item1", "item2", "item3"};
		AlertDialog.Builder listDlg = new AlertDialog.Builder(this);
		listDlg.setTitle("敵の移動方法を選択");
		listDlg.setItems(
				items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		listDlg.create().show();
		return AttackType.Not;
	}


	private class OnEndButton implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//enemyJSONに敵情法を格納
			JSONArray enemyJSON = new JSONArray();
			for(EnemyFighterBase enemy : enemyBaseArray){
				enemyJSON.put(enemy.toJson());
			}
			FileOutputStream outputStream;

			try{
                outputStream = openFileOutput(stageName + ".json", Context.MODE_PRIVATE);
                String s = enemyJSON.toString();
				outputStream.write(enemyJSON.toString().getBytes());
				outputStream.close();
			}catch(Exception e){

			}
			IntentTestPlay();
		}

	}
}