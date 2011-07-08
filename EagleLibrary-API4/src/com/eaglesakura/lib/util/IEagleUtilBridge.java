/**
 *
 * @author eagle.sakura
 * @version 2010/03/20 : 新規作成
 */
package com.eaglesakura.lib.util;

/**
 * デバイス依存の機能を切り替える。<BR>
 * 主にログ出力等に対応する。
 *
 * @author eagle.sakura
 * @version 2010/03/20 : 新規作成
 */
public interface IEagleUtilBridge {
    /**
     * インフォメーション出力。
     */
    public static final int eLogTypeInfo = 0;

    /**
     * デバッグ出力。
     */
    public static final int eLogTypeDebug = 1;

    /**
     * デバッグ用のログを表示する。
     *
     * @author eagle.sakura
     * @param message
     *            出力するメッセージ
     * @version 2010/03/20 : 新規作成
     */
    public void log(int eLogType, String message);

    /**
     * 実行しているプラットフォームのバージョンを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/12 : 新規作成
     */
    public int getPlatformVersion();
}
