package com.eaglesakura.lib.android.game;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Rect;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.gles11.ITexture;
import com.eaglesakura.lib.android.gles11.TextureSprite;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * アニメーションテクスチャを指定する。
 */
public class AnimatedTexture extends ITexture {
    List<AnimationFrame> frames = new ArrayList<AnimationFrame>();

    /**
     * フレームが終端まで来た場合に戻るコマ数
     */
    int endOffset = 0;

    /**
     * 1コマでのフレーム数
     */
    int comaFrame = 1;

    /**
     * 現在のフレーム
     */
    int frame = 0;

    /**
     * 1フレームでの増量。
     */
    int frameOffset = 1;

    /**
     *
     * @param gl
     */
    public AnimatedTexture(GLManager gl) {
        super(null);
        glManager = gl;
    }

    /**
     * 次のフレームを描画する。
     */
    public void nextFrame() {
        frame += frameOffset;

        int current = frame / comaFrame;
        if (current >= frames.size()) {
            frame += (comaFrame * endOffset);

        }

        //! 上限・下限の指定する
        frame = EagleUtil.minmax(0, (frames.size()) * comaFrame, frame);
    }

    /**
     * 現在のフレームを取得する
     * @return
     */
    public AnimationFrame getCurrentFrame() {
        int current = frame / comaFrame;

        return frames.get(EagleUtil.minmax(0, frames.size() - 1, current));
    }

    /**
     * テクスチャ情報を取得する。
     * @param result
     * @return
     */
    public TextureSprite getTexture(TextureSprite result) {
        AnimationFrame current = getCurrentFrame();
        result.setTextureArea(current.area);
        result.setTexture(current.texture);
        return result;
    }

    /**
     * フレームを追加する。
     * @param frame
     */
    public void addAnimationFrame(AnimationFrame frame) {
        frames.add(frame);
    }

    /**
     * フレームを追加する。
     * @param texture
     */
    public void addAnimationFrame(ITexture texture) {
        AnimationFrame frame = new AnimationFrame();
        frame.texture = texture;
        frame.area = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        addAnimationFrame(frame);
    }

    /**
     * フレームを追加する。
     * @param texture
     * @param area
     */
    public void addAnimationFrame(ITexture texture, Rect area) {
        AnimationFrame frame = new AnimationFrame();
        frame.texture = texture;
        frame.area = area;
        addAnimationFrame(frame);
    }

    /**
     * 終端に来た場合のループコマ数を指定する。
     * @param endOffset
     */
    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    /**
     * テクスチャ幅を取得する。
     */
    @Override
    public int getWidth() {
        return getCurrentFrame().area.width();
    }

    /**
     * テクスチャの高さを取得する。
     */
    @Override
    public int getHeight() {
        return getCurrentFrame().area.height();
    }

    /**
     * アニメーションの遷移レベルを取得する。
     * @return
     */
    public float getAnimationLevel() {
        float maxFrame = comaFrame * frames.size();

        return EagleUtil.minmax(0, 1, (float) frame / maxFrame);
    }

    /**
     * アニメーションが終了していたらtrueを返す。
     * @return
     */
    public boolean isAnimationFinish() {
        return getAnimationLevel() == 1.0f;
    }

    /**
     * 現在のフレームを取得する。
     * @return
     */
    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public void update() {
        super.update();
        nextFrame();
    }

    /**
     * テクスチャをバインドし、UVの情報をリセットする。
     * @param poly
     * @param glManager
     */
    @Override
    public void bind() {
        AnimationFrame frame = getCurrentFrame();
        GL10 gl = glManager.getGL10();
        frame.texture.bind();

        //! 行列情報を書き換える
        {
            gl.glMatrixMode(GL10.GL_TEXTURE);
            gl.glPushMatrix();
            int tw = frame.area.width();
            int th = frame.area.height();
            int tx = frame.area.left;
            int ty = frame.area.top;
            ITexture tex = frame.texture;
            float sizeX = (float) tw / (float) tex.getWidth();
            float sizeY = (float) th / (float) tex.getHeight();
            float sx = (float) tx / (float) tex.getWidth();
            float sy = (float) ty / (float) tex.getHeight();

            gl.glTranslatef(sx, sy, 0.0f);
            gl.glScalef(sizeX, sizeY, 1.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
        }
    }

    /**
     * テクスチャ情報をアンバインドする。
     * @param glManager
     */
    @Override
    public void unbind() {
        AnimationFrame frame = getCurrentFrame();
        GL10 gl = glManager.getGL10();

        frame.texture.unbind();

        //! 行列情報を書き換える
        {
            gl.glMatrixMode(GL10.GL_TEXTURE);
            gl.glPopMatrix();
            gl.glMatrixMode(GL10.GL_MODELVIEW);
        }
    }

    /**
     * 1フレームを示す。
     */
    public static class AnimationFrame {
        public ITexture texture = null;
        public Rect area = null;
    };
}
