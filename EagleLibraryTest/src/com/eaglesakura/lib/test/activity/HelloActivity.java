package com.eaglesakura.lib.test.activity;

import android.os.Bundle;
import android.widget.TextView;

/**
 *
 */
public class HelloActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hello Activity!!");
        setContentView(tv);
    }
}
