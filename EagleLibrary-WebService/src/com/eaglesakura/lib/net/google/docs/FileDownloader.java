/**
 *
 */
package com.eaglesakura.lib.net.google.docs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.eaglesakura.lib.net.google.docs.GoogleDocsEntries.Entry;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * @author SAKURA
 *
 */
public class FileDownloader {

    /**
     * キャッシュファイル名
     */
    File cache = null;

    /**
     * 書込み先のファイル
     */
    File dstFile = null;

    String srcFileName = null;

    String cacheFileExt = "";

    GoogleDocsDownloader downloader = null;

    boolean resume = false;

    public FileDownloader(String srcFileName, File dstFile) {
        this.srcFileName = srcFileName;
        this.dstFile = dstFile;
    }

    /**
     * ダウンロードが終了していたらtrueを返す。
     * @return
     */
    public boolean isDownloadFinished() {
        return dstFile.exists();
    }

    /**
     * キャッシュファイルの拡張子を指定する。<BR>
     * 明示的にキャッシュ／コンプリートファイルを切り替える場合に有効
     * @param cacheFileExt
     */
    public void setCacheFileExt(String cacheFileExt) {
        this.cacheFileExt = cacheFileExt;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    String token = null;
    String gmail = null;
    String password = null;

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccount(String gmail, String pass) {
        this.gmail = gmail;
        this.password = pass;
    }

    /**
     * ダウンロードを開始する。<BR>
     * ダウンロード済みの場合falseを返す。
     * @param token
     * @throws IOException
     */
    public boolean start() throws IOException {
        if (token != null) {
            return start(token);
        } else {
            return start(gmail, password);
        }

    }

    /**
     * ダウンロードを開始する。<BR>
     * ダウンロード済みの場合falseを返す。
     * @param token
     * @throws IOException
     */
    private boolean start(String email, String password) throws IOException {
        cache = new File(dstFile.getAbsoluteFile() + cacheFileExt);

        //! URLを探す
        GoogleDocsEntries entries = new GoogleDocsEntries(email, password);
        entries.search(srcFileName, true);

        if (entries.getEntriesCount() == 0) {
            throw new FileNotFoundException("not exists google docs : " + srcFileName);
        }
        Entry entry = entries.getEntry(0);

        if (cache.exists() && cache.length() >= entry.getContentSize()) {
            return false;
        }

        //! ダウンロードを開始する
        downloader = new GoogleDocsDownloader(email, password);
        if (resume) {
            downloader.setContentStartAndEnd(cache.length(), entry.getContentSize());
        } else {
            cache.delete();
        }
        downloader.start(entry);

        return true;
    }

    /**
     * ダウンロードを開始する。<BR>
     * ダウンロード済みの場合falseを返す。
     * @param token
     * @throws IOException
     */
    private boolean start(String token) throws IOException {
        cache = new File(dstFile.getAbsoluteFile() + cacheFileExt);

        //! URLを探す
        GoogleDocsEntries entries = new GoogleDocsEntries(token);
        entries.search(srcFileName, true);

        if (entries.getEntriesCount() == 0) {
            throw new FileNotFoundException("not exists google docs : " + srcFileName);
        }
        Entry entry = entries.getEntry(0);

        if (cache.exists() && cache.length() >= entry.getContentSize()) {
            return false;
        }

        //! ダウンロードを開始する
        downloader = new GoogleDocsDownloader(token);
        if (resume) {
            downloader.setContentStartAndEnd(cache.length(), entry.getContentSize());
        } else {
            cache.delete();
        }
        downloader.start(entry);

        return true;
    }

    /**
     * 指定バイト数ダウンロードを行う。
     * @param length
     * @return ダウンロードが完了したらtrue
     * @throws IOException
     */
    public boolean download(int length) throws IOException {
        //! キャッシュファイルを探す
        boolean complete = false;

        OutputStream stream = new FileOutputStream(cache, (cache.exists() && cache.length() > 0) || resume);
        {
            complete = downloader.downloadBytes(length, stream);
        }
        stream.close();

        if (complete) {

            //! キャッシュをリネーム
            if (!dstFile.getAbsolutePath().equals(cache.getAbsolutePath())) {
                dstFile.delete();
                cache.renameTo(dstFile);
            }

            try {
                downloader.close();
            } catch (Exception e) {
                EagleUtil.log(e);
            }

            return true;
        }

        return complete;
    }

    /**
     * 処理を中断する。
     * @throws IOException
     */
    public void abort() throws IOException {
        downloader.close();
        downloader = null;
    }
}
