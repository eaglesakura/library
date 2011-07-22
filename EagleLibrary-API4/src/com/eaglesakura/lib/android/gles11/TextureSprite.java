package com.eaglesakura.lib.android.gles11;

import android.graphics.Rect;

/**
 * テクスチャ画像の一部をスプライトとして扱うクラス。
 */
public class TextureSprite {
    ITexture texture = null;
    Rect area = new Rect();
    int color = 0xffffffff;

    /**
     *
     * @param texture
     * @param area
     */
    public TextureSprite(ITexture texture, Rect area) {
        this.texture = texture;
        setArea(area);
    }

    /**
     * 描画エリアを再指定する。
     * @param area
     */
    public void setArea(Rect area) {
        this.area.set(area);
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
            x -= (area.width() / 2);
        }
        //! 中央？
        if ((flags & eDrawFlagCenterY) == eDrawFlagCenterY) {
            y -= (area.height() / 2);
        }
        gl.drawImage(x, y, area.width(), area.height(), degree, color, texture, area.left, area.top, area.width(), area.height());
    }
}
