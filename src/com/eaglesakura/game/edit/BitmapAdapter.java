package com.eaglesakura.game.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.eaglesakura.game.foxone.R;

import java.util.List;

/**
 * Created by okadakeiko on 13/08/27.
 */
public class BitmapAdapter extends ArrayAdapter<Bitmap> {
    private int resourceId;

    public BitmapAdapter(Context context, int resource, List<Bitmap> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
        }

        ImageView view = (ImageView) convertView.findViewById(R.id.imageView);
        view.setImageBitmap(getItem(position));

        return view;
    }

}
