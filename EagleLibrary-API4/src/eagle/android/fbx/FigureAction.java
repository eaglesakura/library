/**
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
package eagle.android.fbx;

import java.io.IOException;

import eagle.io.DataInputStream;
import eagle.util.EagleException;

/**
 * フィギュアに関連付けたアニメーション情報を扱う。
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
public class FigureAction {
    /**
     * ノードごとのアクション情報の集合。
     */
    private NodeAction[] nodes = null;

    /**
     * 終端キーフレーム。
     */
    private float lastKeyFrame = 0.0f;

    /**
     * 終端でループを行うか。
     */
    private boolean loop = false;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/11 : 新規作成
     */
    private FigureAction() {

    }

    /**
     * ノードを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public NodeAction getNodeAction(int index) {
        return nodes[index];
    }

    /**
     * アニメーションのループフラグを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * アニメーションのループフラグを設定する。
     *
     * @author eagle.sakura
     * @param loop
     * @version 2010/07/11 : 新規作成
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * 最終フレーム数を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/21 : 新規作成
     */
    public float getMaxFrame() {
        return lastKeyFrame;
    }

    /**
     *
     * @author eagle.sakura
     * @param frame
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public float normalizeFrame(float frame) {
        if (frame <= lastKeyFrame) {
            return frame;
        }

        if (loop) {
            while (frame > lastKeyFrame) {
                frame -= lastKeyFrame;
            }
            return frame;
        } else {
            return lastKeyFrame;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param dis
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/07/11 : 新規作成
     */
    public static FigureAction createInstance(DataInputStream dis) throws IOException, EagleException {
        {
            int version = dis.readS32();
            if (version != 0x1) {
                throw new EagleException(EagleException.eStatusUnknownFileVersion);
            }
        }
        FigureAction result = new FigureAction();

        int nodeNum = dis.readS32();
        result.nodes = new NodeAction[nodeNum];
        for (int i = 0; i < nodeNum; ++i) {
            NodeAction node = new NodeAction(dis);
            result.nodes[node.getNodeNumber()] = node;
        }

        result.lastKeyFrame = result.nodes[0].getLastKeyFrame();
        return result;
    }
}
