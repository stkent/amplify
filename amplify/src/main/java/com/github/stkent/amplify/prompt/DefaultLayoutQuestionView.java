/**
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
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.utils.DisplayUtils;

import static java.lang.Math.floor;
import static java.lang.Math.max;

@SuppressLint("ViewConstructor")
final class DefaultLayoutQuestionView extends CustomLayoutQuestionView {

    private static final int NUMBER_OF_COLOR_DIMENSIONS = 3;

    DefaultLayoutQuestionView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_question_view);

        setBackgroundColor(config.getFillColor());

        getTitleTextView().setTextColor(config.getTitleTextColor());

        if (getSubtitleTextView() != null) {
            getSubtitleTextView().setTextColor(config.getSubtitleTextColor());
        }

        setButtonViewBackground(
                getPositiveButton(),
                config.getPositiveButtonBackgroundColor(),
                config.getPositiveButtonBorderColor());

        setButtonViewBackground(
                getNegativeButton(),
                config.getNegativeButtonBackgroundColor(),
                config.getNegativeButtonBorderColor());

        setQuoteButtonUnquoteTextColor(getPositiveButton(), config.getPositiveButtonTextColor());
        setQuoteButtonUnquoteTextColor(getNegativeButton(), config.getNegativeButtonTextColor());
    }

    /**
     * We are defensive here, because it's not uncommon to make "buttons" out of UI components like
     * FrameLayouts, say. If we can't cast to a TextView to obtain a setText method, the button text
     * will be left unchanged from the original layout.
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

    private void setButtonViewBackground(
            @NonNull final View button,
            @ColorInt final int fillColor,
            @ColorInt final int borderColor) {

        final Drawable buttonBackgroundDrawable
                = getButtonBackgroundDrawable(fillColor, borderColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackground(buttonBackgroundDrawable);
        } else {
            button.setBackgroundDrawable(buttonBackgroundDrawable);
        }
    }

    @NonNull
    private Drawable getButtonBackgroundDrawable(
            @ColorInt final int fillColor,
            @ColorInt final int borderColor) {

        final Drawable defaultDrawable = getStaticButtonBackgroundDrawable(
                fillColor, borderColor);

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
                pressedFillColor, borderColor);

        final StateListDrawable result = new StateListDrawable();
        result.addState(new int[] {android.R.attr.state_pressed}, pressedDrawable);
        result.addState(new int[] {android.R.attr.state_enabled}, defaultDrawable);

        return result;
    }

    @NonNull
    private Drawable getStaticButtonBackgroundDrawable(
            @ColorInt final int fillColor,
            @ColorInt final int borderColor) {

        final ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
        borderDrawable.getPaint().setColor(borderColor);

        final ShapeDrawable fillDrawable = new ShapeDrawable(new RectShape());
        fillDrawable.getPaint().setColor(fillColor);

        final LayerDrawable result
                = new LayerDrawable(new Drawable[] {borderDrawable, fillDrawable});

        // todo: make this width configurable
        final int borderWidthPx = (int) floor(DisplayUtils.dpToPx(getContext(), 2));
        result.setLayerInset(1, borderWidthPx, borderWidthPx, borderWidthPx, borderWidthPx);

        return result;
    }

}
