package com.squalala.dz6android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.squalala.dz6android.ui.activity.MainActivity;
import com.squalala.dz6android.ui.activity.ShowPostActivity;
import com.squalala.dz6android.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hugo.weaving.DebugLog;


/**
 * Created by Ravi on 01/06/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @DebugLog
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            String type = data.getString("type");

            String fbNum = data.getString("fb_num");

            String fbName = data.getString("fb_name");

            String postId = data.getString("post_id");

            Intent resultIntent = null;


            if (!isBackground) {


                if (type != null)
                {
                    switch (type)
                    {
                        case "facebook":

                            final String urlFb = "fb://page/"+ fbNum;

                            resultIntent = new Intent(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(urlFb));

                            // If Facebook application is installed, use that else launch a browser
                            final PackageManager packageManager = context.getPackageManager();
                            List<ResolveInfo> list =
                                    packageManager.queryIntentActivities(resultIntent,
                                            PackageManager.MATCH_DEFAULT_ONLY);
                            if (list.size() == 0) {
                                final String urlBrowser = "https://www.facebook.com/" + fbName;
                                resultIntent.setData(Uri.parse(urlBrowser));
                            }

                            break;


                        case "post":


                            resultIntent = new Intent(context, ShowPostActivity.class);

                            resultIntent.putExtra("id", Long.valueOf(postId));
                            resultIntent.putExtra("all_category", true);
                            resultIntent.putExtra("notification", true);

                            break;

                        case "message":

                            resultIntent = new Intent(context, MainActivity.class);

                            break;
                    }


                    showNotificationMessage(context, title, message, resultIntent);
                }








            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}