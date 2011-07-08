/**
 * InputStreamからバッファを読み取るリーダー。
 * @author eagle.sakura
 * @version 2010/03/31 : 新規作成
 */
package eagle.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import eagle.util.Disposable;
import eagle.util.EagleUtil;

/**
 * inputstreamから読み取るリーダー。
 *
 * @author eagle.sakura
 * @version 2010/03/31 : 新規作成
 */
public class InputStreamBufferReader implements IBufferReader, Disposable {
    protected InputStream is = null;

    /**
     * InputStreamから読み取るリーダーを作成する。
     *
     * @author eagle.sakura
     * @param is
     * @version 2010/03/31 : 新規作成
     */
    public InputStreamBufferReader(InputStream is) {
        this.is = is;
    }

    /**
     * バッファの大きさを取得する。
     *
     * @author eagle.sakura
     * @return
     * @throws IOException
     * @version 2010/06/21 : 新規作成
     */
    @Override
    public int getBufferSize() throws IOException {
        return is.available();
    }

    /**
     * @author eagle.sakura
     * @version 2010/03/31 : 新規作成
     */
    @Override
    public void dispose() {
        try {
            is.close();
            is = null;
        } catch (IOException e) {
            EagleUtil.log(e);
        }
    }

    /**
     * @author eagle.sakura
     * @param result
     * @param head
     * @param length
     * @return
     * @throws IOException
     * @version 2010/03/31 : 新規作成
     */
    @Override
    public int readBuffer(byte[] result, int head, int length) throws IOException {
        return is.read(result, head, length);
    }

    /**
     * @author eagle.sakura
     * @param eSeekType
     * @param length
     * @throws IOException
     * @version 2010/03/31 : 新規作成
     */
    @Override
    public void seek(int eSeekType, int length) throws IOException {
        if (eSeekType == eSeekTypeCurrent) {
            is.skip(length);
        } else if (eSeekType == eSeekTypeSet) {
            is.reset();
            is.skip(length);
        }
    }

    /**
     * ファイルから読み取るリーダーを作成する。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @throws FileNotFoundException
     * @version 2010/03/31 : 新規作成
     */
    public static InputStreamBufferReader createByFile(String path) throws FileNotFoundException {
        return new InputStreamBufferReader(new FileInputStream(path));
    }

    /**
     * ファイルをバイト配列に読み込み、それを返す。
     *
     * @author eagle.sakura
     * @param path
     *            ファイルへの絶対、もしくは相対パス
     * @return 読み込んだファイルのバイナリ
     * @throws FileNotFoundException
     * @version 2010/03/31 : 新規作成
     */
    public static final byte[] createFileBuffer(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] result = new byte[fis.available()];

        // ! バッファを取得
        fis.read(result);
        fis.close();

        return result;
    }

}
