package com.eaglesakura.lib.net.google.docs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.eaglesakura.lib.util.EagleUtil;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;

/**
 * Google Docsからファイルをダウンロードする。
 * キャンセル・レジューム・レンジ指定に対応。
 * @author SAKURA
 *
 */
public class GoogleDocsDownloader {

    /**
     * アクセストークン
     */
    String token = null;

    String gmail = null;
    String password = null;

    /**
     * サーバーとの接続
     */
    HttpResponse response = null;

    String applicationName = "Eagle/GoogleDocsDownloader";

    /**
     * 一時的なバッファ
     */
    byte[] buffer = new byte[1024 * 5];

    /**
     * ダウンロード用ストリーム。
     */
    InputStream stream = null;

    HttpTransport downTrans = GoogleTransport.create();
    GoogleHeaders downHeader = (GoogleHeaders) downTrans.defaultHeaders;
    HttpRequest downRequest = null;

    long rangeStart = -1;
    long rangeLength = -1;

    public GoogleDocsDownloader(String token) {
        this.token = token;
    }

    public GoogleDocsDownloader(String gmail, String password) {
        this.gmail = gmail;
        this.password = password;
    }

    /**
     * ダウンロードする長さを設定する。
     * @param start
     * @param length
     */
    public void setContentStartAndLength(long start, long length) {
        rangeStart = start;
        rangeLength = length;
    }

    /**
     * ダウンロードする長さを設定する。
     * @param start
     * @param length
     */
    public void setContentStartAndEnd(long start, long end) {
        rangeStart = start;
        rangeLength = end - start + 1;
    }

    void loginEmail(HttpTransport transport) throws IOException {

        ClientLogin authenticator = new ClientLogin();
        authenticator.applicationName = applicationName;
        authenticator.authTokenType = "writely";
        authenticator.username = gmail;
        authenticator.password = password;

        Response authenticate = authenticator.authenticate();
        authenticate.setAuthorizationHeader(transport);
    }

    private HttpResponse getResponse(String url, long itemSize) throws IOException {
        downHeader.setApplicationName(applicationName);
        downHeader.gdataVersion = "3";

        if (token != null) {
            downHeader.setGoogleLogin(token);
        } else {
            //            downHeader.setBasicAuthentication(gmail, password);
            loginEmail(downTrans);
        }

        downHeader.range = null;
        /*
        if (dstFile.exists() && dstFile.length() > 0) {
            downHeader.range = "bytes=" + dstFile.length() + "-" + (itemSize - 1);
            EagleUtil.log("Range : " + downHeader.range);
        }
        */

        if (rangeStart >= 0 && rangeLength > 0) {
            downHeader.range = "bytes=" + rangeStart + "-" + (rangeStart + rangeLength - 1);
        }

        downRequest = downTrans.buildGetRequest();

        if (url.endsWith("&e=download&gd=true")) {
            url = url.replaceAll("&e=download&gd=true", "");
        }
        downRequest.setUrl(url);
        try {
            return downRequest.execute();
        } catch (HttpResponseException re) {
            if (re.response != null && re.response.headers != null && re.response.headers.get("Location") != null) {
                try {
                    if (re.response.getContent() != null) {
                        EagleUtil.log("close!");
                        re.response.getContent().close();
                    }
                    re.response.ignore();
                } catch (Exception e) {
                    EagleUtil.log(e);
                }
                EagleUtil.log("Redirect : " + re.response.headers.get("Location").toString());
                return getResponse(re.response.headers.get("Location").toString(), itemSize);
            } else {
                EagleUtil.log(re);
            }
        }
        throw new FileNotFoundException(url);
    }

    /**
     * ダウンロードを開始する。
     * @param url
     * @throws IOException
     */
    public void start(GoogleDocsEntries.Entry entry) throws IOException {
        EagleUtil.log(entry.getTitle() + " :: " + entry.getContentSize() + "bytes = " + entry.getContentUrl());
        start(entry.getContentUrl(), entry.getContentSize());
    }

    /**
     * ダウンロードを開始する。
     * @param url
     * @param length
     * @throws IOException
     */
    public void start(String url, long length) throws IOException {
        response = getResponse(url, length);
        EagleUtil.log("header : " + response.headers);
        stream = response.getContent();
    }

    /**
     *
     * @param length ダウンロードバイト数
     * @param os 書込み先ストリーム
     * @return ダウンロードが終了したらtrue
     * @throws IOException
     */
    public boolean downloadBytes(int length, OutputStream os) throws IOException {

        int size = 0;
        while ((size = stream.read(buffer, 0, length <= buffer.length ? length : buffer.length)) > 0) {

            //! 書きこむ
            os.write(buffer, 0, size);

            //! 残り読み込みサイズを縮める。
            length -= size;
        }

        //        EagleUtil.log(dstFile.getName() + " : " + ((float) dstFile.length() / 1024 / 1024) + "MB");

        if (length > 0) {
            //! DLが終了した
            return true;
        } else {
            return false;
        }
    }

    /**
     * ダウンロードを終了する。
     * @throws IOException
     */
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }

        response = null;
    }

}
