package com.eaglesakura.lib;

import android.test.AndroidTestCase;

import com.eaglesakura.lib.android.util.UtilBridgeAndroid;
import com.eaglesakura.lib.util.EagleUtil;

public class BaseTestCase extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        EagleUtil.init(new UtilBridgeAndroid("EAGLE-TEST"));
    }

    protected void log(String msg) {
        EagleUtil.log(msg);
    }
}
