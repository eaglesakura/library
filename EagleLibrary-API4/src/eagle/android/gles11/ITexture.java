/**
 *
 * @author eagle.sakura
 * @version 2010/04/08 : 新規作成
 */
package eagle.android.gles11;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import eagle.util.Disposable;

/**
 * テクスチャを示す基底クラス。
 *
 * @author eagle.sakura
 * @version 2010/04/08 : 新規作成
 */
public abstract class ITexture implements Disposable {
    /**
     * 不明なテクスチャの型を示す。
     */
    public static final int eTypeUnknown = -1;

    /**
     * RGB各8bit、インデックス8bitのテクスチャを示す。
     */
    public static final int eTypeI8RGB888 = 0;

    /**
     * RGBA各8bit、インデックス8bitのテクスチャを示す。
     */
    public static final int eTypeI8RGBA8888 = 1;

    /**
     * RGB各5bit、アルファ1bit、インデックス8bitのテクスチャを示す。
     */
    public static final int eTypeI8RGBA5551 = 2;

    /**
     * RGB各5bit、アルファ1bit、インデックス4bitのテクスチャを示す。
     */
    public static final int eTypeI4RGBA5551 = 3;

    /**
     * RGBA各8bitのテクスチャを示す。
     */
    public static final int eTypeRGBA8888 = 4;

    /**
     * RGB各8bitのテクスチャを示す。
     */
    public static final int eTypeRGB888 = 5;

    /**
     * テクスチャID。
     */
    private int textureId = -1;

    /**
     * 関連付けられたマネージャ。
     */
    private GLManager glManager = null;

    /**
     *
     * @author eagle.sakura
     * @param gl
     * @version 2010/05/30 : 新規作成
     */
    public ITexture(GLManager gl) {
        glManager = gl;
        int[] n = { -1 };
        gl.getGL().glGenTextures(1, n, 0);
        textureId = n[0];
    }

    /**
     *
     * @author eagle.sakura
     * @throws Throwable
     * @version 2010/07/11 : 新規作成
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            dispose();
        }
    }

    /**
     * 関連付けられたGL管理クラスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    public GLManager getGLManager() {
        return glManager;
    }

    /**
     * テクスチャIDを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    public int getTextureID() {
        return textureId;
    }

    /**
     * テクスチャの種類を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/13 : 新規作成
     * @see ITexture#eTypeUnknown
     * @see ITexture#eTypeI8RGB888
     * @see ITexture#eTypeI8RGBA8888
     * @see ITexture#eTypeI8RGBA5551
     * @see ITexture#eTypeI4RGBA5551
     * @see ITexture#eTypeRGBA8888
     * @see ITexture#eTypeRGB888
     */
    public int getType() {
        return eTypeUnknown;
    }

    /**
     * テクスチャの高さを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/13 : 新規作成
     */
    public abstract int getHeight();

    /**
     * テクスチャの幅を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/13 : 新規作成
     */
    public abstract int getWidth();

    /**
     * @author eagle.sakura
     * @param glMgr
     * @version 2010/05/30 : 新規作成
     */
    public void bind() {
        GL11 gl = glManager.getGL();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, getTextureID());
    }

    /**
     * @author eagle.sakura
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public void dispose() {
        if (textureId == -1) {
            return;
        }
        int n[] = { getTextureID() };
        getGLManager().getGL().glDeleteTextures(1, n, 0);
        textureId = -1;
    }

    public void setScalingFilter(boolean linear) {
        GL10 gl = getGLManager().getGL10();
        int type = linear ? GL10.GL_LINEAR : GL10.GL_NEAREST;
        // ! テクスチャ属性指定。
        bind();
        {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, type);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, type);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, type);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, type);
        }
        unbind();
    }

    /**
     * @author eagle.sakura
     * @param glMgr
     * @version 2010/05/30 : 新規作成
     */
    public void unbind() {
        GL11 gl = glManager.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

}
