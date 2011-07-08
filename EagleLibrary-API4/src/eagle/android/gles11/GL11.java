/**
 *
 * @author eagle.sakura
 * @version 2010/07/05 : 新規作成
 */
package eagle.android.gles11;

import eagle.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/07/05 : 新規作成
 */
public class GL11 {
    static {
        System.loadLibrary("eagle_android_gles11_GL11");
        init();
    };

    /**
     * 初期化処理を行う。
     *
     * @author eagle.sakura
     * @version 2010/07/05 : 新規作成
     */
    private static void init() {
        EagleUtil.log("jni init!");
        _nativeInit();
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/05 : 新規作成
     */
    private static native void _nativeInit();

    /**
     * 動作確認用
     *
     * @author eagle.sakura
     * @version 2010/07/05 : 新規作成
     */
    public native void test();
}
