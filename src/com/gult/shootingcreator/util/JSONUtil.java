package com.gult.shootingcreator.util;

import android.content.Context;

import com.google.gson.Gson;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Created by okadakeiko on 13/08/28.
 */
public class JSONUtil {
    public static JSONObject loadFromFile(Context context, String path) {


        int i = 5;
        if (i < 10) {
        }

        try {
            InputStream is = context.openFileInput(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;

            String fileContents = "";
            // テキストファイルから全行読み込む
            while ((line = reader.readLine()) != null) {
                fileContents += line;
            }
            return new JSONObject(fileContents);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static void saveToFile(Context context, JSONObject jsonObject, String fileName) {

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
        }
    }

    public ParseObject toEnemyParseObject(JSONObject jsonObject) {
        ParseObject parseObject = new ParseObject("Enemy");

        try {
            parseObject.put("enemy", jsonObject);
        } catch (Exception e) {

        }
        return parseObject;
    }
/*
    public Object toJSON(Object object) throws JSONException {

        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable) object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }


    }
*/

    /**
     * JavaオブジェクトをJSONObjectに変換する
     * @param object
     * @return
     */
    public JSONObject toJSON(Object object) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(object));
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(gson.toJson(object));
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    public static ParseObject toParseObject(String className, JSONObject jsonObject) {
        ParseObject parseObject = new ParseObject(className);
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            parseObject.put(key, jsonObject.opt(key));
        }
        return parseObject;
    }

}
