/**
 *
 * @author eagle.sakura
 * @version 2010/07/10 : 新規作成
 */
package eagle.android.gles11;

import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL11Ext;

/**
 * glext.h内部、{@link GL11Ext}が本来呼び出すべきメソッドの呼出を代行する。<BR> {@link GL11Ext}
 * は強制定期に未対応の例外を投げるため、このクラスにNDKを介した呼出を行わせる。
 *
 * @author eagle.sakura
 * @version 2010/07/10 : 新規作成
 */
public class GL11Extension {
    static {
        System.loadLibrary("eagle_android_gles11_GL11Extension");
    }

    /**
     *
     */
    public static final int GL_MATRIX_PALETTE_OES = 0x8840;

    /**
     *
     */
    public static final int GL_MATRIX_INDEX_ARRAY_OES = 0x8844;

    /**
     *
     */
    public static final int GL_WEIGHT_ARRAY_OES = 0x86AD;

    /**
     * ネイティブのglMatrixModeを呼び出す。
     *
     * @author eagle.sakura
     * @param mode
     * @version 2010/07/10 : 新規作成
     */
    public native void glMatrixMode(int mode);

    /**
     * ネイティブのglCurrentPaletteMatrixOESを呼び出す。<BR>
     * Xperiaは20、HT-03Aは48の行列パレットを使用可能。
     *
     * @author eagle.sakura
     * @param index
     * @version 2010/07/10 : 新規作成
     */
    public native void glCurrentPaletteMatrixOES(int index);

    /**
     * ネイティブのglLoadMatrixxを呼び出す。
     *
     * @author eagle.sakura
     * @param buffer
     * @version 2010/07/10 : 新規作成
     */
    public native void glLoadMatrixx(Buffer buffer);

    /**
     * ネイティブのglLoadMatrixfを呼び出す。
     *
     * @author eagle.sakura
     * @param buffer
     * @version 2010/07/10 : 新規作成
     */
    public native void glLoadMatrixf(Buffer buffer);

    /**
     * ネイティブのglEnableを呼び出す。
     *
     * @author eagle.sakura
     * @param flag
     * @version 2010/07/10 : 新規作成
     * @see GL11Ext#GL_MATRIX_PALETTE_OES
     */
    public native void glEnable(int flag);

    /**
     * ネイティブのglEnableClientStateを呼び出す。
     *
     * @author eagle.sakura
     * @param flag
     * @version 2010/07/10 : 新規作成
     * @see GL11Ext#GL_MATRIX_INDEX_ARRAY_OES
     */
    public native void glEnableClientState(int flag);

    /**
     * ネイティブのglWeightPointerOESを呼び出す。<BR>
     * bufferは描画までポインタが有効である必要がある。
     *
     * @author eagle.sakura
     * @param num
     * @param type
     * @param stride
     * @param buffer
     * @version 2010/07/10 : 新規作成
     */
    public native void glWeightPointerOES(int num, int type, int stride, Buffer buffer);

    /**
     * ネイティブのglMatrixIndexPointerOESを呼び出す。<BR>
     * Xperia/HT-03A共に頂点ブレンドの最大数は４である。
     *
     * @author eagle.sakura
     * @param num
     * @param type
     * @param stride
     * @param buffer
     * @version 2010/07/10 : 新規作成
     */
    public native void glMatrixIndexPointerOES(int num, int type, int stride, Buffer buffer);

    /**
     * ネイティブのglLoadPaletteFromModelViewMatrixOESを呼び出す。
     *
     * @author eagle.sakura
     * @version 2010/07/10 : 新規作成
     */
    public native void glLoadPaletteFromModelViewMatrixOES();
}
