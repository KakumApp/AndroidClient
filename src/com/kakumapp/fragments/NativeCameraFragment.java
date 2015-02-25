package com.kakumapp.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakumapp.R;

@SuppressWarnings("deprecation")
public class NativeCameraFragment extends Fragment {

	private static final String TAG = "NativeCameraFragment";
	// Native camera.
	private Camera mCamera;
	// View to display the camera output.
	private CameraPreview mPreview;
	// Reference to the containing view.
	private View mCameraView;
	// directory for storing images
	private String DIRECTORY_IMAGES = "Kakuma";
	// control buttons
	private static ImageView flashImageView, switchImageView;
	// take photo
	private Button captureButton;
	// camera id
	private static boolean frontCamera = true;

	// private ActionBarActivity actionBarActivity;

	/**
	 * Default empty constructor.
	 */
	public NativeCameraFragment() {
		super();
	}

	/**
	 * OnCreateView fragment override
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mCameraView = inflater.inflate(R.layout.fragment_native_camera,
				container, false);
		// Create our Preview view and set it as the content of our activity.
		boolean opened = safeCameraOpenInView();
		if (opened == false) {
			Log.e(TAG, "Error, Camera failed to open");
			return mCameraView;
		}
		// Trap the capture button.
		captureButton = (Button) mCameraView.findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPicture);
			}
		});

		flashImageView = (ImageView) mCameraView
				.findViewById(R.id.imageView_flash);
		flashImageView.setTag(R.drawable.ic_action_flash_automatic);

		switchImageView = (ImageView) mCameraView
				.findViewById(R.id.imageView_switch);

		if (!hasMultipleCameras()) {
			flashImageView.setVisibility(View.GONE);
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

		switchImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				frontCamera = !frontCamera;
				safeCameraOpenInView();
			}
		});
		return mCameraView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// actionBarActivity = (ActionBarActivity) getActivity();
	}

	/**
	 * Recommended "safe" way to open the camera.
	 * 
	 * @param view
	 * @return
	 */
	private boolean safeCameraOpenInView() {
		boolean qOpened = false;
		releaseCameraAndPreview();
		mCamera = getCameraInstance();
		qOpened = (mCamera != null);

		if (qOpened == true) {
			mPreview = new CameraPreview(getActivity().getBaseContext(),
					mCamera, mCameraView);
			FrameLayout preview = (FrameLayout) mCameraView
					.findViewById(R.id.camera_preview);
			preview.removeAllViews();
			preview.addView(mPreview);
			mPreview.startCameraPreview();
		}
		return qOpened;
	}

	private static boolean hasMultipleCameras() {
		return Camera.getNumberOfCameras() > 1;
	}

	/**
	 * Safe method for getting a camera instance.
	 * 
	 * @return
	 */
	public static Camera getCameraInstance() {
		Camera camera = null;
		try {
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);
				try {
					if (frontCamera) {
						// prefer front
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							camera = Camera.open(i);
							break;
						}
					} else if (!frontCamera || camera == null) {
						// if absent use back
						if (camera == null) {
							camera = Camera.open(i);
							break;
						}
					}
				} catch (RuntimeException e) {
					Log.e(TAG,
							"Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// returns null if camera is unavailable
		return camera;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseCameraAndPreview();
	}

	/**
	 * Clear any existing preview / camera.
	 */
	private void releaseCameraAndPreview() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
		if (mPreview != null) {
			mPreview.destroyDrawingCache();
			mPreview.mCamera = null;
		}
	}

	/**
	 * Surface on which the camera projects it's capture results.
	 */
	class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

		// SurfaceHolder
		private SurfaceHolder mHolder;

		// Our Camera.
		private Camera mCamera;

		// Parent Context.
		private Context mContext;

		// Camera Sizing (For rotation, orientation changes)
		private Camera.Size mPreviewSize;

		// List of supported preview sizes
		private List<Camera.Size> mSupportedPreviewSizes;

		// Flash modes supported by this camera
		private List<String> mSupportedFlashModes;

		// View holding this camera.
		private View mCameraView;

