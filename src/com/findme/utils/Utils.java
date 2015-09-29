package com.findme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.cocosw.bottomsheet.BottomSheet;
import com.findme.R;

public class Utils {
	private AppCompatActivity activity;

	public Utils(AppCompatActivity activity) {
		this.activity = activity;
	}

	/**
	 * quit
	 */
	public void quit() {
		BottomSheet sheet = new BottomSheet.Builder(activity,
				R.style.BottomSheet_StyleDialog).icon(R.drawable.ic_launcher)
				.title("Do you want to exit?").limit(R.integer.bs_grid_colum)
				.sheet(R.menu.confirm_menu)
				.listener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
						case R.id.yes:
							activity.finish();
							activity.moveTaskToBack(true);
							break;
						case R.id.no:
							break;
						default:
							break;
						}
					}
				}).grid().build();
		sheet.show();
	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	/**
	 * show an error message
	 * 
	 * @param title
	 * @param message
	 */
	public void showDialog(String title, String message, boolean isError) {
		BottomSheet bottomSheet = null;
		if (isError) {
			bottomSheet = new BottomSheet.Builder(activity,
					R.style.BottomSheet_StyleDialog).title(title)
					.icon(R.drawable.ic_action_warning).sheet(1, message)
					.build();
		} else {
			bottomSheet = new BottomSheet.Builder(activity,
					R.style.BottomSheet_StyleDialog).title(title)
					.sheet(1, message).build();
		}
		bottomSheet.show();
	}

	public Typeface getFont(String font) {
		Typeface typeface = Typeface.createFromAsset(activity.getAssets(),
				"fonts/" + font + ".ttf");
		return typeface;
	}
}
