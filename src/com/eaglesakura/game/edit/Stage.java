package com.eaglesakura.game.edit;

import android.content.Context;
import com.eaglesakura.game.App;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Stage{
    ArrayList<EnemyFighterBase> enemyFighterBases = new ArrayList<EnemyFighterBase>();
    Context context;

    public Stage(ArrayList<EnemyFighterBase> enemyFighterBase){
        this.enemyFighterBases = enemyFighterBase;
        context = App.getContext();
    }

    public void toJSON(String stageName){
        JSONObject stage = new JSONObject();

        //enemyJSONに敵情法を格納
        JSONObject stageData = new JSONObject();
        JSONArray enemyJSON = new JSONArray();


        for(EnemyFighterBase enemy : enemyFighterBases){
            enemyJSON.put(enemy.toJson());
        }
        FileOutputStream outputStream;

        try{
            stageData.put("enemies", enemyJSON);
            stage.put(stageName,stageData);
            outputStream = context.openFileOutput("stageData.json", Context.MODE_PRIVATE);
            outputStream.write(stageData.toString().getBytes());
            outputStream.close();
        }catch(Exception e){

        }
    }

    public static Stage fromJSON(JSONObject jsonObject){
        jsonObject

    }
}
