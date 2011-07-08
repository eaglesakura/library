package eagle.android.gles11;

import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class IndexBufferHW implements IIndexBuffer {
    private VRAMResource vram = null;
    private GLManager glManager = null;
    private int indicesLength = 0;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    public IndexBufferHW(GLManager glManager) {
        this.glManager = glManager;
        vram = new VRAMResource(glManager);
    }

    /**
     * 頂点バッファを作成する。
     *
     * @author eagle.sakura
     * @param indices
     * @version 2009/11/15 : 新規作成
     */
    public void init(short[] indices) {
        vram.create(1);
        indicesLength = indices.length;
        ShortBuffer sb = IGLResource.createBuffer(indices);
        vram.toGLBuffer(0, sb, indices.length * 2, GL11.GL_ELEMENT_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);
    }

    /**
     * 描画処理を行う。
     *
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    @Override
    public void drawElements() {
        vram.bind(0, GL11.GL_ELEMENT_ARRAY_BUFFER);
        glManager.getGL().glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_SHORT, 0);
    }

    @Override
    public void dispose() {
    }

}
