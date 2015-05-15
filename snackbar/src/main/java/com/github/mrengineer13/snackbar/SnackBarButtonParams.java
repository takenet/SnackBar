package com.github.mrengineer13.snackbar;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;

class SnackBarButtonParams implements Parcelable{
    String mTitle;
    int mIcon = 0;
    ColorStateList mTextColor;

    SnackBarButtonParams(ColorStateList mTextColor){

    }

    SnackBarButtonParams(Parcel p){
        mTitle = p.readString();
        mIcon = p.readInt();
        mTextColor = p.readParcelable(p.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeInt(mIcon);
        out.writeParcelable(mTextColor, 0);
    }

    // creates snack array
    public static final Parcelable.Creator<SnackBarButtonParams> CREATOR = new Parcelable.Creator<SnackBarButtonParams>() {
        public SnackBarButtonParams createFromParcel(Parcel in) {
            return new SnackBarButtonParams(in);
        }

        public SnackBarButtonParams[] newArray(int size) {
            return new SnackBarButtonParams[size];
        }
    };
}
