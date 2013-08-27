package com.eaglesakura.game.edit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eaglesakura.game.foxone.R;

/**
 * Created by okadakeiko on 13/08/27.
 */
public class GridActivity extends Activity implements View.OnClickListener{
    Button pickButton;
    int RESULT_PICK_FILENAME = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        pickButton = (Button)findViewById(R.id.pickButton);
        pickButton.setOnClickListener(this);
        pickFilenameFromGallery();
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.pickButton:
                pickFilenameFromGallery();
                break;
        }
    }

    private void pickFilenameFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PICK_FILENAME
                && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { Media.DATA };

            Cursor cursor = getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex
                    = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Toast.makeText(
                    this,
                    picturePath,
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.putExtra("picturePath",picturePath);
            System.out.println(intent.hasExtra("picturePath"));
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}