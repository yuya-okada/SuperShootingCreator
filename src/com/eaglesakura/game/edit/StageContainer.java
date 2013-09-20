package com.eaglesakura.game.edit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eaglesakura.game.App;
import com.eaglesakura.game.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by okadakeiko on 13/08/29.
 */
public class StageContainer {
    ArrayList<Stage> stages = new ArrayList<Stage>();
    private int currentStageNumber;
    private Stage currentStageEdit;
    int stageNumber = 0;
    private static StageContainer instance;

    private StageContainer() {

    }

    public static StageContainer getInstance() {
        if (instance == null) {
            instance = new StageContainer();
            instance.loadStages();
        }
        return instance;
    }

    private void loadStages() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        String filePath = sharedPreferences.getString(App.DATA_FILE_KEY, null);
        JSONObject json = JSONUtil.loadFromFile(App.getContext(), filePath);
        JSONArray jsonStages = null;
        try {
            jsonStages = json.getJSONArray("stages");
            for (int i = 0; i < jsonStages.length(); i++) {
                Stage stage = Stage.fromJSON(jsonStages.getJSONObject(i));
                stages.add(stage);
            }

        } catch (Exception e) {
        }

    }

    public void saveStages() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        JSONObject data = JSONUtil.loadFromFile(App.getContext(), sharedPreferences.getString(App.DATA_FILE_KEY, null));
        JSONArray stagesJSON = new JSONArray();

        for (Stage stage : stages) {
            stagesJSON.put(stage.toJSON());
        }

        try {
            data.put("stages", stagesJSON);
        } catch (Exception e) {
        }
        JSONUtil.saveToFile(App.getContext(), data, sharedPreferences.getString(App.DATA_FILE_KEY, null));

    }

    public Stage getStage(int stageNumber) {

        return stages.get(stageNumber);

    }

    public Stage getCurrentStageEdit() {
        return currentStageEdit;
    }

    public void setCurrentStageEdit(Stage currentStageEdit) {
        this.currentStageEdit = currentStageEdit;
        currentStageNumber = stages.indexOf(currentStageEdit);
    }

    public void addStage(Stage stage) {
        if (!stages.contains(stage)) {
            stages.add(stage);
        }
    }

    public Stage getNextStage(){
        if(currentStageNumber < stages.size() - 1){
            currentStageNumber ++;
            return stages.get(currentStageNumber);
        }
        return null;
    }

    public void removeItem(int position){
        stages.remove(position);
    }

}
