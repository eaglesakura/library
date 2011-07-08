/**
 *
 * @author eagle.sakura
 * @version 2010/04/02 : 新規作成
 */
package com.eaglesakura.lib.io;

import java.io.IOException;
import java.io.OutputStream;

import com.eaglesakura.lib.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/04/02 : 新規作成
 */
public class OutputStreamBufferWriter implements IBufferWriter {
    protected OutputStream os = null;

    /**
     *
     * @author eagle.sakura
     * @param os
     * @version 2010/04/02 : 新規作成
     */
    public OutputStreamBufferWriter(OutputStream os) {
        this.os = os;
    }

    /**
     * @author eagle.sakura
     * @version 2010/04/02 : 新規作成
     */
    @Override
    public void dispose() {
        try {
            os.flush();
            os.close();
            os = null;
        } catch (Exception e) {
            EagleUtil.log(e);
        }
    }

    /**
     * @author eagle.sakura
     * @param eSeekType
     * @param position
     * @throws IOException
     * @version 2010/04/02 : 新規作成
     */
    @Override
    public void seek(int eSeekType, int position) throws IOException {
    }

    /**
     * @author eagle.sakura
     * @param data
     * @param head
     * @param length
     * @return
     * @throws IOException
     * @version 2010/04/02 : 新規作成
     */
    @Override
    public int writeBuffer(byte[] data, int head, int length) throws IOException {
        os.write(data, head, length);
        return length;
    }

}
