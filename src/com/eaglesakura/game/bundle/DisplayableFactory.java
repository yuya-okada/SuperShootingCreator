package com.eaglesakura.game.bundle;


import org.json.JSONObject;

public class DisplayableFactory {

    public static Displayable createFromJSON(JSONObject json){

        String key = String.valueOf(json.keys().next());
        JSONObject value = null;
        try{
            value = json.getJSONObject(key);
        }catch(Exception e){

        }
        if(key.equals("file")){
            return new FileDisplayable(value);
        }else if(key.equals("resource")) {
            return new ResourceDisplayable(value);
        } else {
            return new ImageResourceDisplayable(value);
        }


    }

}
