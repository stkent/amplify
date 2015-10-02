package com.github.stkent.amplify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedbackUtils {

    public static void showFeedbackEmailChooser(final Activity activity) {
        activity.startActivity(Intent.createChooser(getFeedbackEmailIntent(), "Choose an email provider:"));
        activity.overridePendingTransition(0, 0);
    }

    public static boolean canHandleFeedbackEmailIntent(final Context context) {
        final List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(getFeedbackEmailIntent(), PackageManager.MATCH_DEFAULT_ONLY);
        return !resolveInfoList.isEmpty();
    }

    public static String getAppInfo() {
        //noinspection StringBufferReplaceableByString
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("\n\n\n");
        messageBuilder.append("---------------------\n");

        messageBuilder.append("App Version: ");
        messageBuilder.append(BuildConfig.VERSION_NAME);
        messageBuilder.append(" - ");
        messageBuilder.append(BuildConfig.VERSION_CODE);
        messageBuilder.append("\n");

        messageBuilder.append("Android OS Version: ");
        messageBuilder.append(Build.VERSION.RELEASE);
        messageBuilder.append(" - ");
        messageBuilder.append(Build.VERSION.SDK_INT);

        messageBuilder.append("\n");
        messageBuilder.append("Date: ");
        messageBuilder.append(System.currentTimeMillis());
        return messageBuilder.toString();
    }

    /**
     * Creates a bitmap from the activity's root view
     */
    public static void getBitmapFromRootView(Activity activity, final OnBitmapCapturedListener bitmapCapturedListener) {
        final View view = activity.getWindow().getDecorView().getRootView();
            bitmapCapturedListener.onCapture(getScreenBitmap(view), null);
    }

    public static ArrayList<Uri> getAttachmentList(Uri... uris) {
        ArrayList<Uri> arrayList = new ArrayList<>(uris.length);
        arrayList.addAll(Arrays.asList(uris));
        return arrayList;
    }

    private static Intent getFeedbackEmailIntent() {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

//        TODO: this
//        final String email = AppProvider.getAppContext().getString(R.string.feedback_email);
        final String feedbackMailTo = "mailto:";//+email;
        final String feedbackEmailSubject = Uri.encode("Android App Feedback", "UTF-8");
        final String appInfo = getAppInfo();

        final String uriString = feedbackMailTo +
                "?subject=" + feedbackEmailSubject +
                "&body=" + Uri.encode(appInfo);

        final Uri uri = Uri.parse(uriString);
        emailIntent.setData(uri);

        return emailIntent;
    }

    private static Bitmap getScreenBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    /**
     * callback methods for when screen capture has taken place. This is required to be able to get a bitmap from a map.
     *
     * @see
     */
    public interface OnBitmapCapturedListener{
        void onCapture(Bitmap screenBitmap, Bitmap mapBitmap);
    }

}
