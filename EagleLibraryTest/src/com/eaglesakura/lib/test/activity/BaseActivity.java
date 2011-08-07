package com.eaglesakura.lib.test.activity;

import android.app.Activity;
import android.os.Bundle;

import com.eaglesakura.lib.android.util.UtilBridgeAndroid;
import com.eaglesakura.lib.util.EagleUtil;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EagleUtil.init(new UtilBridgeAndroid("Eagle-TEST"));

        //! Activity名を指定
        {
            String title = "" + this;
            title = title.substring(title.lastIndexOf(".") + 1);
            title = title.substring(0, title.lastIndexOf("@"));
            setTitle(title);
        }
    }

    BaseActivity getActivity() {
        return this;
    }
}
