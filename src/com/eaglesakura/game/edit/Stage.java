package com.eaglesakura.game.edit;

import android.content.Context;

import com.eaglesakura.game.App;
import com.eaglesakura.game.bundle.Displayable;
import com.eaglesakura.game.bundle.DisplayableFactory;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.AttackType;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.MoveType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Stage {
    private ArrayList<EnemyFighterBase> enemyFighterBases = new ArrayList<EnemyFighterBase>();
    private Context context;

    public Stage(ArrayList<EnemyFighterBase> enemyFighterBase) {
        this.enemyFighterBases = enemyFighterBase;
        context = App.getContext();
    }

    public JSONObject toJSON() {

        //enemyJSONに敵情法を格納
        JSONObject stageData = new JSONObject();
        JSONArray enemyJSON = new JSONArray();


        for (EnemyFighterBase enemy : enemyFighterBases) {
            enemyJSON.put(enemy.toJson());
        }

        try {
            stageData.put("enemies", enemyJSON);
        } catch (Exception e) {
        }

        return stageData;
    }

    public static Stage fromJSON(JSONObject jsonObject) {
        ArrayList<EnemyFighterBase> enemyFighterBases = new ArrayList<EnemyFighterBase>();
        ;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("enemies");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject enemy = jsonArray.getJSONObject(i);
                AttackType attackType = AttackType.valueOf(enemy.getString("attackType"));
                MoveType moveType = MoveType.valueOf(enemy.getString("moveType"));
                Displayable displayable = DisplayableFactory.createFromJSON(enemy.getJSONObject("imageType"));
                int x = enemy.getInt("x");
                int y = enemy.getInt("y");

                EnemyFighterBase enemyFighterBase = new EnemyFighterBase(displayable, moveType, attackType, x, y);

                enemyFighterBases.add(enemyFighterBase);
            }
        } catch (Exception e) {
        }

        Stage stage = new Stage(enemyFighterBases);
        return stage;
    }

    public ArrayList<EnemyFighterBase> getEnemies() {
        return enemyFighterBases;
    }
}
