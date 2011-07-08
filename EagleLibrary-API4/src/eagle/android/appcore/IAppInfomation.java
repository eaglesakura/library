/**
 *
 * @author eagle.sakura
 * @version 2010/06/05 : 新規作成
 */
package eagle.android.appcore;

import android.app.Activity;
import android.view.View;

/**
 * @author eagle.sakura
 * @version 2010/06/05 : 新規作成
 */
public interface IAppInfomation {
    /**
     * Admob用のviewを生成する。<BR>
     * 必要ない場合、nullを返す。
     *
     * @author eagle.sakura
     * @param activity
     * @return
     * @version 2010/06/05 : 新規作成
     */
    public View createAdView(Activity activity);

    /**
     * 有料モードで起動する場合、trueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/05 : 新規作成
     */
    public boolean isSharewareMode();
}
