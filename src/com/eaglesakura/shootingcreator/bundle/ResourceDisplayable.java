package com.eaglesakura.shootingcreator.bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.gult.shootingcreator.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by okadakeiko on 13/08/24.
 */
public class ResourceDisplayable implements Displayable {

    protected int resId;

    protected ResourceDisplayable() {
    }

    public ResourceDisplayable(int resId) {
        this.resId = resId;
    }

    public Drawable getDrawable(Context context) {
        return context.getResources().getDrawable(resId);
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(resId);
    }

    public static final Parcelable.Creator<ResourceDisplayable> CREATOR
            = new Parcelable.Creator<ResourceDisplayable>() {
        public ResourceDisplayable createFromParcel(Parcel in) {
            return new ResourceDisplayable(in) {
            };
        }

        public ResourceDisplayable[] newArray(int size) {
            return new ResourceDisplayable[size];
        }
    };

    private ResourceDisplayable(Parcel in) {
        resId = in.readInt();
    }
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try{
            JSONObject sub = new JSONObject();
            sub.put("resId",String.valueOf(resId));
            json.put("resource", sub);

        }catch(Exception e){

        }
        return json;
    }

    public ResourceDisplayable(JSONObject json){
        try{
            this.resId = json.getInt("resId");
        }catch(Exception e){

        }
    }

    public DisplayableType getType(){
        return DisplayableType.Resource;
    }

    public UUID getUUId(){
        return UUID.randomUUID();
    }

    public int getResourceID(){
        return resId;
    }

    public ImageBase getImageBase(FoxOne game) {
        return game.loadImageDrawable(getResourceID());
    }
}
