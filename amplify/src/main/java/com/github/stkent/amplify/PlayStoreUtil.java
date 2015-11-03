package com.github.stkent.amplify;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.content.Intent.ACTION_VIEW;

public final class PlayStoreUtil {

    private static final String ANDROID_MARKET_URI_PREFIX = "market://details?id=";
    private static final String GOOGLE_PLAY_STORE_URI_PREFIX = "https://play.google.com/store/apps/details?id=";

    private PlayStoreUtil() {

    }

    public static void openPlayStoreToRate(@NonNull final Activity activity) {
        if (ActivityStateUtil.isActivityValid(activity)) {
            final String packageName = activity.getPackageName();

            try {
                activity.startActivity(
                        new Intent(
                                ACTION_VIEW,
                                getAndroidMarketUriForPackageName(packageName)
                        )
                );
            } catch (ActivityNotFoundException e) {
                activity.startActivity(
                        new Intent(
                                ACTION_VIEW,
                                getGooglePlayStoreUriForPackageName(packageName)
                        )
                );
            } finally {
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

    @NonNull
    private static Uri getAndroidMarketUriForPackageName(@NonNull final String packageName) {
        return Uri.parse(ANDROID_MARKET_URI_PREFIX + packageName);
    }

    @NonNull
    private static Uri getGooglePlayStoreUriForPackageName(@NonNull final String packageName) {
        return Uri.parse(GOOGLE_PLAY_STORE_URI_PREFIX + packageName);
    }

}
