package com.eaglesakura.lib.test.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.eaglesakura.lib.android.device.TouchImageView;
import com.eaglesakura.lib.test.R;

/**
 * マルチタッチ操作可能なImageView
 */
public class ScaleImageViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchImageView tiv = new TouchImageView(this);
        tiv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon));
        setContentView(tiv);
    }
}
