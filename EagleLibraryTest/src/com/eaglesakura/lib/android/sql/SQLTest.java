package com.eaglesakura.lib.android.sql;

import java.io.File;

import android.os.Environment;

import com.eaglesakura.lib.BaseTestCase;
import com.eaglesakura.lib.util.EagleUtil;

public class SQLTest extends BaseTestCase {

    public void hello() {
        EagleUtil.log("hello!! SQL Test");
    }

    /**
     * データベースファイルを取得する。
     * @return
     */
    public File getSQLFile() {
        return new File(Environment.getExternalStorageDirectory(), "sqltest.db");
    }
}
