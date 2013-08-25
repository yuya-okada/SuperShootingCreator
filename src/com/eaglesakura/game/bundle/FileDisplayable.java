package com.eaglesakura.game.bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import org.json.JSONObject;

import java.util.UUID;

public class FileDisplayable implements Displayable {

    private String path;

    public FileDisplayable(String path) {
        this.path = path;
    }

    @Override
    public Drawable getDrawable(Context context) {
        return Drawable.createFromPath(path);
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(path);
    }

    public static final Creator<FileDisplayable> CREATOR
            = new Creator<FileDisplayable>() {
        public FileDisplayable createFromParcel(Parcel in) {
            return new FileDisplayable(in) {
            };
        }

        public FileDisplayable[] newArray(int size) {
            return new FileDisplayable[size];
        }
    };

    private FileDisplayable(Parcel in) {
        path = in.readString();
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try{
            JSONObject sub = new JSONObject();
            sub.put("path", path);
            json.put("file", sub);
        }catch(Exception e){

        }
        return json;
    }

    public FileDisplayable(JSONObject json){
        try{
            this.path = json.getString("path");
        }catch(Exception e){

        }
    }

    public UUID getUUId(){
        return UUID.randomUUID();

    }

    public DisplayableType getType(){
        return DisplayableType.File;
    }

}
