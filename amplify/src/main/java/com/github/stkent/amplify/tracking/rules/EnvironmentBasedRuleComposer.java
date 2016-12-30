package com.github.stkent.amplify.tracking.rules;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;

import java.util.Arrays;

public final class EnvironmentBasedRuleComposer {

    @NonNull
    public static IEnvironmentBasedRule anyOf(@NonNull final IEnvironmentBasedRule... environmentBasedRules) {
        return new IEnvironmentBasedRule() {
            @Override
            public boolean shouldAllowFeedbackPrompt(@NonNull final IEnvironment environment) {
                for (final IEnvironmentBasedRule environmentBasedRule : environmentBasedRules) {
                    if (environmentBasedRule.shouldAllowFeedbackPrompt(environment)) {
                        return true;
                    }
                }

                return false;
            }

            @NonNull
            @Override
            public String getDescription() {
                return "AnyOf" + Arrays.toString(environmentBasedRules);
            }
        };
    }

    @NonNull
    public static IEnvironmentBasedRule allOf(@NonNull final IEnvironmentBasedRule... environmentBasedRules) {
        return new IEnvironmentBasedRule() {
            @Override
            public boolean shouldAllowFeedbackPrompt(@NonNull final IEnvironment environment) {
                for (final IEnvironmentBasedRule environmentBasedRule : environmentBasedRules) {
                    if (!environmentBasedRule.shouldAllowFeedbackPrompt(environment)) {
                        return false;
                    }
                }

                return true;
            }

            @NonNull
            @Override
            public String getDescription() {
                return "AllOf" + Arrays.toString(environmentBasedRules);
            }
        };
    }

    private EnvironmentBasedRuleComposer() {
        // This constructor intentionally left blank.
    }

}
