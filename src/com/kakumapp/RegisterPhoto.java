package com.kakumapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class RegisterPhoto extends ActionBarActivity implements
		SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

	private static final String DIRECTORY_IMAGES = "Kakuma";
	private static final String TAG = "RegisterPhoto";
	private Camera mCamera;
	private SurfaceView mPreview;
	private File photoFile;
	private int currentCameraId = 0;
	private ActionBarActivity activity;
	// control buttons
	private ImageView flashImageView, switchImageView;
	// take photo
	private Button captureButton;
	private boolean hasFrontCamera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		activity = this;

		mPreview = (SurfaceView) findViewById(R.id.camera_preview);
		switchImageView = (ImageView) findViewById(R.id.imageView_switch);
		flashImageView = (ImageView) findViewById(R.id.imageView_flash);
		flashImageView.setTag(R.drawable.ic_action_flash_automatic);

		mPreview.getHolder().addCallback(this);
		mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// prefer starting with front camera if present
		if (hasAFrontCamera()) {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
			showFlash(false);
		} else {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
			// hide switch imageview
			switchImageView.setVisibility(View.GONE);
		}
		mCamera = Camera.open(currentCameraId);

		switchImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchCamera();
			}
		});

		// if flash is not available,hide the flash imageView
		if (!hasFlash()) {
			showFlash(false);
		}

		flashImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int imageResource = R.drawable.ic_action_flash_automatic;
				Camera.Parameters parameters = mCamera.getParameters();
				int tag = (int) flashImageView.getTag();
				if (tag == R.drawable.ic_action_flash_automatic) {
					imageResource = R.drawable.ic_action_flash_on;
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
				} else if (tag == R.drawable.ic_action_flash_on) {
					imageResource = R.drawable.ic_action_flash_off;
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				} else if (tag == R.drawable.ic_action_flash_off) {
					imageResource = R.drawable.ic_action_flash_automatic;
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				}
				try {
					mCamera.setParameters(parameters);
					flashImageView.setTag(imageResource);
					flashImageView.setImageResource(imageResource);
				} catch (Exception exception) {
					Log.e(TAG, "Exception " + exception.getLocalizedMessage());
				}
			}
		});

		// Trap the capture button.
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(RegisterPhoto.this, null, null, RegisterPhoto.this);
			}
		});

	}

	private boolean hasFlash() {
		Camera.Parameters p = mCamera.getParameters();
		return p.getFlashMode() == null ? false : true;
	}

	private void showFlash(boolean flash) {
		if (flashImageView != null) {
			if (flash) {
				flashImageView.setVisibility(View.VISIBLE);
			} else {
				flashImageView.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * Refresh the camera after a switch is called
	 */
	public void switchCamera() {
		mCamera.stopPreview();
		mCamera.release();

		if (hasFrontCamera) {
			if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
				currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
				showFlash(false);
			} else {
				currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
				showFlash(true);
			}
		} else {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
			showFlash(true);
		}

		mCamera = Camera.open(currentCameraId);
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
		int rotation = this.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break; // Natural orientation
		case Surface.ROTATION_90:
			degrees = 90;
			break; // Landscape left
		case Surface.ROTATION_180:
			degrees = 180;
			break;// Upside down
		case Surface.ROTATION_270:
			degrees = 270;
			break;// Landscape right
		}
		int rotate = (info.orientation - degrees + 360) % 360;

		// Set the 'rotation' parameter
		Camera.Parameters params = mCamera.getParameters();
		params.setRotation(rotate);
		try {
			mCamera.setPreviewDisplay(mPreview.getHolder());
		} catch (IOException e) {
			Log.e(TAG, "Exception " + e.getLocalizedMessage());
		}
		mCamera.setParameters(params);
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}

	@Override
	public void onShutter() {
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			photoFile = getOutputMediaFile();
			if (photoFile == null) {
				Toast.makeText(activity,
						"Taking photo failed.Please try again",
						Toast.LENGTH_SHORT).show();
				return;
			}
			FileOutputStream fos = new FileOutputStream(photoFile);
			fos.write(data);
			fos.close();

		} catch (Exception e) {
			Log.e(TAG, "onPictureTaken - wrote bytes: " + data.length);
		}
		camera.startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
		int rotation = this.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break; // Natural orientation
		case Surface.ROTATION_90:
			degrees = 90;
			break; // Landscape left
		case Surface.ROTATION_180:
			degrees = 180;
			break;// Upside down
		case Surface.ROTATION_270:
			degrees = 270;
			break;// Landscape right
		}
		int rotate = (info.orientation - degrees + 360) % 360;
		// Set the 'rotation' parameter
		Camera.Parameters params = mCamera.getParameters();
		params.setRotation(rotate);
		mCamera.setParameters(params);
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}

	private boolean hasAFrontCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				hasFrontCamera = true;
				break;
			}
		}
		return hasFrontCamera;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(mPreview.getHolder());
		} catch (Exception e) {
			Log.e(TAG, "Exception " + e.getLocalizedMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "Surface destroyed");
	}

	@Override
	public void onPause() {
		super.onPause();
		mCamera.stopPreview();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCamera.release();
	}

	/**
	 * Used to return the camera File output.
	 * 
	 * @return
	 */
	private File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				DIRECTORY_IMAGES);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e("Camera Guide", "Required media storage does not exist");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		Toast.makeText(activity, "Your photo has been saved!",
				Toast.LENGTH_LONG).show();
		return mediaFile;
	}
}