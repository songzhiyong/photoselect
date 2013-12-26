package com.jerome.photoselect;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class DragAndDropBasicActivity extends Activity implements
		OnTouchListener {
	private ImageView letterView; // The letter that the user drags.
	private ImageView emptyLetterView; // The letter outline that the user is
										// supposed to drag letterView to.
	private AbsoluteLayout mainLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_drop);

		mainLayout = (AbsoluteLayout) findViewById(R.id.mainLayout);
		mainLayout.setOnTouchListener(this);
		letterView = (ImageView) findViewById(R.id.letterView);
		letterView.setOnTouchListener(this);

		emptyLetterView = (ImageView) findViewById(R.id.letterView);
	}

	private boolean dragging = false;
	private Rect hitRect = new Rect();

	@Override
	/**
	 * NOTE:  Had significant problems when I tried to react to ACTION_MOVE on letterView.   Kept getting alternating (X,Y) 
	 * locations of the motion events, which caused the letter to flicker and move back and forth.  The only solution I could 
	 * find was to determine when the user had touched down on the letter, then process moves in the ACTION_MOVE 
	 * associated with the mainLayout.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		boolean eventConsumed = true;
		int x = (int) event.getX();
		int y = (int) event.getY();

		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			if (v == letterView) {
				dragging = true;
				eventConsumed = false;
			}
		} else if (action == MotionEvent.ACTION_UP) {

			if (dragging) {
				emptyLetterView.getHitRect(hitRect);
				if (hitRect.contains(x, y))
					setSameAbsoluteLocation(letterView, emptyLetterView);
			}
			dragging = false;
			eventConsumed = false;

		} else if (action == MotionEvent.ACTION_MOVE) {
			if (v != letterView) {
				if (dragging) {
					setAbsoluteLocationCentered(letterView, x, y);
				}
			}
		}

		return eventConsumed;

	}

	private void setSameAbsoluteLocation(View v1, View v2) {
		AbsoluteLayout.LayoutParams alp2 = (AbsoluteLayout.LayoutParams) v2
				.getLayoutParams();
		setAbsoluteLocation(v1, alp2.x, alp2.y);
	}

	private void setAbsoluteLocationCentered(View v, int x, int y) {
		setAbsoluteLocation(v, x - v.getWidth() / 2, y - v.getHeight() / 2);
	}

	private void setAbsoluteLocation(View v, int x, int y) {
		AbsoluteLayout.LayoutParams alp = (AbsoluteLayout.LayoutParams) v
				.getLayoutParams();
		alp.x = x;
		alp.y = y;
		v.setLayoutParams(alp);
	}
}