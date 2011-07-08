/**
 *
 */
package com.eaglesakura.lib.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ビット反転するOutputStream あまり実行速度がよろしくないため、小さなファイルに利用するほうがいい。 skipには対応しない。
 */
public class BitTransOutputStream extends OutputStream {
    private OutputStream out = null;
    private long passBit = 0;
    private int current = 0;

    /**
     *
     * @param os
     * @param pass
     */
    public BitTransOutputStream(OutputStream os, long pass) {
        passBit = pass;
        out = os;
    }

    /**
     * 出力先を取得する。
     *
     * @return
     */
    public OutputStream getTarget() {
        return out;
    }

    @Override
    public void write(int oneByte) throws IOException {
        int bits = (int) (passBit >> (current % 64)) & 0xff;
        out.write((oneByte ^ bits) & 0xff);
        ++current;
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        for (int i = 0; i < count; ++i) {
            write(((int) buffer[i + offset]) & 0xff);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
