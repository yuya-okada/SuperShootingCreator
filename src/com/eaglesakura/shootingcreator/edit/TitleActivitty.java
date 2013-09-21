package com.eaglesakura.shootingcreator.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

import com.gult.shootingcreator.R;

public class TitleActivitty extends Activity {
    private boolean startedStageChoose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_activitty);
        startedStageChoose = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_title_activitty, menu);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if(startedStageChoose) {
            return false;
        }

        startedStageChoose = true;
		Intent intent = new Intent(this,StageChoose.class);
		startActivityForResult(intent, 1);
		
		return false;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        startedStageChoose = false;
    }

}
