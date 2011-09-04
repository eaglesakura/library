package com.eaglesakura.lib.test;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.eaglesakura.lib.android.util.UtilBridgeAndroid;
import com.eaglesakura.lib.util.EagleUtil;

public class Test extends AndroidTestCase {

    @MediumTest
    public void test() {
        EagleUtil.init(new UtilBridgeAndroid("Eagle-TEST"));
        EagleUtil.log("Hello Test!!");
    }
}
