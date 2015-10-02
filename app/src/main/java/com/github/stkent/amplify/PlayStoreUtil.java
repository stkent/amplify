package com.github.stkent.amplify;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import static android.content.Intent.ACTION_VIEW;

public class PlayStoreUtil {

    // todo: this thing
    private static final String PLAY_STORE_ID = "";
    private static final String ANDROID_MARKET_URI = "market://details?id=" + PLAY_STORE_ID;
    private static final String GOOGLE_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=" + PLAY_STORE_ID;

    public static void openPlayStoreToRate(Activity activity) {
        if (ActivityStateUtil.isActivityValid(activity)) {
            try {
                activity.startActivity(new Intent(ACTION_VIEW, Uri.parse(ANDROID_MARKET_URI)));
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_URL)));
            } finally {
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

}
