/**
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
package eagle.android.gles11;

/**
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
public interface IVertexBuffer {
    /**
     * バッファに転送する。
     *
     * @author eagle.sakura
     * @param gl
     * @version 2009/11/14 : 新規作成
     */
    public void bind();

    /**
     * デバイスから切り離す。
     *
     * @author eagle.sakura
     * @param gl
     * @version 2009/11/15 : 新規作成
     */
    public void unbind();

    /**
     * リソースを解放する。
     *
     * @author eagle.sakura
     * @version 2009/11/15 : 新規作成
     */
    public void dispose();
}
