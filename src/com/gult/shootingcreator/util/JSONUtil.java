package com.gult.shootingcreator.util;

import android.content.Context;

import com.parse.ParseObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by okadakeiko on 13/08/28.
 */
public class JSONUtil {
    public static JSONObject loadFromFile(Context context, String path) {
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

    public static void saveToFile(Context context,JSONObject jsonObject,String fileName){

        FileOutputStream outputStream;

        try{
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();
        }catch(Exception e){
        }
    }

    public ParseObject toEnemyParseObject(JSONObject jsonObject){
        ParseObject parseObject = new ParseObject("Enemy");

        try{
        parseObject.put("enemy",jsonObject);
        }catch (Exception e){

        }
        return parseObject;
    }

}
