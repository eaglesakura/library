/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package eagle.android.fbx;

import java.io.IOException;

import eagle.android.gles11.GLManager;
import eagle.android.gles11.ITexture;
import eagle.android.gles11.IVertexBuffer;
import eagle.android.gles11.IndexBufferHW;
import eagle.android.gles11.VertexBufferHW;
import eagle.io.DataInputStream;
import eagle.util.EagleException;
import eagle.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class Frame extends Node {
    /**
     * スキンメッシュの変形情報。
     */
    private Deformer deformer = null;

    /**
     * 頂点バッファ。
     */
    private IVertexBuffer vertices = null;

    /**
     *
     */
    private FrameSubset[] subsets = null;

    /**
     * このフレームを管理しているフィギュア。
     */
    private Figure figure = null;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    @Override
    public void dispose() {
        super.dispose();

        vertices.dispose();
        vertices = null;
        for (FrameSubset fs : subsets) {
            fs.dispose();
        }
    }

    /**
     * 描画用のメッシュ・スキンメッシュを管理する。
     *
     * @author eagle.sakura
     * @param figure
     * @param parent
     * @param number
     * @version 2010/07/08 : 新規作成
     */
    public Frame(Figure figure, Node parent, int number) {
        super(parent, number);
        this.figure = figure;
    }

    /**
     * 描画を行う。
     *
     * @author eagle.sakura
     * @version 2010/07/08 : 新規作成
     */
    @Override
    public void draw() {
        GLManager glManager = figure.getGLManager();

        // ! ボーン変形行列が指定されている場合、それを設定する。
        if (deformer != null) {
            deformer.bind();
        } else {
            // ! 初期姿勢への逆行列がある場合、そちらを設定する。

            glManager.pushMatrixF(getMatrix());
            // glManager.pushMatrixF( getPoseInvertMatrixLocal() );
            // glManager.pushMatrixF( getPoseInvertMatrixGlobal( ) );
            // glManager.pushMatrixF( getPoseInvertMatrixGlobal() );
        }

        vertices.bind();

        for (FrameSubset fs : subsets) {
            fs.drawSubset(glManager);
        }

        vertices.unbind();

        if (deformer != null) {
            deformer.unbind();
        } else {
            glManager.popMatrix();
            // glManager.popMatrix();
            // glManager.popMatrix();
        }

    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    @Override
    public int getNodeType() {
        return deformer == null ? eNodeTypeMesh : eNodeTypeSkin;
    }

    /**
     * 変形用バッファを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Deformer getDeformer() {
        return deformer;
    }

    /**
     * nameに一致するテクスチャを関連付ける。
     *
     * @author eagle.sakura
     * @param name
     * @param texture
     * @version 2010/07/11 : 新規作成
     */
    public void bindTexture(String name, ITexture texture) {
        for (FrameSubset fs : subsets) {
            if (fs.getMaterial().getTextureName().equals(name)) {
                fs.getMaterial().setTexture(texture);
            }
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param dis
     * @throws IOException
     * @throws EagleException
     * @version 2010/07/08 : 新規作成
     */
    @Override
    public void initialize(DataInputStream dis, GLResourceCreater glCreater) throws IOException, EagleException {
        super.initialize(dis, glCreater);

        // GLResourceCreater.FbxVertices fbxVertices = new
        // GLResourceCreater.FbxVertices();

        VertexBufferHW vb = new VertexBufferHW(figure.getGLManager());
        // VertexBufferSW vb = new VertexBufferSW( figure.getGLManager() );
        vertices = vb;

        // ! 位置
        {
            int length = dis.readS32() * 3;
            float[] buffer = new float[length];

            for (int i = 0; i < (length); ++i) {
                // buffer[ i ] = ( int )( dis.readFloat() *
                // EagleUtil.eGLFixed1_0 );
                buffer[i] = dis.readFloat();
            }
            vb.initPosBuffer(buffer);
        }

        // ! 法線
        {
            int length = dis.readS32() * 3;
            int[] buffer = new int[length];

            for (int i = 0; i < (length); ++i) {
                buffer[i] = (int) (dis.readFloat() * EagleUtil.eGLFixed1_0);
            }
        }

        // ! UV
        {
            int length = dis.readS32() * 2;
            float[] buffer = new float[length];

            for (int i = 0; i < (length); ++i) {
                // buffer[ i ] = ( int )( dis.readFloat() *
                // EagleUtil.eGLFixed1_0 );
                buffer[i] = dis.readFloat();
            }
            vb.initUVBuffer(buffer);
        }

        // ! インデックスバッファ
        {
            int length = dis.readS32();
            subsets = new FrameSubset[length];

            for (int i = 0; i < length; ++i) {
                Material m = Material.createInstance(figure.getGLManager(), dis);
                // IndexBufferSW ib = new IndexBufferSW( figure.getGLManager()
                // );
                IndexBufferHW ib = new IndexBufferHW(figure.getGLManager());

                int ibLength = dis.readS32();
                short[] buffer = new short[ibLength];
                for (int k = 0; k < ibLength; ++k) {
                    buffer[k] = (short) dis.readS32();
                }
                ib.init(buffer);

                subsets[i] = new FrameSubset(ib, m);
            }

        }

        // ! デフォーマ
        {
            int size = dis.readS32();
            if (size > 0) {
                deformer = new Deformer(this, size, figure.getGLManager());

                for (int i = 0; i < size; ++i) {
                    String name = dis.readString();
                    deformer.setBoneName(i, name);
                }
                // ! 頂点数
                int length = dis.readS32() * 3;
                byte[] indices = new byte[length];
                float[] weights = new float[length];

                for (int i = 0; i < length; ++i) {
                    indices[i] = dis.readS8();
                }
                for (int i = 0; i < length; ++i) {
                    weights[i] = dis.readFloat();
                }

                DeformBufferSW db = new DeformBufferSW(figure.getGLManager());
                // DeformBufferHW db = new DeformBufferHW( figure.getGLManager()
                // );
                db.init(weights, indices);
                deformer.setDeform(db);
            }
        }

        // System.gc();
    }
}
