package com.eaglesakura.lib.android.gles11;

import android.graphics.Rect;

import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * テクスチャ画像の一部をスプライトとして扱うクラス。
 */
public class TextureSprite {
    ITexture texture = null;
    Rect area = new Rect();
    int color = 0xffffffff;
    Vector2 size = new Vector2();

    /**
     *
     * @param texture
     * @param area
     */
    public TextureSprite(ITexture texture, Rect area) {
        this.texture = texture;
        setTextureArea(area);
        setDrawSize(area.width(), area.height());
    }

    /**
     * 描画エリアを再指定する。
     * @param area
     */
    public void setTextureArea(Rect area) {
        this.area.set(area);
    }

    public int getTextureAreaWidth() {
        return area.width();
    }

    public int getTextureAreaHeight() {
        return area.height();
    }

    public void setDrawSize(int width, int height) {
        size.set(width, height);
    }

    public ITexture getTexture() {
        return texture;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b, float a) {
        color = EagleUtil.toColorRGBA((int) (255.0f * r), (int) (255.0f * g), (int) (255.0f * b), (int) (255.0f * a));
    }

    /**
     * Xを中心に描画する。
     */
    public static final int eDrawFlagCenterX = 0x00000001;

    /**
     * Yを中心に描画する。
     */
    public static final int eDrawFlagCenterY = 0x00000002;

    /**
     * 描画を行う。
     * @param gl
     * @param x
     * @param y
     * @param degree
     * @param flags
     */
    public void draw(GLManager gl, int x, int y, float degree, int flags) {
        //! 中央？
        if ((flags & eDrawFlagCenterX) == eDrawFlagCenterX) {
            x -= (size.x / 2);
        }
        //! 中央？
        if ((flags & eDrawFlagCenterY) == eDrawFlagCenterY) {
            y -= (size.y / 2);
        }
        gl.drawImage(x, y, (int) size.x, (int) size.y, degree, color, texture, area.left, area.top, area.width(), area.height());
    }
}
