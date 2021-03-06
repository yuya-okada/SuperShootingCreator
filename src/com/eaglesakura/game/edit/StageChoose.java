package com.eaglesakura.game.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.eaglesakura.game.App;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by okadakeiko on 13/08/28.
 */
public class StageChoose extends Activity {
    AlertDialog stageNameDialog = null;
    EditText stageNameEdit;
    Intent intent;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_choose);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // アダプターを設定します
        listView.setAdapter(adapter);

        intent = new Intent(this, MainActivity.class);
        stageNameEdit = new EditText(this);

        initDialog();
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

    public void getStageList(){

        adapter.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        JSONObject data = JSONUtil.loadFromFile(StageChoose.this, sharedPreferences.getString(App.DATA_FILE_KEY,null));
        JSONArray stages;
        try {
            stages = data.getJSONArray("stages");
            for(int i = 0;i < stages.length();i++){
                adapter.add(stages.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
        }

    }
   @Override
    public void onResume(){
       super.onResume();
       getStageList();
   }
}
