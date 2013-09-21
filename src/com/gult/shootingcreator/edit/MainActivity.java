package com.gult.shootingcreator.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gult.shootingcreator.App;
import com.gult.shootingcreator.bundle.Displayable;
import com.gult.shootingcreator.foxone.InvaderGameActivity;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.fighter.FighterBase;
import com.gult.shootingcreator.foxone.fighter.enemy.BossFighterBase;
import com.gult.shootingcreator.foxone.fighter.enemy.EnemyFighterBase;
import com.gult.shootingcreator.foxone.fighter.FighterBase.AttackType;
import com.gult.shootingcreator.foxone.fighter.FighterBase.MoveType;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    ArrayList<FighterBase> enemyBaseArray;
    ScrollView scrollView;

    SharedPreferences pref;
    Editor editor;
    Intent intent;
    private Stage stage;

    Button buttonint;
    /**
     * ステージ名
     */
    String stageName = null;

    int stageNumber = -1;
    int playerWidth;
    int playerHeight;
    int playerbulletWidtht;
    int playerbulletHeight;
    int playerbulletSpeed;
    int returnValue;
    float dp_w;
    float dp_h;
    Intent intentEnemy;
    ArrayList<ArrayList<ImageButton>> button = new ArrayList<ArrayList<ImageButton>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (App.mp.isPlaying()) {
            //一時停止
            App.mp.pause();
        }
        App.mp = MediaPlayer.create(this, R.raw.bgm_main);
        App.mp.setLooping(true);
        App.mp.start();

        Intent intent = getIntent();

        if (intent.getBooleanExtra("fromStageChooseActivity", false)) {
            stageName = intent.getStringExtra("stageName");
            stageNumber = intent.getIntExtra("stageNumber", -1);
        }

        if (stageNumber != -1) {
            stage = StageContainer.getInstance().getStage(stageNumber);
            enemyBaseArray = stage.getEnemies();
        } else {
            enemyBaseArray = new ArrayList<FighterBase>();
            stage = new Stage(stageName, enemyBaseArray);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        dp_w = p.x;
        dp_h = p.y;

        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        intentEnemy = getIntent();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        dp_w = dispSize(true);
        dp_h = dispSize(false);
        for (int j = 0; j < 100; j++) {
            LinearLayout line = new LinearLayout(this);
            linearLayout.addView(line);
            ArrayList<ImageButton> buttonRow = new ArrayList<ImageButton>();
            button.add(buttonRow);

            for (int i = 0; i < 5; i++) {
                buttonRow.add(addButton(j, i));
                line.addView(buttonRow.get(i));
                buttonRow.get(i).setOnClickListener(this);
            }
        }

        for (FighterBase enemy : enemyBaseArray) {
            Drawable drawable = enemy.getDisplayable().getDrawable(this);
            button.get(100 - enemy.getY()).get(enemy.getX()).setImageDrawable(drawable);
        }

        buttonint = new Button(this);
        buttonint.setText("テストプレイ");
        buttonint.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_18));
        buttonint.setOnClickListener(new OnEndButton());
        linearLayout.addView(buttonint);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }


    /**
     * インテントの値を取得する
     *
     * @param name
     * @return
     */
    public Integer getIntentValue(String name) {
        if ((intent != null) && (intent.getSerializableExtra(name) != null)) {

            return (Integer) intent.getSerializableExtra(name);
        }
        return 0;
    }

    /**
     * プレイヤーの設定画面に遷移
     *
     * @param v
     */
    public void IntentPlayerOption(View v) {

        Intent intent = new Intent(this, PlayerOptionActivity.class);
        startActivity(intent);


    }

    /**
     * テストプレイに遷移
     */
    public void IntentTestPlay() {


        Intent intent = new Intent(this, InvaderGameActivity.class);
        startActivity(intent);


    }

    /**
     * 全ての設定値を保存する
     *
     * @param v
     */
    public void save(View v) {

        if (intent != null) {
            editor.putInt("playerWidth", getIntentValue("playerWidth"));
            editor.putInt("playerHight", getIntentValue("playerHight"));
            editor.putInt("playerbulletWidth", getIntentValue("playerbulletWidth"));
            editor.putInt("playerbulletHeight", getIntentValue("playerbulletHeight"));
            editor.putInt("playerbulletSpeed", getIntentValue("playerbulletSpeed"));

        }

    }

    public void Initialization() {

    }

    /**
     * 画面サイズを返す。
     *
     * @param type
     * @return
     */
    public int dispSize(boolean type) {
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);

        if (type) {
            return p.x;
        }
        return p.y;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public ImageButton addButton(int y, int x) {

        ImageButton but = new ImageButton(this);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
        } else {
            but.setBackground(getResources().getDrawable(R.drawable.border));
        }

        but.setTag(R.id.x, x);
        but.setTag(R.id.y, y);
        but.setMinimumWidth((int) (dp_w / 5));
        but.setMinimumHeight((int) (dp_w / 5));

        return but;

    }


    public void onClick(View v) {
        final int x = (Integer) v.getTag(R.id.x);
        final int y = 100 - (Integer) v.getTag(R.id.y);

        //100-(Integer)v.getTag(R.id.x))*5+(Integer)v.getTag(R.id.y)-5
        Log.d("", "x=" + x + ",y=" + y);

        final View.OnClickListener context = this;

        final ImageButton imageButton = (ImageButton) v;
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageButton.setImageDrawable(null);
                imageButton.setOnClickListener(context);
                for (int i = 0; i < enemyBaseArray.size(); i++) {
                    FighterBase enemyFighterBase = enemyBaseArray.get(i);
                    if (enemyFighterBase.getX() == x && enemyFighterBase.getY() == y) {
                        enemyBaseArray.remove(i);
                        break;

                    }

                }
            }
        });


        Intent intent = new Intent(this, EnemyConfigTab.class);
        SharedPreferences sharedPreferences = getSharedPreferences("Point", Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putInt("x", x);
        edit.putInt("y", y);
        edit.commit();

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (requestcode == 1) {
            if (resultcode == RESULT_OK) {
                String enemyType = String.valueOf(data.getExtras().get("EnemyType"));
                //ImageType imageType = ImageType.valueOf((String)data.getExtras().get("ImageType"));
                SharedPreferences sharedPreferences = getSharedPreferences("Point", MODE_PRIVATE);
                int x = sharedPreferences.getInt("x", 0);
                int y = sharedPreferences.getInt("y", 0);
                Log.d("", "resourceDisplayable1" + data.getExtras().getParcelable("ImageType"));

                Displayable displayable = data.getExtras().getParcelable("ImageType");

                Drawable drawable = displayable.getDrawable(this);

                button.get(100 - y).get(x).setImageDrawable(drawable);
                if (enemyType.equals("Normal")) {
                    MoveType moveType = MoveType.valueOf((String) data.getExtras().get("MoveType"));
                    AttackType attackType = AttackType.valueOf((String) data.getExtras().get("AttackType"));
                    EnemyFighterBase enemyFighterBase = new EnemyFighterBase(displayable, moveType, attackType, x, y);
                    enemyBaseArray.add(enemyFighterBase);
                } else if (enemyType.equals("Boss")) {
                    ArrayList<BossFighterBase.ConductType> conductArray = data.getParcelableArrayListExtra("ConductArray");
                     BossFighterBase bossFighterBase = new BossFighterBase(displayable, conductArray, x, y);
                    enemyBaseArray.add(bossFighterBase);
                }
            }
        }
    }


    public AttackType makeAlertList() {

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

    @Override
    public void onPause() {
        super.onPause();

    }


    private class OnEndButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            saveStage();
            IntentTestPlay();
        }
    }

    public void saveStage() {
        if (stageNumber == -1) {
            stage = new Stage(stageName, enemyBaseArray);
            StageContainer.getInstance().addStage(stage);
        } else {
            stage = StageContainer.getInstance().getStage(stageNumber);
            stage.setEnemyFighterBases(enemyBaseArray);
        }

        StageContainer.getInstance().saveStages();
        StageContainer.getInstance().setCurrentStageEdit(stage);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("", "らら");
        saveStage();
    }
}