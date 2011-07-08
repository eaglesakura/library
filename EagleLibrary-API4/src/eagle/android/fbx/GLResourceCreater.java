/**
 *
 * @author eagle.sakura
 * @version 2010/07/27 : 新規作成
 */
package eagle.android.fbx;

import eagle.android.gles11.GLManager;
import eagle.android.gles11.IIndexBuffer;
import eagle.android.gles11.IVertexBuffer;
import eagle.android.gles11.IndexBufferHW;
import eagle.android.gles11.VertexBufferHW;

/**
 * @author eagle.sakura
 * @version 2010/07/27 : 新規作成
 */
public class GLResourceCreater {
    /**
     * ファイルローダーが読み込んだ頂点情報をまとめた構造体。
     *
     * @author eagle.sakura
     * @version 2010/07/27 : 新規作成
     */
    public static class FbxVertices {
        /**
         * 関連付けられているGL管理クラス。
         */
        public GLManager glManager = null;

        /**
         * 位置座標配列。<BR>
         * 頂点数＊３以上の長さを保証する。
         */
        public float[] positions = null;

        /**
         * 法線座標配列。<BR>
         * オプションであるため、nullの場合がある。
         */
        public float[] normals = null;

        /**
         * UV座標配列。<BR>
         * オプションであるため、nullの場合がある。
         */
        public float[] coords = null;

        /**
         * インデックス配列。<BR>
         * 面数＊３以上の長さを保証する。
         */
        public short[] indices = null;

        /**
         * １頂点ごとに設定される頂点ウェイト配列。<BR>
         * オプションであるため、nullの場合がある。
         */
        public float[] weights = null;

        /**
         * １頂点ごとに設定される頂点ウェイトの数。
         */
        public int numWeightMatrices = 3;

        /**
         * １頂点ごとに設定されている行列パレット番号。
         */
        public byte[] weightIndices = null;
    };

    /**
     * 頂点バッファを作成する。<BR>
     * このバッファにデフォーマは含まない。
     *
     * @author eagle.sakura
     * @param vertices
     * @return
     * @version 2010/07/27 : 新規作成
     */
    public IVertexBuffer createVertexBuffer(FbxVertices vertices) {
        VertexBufferHW vb = new VertexBufferHW(vertices.glManager);

        // ! 位置バッファを作成する。
        vb.initPosBuffer(vertices.positions);

        // ! 法線バッファを作成する。
        vb.initNormalBuffer(vertices.normals);

        // ! UVバッファを作成する
        vb.initUVBuffer(vertices.coords);

        // ! 結果を返す。
        return vb;
    }

    /**
     * インデックスバッファを作成する。<BR>
     * このバッファにデフォーマは含まない。
     *
     * @author eagle.sakura
     * @param vertices
     * @return
     * @version 2010/07/27 : 新規作成
     */
    public IIndexBuffer createIndexBuffer(FbxVertices vertices) {
        IndexBufferHW ib = new IndexBufferHW(vertices.glManager);
        ib.init(vertices.indices);

        return ib;
    }

    /**
     * 変形用のバッファを作成する。
     *
     * @author eagle.sakura
     * @param vertices
     * @return
     * @version 2010/07/27 : 新規作成
     */
    public IDeformBuffer createDeformBuffer(FbxVertices vertices) {
        DeformBufferSW db = new DeformBufferSW(vertices.glManager);
        db.init(vertices.weights, vertices.weightIndices);
        return db;
    }
}
