package com.findme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cocosw.bottomsheet.BottomSheet;
import com.findme.Constants;
import com.findme.FindMeApplication;
import com.findme.R;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Utils {

    private static String TAG = "Utils";
    private AppCompatActivity activity;
    private static FindMeApplication application;

    public Utils(AppCompatActivity activity) {
        this.activity = activity;
        application = (FindMeApplication) activity.getApplication();
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
     **/
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

    /**
     * Does a GET request
     *
     * @param url
     * @param params
     * @return
     */
    public static String getData(String url, Map<String, String> params) {
        String message = "Fetching data from " + url;
        if (params != null) {
            message += " with params\n" + getQueryString(params);
        }
        Log.e(TAG, message);
        String response = "";
        try {
            if (params != null) {
                url = url + getQueryString(params);
            }
            response = HttpRequest.get(url).basic(application.getApiUsername(),
                    application.getApiPassword()).body();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getLocalizedMessage(), e);
        }
        return response;
    }

    /**
     * Does a POST request
     *
     * @param url
     * @param params
     * @return
     */
    public static String postData(String url, Map<String, String> params) {
        String message = "Posting data to " + url;
        if (params != null) {
            message += " with params\n" + getQueryString(params);
        }
        Log.e(TAG, message);
        String response = "";
        try {
            HttpRequest httpRequest = HttpRequest.post(url).form(params).
                    basic(application.getApiUsername(), application.getApiPassword());
            response = httpRequest.body();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getLocalizedMessage(), e);
        }
        return response;
    }

    /**
     * Does a POST JSON request
     *
     * @param url
     * @return
     */
    public static String postJsonData(String url, JSONObject jsonObject) {
        String message = "Posting data to " + url;
        if (jsonObject != null) {
            message += " with params\n" + jsonObject.toString();
        }
        Log.e(TAG, message);
        String response = "";
        try {
            HttpRequest httpRequest = HttpRequest.post(url).send(jsonObject.toString())
                    .contentType("application/json").
                            basic(application.getApiUsername(), application.getApiPassword());
            response = httpRequest.body();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getLocalizedMessage(), e);
        }
        return response;
    }

    private static String getQueryString(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (HashMap.Entry<String, String> e : params.entrySet()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append('&');
                }
                stringBuilder.append(URLEncoder.encode(e.getKey(), "UTF-8"))
                        .append('=')
                        .append(URLEncoder.encode(e.getValue(), "UTF-8"));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return stringBuilder.toString();
    }

    /**
     * Does generic posting of multipart data
     *
     * @param url
     * @param params
     * @param filesParams
     * @return
     */
    public static String multipartPost(String url, Map<String, String> params, Map<String, File> filesParams) {
        if (params == null) {
            params = new HashMap<>();
        }
        String response = "";
        try {
            HttpRequest request = HttpRequest.post(url).
                    basic(application.getApiUsername(), application.getApiPassword());
            addPostParams(request, params);
            addFilePostParams(request, filesParams);
            response = request.body();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getLocalizedMessage(), e);
        }
        Log.e(TAG, response);
        return response;
    }

    public static void addPostParams(HttpRequest request, Map<String, String> params) {
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry param = (Map.Entry) iterator.next();
            request.part(param.getKey().toString(), param.getValue().toString());
            iterator.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static void addFilePostParams(HttpRequest request, Map<String, File> filesParams) {
        Iterator iterator = filesParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry param = (Map.Entry) iterator.next();
            File file = (File) param.getValue();
            request.part(param.getKey().toString(), file.getName(), "application/octet-stream", file);
            iterator.remove(); // avoids a ConcurrentModificationException
        }
    }
}
