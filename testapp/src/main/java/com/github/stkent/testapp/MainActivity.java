package com.github.stkent.testapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.stkent.amplify.prompt.BasePromptViewConfig;
import com.github.stkent.amplify.prompt.CustomLayoutPromptView;
import com.github.stkent.amplify.prompt.CustomLayoutPromptViewConfig;
import com.github.stkent.amplify.prompt.DefaultLayoutPromptView;
import com.github.stkent.amplify.prompt.DefaultLayoutPromptViewConfig;
import com.github.stkent.amplify.tracking.Amplify;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Amplify.getSharedInstance().promptIfReady(
                    (DefaultLayoutPromptView)
                            findViewById(R.id.default_layout_prompt_view_no_customization));

            Amplify.getSharedInstance().promptIfReady(
                    (DefaultLayoutPromptView) findViewById(
                            R.id.default_layout_prompt_view_xml_config));

            final DefaultLayoutPromptView defaultLayoutPromptView
                    = (DefaultLayoutPromptView)
                            findViewById(R.id.default_layout_prompt_view_code_config);

            final BasePromptViewConfig baseDefaultLayoutPromptViewConfig
                    = new BasePromptViewConfig.Builder()
                            .setUserOpinionQuestionTitle("i. User Opinion Title")
                            .setUserOpinionQuestionSubtitle("ii. User Opinion Subtitle")
                            .setUserOpinionQuestionPositiveButtonLabel("iii. Yes")
                            .setUserOpinionQuestionNegativeButtonLabel("iv. No")
                            .setPositiveFeedbackQuestionTitle("v. Positive Feedback Title")
                            .setPositiveFeedbackQuestionSubtitle("vi. Positive Feedback Subtitle")
                            .setPositiveFeedbackQuestionPositiveButtonLabel("vii. Yes")
                            .setPositiveFeedbackQuestionNegativeButtonLabel("viii. No")
                            .setCriticalFeedbackQuestionTitle("ix. Critical Feedback Title")
                            .setCriticalFeedbackQuestionSubtitle("x. Critical Feedback Subtitle")
                            .setCriticalFeedbackQuestionPositiveButtonLabel("xi. Yes")
                            .setCriticalFeedbackQuestionNegativeButtonLabel("xii. No")
                            .setThanksTitle("xiii. Thanks Title")
                            .setThanksSubtitle("xiv. Thanks Subtitle")
                            .setThanksDisplayTimeMs(2000)
                            .build();

            final DefaultLayoutPromptViewConfig defaultLayoutPromptViewConfig
                    = new DefaultLayoutPromptViewConfig.Builder()
                            .setForegroundColor(Color.parseColor("#FF0000"))
                            .setBackgroundColor(Color.parseColor("#FF9900"))
                            .setTitleTextColor(Color.parseColor("#33FF00"))
                            .setSubtitleTextColor(Color.parseColor("#00FFFF"))
                            .setPositiveButtonTextColor(Color.parseColor("#CC00FF"))
                            .setPositiveButtonBackgroundColor(Color.parseColor("#3300FF"))
                            .setPositiveButtonBorderColor(Color.parseColor("#0066FF"))
                            .setNegativeButtonTextColor(Color.parseColor("#FFFF00"))
                            .setNegativeButtonBackgroundColor(Color.parseColor("#FF0000"))
                            .setNegativeButtonBorderColor(Color.parseColor("#999999"))
                            .setCustomTextSizePx(getResources()
                                    .getDimensionPixelSize(R.dimen.prompt_view_text_size_large))
                            .setButtonBorderWidthPx(getResources()
                                    .getDimensionPixelSize(R.dimen.prompt_view_button_border_width))
                            .setButtonCornerRadiusPx(getResources()
                                    .getDimensionPixelSize(R.dimen.prompt_view_button_corner_radius))
                            .build();

            defaultLayoutPromptView.applyBaseConfig(baseDefaultLayoutPromptViewConfig);
            defaultLayoutPromptView.applyConfig(defaultLayoutPromptViewConfig);

            Amplify.getSharedInstance().promptIfReady(defaultLayoutPromptView);

            Amplify.getSharedInstance().promptIfReady(
                    (CustomLayoutPromptView) findViewById(
                            R.id.custom_layout_prompt_view_xml_config));

            final CustomLayoutPromptView customLayoutPromptView
                    = (CustomLayoutPromptView) findViewById(
                            R.id.custom_layout_prompt_view_code_config);

            final BasePromptViewConfig baseCustomLayoutPromptViewConfig
                    = new BasePromptViewConfig.Builder()
                            .setUserOpinionQuestionTitle("A. User Opinion Title")
                            .setUserOpinionQuestionSubtitle("B. User Opinion Subtitle")
                            .setUserOpinionQuestionPositiveButtonLabel("C. Yes")
                            .setUserOpinionQuestionNegativeButtonLabel("D. No")
                            .setPositiveFeedbackQuestionTitle("E. Positive Feedback Title")
                            .setPositiveFeedbackQuestionSubtitle("F. Positive Feedback Subtitle")
                            .setPositiveFeedbackQuestionPositiveButtonLabel("G. Yes")
                            .setPositiveFeedbackQuestionNegativeButtonLabel("H. No")
                            .setCriticalFeedbackQuestionTitle("I. Critical Feedback Title")
                            .setCriticalFeedbackQuestionSubtitle("J. Critical Feedback Subtitle")
                            .setCriticalFeedbackQuestionPositiveButtonLabel("K. Yes")
                            .setCriticalFeedbackQuestionNegativeButtonLabel("L. No")
                            .setThanksTitle("M. Thanks Title")
                            .setThanksSubtitle("N. Thanks Subtitle")
                            .setThanksDisplayTimeMs(2000)
                            .build();

            final CustomLayoutPromptViewConfig customLayoutPromptViewConfig
                    = new CustomLayoutPromptViewConfig(
                            R.layout.custom_question_view, R.layout.custom_thanks_view);

            customLayoutPromptView.applyBaseConfig(baseCustomLayoutPromptViewConfig);
            customLayoutPromptView.applyConfig(customLayoutPromptViewConfig);

            Amplify.getSharedInstance().promptIfReady(customLayoutPromptView);
        }
    }

}
