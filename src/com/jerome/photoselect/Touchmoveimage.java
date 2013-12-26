package com.jerome.photoselect;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class Touchmoveimage extends Activity {

	int windowwidth;
	int windowheight;

	private LayoutParams layoutParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_drop);

		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		windowheight = getWindowManager().getDefaultDisplay().getHeight();
		final ImageView balls = (ImageView) findViewById(R.id.letterView);

		balls.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				LayoutParams layoutParams = (LayoutParams) balls
						.getLayoutParams();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					int x_cord = (int) event.getRawX();
					int y_cord = (int) event.getRawY();

					if (x_cord > windowwidth) {
						x_cord = windowwidth;
					}
					if (y_cord > windowheight) {
						y_cord = windowheight;
					}

					layoutParams.leftMargin = x_cord;
					layoutParams.topMargin = y_cord;

					balls.setLayoutParams(layoutParams);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
}