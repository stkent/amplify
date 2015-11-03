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

public final class FeedbackUtils {

    private static final int BASE_MESSAGE_LENGTH = 78;

    private FeedbackUtils() {

    }

    public static void showFeedbackEmailChooser(final Activity activity) {
        activity.startActivity(Intent.createChooser(getFeedbackEmailIntent(), "Choose an email provider:"));
        activity.overridePendingTransition(0, 0);
    }

    public static boolean canHandleFeedbackEmailIntent(final Context context) {
        final List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(getFeedbackEmailIntent(),
                PackageManager.MATCH_DEFAULT_ONLY);
        return !resolveInfoList.isEmpty();
    }

    public static String getAppInfo() {
        //noinspection StringBufferReplaceableByString

        return new StringBuilder(BASE_MESSAGE_LENGTH)

        .append("\n\n\n---------------------\nApp Version: ")

        .append(BuildConfig.VERSION_NAME)
        .append(" - ")
        .append(BuildConfig.VERSION_CODE)
        .append("\n")

        .append("Android OS Version: ")
        .append(Build.VERSION.RELEASE)
        .append(" - ")
        .append(Build.VERSION.SDK_INT)

        .append("\n")
        .append("Date: ")
        .append(System.currentTimeMillis())
        .toString();
    }

    /**
     * Creates a bitmap from the activity's root view
     */
    public static void getBitmapFromRootView(Activity activity, final OnBitmapCapturedListener bitmapCapturedListener) {
        final View view = activity.getWindow().getDecorView().getRootView();
            bitmapCapturedListener.onCapture(getScreenBitmap(view), null);
    }

    public static List<Uri> getAttachmentList(Uri... uris) {
        List<Uri> list = new ArrayList<>(uris.length);
        list.addAll(Arrays.asList(uris));
        return list;
    }

    private static Intent getFeedbackEmailIntent() {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

//        TODO: this
//        final String email = AppProvider.getAppContext().getString(R.string.feedback_email);
        final String feedbackMailTo = "mailto:" + emailIntent.toString(); //+email;
        final String feedbackEmailSubject = Uri.encode("Android App Feedback", "UTF-8");
        final String appInfo = getAppInfo();

        final String uriString = feedbackMailTo
                + "?subject=" + feedbackEmailSubject
                + "&body=" + Uri.encode(appInfo);

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
    public interface OnBitmapCapturedListener {
        void onCapture(Bitmap screenBitmap, Bitmap mapBitmap);
    }

}
