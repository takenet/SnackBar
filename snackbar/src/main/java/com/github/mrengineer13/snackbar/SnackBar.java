/*
 * Copyright (c) 2014 MrEngineer13
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mrengineer13.snackbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SnackBar {

    public static final short LONG_SNACK = 5000;

    public static final short MED_SNACK = 3500;

    public static final short SHORT_SNACK = 2000;

    public static final short PERMANENT_SNACK = 0;

    private SnackContainer mSnackContainer;

    private View mParentView;

    private OnMessageClickListener mClickListener;

    private OnCancelClickListener mCancelClickListener;

    private OnVisibilityChangeListener mVisibilityChangeListener;

    public interface OnCancelClickListener {

        void onCancelClick();
    }

    public interface OnMessageClickListener {

        void onMessageClick(Parcelable token);
    }

    public interface OnVisibilityChangeListener {

        /**
         * Gets called when a message is shown
         *
         * @param stackSize the number of messages left to show
         */
        void onShow(int stackSize);

        /**
         * Gets called when a message is hidden
         *
         * @param stackSize the number of messages left to show
         */
        void onHide(int stackSize);
    }

    public SnackBar(Activity activity) {
        ViewGroup container = (ViewGroup) activity.findViewById(android.R.id.content);
        View v = activity.getLayoutInflater().inflate(R.layout.sb__snack, container, false);
        init(container, v);
    }

    public SnackBar(Context context, View v) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sb__snack_container, ((ViewGroup) v));
        View snackLayout = inflater.inflate(R.layout.sb__snack, ((ViewGroup) v), false);
        init((ViewGroup) v, snackLayout);
    }

    private void init(ViewGroup container, View v) {
        mSnackContainer = (SnackContainer) container.findViewById(R.id.snackContainer);
        if (mSnackContainer == null) {
            mSnackContainer = new SnackContainer(container);
        }

        mParentView = v;
        TextView snackBtn = (TextView) v.findViewById(R.id.snackButton);
        snackBtn.setOnClickListener(mButtonListener);

        TextView snackCancelBtn = (TextView) v.findViewById(R.id.snackCancelButton);
        snackCancelBtn.setOnClickListener(mCancelListener);
    }

    public static class Builder {

        private SnackBar mSnackBar;
        private Context mContext;
        private final SnackBarButtonParams mActionButtonParams;
        private final SnackBarButtonParams mCancelButtonParams;
        private String mMessage;
        private Parcelable mToken;
        private short mDuration = MED_SNACK;
        private ColorStateList mBackgroundColor;
        private int mHeight;

        /**
         * Constructs a new SnackBar
         *
         * @param activity the activity to inflate into
         */
        public Builder(Activity activity) {
            mContext = activity.getApplicationContext();
            mSnackBar = new SnackBar(activity);
            mActionButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
            mCancelButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
        }

        private View createSnackBarRootView() {
            WindowManager windowManager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);

            LinearLayout rootView = new LinearLayout(mContext);

            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.BOTTOM;
            windowManager.addView(rootView, params);

            return rootView;
        }

        /**
         * Constructs a new SnackBar, in a floating View
         * This view will be on top of any window
         *
         * @param context the context used to obtain resources
         */
        public Builder(Context context){
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            if (pm.checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, packageName) !=
                    PackageManager.PERMISSION_GRANTED){
                throw new RuntimeException("You must added SYSTEM_ALERT_WINDOW manifest to use this" +
                        "constructor or use a constructor with an Activity or View");
            }
            mContext = context;
            mSnackBar = new SnackBar(context, createSnackBarRootView());
            mActionButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
            mCancelButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
        }

        /**
         * Constructs a new SnackBar
         *
         * @param context the context used to obtain resources
         * @param v the view to inflate the SnackBar into
         */
        public Builder(Context context, View v) {
            mContext = context;
            mSnackBar = new SnackBar(context, v);
            mActionButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
            mCancelButtonParams = new SnackBarButtonParams(getActionTextColor(Style.DEFAULT));
        }

        /**
         * Sets the message to display on the SnackBar
         *
         * @param message the literal string to display
         * @return this builder
         */
        public Builder withMessage(String message) {
            mMessage = message;
            return this;
        }

        /**
         * Sets the message to display on the SnackBar
         *
         * @param messageId the resource id of the string to display
         * @return this builder
         */
        public Builder withMessageId(int messageId) {
            mMessage = mContext.getString(messageId);
            return this;
        }

        /**
         * Sets the message to display as the action message
         *
         * @param actionMessage the literal string to display
         * @return this builder
         */
        public Builder withActionMessage(String actionMessage) {
            mActionButtonParams.mTitle = actionMessage;
            return this;
        }

        /**
         * Sets the message to display as the cancel message
         *
         * @param cancelMessage the literal string to display
         * @return this builder
         */
        public Builder withCancelMessage(String cancelMessage) {
            mCancelButtonParams.mTitle = cancelMessage;
            return this;
        }

        /**
         * Sets the message to display as the action message
         *
         * @param actionMessageResId the resource id of the string to display
         * @return this builder
         */
        public Builder withActionMessageId(int actionMessageResId) {
            if (actionMessageResId > 0) {
                mActionButtonParams.mTitle = mContext.getString(actionMessageResId);
            }

            return this;
        }

        /**
         * Sets the message to display as the action message
         *
         * @param cancelMessageResId the resource id of the string to display
         * @return this builder
         */
        public Builder withCancelMessageId(int cancelMessageResId) {
            if (cancelMessageResId > 0) {
                mCancelButtonParams.mTitle = mContext.getString(cancelMessageResId);
            }
            return this;
        }

        /**
         * Sets the action icon
         *
         * @param id the resource id of the icon to display
         * @return this builder
         */
        public Builder withActionIconId(int id) {
            mActionButtonParams.mIcon = id;
            return this;
        }

        /**
         * Sets the cancel icon
         *
         * @param id the resource id of the icon to display
         * @return this builder
         */
        public Builder withCancelIconId(int id) {
            mCancelButtonParams.mIcon = id;
            return this;
        }

        /**
         * Sets the {@link com.github.mrengineer13.snackbar.SnackBar.Style} for the action message
         *
         * @param style the {@link com.github.mrengineer13.snackbar.SnackBar.Style} to use
         * @return this builder
         */
        public Builder withActionButtonStyle(Style style) {
            mActionButtonParams.mTextColor = getActionTextColor(style);
            return this;
        }

        /**
         * Sets the {@link com.github.mrengineer13.snackbar.SnackBar.Style} for the cancel message
         *
         * @param style the {@link com.github.mrengineer13.snackbar.SnackBar.Style} to use
         * @return this builder
         */
        public Builder withCancelButtonStyle(Style style) {
            mCancelButtonParams.mTextColor = getActionTextColor(style);
            return this;
        }

        /**
         * The token used to restore the SnackBar state
         *
         * @param token the parcelable containing the saved SnackBar
         * @return this builder
         */
        public Builder withToken(Parcelable token) {
            mToken = token;
            return this;
        }

        /**
         * Sets the duration to show the message
         *
         * @param duration the number of milliseconds to show the message
         * @return this builder
         */
        public Builder withDuration(Short duration) {
            mDuration = duration;
            return this;
        }

        /**
         * Sets the {@link android.content.res.ColorStateList} for the action message
         *
         * @param colorId the
         * @return this builder
         */
        public Builder withTextColorId(int colorId) {
            mActionButtonParams.mTextColor = mContext.getResources().getColorStateList(colorId);
            return this;
        }

        /**
         * Sets the {@link android.content.res.ColorStateList} for the cancel message
         *
         * @param colorId the
         * @return this builder
         */
        public Builder withCancelColorId(int colorId) {
            mCancelButtonParams.mTextColor = mContext.getResources().getColorStateList(colorId);
            return this;
        }

        /**
         * Sets the {@link android.content.res.ColorStateList} for the SnackBar background
         *
         * @param colorId the SnackBar Background color
         * @return this builder
         */
        public Builder withBackgroundColorId(int colorId) {
            mBackgroundColor = mContext.getResources().getColorStateList(colorId);
            return this;
        }

        /**
         * Sets the height for SnackBar
         *
         * @param height the height of SnackBar
         * @return this builder
         */
        public Builder withSnackBarHeight(int height) {
            mHeight = height;
            return this;
        }

        /**
         * Sets the OnClickListener for the action button
         *
         * @param onClickListener the listener to inform of click events
         * @return this builder
         */
        public Builder withOnClickListener(OnMessageClickListener onClickListener) {
            mSnackBar.setOnClickListener(onClickListener);
            return this;
        }

        /**
         * Sets the OnClickListener for the action button
         *
         * @param cancelClickListener the listener to inform of click events
         * @return this builder
         */
        public Builder withOnCancelClickListener(OnCancelClickListener cancelClickListener) {
            mSnackBar.setOnCancelClickListener(cancelClickListener);
            return this;
        }

        /**
         * Sets the visibilityChangeListener for the SnackBar
         *
         * @param visibilityChangeListener the listener to inform of visibility changes
         * @return this builder
         */
        public Builder withVisibilityChangeListener(OnVisibilityChangeListener visibilityChangeListener) {
            mSnackBar.setOnVisibilityChangeListener(visibilityChangeListener);
            return this;
        }

        /**
         * Shows the first message in the SnackBar
         *
         * @return the SnackBar
         */
        public SnackBar show() {
            Snack message = new Snack(mMessage,
                    mActionButtonParams,
                    mCancelButtonParams,
                    mToken,
                    mDuration,
                    mBackgroundColor != null ? mBackgroundColor : mContext.getResources().getColorStateList(R.color.sb__snack_bkgnd),
                    mHeight != 0 ? mHeight : 0);

            mSnackBar.showMessage(message);

            return mSnackBar;
        }

        private ColorStateList getActionTextColor(Style style) {
            switch (style) {
                case ALERT:
                    return mContext.getResources().getColorStateList(R.color.sb__button_text_color_red);
                case INFO:
                    return mContext.getResources().getColorStateList(R.color.sb__button_text_color_yellow);
                case CONFIRM:
                    return mContext.getResources().getColorStateList(R.color.sb__button_text_color_green);
                case DEFAULT:
                    return mContext.getResources().getColorStateList(R.color.sb__default_button_text_color);
                default:
                    return mContext.getResources().getColorStateList(R.color.sb__default_button_text_color);
            }
        }
    }

    private void showMessage(Snack message) {
        mSnackContainer.showSnack(message, mParentView, mVisibilityChangeListener);
    }

    /**
     * Calculates the height of the SnackBar
     *
     * @return the height of the SnackBar
     */
    public int getHeight() {
        mParentView.measure(View.MeasureSpec.makeMeasureSpec(mParentView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(mParentView.getHeight(), View.MeasureSpec.AT_MOST));
        return mParentView.getMeasuredHeight();
    }

    /**
     * Getter for the SnackBars parent view
     *
     * @return the parent view
     */
    public View getContainerView() {
        return mParentView;
    }


    private final View.OnClickListener mCance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mClickListener != null && mSnackContainer.isShowing()) {
                mClickListener.onMessageClick(mSnackContainer.peek().mToken);
            }
            mSnackContainer.hide();
        }
    };


    private final View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCancelClickListener != null && mSnackContainer.isShowing()) {
                mCancelClickListener.onCancelClick();
            }
            mSnackContainer.hide();
        }
    };

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mClickListener != null && mSnackContainer.isShowing()) {
                mClickListener.onMessageClick(mSnackContainer.peek().mToken);
            }
            mSnackContainer.hide();
        }
    };

    private SnackBar setOnCancelClickListener(OnCancelClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    private SnackBar setOnClickListener(OnMessageClickListener listener) {
        mClickListener = listener;
        return this;
    }

    private SnackBar setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        mVisibilityChangeListener = listener;
        return this;
    }

    /**
     * Clears all of the queued messages
     *
     * @param animate whether or not to animate the messages being hidden
     */
    public void clear(boolean animate) {
        mSnackContainer.clearSnacks(animate);
    }

    /**
     * Clears all of the queued messages
     *
     */
    public void clear() {
        clear(true);
    }

    /**
     * Hides all snacks
     *
     */
    public void hide() {
        mSnackContainer.hide();
        clear();
    }


    /**
     * All snacks will be restored using the view from this Snackbar
     */
    public void onRestoreInstanceState(Bundle state) {
        mSnackContainer.restoreState(state, mParentView);
    }

    public Bundle onSaveInstanceState() {
        return mSnackContainer.saveState();
    }

    public enum Style {
        DEFAULT,
        ALERT,
        CONFIRM,
        INFO
    }
}
