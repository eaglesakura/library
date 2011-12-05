package com.eaglesakura.lib.test.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.eaglesakura.lib.android.test.activity.BaseActivity;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * Handlerによるマルチスレッド化を確認する。
 */
public class HandlerTestActivity extends BaseActivity {
    Handler handler = null;
    Handler uiHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHandler = new Handler(Looper.getMainLooper());
        final TextView tv = new TextView(getBaseContext());
        setContentView(tv);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(getActivity(), "Toast : thread", Toast.LENGTH_SHORT).show();
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        EagleUtil.log("another thread");
                        uiHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                tv.setText("Text : " + System.currentTimeMillis());
                            }
                        });

                        if (!isFinishing()) {
                            handler.postDelayed(this, 100);
                        }
                    }
                });

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Toast : uiHandler", Toast.LENGTH_SHORT).show();
                    }
                });

                EagleUtil.log("loop start");
                Looper.loop();
                EagleUtil.log("loop end");
            }
        }.start();

    }
}
