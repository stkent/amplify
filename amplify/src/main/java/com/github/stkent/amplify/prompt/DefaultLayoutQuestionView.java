/*
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify.prompt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.view.View;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.utils.Constants;
import com.github.stkent.amplify.utils.DisplayUtils;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static java.lang.Math.max;

@SuppressLint("ViewConstructor")
final class DefaultLayoutQuestionView extends CustomLayoutQuestionView {

    private static final int NUMBER_OF_COLOR_DIMENSIONS = 3;
    private static final int DEFAULT_BUTTON_BORDER_WIDTH_DP = 1;
    private static final int DEFAULT_BUTTON_CORNER_RADIUS_PX = 0;

    /* default */ DefaultLayoutQuestionView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_question_view);
        final TextView subtitleTextView = getSubtitleTextView();

        if (subtitleTextView == null) {
            throw new IllegalStateException(Constants.MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE);
        }

        setBackgroundColor(config.getFillColor());

        getTitleTextView().setTextColor(config.getTitleTextColor());
        subtitleTextView.setTextColor(config.getSubtitleTextColor());

        setQuoteButtonUnquoteTextColor(getPositiveButton(), config.getPositiveButtonTextColor());
        setQuoteButtonUnquoteTextColor(getNegativeButton(), config.getNegativeButtonTextColor());

        configureTextSizes(config);
        configureButtonBackgrounds(context, config);
    }

    /**
     * We are defensive here, because it's not uncommon to make "buttons" out of UI components like
     * FrameLayouts, say. If we can't cast to a TextView to obtain a setTextColor method, the button
     * text color will be left unchanged.
     *
     * @param quoteButtonUnquote the "button" whose text color we wish to set
     * @param color the color we wish to apply
     */
    private void setQuoteButtonUnquoteTextColor(
            @NonNull final View quoteButtonUnquote,
            @ColorInt final int color) {

        if (quoteButtonUnquote instanceof TextView) {
            ((TextView) quoteButtonUnquote).setTextColor(color);
        }
    }

    private void configureTextSizes(@NonNull final DefaultLayoutPromptViewConfig config) {
        final Integer customTextSizePx = config.getCustomTextSizePx();

        if (customTextSizePx != null) {
            getTitleTextView().setTextSize(COMPLEX_UNIT_PX, customTextSizePx);
            setQuoteButtonUnquoteTextSize(getPositiveButton(), customTextSizePx);
            setQuoteButtonUnquoteTextSize(getNegativeButton(), customTextSizePx);

            final TextView subtitleTextView = getSubtitleTextView();

            if (subtitleTextView != null) {
                subtitleTextView.setTextSize(COMPLEX_UNIT_PX, customTextSizePx);
            }
        }
    }

    /**
     * We are defensive here, because it's not uncommon to make "buttons" out of UI components like
     * FrameLayouts, say. If we can't cast to a TextView to obtain a setText method, the button text
     * size will be left unchanged.
     *
     * @param quoteButtonUnquote the "button" whose text size we wish to set
     * @param textSize the text size we wish to apply, in pixels
     */
    private void setQuoteButtonUnquoteTextSize(
            @NonNull final View quoteButtonUnquote,
            @Px final int textSize) {

        if (quoteButtonUnquote instanceof TextView) {
            ((TextView) quoteButtonUnquote).setTextSize(COMPLEX_UNIT_PX, textSize);
        }
    }

    private void configureButtonBackgrounds(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        final Integer customButtonBorderWidthPx = config.getCustomButtonBorderWidthPx();
        final Integer customButtonCornerRadiusPx = config.getCustomButtonCornerRadiusPx();

        final Integer actualButtonBorderWidthPx
                = customDimensionOrDefault(
                        customButtonBorderWidthPx,
                        DisplayUtils.dpToPx(context, DEFAULT_BUTTON_BORDER_WIDTH_DP));

        final Integer actualButtonCornerRadiusPx
                = customDimensionOrDefault(
                        customButtonCornerRadiusPx,
                        DEFAULT_BUTTON_CORNER_RADIUS_PX);

        setButtonViewBackground(
                getPositiveButton(),
                config.getPositiveButtonBackgroundColor(),
                config.getPositiveButtonBorderColor(),
                actualButtonBorderWidthPx,
                actualButtonCornerRadiusPx);

        setButtonViewBackground(
                getNegativeButton(),
                config.getNegativeButtonBackgroundColor(),
                config.getNegativeButtonBorderColor(),
                actualButtonBorderWidthPx,
                actualButtonCornerRadiusPx);
    }

    @Px
    private int customDimensionOrDefault(
            @Nullable @Px final Integer customDimension,
            @Px final int defaultDimension) {

        return customDimension != null ? customDimension : defaultDimension;
    }

    private void setButtonViewBackground(
            @NonNull final View button,
            @ColorInt final int fillColor,
            @ColorInt final int borderColor,
            final int borderWidthPx,
            final int cornerRadiusPx) {

        final Drawable buttonBackgroundDrawable
                = getButtonBackgroundDrawable(fillColor, borderColor, borderWidthPx, cornerRadiusPx);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackground(buttonBackgroundDrawable);
        } else {
            //noinspection deprecation
            button.setBackgroundDrawable(buttonBackgroundDrawable);
        }
    }

    @NonNull
    private Drawable getButtonBackgroundDrawable(
            @ColorInt final int fillColor,
            @ColorInt final int borderColor,
            final int borderWidthPx,
            final int cornerRadiusPx) {

        final Drawable defaultDrawable = getStaticButtonBackgroundDrawable(
                fillColor, borderColor, borderWidthPx, cornerRadiusPx);

        float[] defaultFillColorHSVValues = new float[NUMBER_OF_COLOR_DIMENSIONS];
        Color.colorToHSV(fillColor, defaultFillColorHSVValues);
        final int pressedFillColor = Color.HSVToColor(
                Color.alpha(fillColor),
                new float[]{
                        defaultFillColorHSVValues[0],
                        defaultFillColorHSVValues[1],
                        max(defaultFillColorHSVValues[2] - 0.1f, 0f)
                });

        final Drawable pressedDrawable = getStaticButtonBackgroundDrawable(
                pressedFillColor, borderColor, borderWidthPx, cornerRadiusPx);

        final StateListDrawable result = new StateListDrawable();
        result.addState(new int[] {android.R.attr.state_pressed}, pressedDrawable);
        result.addState(new int[] {android.R.attr.state_enabled}, defaultDrawable);

        return result;
    }

    @NonNull
    private Drawable getStaticButtonBackgroundDrawable(
            @ColorInt final int fillColor,
            @ColorInt final int borderColor,
            final int borderWidthPx,
            final int cornerRadiusPx) {

        final ShapeDrawable borderDrawable = new ShapeDrawable(
                getRoundRectShapeWithOuterCornerRadiusPx(cornerRadiusPx));

        borderDrawable.getPaint().setColor(borderColor);

        final ShapeDrawable fillDrawable = new ShapeDrawable(
                getRoundRectShapeWithOuterCornerRadiusPx(cornerRadiusPx - borderWidthPx));

        fillDrawable.getPaint().setColor(fillColor);

        final LayerDrawable result
                = new LayerDrawable(new Drawable[] {borderDrawable, fillDrawable});

        result.setLayerInset(1, borderWidthPx, borderWidthPx, borderWidthPx, borderWidthPx);

        return result;
    }

    @NonNull
    private RoundRectShape getRoundRectShapeWithOuterCornerRadiusPx(@Px final int rPx) {
        return new RoundRectShape(
                new float[] {rPx, rPx, rPx, rPx, rPx, rPx, rPx, rPx}, null, null);
    }

}
