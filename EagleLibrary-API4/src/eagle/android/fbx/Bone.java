/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package eagle.android.fbx;

import eagle.android.math.Matrix4x4;

/**
 * ボーン用のノードを示す。
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class Bone extends Node {
    /**
     * 初期姿勢の逆行列。
     */
    private Matrix4x4 invertMatrix = new Matrix4x4();

    /**
     *
     * @author eagle.sakura
     * @param parent
     * @param number
     * @version 2010/07/07 : 新規作成
     */
    public Bone(Node parent, int number) {
        super(parent, number);
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    @Override
    public int getNodeType() {
        return eNodeTypeBone;
    }

    /**
     * 初期姿勢の逆行列を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Matrix4x4 getInvertMatrix() {
        return invertMatrix;
    }

    /**
     * 転送用のワールド行列を取得する。
     *
     * @author eagle.sakura
     * @param result
     * @return
     * @version 2010/10/03 : 新規作成
     */
    public Matrix4x4 getWorldMatrix(Matrix4x4 result) {
        result.identity();
        result.multiply(getInvertMatrix());
        result.multiply(getMatrix());

        return result;
    }
}
