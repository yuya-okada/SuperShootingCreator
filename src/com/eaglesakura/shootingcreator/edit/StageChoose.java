package com.eaglesakura.shootingcreator.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.eaglesakura.shootingcreator.App;
import com.gult.shootingcreator.R;
import com.eaglesakura.shootingcreator.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by okadakeiko on 13/08/28.
 */
public class StageChoose extends Activity {
    AlertDialog stageNameDialog = null;
    EditText stageNameEdit;
    Intent intent;
    StageChooseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_choose);

        if (App.mp.isPlaying()) {
            //一時停止
            App.mp.pause();
        }
        App.mp = MediaPlayer.create(this, R.raw.bgm_main);
        App.mp.setLooping(true);
        App.mp.start();

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new StageChooseAdapter(this, android.R.layout.simple_list_item_1);
        // アダプターを設定します
        listView.setAdapter(adapter);

        intent = new Intent(this, MainActivity.class);
        stageNameEdit = new EditText(this);

        initDialog();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 長押しクリックされた時の処理を記述
                String msg = "ItemLongClick : Item" + (position + 1);
                Log.v("OnItemLongClick", msg);
                return false;
                }
        });

    }

    private void initDialog() {
        stageNameDialog = new AlertDialog.Builder(this)
                .setTitle("ステージ名を入力してください")
                .setView(stageNameEdit)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // 処理を書く
                                String stageName = stageNameEdit.getText().toString();

                                if (!(stageName.equals("") || stageName.equals(null))) {

                                    intent.putExtra("stageName", stageName);
                                    intent.putExtra("fromStageChooseActivity", true);
                                    dialog.dismiss();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(StageChoose.this, "ステージ名を入力して下さい。", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).create();
    }


    public void newStage(View v) {
        stageNameEdit.setText("");
        stageNameDialog.show();
    }

    public void getStageList() {


        adapter.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        JSONObject data = JSONUtil.loadFromFile(StageChoose.this, sharedPreferences.getString(App.DATA_FILE_KEY, null));
        JSONArray stages;
        try {
            stages = data.getJSONArray("stages");
            for (int i = 0; i < stages.length(); i++) {
                adapter.add(stages.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStageList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.mp.stop();

    }



}
