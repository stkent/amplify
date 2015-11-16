package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.TrackingUtils;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

/**
 * Created by bobbake4 on 11/16/15.
 */
public class LastVersionMap extends PredicateMap<String> {

    public LastVersionMap(ILogger logger, Context applicationContext) {
        super(logger, new GenericSettings<String>(applicationContext), applicationContext);
    }

    @Override
    public void eventTriggered(@NonNull IEvent event) {

        if (containsKey(event)) {
            try {
                final String currentVersion = TrackingUtils.getAppVersionName(applicationContext);
                updateEventValue(event, currentVersion);
            } catch (final PackageManager.NameNotFoundException e) {
                logger.d("Could not read current app version name.");
            }
        }
    }
}
