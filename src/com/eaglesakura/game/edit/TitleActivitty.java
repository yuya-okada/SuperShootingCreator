package com.eaglesakura.game.edit;

import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.R.layout;
import com.eaglesakura.game.foxone.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class TitleActivitty extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_activitty);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_title_activitty, menu);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		
		return false;
	}

}
