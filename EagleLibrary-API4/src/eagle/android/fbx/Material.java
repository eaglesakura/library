/**
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
package eagle.android.fbx;

import java.io.IOException;
import java.nio.FloatBuffer;

import eagle.android.gles11.GLManager;
import eagle.android.gles11.ITexture;
import eagle.io.DataInputStream;
import eagle.util.EagleException;
import eagle.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
public class Material {
    /**
     *
     */
    @SuppressWarnings("all")
    private FloatBuffer diffuse = null;
    /**
     *
     */
    @SuppressWarnings("all")
    private FloatBuffer ambient = null;
    /**
     *
     */
    @SuppressWarnings("all")
    private FloatBuffer emissive = null;

    /**
     * 関連付けられたテクスチャ名
     */
    private String textureName = "";

    /**
     * 関連付けられたテクスチャ。
     */
    private ITexture texture = null;

    @SuppressWarnings("all")
    private GLManager glManager = null;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/11 : 新規作成
     */
    private Material(GLManager gl) {
        glManager = gl;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public String getTextureName() {
        return textureName;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public ITexture getTexture() {
        return texture;
    }

    /**
     *
     * @author eagle.sakura
     * @param texture
     * @version 2010/07/11 : 新規作成
     */
    public void setTexture(ITexture texture) {
        this.texture = texture;
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/11 : 新規作成
     */
    public void bind() {
        // GL11 gl = glManager.getGL();
        if (texture != null) {
            texture.bind();
        }

        // gl.glMaterialfv( GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, diffuse );
        // gl.glMaterialfv( GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, ambient );
        // gl.glMaterialfv( GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, emissive
        // );
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/11 : 新規作成
     */
    public void unbind() {
        if (texture != null) {
            texture.unbind();
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
    public static Material createInstance(GLManager gl, DataInputStream dis)
            throws IOException, EagleException {
        Material result = new Material(gl);

        result.textureName = dis.readString();
        EagleUtil.log(result.textureName);
        {
            float[] diffuse = { 0, 0, 0, 0 };
            float[] ambient = { 0, 0, 0, 0 };
            float[] emissive = { 0, 0, 0, 0 };
            for (int i = 0; i < 3; ++i) {
                diffuse[i] = ((float) (dis.readU8())) / 255.0f;
                ambient[i] = ((float) (dis.readU8())) / 255.0f;
                emissive[i] = ((float) (dis.readU8())) / 255.0f;
            }

            float alpha = dis.readFloat();
            diffuse[3] = alpha;
            ambient[3] = alpha;
            emissive[3] = alpha;

            // result.diffuse = IGLResource.createBuffer( diffuse );
            // result.ambient = IGLResource.createBuffer( ambient );
            // result.emissive = IGLResource.createBuffer( emissive );
        }

        return result;
    }
}
