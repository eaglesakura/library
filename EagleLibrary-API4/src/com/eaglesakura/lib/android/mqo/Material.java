package com.eaglesakura.lib.android.mqo;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.gles11.ITexture;
import com.eaglesakura.lib.mqo.MQOMaterial;

public class Material extends GLObject {
    private ITexture texture = null;

    public Material(GLManager gl, MQOMaterial material) {
        setGLManager(gl);
    }

    public ITexture getTexture() {
        return texture;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
    }

    @Override
    public void bind() {
        if (texture != null) {
            texture.bind();
        }
    }

    @Override
    public void unbind() {
        if (texture != null) {
            texture.unbind();
        }
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
