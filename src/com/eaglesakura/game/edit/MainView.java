package com.eaglesakura.game.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MainView extends View {
	MainActivity main = (MainActivity)this.getContext();
	int dp_w;
	int dp_h;

	public MainView(Context context) {
		super(context);
		
        
		dp_w=main.dispSize(true);
		dp_h=main.dispSize(false);


	}

	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.argb(255, 255, 255, 255));

		for(int a=1;a <=4;a++){
			canvas.drawLine(dp_w /4*a,0,dp_w/4*a,dp_h, paint);
		}
		for(int a=1;a <=8;a++){
			canvas.drawLine(0,dp_h /8*a,dp_w,dp_h/8*a, paint);
		}
		
	}

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
