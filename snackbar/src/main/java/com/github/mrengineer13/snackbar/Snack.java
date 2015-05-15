package com.github.mrengineer13.snackbar;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;

class Snack implements Parcelable {

    final String mMessage;

    final SnackBarButtonParams mActionButtonParams;

    final SnackBarButtonParams mCancelButtonParams;

    final Parcelable mToken;

    final short mDuration;

    final ColorStateList mBackgroundColor;

    final int mHeight;

    Snack(String message, SnackBarButtonParams actionButtonParams, SnackBarButtonParams cancelButtonParams,
          Parcelable token, short duration,
          ColorStateList backgroundColor, int height) {
        this.mMessage = message;
        mActionButtonParams = actionButtonParams;
        mCancelButtonParams = cancelButtonParams;
        mToken = token;
        mDuration = duration;
        mBackgroundColor = backgroundColor;
        mHeight = height;
    }
    // reads data from parcel
    Snack(Parcel p) {
        mMessage = p.readString();
        mActionButtonParams = p.readParcelable(p.getClass().getClassLoader());
        mCancelButtonParams = p.readParcelable(p.getClass().getClassLoader());
        mToken = p.readParcelable(p.getClass().getClassLoader());
        mDuration = (short) p.readInt();
        mBackgroundColor = p.readParcelable(p.getClass().getClassLoader());
        mHeight = p.readInt();
    }

    // writes data to parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mMessage);
        out.writeParcelable(mActionButtonParams, 0);
        out.writeParcelable(mCancelButtonParams, 0);
        out.writeParcelable(mToken, 0);
        out.writeInt((int) mDuration);
        out.writeParcelable(mBackgroundColor, 0);
        out.writeInt(mHeight);
    }

    public int describeContents() {
        return 0;
    }

    // creates snack array
    public static final Parcelable.Creator<Snack> CREATOR = new Parcelable.Creator<Snack>() {
        public Snack createFromParcel(Parcel in) {
            return new Snack(in);
        }

        public Snack[] newArray(int size) {
            return new Snack[size];
        }
    };
}
