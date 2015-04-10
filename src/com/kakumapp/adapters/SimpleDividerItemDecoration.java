package com.kakumapp.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kakumapp.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
	private Drawable mDivider;
	private int space;

	public SimpleDividerItemDecoration(Context context, int space) {
		this.space = space;
		mDivider = context.getResources().getDrawable(R.drawable.line_divider);
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent,
			RecyclerView.State state) {
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();

			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + mDivider.getIntrinsicHeight();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
			RecyclerView.State state) {
		outRect.left = space;
		outRect.right = space;
		outRect.bottom = space;

		// Add top margin only for the first item to avoid double space between
		// items
		if (parent.getChildPosition(view) == 0)
			outRect.top = space;
	}
}