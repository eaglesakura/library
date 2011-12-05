package com.eaglesakura.lib.android.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ファイル名、MD5で小規模なファイルをキャッシュするデータベースを構築する。
 * ファイル名はすべて小文字に変換される。
 * @author Takeshi
 *
 */
public class CacheFileDatabase extends SQLiteOpenHelper {

    public CacheFileDatabase(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
