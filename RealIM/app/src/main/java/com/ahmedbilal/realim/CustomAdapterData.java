package com.ahmedbilal.realim;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Ashi_3 on 5/3/2015.
 */
public class CustomAdapterData {

    private String userName;
    private String message;

    private Bitmap imageBitmap;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
