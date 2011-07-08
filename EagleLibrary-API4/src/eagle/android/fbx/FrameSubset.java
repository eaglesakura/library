/**
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
package eagle.android.fbx;

import eagle.android.gles11.GLManager;
import eagle.android.gles11.IIndexBuffer;

/**
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
public class FrameSubset {
    /**
     * インデックスバッファ。
     */
    private IIndexBuffer indices = null;

    /**
     * 関連付けられたマテリアル。
     */
    private Material material = null;

    /**
     *
     * @author eagle.sakura
     * @param ib
     * @param m
     * @version 2010/07/11 : 新規作成
     */
    public FrameSubset(IIndexBuffer ib, Material m) {
        indices = ib;
        material = m;
    }

    /**
     * 材質情報を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Material getMaterial() {
        return material;
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/11 : 新規作成
     */
    public void drawSubset(GLManager gl) {
        material.bind();
        indices.drawElements();
        material.unbind();
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    public void dispose() {
        indices.dispose();
        indices = null;
    }
}
