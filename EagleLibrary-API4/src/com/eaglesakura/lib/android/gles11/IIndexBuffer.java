/**
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

/**
 * ポリゴン集合のインデックスバッファを管理する。<BR>
 * 管理方法は実装に依存する。
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
public interface IIndexBuffer {
    /**
     * 描画を行う。
     *
     * @author eagle.sakura
     * @param glMgr
     * @version 2009/11/16 : 新規作成
     */
    public void drawElements();

    /**
     * 管理している資源を開放する。
     *
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    public void dispose();
}
