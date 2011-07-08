/**
 *
 * @author eagle.sakura
 * @version 2010/09/17 : 新規作成
 */
package eagle.android.gles11;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;
import eagle.android.graphic.Graphics;

/**
 * 書き換え型のテクスチャを提供する。
 *
 * @author eagle.sakura
 * @version 2010/09/17 : 新規作成
 */
public class RewritingTexture extends ITexture {

    private Graphics graphics = null;
    private Bitmap bitmap = null;

    /**
     * プログラム中で書き換えを目的としたテクスチャを作成する。<BR>
     * 内部形式はARGB8888固定となる。
     *
     * @author eagle.sakura
     * @param w
     * @param h
     * @param glMgr
     * @version 2010/09/17 : 新規作成
     */
    public RewritingTexture(int w, int h, GLManager glMgr) {
        super(glMgr);
        bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        graphics = new Graphics();
        graphics.setCanvas(new Canvas(bitmap));
    }

    /**
     * 内部描画を開始する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/17 : 新規作成
     */
    public Graphics lock() {
        return graphics;
    }

    /**
     * 描画を終了し、テクスチャ化する。
     *
     * @author eagle.sakura
     * @version 2010/09/17 : 新規作成
     */
    public void unlock() {
        GL11 gl = getGLManager().getGL();

        // ! テクスチャ位置を指定。
        gl.glBindTexture(GL10.GL_TEXTURE_2D, getTextureID());

        // ! テクスチャ属性指定。
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        // ! テクスチャを指定。
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // ! 念のためテクスチャ解除
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl.glDisableClientState(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    /**
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public int getType() {
        return eTypeUnknown;
    }

    /**
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

}
