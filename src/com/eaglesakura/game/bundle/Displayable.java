package com.eaglesakura.game.bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import java.util.UUID;

public interface Displayable extends Parcelable , JSONSerializable{
    public Drawable getDrawable(Context context);
    public UUID getUUId();
    public DisplayableType getType();


}
