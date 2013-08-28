package com.eaglesakura.game.bundle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class FileDisplayable implements Displayable {

    private String path;

    public FileDisplayable(String path) {

        this.path = path;
    }

    @Override
    public Drawable getDrawable(Context context){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = 60;
        options.outWidth = 60;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap,60,60,false);
        return new BitmapDrawable(context.getResources(),resizeBitmap);
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

    public String getPath() {
        return path;
    }

    public ImageBase getImageBase(FoxOne game) {
        try {
            ImageBase img = game.loadImageFromFile(getPath(),60,60);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}