package com.eaglesakura.game.bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import com.eaglesakura.game.foxone.FoxOne;
import com.eaglesakura.lib.android.game.graphics.ImageBase;

import java.util.UUID;

public interface Displayable extends Parcelable , JSONSerializable{
    public Drawable getDrawable(Context context);
    public UUID getUUId();
    public DisplayableType getType();
    public ImageBase getImageBase(FoxOne game);
}
