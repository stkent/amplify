package com.github.stkent.amplify.example;

import android.app.Application;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.AmplifyStateTracker;
import com.github.stkent.amplify.tracking.checks.CooldownDaysCheck;
import com.github.stkent.amplify.tracking.checks.GooglePlayStoreIsAvailableCheck;
import com.github.stkent.amplify.tracking.checks.MaximumCountCheck;
import com.github.stkent.amplify.tracking.checks.VersionChangedCheck;
import com.github.stkent.amplify.tracking.checks.WarmUpDaysCheck;

import static com.github.stkent.amplify.tracking.IntegratedEvent.APP_CRASHED;
import static com.github.stkent.amplify.tracking.IntegratedEvent.APP_INSTALLED;
import static com.github.stkent.amplify.tracking.IntegratedEvent.USER_DECLINED_CRITICAL_FEEDBACK;
import static com.github.stkent.amplify.tracking.IntegratedEvent.USER_DECLINED_POSITIVE_FEEDBACK;
import static com.github.stkent.amplify.tracking.IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK;
import static com.github.stkent.amplify.tracking.IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AmplifyStateTracker.get(this)
                .setLogLevel(Logger.LogLevel.DEBUG)
                .addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .trackLastEventTime(APP_INSTALLED, new WarmUpDaysCheck(7))
                .trackTotalEventCount(USER_GAVE_POSITIVE_FEEDBACK, new MaximumCountCheck(1))
                .trackLastEventTime(USER_GAVE_NEGATIVE_FEEDBACK, new CooldownDaysCheck(7))
                .trackLastEventTime(USER_DECLINED_CRITICAL_FEEDBACK, new CooldownDaysCheck(7))
                .trackLastEventVersion(USER_DECLINED_CRITICAL_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(USER_DECLINED_POSITIVE_FEEDBACK, new CooldownDaysCheck(7))
                .trackLastEventVersion(USER_DECLINED_POSITIVE_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(APP_CRASHED, new CooldownDaysCheck(7))
                .trackLastEventVersion(USER_GAVE_NEGATIVE_FEEDBACK, new VersionChangedCheck());
    }

}
