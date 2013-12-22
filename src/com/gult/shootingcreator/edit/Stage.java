package com.gult.shootingcreator.edit;

import android.content.Context;

import com.gult.shootingcreator.App;
import com.gult.shootingcreator.foxone.fighter.FighterBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Stage {
    private ArrayList<FighterBase> enemyFighterBases = new ArrayList<FighterBase>();
    private Context context;
    private String stageName;

    public Stage(String stageName, ArrayList<FighterBase> enemyFighterBase) {
        this.enemyFighterBases = enemyFighterBase;
        context = App.getContext();

        this.stageName = stageName;
    }

    public JSONObject toJSON() {

        //enemyJSONに敵情法を格納
        JSONObject stageData = new JSONObject();
        JSONArray enemyJSON = new JSONArray();


        for (FighterBase enemy : enemyFighterBases) {
            JSONObject json = new JSONObject();

            try {
                json.put("type", enemy.getType());
                json.put("data", enemy.toJson());
            } catch (Exception e) {

            }
            enemyJSON.put(json);
        }
        try {
            stageData.put("name", stageName);
            stageData.put("enemies", enemyJSON);
        } catch (Exception e) {
        }

        return stageData;
    }

    public static Stage fromJSON(JSONObject jsonObject) {
        ArrayList<FighterBase> enemyFighterBases = new ArrayList<FighterBase>();

        String stageName = null;
        try {
            stageName = jsonObject.getString("name");
            JSONArray jsonArray = jsonObject.getJSONArray("enemies");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject enemyData = jsonArray.getJSONObject(i);
                enemyFighterBases.add(FighterBase.createFromJSON(enemyData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Stage(stageName, enemyFighterBases);
    }

    public ArrayList<FighterBase> getEnemies() {
        return enemyFighterBases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stage stage = (Stage) o;

//        if (!stageName.equals(stage.stageName)) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return stageName.hashCode();
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setEnemyFighterBases(ArrayList<FighterBase> enemyFighterBases) {
        this.enemyFighterBases = enemyFighterBases;
    }
}
