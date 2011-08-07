/**
 *
 */
package com.eaglesakura.lib.test.activity;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.os.Handler;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.mqo.Figure;
import com.eaglesakura.lib.android.thread.ILoopManager;
import com.eaglesakura.lib.android.thread.ILooper;
import com.eaglesakura.lib.android.thread.LooperHandler;
import com.eaglesakura.lib.android.view.OpenGLView;
import com.eaglesakura.lib.mqo.MQOFigure;
import com.eaglesakura.lib.mqo.MQOFormatImporter;
import com.eaglesakura.lib.util.EagleUtil;

/**
 *
 */
public class LineDrawing extends BaseActivity {
    OpenGLView glView = null;
    ILooper looper = null;
    ILoopManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glView = new OpenGLView(this);
        looper = new ILooper() {
            Figure figure = null;

            @Override
            public void onFinalize() {
            }

            @Override
            public void onInitialize() {
                glView.getGLManager().initGL();

                try {
                    InputStream is = getAssets().open("codeobj.mqo");
                    MQOFormatImporter mqo = new MQOFormatImporter(is);
                    is.close();

                    MQOFigure mqoFigure = mqo.convertFigure();
                    figure = new Figure(glView.getGLManager(), mqoFigure);
                } catch (Exception e) {
                    EagleUtil.log(e);
                }
            }

            @Override
            public void onLoop() {
                GLManager glManager = glView.getGLManager();

                glManager.clearColorRGBA(0.0f, 1.0f, 1.0f, 1.0f);
                glManager.clear();

                glManager.setColor(1.0f, 0, 0, 1.0f);
                {
                    GL10 gl = glManager.getGL10();
                    float scale = 0.001f;
                    gl.glLoadIdentity();
                    gl.glScalef(scale, scale, scale);
                    figure.draw();
                }
                glManager.swapBuffers();
            }
        };

        manager = new LooperHandler(new Handler(), looper);
        manager.addSurface(glView);
        manager.startLoop();

        setContentView(glView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        manager.loopPause();
        manager.dispose();
    }
}
