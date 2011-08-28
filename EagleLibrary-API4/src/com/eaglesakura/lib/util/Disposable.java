/**
 * 開放することを示すインターフェース。
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
package com.eaglesakura.lib.util;

/**
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
public interface Disposable {
    /**
     * 管理しているリソースを開放する。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    public void dispose();
}
