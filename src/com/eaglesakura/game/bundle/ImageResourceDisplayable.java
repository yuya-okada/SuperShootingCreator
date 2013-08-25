package com.eaglesakura.game.bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import com.eaglesakura.game.foxone.scene.GameSceneStage1;

import org.json.JSONObject;

public class ImageResourceDisplayable extends ResourceDisplayable {
    private GameSceneStage1.ImageType type;

    public ImageResourceDisplayable(GameSceneStage1.ImageType type) {
        this.type = type;
    }

    public Drawable getDrawable(Context context) {
        return context.getResources().getDrawable(type.getResource());
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(type.toString());
    }

    public static final Creator<ImageResourceDisplayable> CREATOR
            = new Creator<ImageResourceDisplayable>() {
        public ImageResourceDisplayable createFromParcel(Parcel in) {
            return new ImageResourceDisplayable(in) {
            };
        }

        public ImageResourceDisplayable[] newArray(int size) {
            return new ImageResourceDisplayable[size];
        }
    };

    private ImageResourceDisplayable(Parcel in) {
        type = GameSceneStage1.ImageType.valueOf(in.readString());
    }
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try{
            JSONObject sub = new JSONObject();
            sub.put("type",String.valueOf(type));
            json.put("imageResource", sub);
        }catch(Exception e){

        }
        return json;
    }

    public ImageResourceDisplayable(JSONObject json){
        try{
            this.type = GameSceneStage1.ImageType.valueOf(json.getString("type"));
        }catch(Exception e){

        }
    }

    public DisplayableType getType(){
        return DisplayableType.Resource;
    }

    @Override
    public int getResourceID(){
        return type.getResource();
    }
}