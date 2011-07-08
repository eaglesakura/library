/**
 *
 * @author eagle.sakura
 * @version 2010/03/13 : 新規作成
 */
package eagle.io;

import java.io.IOException;

import eagle.util.Disposable;

/**
 * バッファへのデータ書き込みを行うインターフェース。<BR>
 * このインターフェースを実装するクラスは、 {@link Disposable}も実装すること。
 *
 * @author eagle.sakura
 * @version 2010/03/13 : 新規作成
 */
public interface IBufferWriter {
    /**
     * 管理しているデータの書き込みを行う。
     *
     * @author eagle.sakura
     * @param data
     *            書き込みを行うデータ配列
     * @param head
     *            書き込み開始位置
     * @param length
     *            書き込みを行うバイト数
     * @return 書き込みに成功したバイト数を返す。
     * @version 2010/03/13 : 新規作成
     */
    public int writeBuffer(byte[] data, int head, int length) throws IOException;

    /**
     * 読み飛ばしを行う。
     *
     * @author eagle.sakura
     * @param length
     * @throws IOException
     * @version 2010/03/13 : 新規作成
     */
    public void seek(int eSeekType, int position) throws IOException;

    /**
     * バッファの開放を行う。
     *
     * @author eagle.sakura
     * @version 2010/03/13 : 新規作成
     */
    public void dispose();
}
