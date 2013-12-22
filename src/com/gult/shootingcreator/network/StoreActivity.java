package com.gult.shootingcreator.network;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gult.shootingcreator.R;
import com.gult.shootingcreator.edit.Stage;
import com.gult.shootingcreator.edit.StageContainer;
import com.gult.shootingcreator.util.JSONUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okadakeiko on 13/10/01.
 */
public class StoreActivity extends Activity {

    ListView listView;
    //ParseObjectをサーバから取り出すのにつかうやつ。
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StageData");
    //取り出したデータを入れるリスト
    private List<ParseObject> datas;
    ArrayList<JSONObject> jsonData = new ArrayList<JSONObject>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        //作られた時間の降順にソートする
        query.orderByDescending("_created_at");
        try {
            //findメソッドは設定済みの条件に合うデータを全て取り出す。
            //この場合、"_ceated_at"が設定されているから、データが作られた時間の降順にリストに入る。
            datas = query.find();
            JSONUtil jsonUtil = new JSONUtil();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < datas.size(); i++) {


                //jsonData.add(jsonUtil.toJSONfromParse(datas.get(i)));
                //String stageName = jsonData.get(i).keys().next().toString();
                JSONObject object = JSONUtil.getCleanObject(datas.get(i));
                String name = object.optString("name");
                arrayAdapter.add(name);
                // TODO: ここでobjectのデータを使ってステージもしくはステージのJSONを作成する。
                try {
                    JSONObject convertedObject = jsonUtil.toJSON(object);
                    jsonData.add(convertedObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (ParseException e) {
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
                saveStage(jsonData.get(position), item);
            }
        });
    }

    public void search(View v) {
        query.orderByAscending("lastName");
    }

    public void saveStage(JSONObject jsonObject, String stageName) {
        Log.d("StageJSON=", "" + jsonObject);
        Stage stage = Stage.fromJSON(jsonObject);
        StageContainer.getInstance().addStage(stage);
        StageContainer.getInstance().saveStages();
        Toast.makeText(StoreActivity.this, "'" + stageName + "'" + " was downloaded", Toast.LENGTH_LONG).show();

    }
}