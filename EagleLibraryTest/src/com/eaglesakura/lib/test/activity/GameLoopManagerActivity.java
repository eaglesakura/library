package com.eaglesakura.lib.test.activity;

import android.os.Bundle;

import com.eaglesakura.lib.android.appcore.GameLoopManager;
import com.eaglesakura.lib.util.EagleUtil;

public class GameLoopManagerActivity extends BaseActivity {
    GameLoopManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new GameLoopManager(this, new GameLoopManager.ILoopParent() {

            @Override
            public boolean isFinished() {
                return isFinishing();
            }
        }) {

            int frame = 0;

            @Override
            public void onGameFinalize() {

            }

            @Override
            public void onGameInitialize() {
                EagleUtil.log("onInitialize");
                getGLManager().initGL();
            }

            @Override
            public void onGameFrame() {
                EagleUtil.log("onGameFrame : " + frame++);
                getGLManager().clearTest();
            }
        };

        setContentView(manager.getRootView());
        manager.setFrameRateParSec(30);
        manager.startGameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }
}
