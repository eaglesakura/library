/**
 *
 */
package eagle.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class BitTransInputStream extends InputStream {
    private InputStream input = null;
    private long passBit = 0;
    private int current = 0;

    /**
     *
     * @param os
     * @param pass
     */
    public BitTransInputStream(InputStream is, long pass) {
        passBit = pass;
        input = is;
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    public InputStream getOrigin() {
        return input;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        int bits = (int) (passBit >> (current % 64)) & 0xff;
        ++current;
        int n = input.read();
        return n >= 0 ? ((n ^ bits) & 0xff) : n;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        int result = 0;
        for (int i = 0; i < length; ++i) {
            int n = read();
            if (n >= 0) {
                b[offset + i] = (byte) n;
                ++result;
            } else {
                return result;
            }
        }
        return result;
    }
}
