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

package com.mrengineer13.snackbar.sample;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mrengineer13.about.AboutActivity;
import com.github.mrengineer13.snackbar.SnackBar;


public class SnackBarActivity extends ActionBarActivity
        implements SnackBar.OnMessageClickListener {

    public static final String SAVED_SNACKBAR = "SAVED_SNACKBAR";

    static final int SHORT_MSG = 0, LONG_MSG = 1;

    static final int SHORT_SNACK = 0, MED_SNACK = 1, LONG_SNACK = 2;

    static final int DEFAULT = 0, ALERT = 1, CONFIRM = 2, INFO = 3;

    static final int BTN_TEXT_AND_ICON = 0, BTN_ICON_ONLY = 1, BTN_TEXT_ONLY = 2,  BTN_HIDE = 3;

    static final int STRING_TYPE_STRING = 0;

    static final int CANCEL_BTN = 0;

    private static final int SHOW_ON_ACTIVITY = 0;

    private Spinner mMsgLengthOptions, mDurationOptions, mActionBtnOptions, mCancelBtnOptions,
            mActionBtnColorOptions, mBGColorOptions, mStringTypeOptions, mShowOnOptions;
    private SnackBar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_bar);

        mMsgLengthOptions = (Spinner) findViewById(R.id.message_length_selector);
        mDurationOptions = (Spinner) findViewById(R.id.snack_duration_selector);
        mActionBtnOptions = (Spinner) findViewById(R.id.action_btn_presence_selector);
        mCancelBtnOptions = (Spinner) findViewById(R.id.cancel_btn);
        mActionBtnColorOptions = (Spinner) findViewById(R.id.action_btn_color);
        mBGColorOptions = (Spinner) findViewById(R.id.bg_color);
        mStringTypeOptions = (Spinner) findViewById(R.id.action_btn_string_type);
        mShowOnOptions = (Spinner) findViewById(R.id.show_on);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snack_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(AboutActivity.getAboutActivityIntent(this, "MrEngineer13", "https://github.com/MrEngineer13",
                    "MrEngineer13@live.com", "@MrEngineer13",
                    "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=K6PSQMTJYG5VJ",
                    true, "MrEngineer13", null));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMessage(SnackBar.Builder snackBarBuilder){
        String message;
        int messageRes;

        int selectedStringType = mStringTypeOptions.getSelectedItemPosition();
        int selectedMessageLength = mMsgLengthOptions.getSelectedItemPosition();

        switch (selectedMessageLength) {
            case SHORT_MSG:
                if (selectedStringType == STRING_TYPE_STRING) {
                    message = "This is a one-line message.";
                    snackBarBuilder.withMessage(message);
                } else {
                    messageRes = R.string.short_message;
                    snackBarBuilder.withMessageId(messageRes);
                }
                break;
            case LONG_MSG:
                if (selectedStringType == STRING_TYPE_STRING) {
                    message = "This message is a lot longer, it should stretch at least two lines. ";
                    snackBarBuilder.withMessage(message);
                } else {
                    messageRes = R.string.long_message;
                    snackBarBuilder.withMessageId(messageRes);
                }
                break;
        }
    }

    public void onCreateClicked(View view) {
        SnackBar.Builder snackBarBuilder;
        short duration = 0;
        SnackBar.Style style;
        int bgColor;

        int showOnIndex = mShowOnOptions.getSelectedItemPosition();
        if(showOnIndex == SHOW_ON_ACTIVITY) {
            snackBarBuilder = new SnackBar.Builder(this);
        }else {
            snackBarBuilder = new SnackBar.Builder(this.getApplicationContext());
        }

        setMessage(snackBarBuilder);

        int selectedDuration = mDurationOptions.getSelectedItemPosition();
        switch (selectedDuration) {
            case SHORT_SNACK:
                duration = SnackBar.SHORT_SNACK;
                break;
            case MED_SNACK:
                duration = SnackBar.MED_SNACK;
                break;
            case LONG_SNACK:
                duration = SnackBar.LONG_SNACK;
                break;
        }

        snackBarBuilder.withDuration(duration);

        int selectedActionBtnColor = mActionBtnColorOptions.getSelectedItemPosition();
        switch (selectedActionBtnColor) {
            default:
            case DEFAULT:
                style = SnackBar.Style.DEFAULT;
                break;
            case ALERT:
                style = SnackBar.Style.ALERT;
                break;
            case CONFIRM:
                style = SnackBar.Style.CONFIRM;
                break;
            case INFO:
                style = SnackBar.Style.INFO;
                break;
        }

        snackBarBuilder.withActionButtonStyle(style);

        int selectedBGColor = mBGColorOptions.getSelectedItemPosition();
        switch (selectedBGColor) {
            default:
            case DEFAULT:
                bgColor = R.color.sb__snack_bkgnd;
                break;
            case ALERT:
                bgColor = R.color.sb__snack_alert_bkgnd;
                break;
        }

        snackBarBuilder.withBackgroundColorId(bgColor);

        int selectedActionBtnExistence = mActionBtnOptions.getSelectedItemPosition();

        switch (selectedActionBtnExistence){
            case BTN_TEXT_AND_ICON:
                snackBarBuilder.withActionMessage("Action");
            case BTN_ICON_ONLY:
                snackBarBuilder.withActionIconId(R.drawable.ic_done_white_24dp);
                break;
            case BTN_TEXT_ONLY:
                snackBarBuilder.withActionMessage("Action");
            case BTN_HIDE:
                break;
        }

        int selectedCancelBtnExistence = mCancelBtnOptions.getSelectedItemPosition();
        switch (selectedCancelBtnExistence){
            case BTN_TEXT_AND_ICON:
                snackBarBuilder.withCancelMessage("Cancel");
            case BTN_ICON_ONLY:
                snackBarBuilder.withCancelIconId(R.drawable.ic_close_white_24dp);
                break;
            case BTN_TEXT_ONLY:
                snackBarBuilder.withCancelMessage("Cancel");
            case BTN_HIDE:
                break;
        }

        snackBarBuilder.withOnCancelClickListener(new SnackBar.OnCancelClickListener() {
            @Override
            public void onCancelClick() {
                Toast.makeText(getApplicationContext(), "Cancel Button Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mSnackBar = snackBarBuilder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        if (mSnackBar != null) {
            saveState.putBundle(SAVED_SNACKBAR, mSnackBar.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle loadState) {
        super.onRestoreInstanceState(loadState);
        if (mSnackBar != null) {
            mSnackBar.onRestoreInstanceState(loadState.getBundle(SAVED_SNACKBAR));
        }
    }

    @Override
    public void onMessageClick(Parcelable token) {
        Toast.makeText(this, "Button clicked!", Toast.LENGTH_LONG).show();
    }
}
