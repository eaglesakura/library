/**
 *
 * @author eagle.sakura
 * @version 2010/02/23 : 新規作成
 */
package com.eaglesakura.lib.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.eaglesakura.lib.util.Disposable;

/**
 * @author eagle.sakura
 * @version 2010/02/23 : 新規作成
 */
public class FileAccessStream implements Disposable, IBufferWriter, IBufferReader {
    /**
     * ファイルアクセス用。
     */
    private RandomAccessFile raf = null;
    /**
     * 書き込み用ストリーム。
     */
    private DataOutputStream dos = null;
    /**
     * 読み込み用ストリーム。
     */
    private DataInputStream dis = null;

    /**
     * ランダムアクセスファイルへの書き込みを行う。
     *
     * @author eagle.sakura
     * @param raf
     * @version 2010/02/23 : 新規作成
     */
    public FileAccessStream(RandomAccessFile raf) {
        this.raf = raf;
        dos = new DataOutputStream(this);
        dis = new DataInputStream(this);
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @throws IOException
     * @version 2010/06/21 : 新規作成
     */
    @Override
    public int getBufferSize() throws IOException {
        return (int) raf.length();
    }

    /**
     * 書き込み用ストリームを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/25 : 新規作成
     */
    public DataOutputStream getOutputStream() {
        return dos;
    }

    /**
     * 読み込み用のストリームを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/25 : 新規作成
     */
    public DataInputStream getInputStream() {
        return dis;
    }

    /**
     * アクセス用のインスタンスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/31 : 新規作成
     */
    public RandomAccessFile getRandomAccessFile() {
        return raf;
    }

    /**
     * 現在のファイル位置を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/02/23 : 新規作成
     */
    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    /**
     * 現在位置からファイルポインタを移動する。
     */
    public static final int eSeekTypeCurrent = 0;

    /**
     * 指定したファイルポインタの位置に直接移動する。
     */
    public static final int eSeekTypeSet = 1;

    /**
     * ファイルの末尾から加算する。<BR>
     * この場合、positionに正の値を与えてはならない。
     */
    public static final int eSeekTypeEnd = 2;

    /**
     * ファイルポインタの位置を変更する。
     *
     * @author eagle.sakura
     * @param position
     * @param type
     * @version 2010/02/23 : 新規作成
     */
    @Override
    public void seek(int eSeekType, int position) throws IOException {
        switch (eSeekType) {
        case eSeekTypeCurrent:
            raf.seek(getFilePointer() + position);
            break;
        case eSeekTypeSet:
            raf.seek(position);
            break;
        case eSeekTypeEnd:
            raf.seek(raf.length() + position);
            break;
        }
    }

    /**
     * ファイルポインタを指定の位置に移動する。
     *
     * @author eagle.sakura
     * @param position
     * @version 2010/02/23 : 新規作成
     */
    public void seekFilePointer(long position) throws IOException {
        raf.seek(position);
    }

    /**
     * 管理している資源の開放を行う。
     *
     * @author eagle.sakura
     * @version 2010/02/23 : 新規作成
     */
    @Override
    public void dispose() {
        try {
            raf.close();
            raf = null;
            dos = null;
            dis = null;
        } catch (IOException ioe) {

        } catch (Exception e) {

        }
    }

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
     * @version 2010/03/25 : 新規作成
     */
    @Override
    public int readBuffer(byte[] result, int head, int length) throws IOException {
        return raf.read(result, head, length);
    }

    /**
     * @author eagle.sakura
     * @param buf
     * @param position
     * @param length
     * @version 2010/02/23 : 新規作成
     */
    @Override
    public int writeBuffer(byte[] buf, int position, int length) throws IOException {
        raf.write(buf, position, (int) length);
        return length;
    }

}
