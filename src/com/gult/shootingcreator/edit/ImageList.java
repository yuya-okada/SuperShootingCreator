package com.gult.shootingcreator.edit;

import android.graphics.Bitmap;

/**
 * Created by okadakeiko on 2013/10/27.
 */
public class ImageList {
    private Bitmap imageData_;
    private String textData_;

    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }

    public Bitmap getImageData() {
        return imageData_;
    }

    public void setTextData(String text) {
        textData_ = text;
    }

    public String getTextData() {
        return textData_;
    }
}
