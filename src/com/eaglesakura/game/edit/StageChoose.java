package com.eaglesakura.game.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.eaglesakura.game.foxone.R;

/**
 * Created by okadakeiko on 13/08/28.
 */
public class StageChoose extends Activity {
    AlertDialog stageNameDialog = null;
    EditText stageNameEdit;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_choose);

        intent = new Intent(this,MainActivity.class);
        stageNameEdit = new EditText(this);
    }

    public void newStage(View v){
         stageNameDialog = new AlertDialog.Builder(this)
                .setTitle("ステージ名を入力してください")
                .setView(stageNameEdit)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 処理を書く
                                String stageName = stageNameEdit.getText().toString();
                                intent.putExtra("stageName",stageName);
                                intent.putExtra("fromStageChooseActivity",true);

                                startActivity(intent);

                            }
                        }).create();
        stageNameDialog.show();
    }
}
