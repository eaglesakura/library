/**
 *
 * @author eagle.sakura
 * @version 2010/03/13 : 新規作成
 */
package eagle.io;

import java.io.IOException;

import eagle.util.Disposable;

/**
 * バッファからの読み取りをサポートするインターフェース。<BR>
 * このインターフェースを実装するクラスは、 {@link Disposable}も実装すること。
 *
 * @author eagle.sakura
 * @version 2010/03/13 : 新規作成
 */
public interface IBufferReader {
    /**
     * バッファからの読み取りを行う。<BR>
     * result[ head ]からresult[ head + length ]までにlengthバイトのデータを書き込む。
     *
     * @author eagle.sakura
     * @param result
     *            データを返す配列
     * @param head
     *            データ書き込み開始位置
     * @param length
     *            データを書き込む長さ
     * @return 読み込みに成功したバイト数
     * @version 2010/03/13 : 新規作成
     */
    public int readBuffer(byte[] result, int head, int length) throws IOException;

    /**
     * 現在位置からシークする。
     */
    public static final int eSeekTypeCurrent = 0;

    /**
     * シーク位置を直接指定する。
     */
    public static final int eSeekTypeSet = 1;

    /**
     * 指定した位置へポインタを移動する。
     *
     * @author eagle.sakura
     * @param eSeekType
     * @param length
     * @throws IOException
     * @version 2010/03/25 : 新規作成
     */
    public void seek(int eSeekType, int length) throws IOException;

    /**
     * バッファの大きさを取得する。<BR>
     * 不明である場合、負の値を返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/21 : 新規作成
     */
    public int getBufferSize() throws IOException;

    /**
     * バッファの開放を行う。
     *
     * @author eagle.sakura
     * @version 2010/03/13 : 新規作成
     */
    public void dispose();
}
