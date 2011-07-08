/**
 *
 * @author eagle.sakura
 * @version 2010/09/12 : 新規作成
 */
package eagle.android.util;

import android.util.Log;
import eagle.util.EagleUtil;
import eagle.util.IEagleUtilBridge;

/**
 * @author eagle.sakura
 * @version 2010/09/12 : 新規作成
 */
public class UtilBridgeAndroidSDK3 implements IEagleUtilBridge {
    private String tag = "";

    /**
     * ログ出力用のクラス。
     *
     * @author eagle.sakura
     * @param tag
     * @version 2010/05/30 : 新規作成
     */
    public UtilBridgeAndroidSDK3(String tag) {
        this.tag = tag;
    }

    /**
     * 実際の出力を行う。
     *
     * @author eagle.sakura
     * @param eLogType
     * @param message
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public void log(int eLogType, String message) {
        switch (eLogType) {
        case eLogTypeDebug:
            Log.d(tag, message);
            return;
        case eLogTypeInfo:
            Log.i(tag, message);
            return;
        }
    }

    /**
     * @author eagle.sakura
     * @return
     * @version 2010/09/12 : 新規作成
     */
    @Override
    public int getPlatformVersion() {
        EagleUtil.log("getVersion");
        return 3;
    }
}
