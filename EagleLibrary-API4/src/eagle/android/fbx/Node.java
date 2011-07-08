/**
 *
 * @author eagle.sakura
 * @version 2010/07/07 : 新規作成
 */
package eagle.android.fbx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eagle.android.math.Matrix4x4;
import eagle.io.DataInputStream;
import eagle.math.Vector3;
import eagle.util.EagleException;

/**
 * @author eagle.sakura
 * @version 2010/07/07 : 新規作成
 */
public class Node {
    private Node parent = null;
    private List<Node> childs = new ArrayList<Node>();

    public Node getParent() {
        return parent;
    }

    /**
     * デフォルトの姿勢情報を示す。
     */
    private Vector3 scale = new Vector3();
    /**
     * デフォルトの姿勢情報を示す。
     */
    private Vector3 rotate = new Vector3();
    /**
     * デフォルトの姿勢情報を示す。
     */
    private Vector3 translate = new Vector3();

    /**
     * バインドしたキーフレーム情報。
     */
    private KeyFrame bindKey = new KeyFrame(-1.0f);

    /**
     * 行列情報を示す。
     */
    private Matrix4x4 matrix = new Matrix4x4();

    /**
     * 初期姿勢への逆行列を示す。<BR>
     * 基点情報を変更している場合の対応策として利用する。
     */
    private Matrix4x4 invertLocalMatrix = null;

    /**
     *
     */
    private Matrix4x4 invertGlobalMatrix = null;

    /**
     * ノード番号。
     */
    private int number = 0;

    /**
     * ノード名
     */
    private String name = "";

    /**
     * NULLノード。
     */
    public static final int eNodeTypeNull = 0;

    /**
     * 通常メッシュノード。
     */
    public static final int eNodeTypeMesh = 1;

    /**
     * スキンメッシュノード。
     */
    public static final int eNodeTypeSkin = 2;

    /**
     * ボーン情報。
     */
    public static final int eNodeTypeBone = 3;

    /**
     *
     * @author eagle.sakura
     * @param parent
     * @param number
     * @version 2010/07/07 : 新規作成
     */
    protected Node(Node parent, int number) {
        this.parent = parent;
        this.number = number;
    }

    /**
     * 初期姿勢や基本情報を保存する。
     *
     * @author eagle.sakura
     * @param dis
     * @throws IOException
     * @throws EagleException
     * @version 2010/07/08 : 新規作成
     */
    protected void initialize(DataInputStream dis, GLResourceCreater glCreater) throws IOException, EagleException {
        {
            String name = dis.readString();
            setName(name);
        }

        {
            // !
            scale.x = dis.readFloat();
            scale.y = dis.readFloat();
            scale.z = dis.readFloat();
        }
        {
            // !
            rotate.x = dis.readFloat();
            rotate.y = dis.readFloat();
            rotate.z = dis.readFloat();
        }
        {
            // !
            translate.x = dis.readFloat();
            translate.y = dis.readFloat();
            translate.z = dis.readFloat();
        }

        // ! 初期姿勢にバインドする。
        getBindKey().set(scale, rotate, translate);

        // ! 逆行列を求める。
        // if( translate.length() > 0.01f )
        {
            invertLocalMatrix = new Matrix4x4();
            invertGlobalMatrix = new Matrix4x4();

            // ! ローカルキーを保存する。
            getBindKey().calcMatrix(invertLocalMatrix).invert();
        }
    }

    /**
     * 初期姿勢への逆行列を取得する。<BR>
     * 初期姿勢が単位行列の場合、nullを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/08/17 : 新規作成
     */
    protected Matrix4x4 getPoseInvertMatrixLocal() {
        return invertLocalMatrix;
    }

    protected Matrix4x4 getPoseInvertMatrixGlobal() {
        return invertGlobalMatrix;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public KeyFrame getBindKey() {
        return bindKey;
    }

    /**
     * 行列情報を更新する。
     *
     * @author eagle.sakura
     * @version 2010/07/09 : 新規作成
     */
    public void updateMatrix() {
        /*
         * Matrix4x4 m = new Matrix4x4( ); getPoseInvertMatrixLocal().multiply(
         * getBindKey().calcMatrix( m ), getMatrix() ); if( getParent() != null
         * ) { getMatrix().multiply( getParent().getMatrix() ); }
         */

        getBindKey().calcMatrix(getMatrix());
        if (getParent() != null) {
            getMatrix().multiply(getParent().getMatrix());
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param node
     * @version 2010/07/09 : 新規作成
     */
    public void addChild(Node node) {
        childs.add(node);
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public Matrix4x4 getMatrix() {
        return matrix;
    }

    /**
     *
     * @author eagle.sakura
     * @param matrix
     * @version 2010/07/08 : 新規作成
     */
    public void setMatrix(Matrix4x4 matrix) {
        this.matrix = matrix;
    }

    /**
     * 一意の名称を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public String getName() {
        return name;
    }

    /**
     *
     * 一意の名称を設定する。
     *
     * @author eagle.sakura
     * @param name
     * @version 2010/07/08 : 新規作成
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * スケーリング情報を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public Vector3 getScale() {
        return scale;
    }

    /**
     * 回転情報を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public Vector3 getRotate() {
        return rotate;
    }

    /**
     * 位置情報を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public Vector3 getTranslate() {
        return translate;
    }

    /**
     * ノード番号を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public int getNodeNumber() {
        return number;
    }

    /**
     * 子ノード数を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public int getChildCount() {
        return childs.size();
    }

    /**
     * 子ノードを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/07 : 新規作成
     */
    public Node getChild(int index) {
        return childs.get(index);
    }

    /**
     * インスタンスの種類を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public int getNodeType() {
        return eNodeTypeNull;
    }

    /**
     * 現在の行列スタックで描画を行う。<BR>
     * 子を描画する必要はない。
     *
     * @author eagle.sakura
     * @version 2010/07/08 : 新規作成
     */
    public void draw() {
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    public void dispose() {
    }
}
