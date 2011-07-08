/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package eagle.android.fbx;

import java.io.IOException;

import eagle.android.gles11.GLManager;
import eagle.android.gles11.ITexture;
import eagle.android.math.Matrix4x4;
import eagle.io.DataInputStream;
import eagle.util.Disposable;
import eagle.util.EagleException;
import eagle.util.EagleUtil;

/**
 * フィギュアは階層構造のノードを持ち、それを管理する。
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class Figure implements Disposable {
    /**
     *
     */
    private Node[] nodes; // !< ノードを直線状にした配列。

    /**
     *
     */
    private GLManager glManager;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/08 : 新規作成
     */
    private Figure() {

    }

    /**
     * gc時の処理。
     *
     * @author eagle.sakura
     * @throws Throwable
     * @version 2010/07/12 : 新規作成
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    /**
     * GL関連リソースを処理する。
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    @Override
    public void dispose() {
        if (nodes != null) {
            for (Node node : nodes) {
                node.dispose();
            }
            nodes = null;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public GLManager getGLManager() {
        return glManager;
    }

    /**
     *
     * @author eagle.sakura
     * @param node
     * @version 2010/07/08 : 新規作成
     */
    private void drawNodeTree(Node node) {
        node.draw();
        int num = node.getChildCount();
        for (int i = 0; i < num; ++i) {
            drawNodeTree(node.getChild(i));
        }
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/09 : 新規作成
     */
    private void updateNodeTree(Node node) {
        node.updateMatrix();
        int num = node.getChildCount();
        for (int i = 0; i < num; ++i) {
            updateNodeTree(node.getChild(i));
        }
    }

    /**
     * 一致する名前のテクスチャを関連付ける。
     *
     * @author eagle.sakura
     * @param name
     * @param texture
     * @version 2010/07/11 : 新規作成
     */
    public void bindTexture(String name, ITexture texture) {
        for (Node node : nodes) {
            // ! 全フレームに適用する。
            if (node.getNodeType() == Node.eNodeTypeSkin || node.getNodeType() == Node.eNodeTypeMesh) {
                Frame frame = (Frame) node;
                frame.bindTexture(name, texture);
            }
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param trans
     * @version 2010/07/08 : 新規作成
     */
    public void draw(Matrix4x4 trans) {
        /*
         * //! 全ノードを描画する。 for( Node node : nodes ) { // node.draw(); }
         */
        updateNodeTree(getRootNode());
        drawNodeTree(getRootNode());
    }

    /**
     * 指定した名称のノードを取得する。
     *
     * @author eagle.sakura
     * @param name
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Node getNode(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 番号を指定してノードを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Node getNode(int index) {
        return nodes[index];
    }

    /**
     * 最上階となるノードを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public Node getRootNode() {
        return nodes[0];
    }

    /**
     * 名前を指定してボーンを取得する。<BR>
     * 見つからなかった場合、nullを返す。
     *
     * @author eagle.sakura
     * @param name
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public Node getBone(String name) {
        for (Node node : nodes) {
            if (node.getNodeType() == Node.eNodeTypeBone && node.getName().equals(name)
            // && name.startsWith( node.getName( )
            ) {
                return node;
            }
        }

        for (Node node : nodes) {
            if (node.getNodeType() == Node.eNodeTypeBone
            // && node.getName().equals( name )
                    && name.startsWith(node.getName()) && (node.getName().length() + 3) > name.length()) {
                return node;
            }
        }
        return null;
    }

    /**
     * アニメーションを指定する。
     *
     * @author eagle.sakura
     * @param action
     * @param frame
     * @version 2010/07/11 : 新規作成
     */
    public void bindAction(FigureAction action, float frame) {
        frame = action.normalizeFrame(frame);
        // ! 全ノードに適用する
        for (Node node : nodes) {
            NodeAction na = action.getNodeAction(node.getNodeNumber());
            na.bind(frame, node.getBindKey());
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param parent
     * @param dis
     * @throws IOException
     * @throws EagleException
     * @version 2010/07/08 : 新規作成
     */
    private Node createNode(Node parent, DataInputStream dis, GLResourceCreater glCreater) throws IOException, EagleException {
        {
            int version = dis.readS32();
            if (version != 0x1) {
                throw new EagleException(EagleException.eStatusUnknownFileVersion);
            }
        }

        // ! ノードタイプ
        int type = dis.readS32();
        int number = dis.readS32();

        // EagleUtil.log( "type : " + type + "  number : " + number );
        // EagleUtil.log( "Memory : " + ( Runtime.getRuntime().freeMemory() /
        // 1024 ) + " KB" );

        if (type == Node.eNodeTypeMesh || type == Node.eNodeTypeSkin) {
            Frame frame = new Frame(this, parent, number);
            nodes[number] = frame;
        } else {
            Node node = null;
            if (type == Node.eNodeTypeBone) {
                node = new Bone(parent, number);
            } else {
                node = new Node(parent, number);
            }
            nodes[number] = node;
        }

        nodes[number].initialize(dis, glCreater);

        if (nodes[number].getNodeType() == Node.eNodeTypeBone) {
            EagleUtil.log("Bone : " + nodes[number].getName());
        }

        {
            int childs = dis.readS16();
            for (int i = 0; i < childs; ++i) {
                Node node = createNode(nodes[number], dis, glCreater);
                nodes[number].addChild(node);
            }
        }

        return nodes[number];
    }

    /**
     * ボーン情報を関連付ける。<BR>
     * 初回のみ行う。
     *
     * @author eagle.sakura
     * @version 2010/07/10 : 新規作成
     */
    private void bindBones() {
        for (Node node : nodes) {
            if (node.getNodeType() == Node.eNodeTypeSkin) {
                Frame frame = (Frame) node;
                Deformer deformer = frame.getDeformer();

                for (int i = 0; i < deformer.getNumBones(); ++i) {
                    Node bone = getBone(deformer.getBoneName(i));
                    deformer.setBone(i, (Bone) bone);
                }
            }

            // ! 初期への逆行列を指定する。
            if (node.getPoseInvertMatrixGlobal() != null
            // && node.getParent() != null
            ) {
                node.getMatrix().invert(node.getPoseInvertMatrixGlobal());
                // node.getMatrix().invert( node.getPoseInvertMatrixGlobal( )
                // ).multiply( getRootNode().getChild( 0 ).getMatrix() );
                // node.getMatrix().invert( node.getDefaultPoseInvertMatrix( )
                // );
            }
        }
    }

    /**
     * ボーンの初期逆行列を計算する。
     *
     * @author eagle.sakura
     * @version 2010/07/10 : 新規作成
     */
    private void calcInvertMatrix() {
        for (Node node : nodes) {
            if (node.getNodeType() == Node.eNodeTypeBone) {
                Bone bone = (Bone) node;
                bone.getMatrix().invert(bone.getInvertMatrix());
            }
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param glManager
     * @param dis
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/07/08 : 新規作成
     */
    public static Figure createInstance(GLManager glManager, DataInputStream dis, GLResourceCreater glCreater) throws IOException, EagleException {
        Figure figure = new Figure();
        figure.glManager = glManager;

        {
            int version = dis.readS32();
            if (version != 0x1) {
                throw new EagleException(EagleException.eStatusUnknownFileVersion);
            }
        }

        // ! ノード格納配列
        {
            int nodes = dis.readS32();
            EagleUtil.log("Nodes : " + nodes);
            figure.nodes = new Node[nodes];
        }

        // ! ノードを作成する
        {
            figure.createNode(null, dis, glCreater);
            EagleUtil.log("node create complete!");

            String[] str = new String[figure.nodes.length];
            {
                int i = 0;
                for (Node node : figure.nodes) {
                    str[i] = node.getName();
                    ++i;
                }
            }

            // ! 初期行列を生成する。
            figure.updateNodeTree(figure.getRootNode());

            // ! ボーンを関連付ける
            figure.bindBones();

            // ! 逆行列を生成する
            figure.calcInvertMatrix();
        }

        return figure;
    }
}
