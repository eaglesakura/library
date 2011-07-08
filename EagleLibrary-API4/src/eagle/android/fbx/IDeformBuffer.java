package eagle.android.fbx;

/**
 * スキンメッシュ変形用バッファを作成する。
 *
 * @author eagle.sakura
 * @version 2010/07/26 : 新規作成
 */
public interface IDeformBuffer {
    /**
     * デバイスに関連付ける。
     *
     * @author eagle.sakura
     * @version 2010/07/26 : 新規作成
     */
    public void bind();

    /**
     * デバイスから切り離す。
     *
     * @author eagle.sakura
     * @version 2010/07/26 : 新規作成
     */
    public void unbind();

    /**
     * 管理している資源を開放する。
     *
     * @author eagle.sakura
     * @version 2010/07/26 : 新規作成
     */
    public void dispose();
}
