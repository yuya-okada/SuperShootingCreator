package com.gult.shootingcreator.edit;

import android.content.Context;

import com.gult.shootingcreator.App;
import com.gult.shootingcreator.bundle.Displayable;
import com.gult.shootingcreator.bundle.DisplayableFactory;
import com.gult.shootingcreator.foxone.fighter.FighterBase;
import com.gult.shootingcreator.foxone.fighter.FighterBase.AttackType;
import com.gult.shootingcreator.foxone.fighter.FighterBase.MoveType;
import com.gult.shootingcreator.foxone.fighter.enemy.BossFighterBase;
import com.gult.shootingcreator.foxone.fighter.enemy.EnemyFighterBase;

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
                JSONObject enemy = enemyData.getJSONObject("data");

                if (enemyData.getString("type").equals("Normal")) {

                    AttackType attackType = AttackType.valueOf(enemy.getString("attackType"));
                    MoveType moveType = MoveType.valueOf(enemy.getString("moveType"));
                    Displayable displayable = DisplayableFactory.createFromJSON(enemy.getJSONObject("imageType"));

                    int x = enemy.getInt("x");
                    int y = enemy.getInt("y");

                    EnemyFighterBase enemyFighterBase = new EnemyFighterBase(displayable, moveType, attackType, x, y);
                    enemyFighterBases.add(enemyFighterBase);

                } else if (enemyData.getString("type").equals("Boss")) {

                    JSONArray conductsJSON = enemy.getJSONArray("conduct");
                    ArrayList<BossFighterBase.ConductType> conductArray = new ArrayList<BossFighterBase.ConductType>();

                    for (int j = 0; i < conductsJSON.length(); i++) {
                        conductArray.add(BossFighterBase.ConductType.valueOf(conductsJSON.get(i).toString()));
                    }
                    Displayable displayable = DisplayableFactory.createFromJSON(enemy.getJSONObject("imageType"));

                    int x = enemy.getInt("x");
                    int y = enemy.getInt("y");

                    BossFighterBase bossFighterBase = new BossFighterBase(displayable, conductArray, x, y);
                    enemyFighterBases.add(bossFighterBase);

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = new Stage(stageName, enemyFighterBases);
        return stage;
    }

    public ArrayList<FighterBase> getEnemies() {
        return enemyFighterBases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stage stage = (Stage) o;

        if (!stageName.equals(stage.stageName)) return false;

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
