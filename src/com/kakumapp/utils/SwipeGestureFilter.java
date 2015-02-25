/**
 * useful for detecting swipes
 */

package com.kakumapp.utils;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class SwipeGestureFilter extends SimpleOnGestureListener {

	public final static int SWIPE_UP = 1;
	public final static int SWIPE_DOWN = 2;
	public final static int SWIPE_LEFT = 3;
	public final static int SWIPE_RIGHT = 4;

	public final static int MODE_TRANSPARENT = 0;
	public final static int MODE_SOLID = 1;
	public final static int MODE_DYNAMIC = 2;
	// just an unlikely number
	private final static int ACTION_FAKE = -13;
	private int SWIPE_MIN_DISTANCE = 100;
	private int SWIPE_MAX_DISTANCE = 350;
	private int SWIPE_MIN_VELOCITY = 100;

	private int mode = MODE_DYNAMIC;
	private boolean running = true;
	private boolean tapIndicator = false;

	private Activity context;
	private GestureDetector detector;
	private SwipeGestureListener listener;

	/**
	 * 
	 * @param context
	 * @param gestureListener
	 */
	public SwipeGestureFilter(Activity context, SwipeGestureListener gestureListener) {
		this.context = context;
		this.detector = new GestureDetector(context, this);
		this.listener = gestureListener;
	}

	/**
	 * 
	 * @param event
	 */
	public void onTouchEvent(MotionEvent event) {
		if (!this.running)
			return;
		boolean result = this.detector.onTouchEvent(event);
		if (this.mode == MODE_SOLID)
			event.setAction(MotionEvent.ACTION_CANCEL);
		else if (this.mode == MODE_DYNAMIC) {
			if (event.getAction() == ACTION_FAKE)
				event.setAction(MotionEvent.ACTION_UP);
			else if (result)
				event.setAction(MotionEvent.ACTION_CANCEL);
			else if (this.tapIndicator) {
				event.setAction(MotionEvent.ACTION_DOWN);
				this.tapIndicator = false;
			}
		}
	}

	public void setMode(int m) {
		this.mode = m;
	}
	
	public int getMode() {
		return this.mode;
	}

	public void setEnabled(boolean status) {
		this.running = status;
	}

	public void setSwipeMaxDistance(int distance) {
		this.SWIPE_MAX_DISTANCE = distance;
	}

	public void setSwipeMinDistance(int distance) {
		this.SWIPE_MIN_DISTANCE = distance;
	}

	public void setSwipeMinVelocity(int distance) {
		this.SWIPE_MIN_VELOCITY = distance;
	}

	public int getSwipeMaxDistance() {
		return this.SWIPE_MAX_DISTANCE;
	}

	public int getSwipeMinDistance() {
		return this.SWIPE_MIN_DISTANCE;
	}

	public int getSwipeMinVelocity() {
		return this.SWIPE_MIN_VELOCITY;
	}

	/**
	 * @e1 The first down motion event that started the fling.
	 * @e2 The move motion event that triggered the current onFling.
	 * @velocityX The velocity of this fling measured in pixels per second along
	 *            the x axis.
	 * @velocityY The velocity of this fling measured in pixels per second along
	 *            the y axis.
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// get the distances swiped
		final float xDistance = Math.abs(e1.getX() - e2.getX());
		final float yDistance = Math.abs(e1.getY() - e2.getY());

		// check if the distances are larger than expected
		if (xDistance > this.SWIPE_MAX_DISTANCE
				|| yDistance > this.SWIPE_MAX_DISTANCE)
			return false;
		// absolute velocities
		velocityX = Math.abs(velocityX);
		velocityY = Math.abs(velocityY);

		// check for horizontal swipes
		boolean result = false;
		if (velocityX > this.SWIPE_MIN_VELOCITY
				&& xDistance > this.SWIPE_MIN_DISTANCE) {
			if (e1.getX() > e2.getX())
				// right to left
				this.listener.onSwipe(SWIPE_LEFT);
			else
				this.listener.onSwipe(SWIPE_RIGHT);

			result = true;
		}
		// check for vertical swipes
		else if (velocityY > this.SWIPE_MIN_VELOCITY
				&& yDistance > this.SWIPE_MIN_DISTANCE) {
			if (e1.getY() > e2.getY())
				// bottom to up
				this.listener.onSwipe(SWIPE_UP);
			else
				this.listener.onSwipe(SWIPE_DOWN);
			result = true;
		}
		return result;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		this.tapIndicator = true;
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		this.listener.onDoubleTap();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return true;
	}

	/**
	 * @e The down motion event of the single-tap.
	 */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (this.mode == MODE_DYNAMIC) {
			e.setAction(ACTION_FAKE);
			this.context.dispatchTouchEvent(e);
		}
		return false;
	}

	/**
	 * create the interface
	 * 
	 * @author paul
	 * 
	 */
	public static interface SwipeGestureListener {
		void onSwipe(int direction);

		void onDoubleTap();
	}

}