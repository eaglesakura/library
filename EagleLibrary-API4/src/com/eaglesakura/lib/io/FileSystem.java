/**
 *
 * @author eagle.sakura
 * @version 2010/04/05 : 新規作成
 */
package com.eaglesakura.lib.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.eaglesakura.lib.util.Disposable;
import com.eaglesakura.lib.util.EagleException;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * ファイルIO用のストリームを作成する。<BR>
 * 各プラットフォームごとに入出力先は最適化すること。
 *
 * @author eagle.sakura
 * @version 2010/04/05 : 新規作成
 */
public class FileSystem implements Disposable {
    /**
     * uniqueID 一意の識別子。乱数で問題ない。
     */
    protected int uniqueID = -1;

    /**
     * 対象フォルダ。
     */
    protected String directory = "";

    /**
     * @author eagle.sakura
     * @version 2010/04/05 : 新規作成
     */
    public FileSystem() {
        this.uniqueID = EagleUtil.getRand();
    }

    /**
     *
     * @param dir
     */
    public FileSystem(String dir) {
        this.uniqueID = EagleUtil.getRand();
        directory = dir;
    }

    /**
     * クラスの識別子を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/05 : 新規作成
     */
    public int getUniqueID() {
        return uniqueID;
    }

    /**
     * 管理している資源を開放する。
     *
     * @author eagle.sakura
     * @version 2010/04/05 : 新規作成
     */
    @Override
    public void dispose() {

    }

    /**
     * ファイル入力用バッファリーダーを作成する。<BR>
     * 文字コードはSJIS限定。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @version 2010/04/04 : 新規作成
     */
    public BufferedReader createFileReaderSJIS(String path) throws IOException, UnsupportedEncodingException, EagleException {
        return new BufferedReader(new InputStreamReader(createInputStream(directory + path), EagleUtil.eEncodeSJIS));
    }

    /**
     * ファイル出力用バッファライターを作成する。<BR>
     * 文字コードはSJIS限定。
     *
     * @author eagle.sakura
     * @param path
     *            書き込むファイルのパス
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @version 2010/04/04 : 新規作成
     */
    public BufferedWriter createFileWriterSJIS(String path) throws IOException, UnsupportedEncodingException, EagleException {
        return new BufferedWriter(new OutputStreamWriter(createOutputStream(directory + path), EagleUtil.eEncodeSJIS));
    }

    /**
     * 入力用ストリームを作成する。
     *
     * @author eagle.sakura
     * @param filePath
     *            ファイルへのパス
     * @return 入力用ストリーム
     * @throws IOException
     * @throws EagleException
     * @version 2010/04/05 : 新規作成
     */
    public InputStream createInputStream(String filePath) throws IOException, EagleException {
        return new FileInputStream(directory + filePath);
    }

    public File getFile(String filePath) {
        return new File(directory + filePath);
    }

    /**
     * 出力用ストリームを作成する。
     *
     * @author eagle.sakura
     * @param filePath
     *            ファイルへのパス
     * @return 出力用のストリーム
     * @throws IOException
     * @throws EagleException
     * @version 2010/04/05 : 新規作成
     */
    public OutputStream createOutputStream(String filePath) throws IOException, EagleException {
        return new FileOutputStream(directory + filePath);
    }

}