		public CameraPreview(Context context, Camera camera, View cameraView) {
			super(context);

			// Capture the context
			mCameraView = cameraView;
			mContext = context;
			setCamera(camera);

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setKeepScreenOn(true);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		/**
		 * Begin the preview of the camera input.
		 */
		public void startCameraPreview() {
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Extract supported preview and flash modes from the camera.
		 * 
		 * @param camera
		 */
		private void setCamera(Camera camera) {
			mCamera = camera;
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			mSupportedFlashModes = mCamera.getParameters()
					.getSupportedFlashModes();

			// Set the camera to Auto Flash mode.
			if (mSupportedFlashModes != null
					&& mSupportedFlashModes
							.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				mCamera.setParameters(parameters);
			}
			requestLayout();
		}

		/**
		 * The Surface has been created, now tell the camera where to draw the
		 * preview.
		 * 
		 * @param holder
		 */
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Dispose of the camera preview.
		 * 
		 * @param holder
		 */
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mCamera != null) {
				mCamera.stopPreview();
			}
		}

		/**
		 * React to surface changed events
		 * 
		 * @param holder
		 * @param format
		 * @param w
		 * @param h
		 */
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				Camera.Parameters parameters = mCamera.getParameters();

				// Set the auto-focus mode to "continuous"
				parameters
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

				// Preview size must exist.
				if (mPreviewSize != null) {
					Camera.Size previewSize = mPreviewSize;
					parameters.setPreviewSize(previewSize.width,
							previewSize.height);
				}

				mCamera.setParameters(parameters);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Calculate the measurements of the layout
		 * 
		 * @param widthMeasureSpec
		 * @param heightMeasureSpec
		 */
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// Source:
			// http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
			final int width = resolveSize(getSuggestedMinimumWidth(),
					widthMeasureSpec);
			final int height = resolveSize(getSuggestedMinimumHeight(),
					heightMeasureSpec);
			setMeasuredDimension(width, height);

			if (mSupportedPreviewSizes != null) {
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
						width, height);
			}
		}

		/**
		 * Update the layout based on rotation and orientation changes.
		 * 
		 * @param changed
		 * @param left
		 * @param top
		 * @param right
		 * @param bottom
		 */
		@Override
		protected void onLayout(boolean changed, int left, int top, int right,
				int bottom) {
			if (changed) {
				final int width = right - left;
				final int height = bottom - top;

				int previewWidth = width;
				int previewHeight = height;

				if (mPreviewSize != null) {
					Display display = ((WindowManager) mContext
							.getSystemService(Context.WINDOW_SERVICE))
							.getDefaultDisplay();

					switch (display.getRotation()) {
					case Surface.ROTATION_0:
						previewWidth = mPreviewSize.height;
						previewHeight = mPreviewSize.width;
						mCamera.setDisplayOrientation(90);
						break;
					case Surface.ROTATION_90:
						previewWidth = mPreviewSize.width;
						previewHeight = mPreviewSize.height;
						break;
					case Surface.ROTATION_180:
						previewWidth = mPreviewSize.height;
						previewHeight = mPreviewSize.width;
						break;
					case Surface.ROTATION_270:
						previewWidth = mPreviewSize.width;
						previewHeight = mPreviewSize.height;
						mCamera.setDisplayOrientation(180);
						break;
					}
				}

				final int scaledChildHeight = previewHeight * width
						/ previewWidth;
				mCameraView
						.layout(0, height - scaledChildHeight, width, height);
			}
		}

		/**
		 * 
		 * @param sizes
		 * @param width
		 * @param height
		 * @return
		 */
		private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes,
				int width, int height) {
			// Source:
			// http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
			Camera.Size optimalSize = null;

			final double ASPECT_TOLERANCE = 0.1;
			double targetRatio = (double) height / width;

			// Try to find a size match which suits the whole screen minus the
			// menu on the left.
			for (Camera.Size size : sizes) {

				if (size.height != width)
					continue;
				double ratio = (double) size.width / size.height;
				if (ratio <= targetRatio + ASPECT_TOLERANCE
						&& ratio >= targetRatio - ASPECT_TOLERANCE) {
					optimalSize = size;
				}
			}

			// If we cannot find the one that matches the aspect ratio, ignore
			// the requirement.
			if (optimalSize == null) {
				// TODO : Backup in case we don't get a size.
			}

			return optimalSize;
		}
	}

	/**
	 * Picture Callback for handling a picture capture and saving it out to a
	 * file.
	 */
	private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				Toast.makeText(getActivity(), "Image retrieval failed.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				// Restart the camera preview.
				safeCameraOpenInView();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

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
				Log.d("Camera Guide", "Required media storage does not exist");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		// DialogHelper.showDialog("Success!", "Your picture has been saved!",
		// getActivity());

		Toast.makeText(getActivity(), "Your picture has been saved!",
				Toast.LENGTH_LONG).show();

		return mediaFile;
	}
}
