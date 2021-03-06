/**
 *
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
package com.eaglesakura.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.eaglesakura.lib.math.Vector2;

/**
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
public class EagleUtil {
    /**
     * GLの固定小数点で1.0を示す。
     */
    public static final int eGLFixed1_0 = 0x10000;
    /**
     * MCの固定小数点で1.0を示す。
     */
    public static final int eMCFixed1_0 = 0x1000;

    /**
     * ShiftJISを示すエンコード文字列。<BR>
     * C++関連のライブラリと連携するため、<BR>
     * 基本的にSJISをIO用の文字コードに使用する。
     */
    public static final String eEncodeSJIS = "Shift_JIS";

    /**
     * 標準のユーティリティを作成する。
     */
    private static IEagleUtilBridge utilBridge = new IEagleUtilBridge() {
        /**
         * ログはprintlnで行う。
         *
         * @author eagle.sakura
         * @param eLogType
         * @param message
         * @version 2010/03/31 : 新規作成
         */
        @Override
        public void log(int eLogType, String message) {
            System.out.println(message);
        }

        /**
         *
         * @author eagle.sakura
         * @return
         * @version 2010/09/12 : 新規作成
         */
        @Override
        public int getPlatformVersion() {
            return 0;
        }
    };

    /**
     * 手軽な乱数生成。<BR>
     * この乱数に再現性はない。
     */
    private static Random rand = new Random();

    /**
     * ユーティリティ初期化を行う。
     *
     * @author eagle.sakura
     * @param bridge
     * @version 2010/03/20 : 新規作成
     */
    public static void init(IEagleUtilBridge bridge) {
        utilBridge = bridge;
    }

    /**
     * 係数ブレンドを行い、結果を返す。
     * @param a
     * @param b
     * @param blend aのブレンド値
     * @return
     */
    public static float blendValue(float a, float b, float blend) {
        return a * blend + b * (1.0f - blend);
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/12 : 新規作成
     */
    public static IEagleUtilBridge getBridge() {
        return utilBridge;
    }

    /**
     * 乱数を取得する。<BR>
     * この乱数の再現性は保証されない。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/12/01 : 新規作成
     */
    public static final int getRand() {
        return rand.nextInt();
    }

    /**
     * min～maxまでの乱数を取得する。
     *
     * @author eagle.sakura
     * @param min
     * @param max
     * @return
     * @version 2009/12/01 : 新規作成
     */
    public static final int getRand(int min, int max) {
        return min + ((getRand() & 0x7fffffff) % (max - min));
    }

    /**
     * 指定箇所へファイルをコピーする。
     * @param src
     * @param dst
     * @return
     */
    public static final void copy(File src, File dst) throws IOException {
        InputStream srcStream = new FileInputStream(src);
        OutputStream dstStream = new FileOutputStream(dst);

        //! 適当なバッファサイズを決める。
        byte[] buffer = new byte[128 * 1024];
        int readed = 0;

        //! 読めなくなるまで読み込みを続ける
        while ((readed = srcStream.read(buffer)) > 0) {
            dstStream.write(buffer, 0, readed);
        }

        srcStream.close();
        dstStream.close();

        //! 最終変更日を修正する
        dst.setLastModified(src.lastModified());
    }

    /**
     * 0～255の色情報をひとつのintにまとめる。
     *
     * @author eagle.sakura
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     * @version 2010/10/11 : 新規作成
     */
    public static final int toColorRGBA(int r, int g, int b, int a) {
        return r << 24 | g << 16 | b << 8 | a;
    }

    /**
     *
     * @author eagle.sakura
     * @param rgba
     * @return
     * @version 2010/10/11 : 新規作成
     */
    public static final int toColorR(int rgba) {
        return (rgba >> 24) & 0xff;
    }

    /**
     *
     * @author eagle.sakura
     * @param rgba
     * @return
     * @version 2010/10/11 : 新規作成
     */
    public static final int toColorG(int rgba) {
        return (rgba >> 16) & 0xff;
    }

    /**
     *
     * @author eagle.sakura
     * @param rgba
     * @return
     * @version 2010/10/11 : 新規作成
     */
    public static final int toColorB(int rgba) {
        return (rgba >> 8) & 0xff;
    }

    /**
     *
     * @author eagle.sakura
     * @param rgba
     * @return
     * @version 2010/10/11 : 新規作成
     */
    public static final int toColorA(int rgba) {
        return (rgba >> 0) & 0xff;
    }

    /**
     * min <= result <= maxとなるようにnowを補正する。
     *
     * @author eagle.sakura
     * @param min
     * @param max
     * @param now
     * @return
     * @version 2009/12/06 : 新規作成
     */
    public static final int minmax(int min, int max, int now) {
        if (now < min)
            return min;
        if (now > max)
            return max;
        return now;
    }

    /**
     * min <= result <= maxとなるようにnowを補正する。
     *
     * @author eagle.sakura
     * @param min
     * @param max
     * @param now
     * @return
     * @version 2009/12/06 : 新規作成
     */
    public static final float minmax(float min, float max, float now) {
        if (now < min)
            return min;
        if (now > max)
            return max;
        return now;
    }

    /**
     * 目標数値へ移動する。
     *
     * @author eagle.sakura
     * @param now
     * @param offset
     * @param target
     * @return
     * @version 2009/12/02 : 新規作成
     */
    public static final int targetMove(int now, int offset, int target) {
        offset = Math.abs(offset);
        if (Math.abs(target - now) <= offset) {
            return target;
        } else if (target > now) {
            return now + offset;
        } else {
            return now - offset;
        }
    }

    /**
     * 目標数値へ移動する。
     *
     * @author eagle.sakura
     * @param now
     * @param offset
     * @param target
     * @return
     * @version 2009/12/02 : 新規作成
     */
    public static final float targetMove(float now, float offset, float target) {
        offset = Math.abs(offset);
        if (Math.abs(target - now) <= offset) {
            return target;
        } else if (target > now) {
            return now + offset;
        } else {
            return now - offset;
        }
    }

    /**
     * 360度系の正規化を行う。
     *
     * @author eagle.sakura
     * @param now
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public static final float normalizeDegreeHalf(float now) {
        now = normalizeDegree(now);

        if (now > 180.0f) {
            now -= 360.0f;
        }
        return now;
    }

    /**
     * 角度Aから見た角度Bへの角度の差を返す。
     * 時計回りは負、反時計回りは正の値を返す。
     * @param a
     * @param b
     * @return
     */
    public static float getAngleDiff(float a, float b) {
        float result = normalizeDegree(b) - normalizeDegree(a);
        if (result > 180.0f) {
            result -= 360.0f;
        }
        if (result < -180.0f) {
            result += 360.0f;
        }

        return result;
    }

    /**
     * centerから見たpositionが何度になるか360度系で返す。
     * １２時の方向が0度で、反時計回りに角度を進ませる。
     * @param center
     * @param position
     * @return
     */
    public static float getAngleHalfDegree(Vector2 center, Vector2 position) {
        float result = getAngleDegree(center, position);

        if (result > 180.f) {
            result -= 360.0f;
        }

        return result;
    }

    /**
     * centerから見たpositionが何度になるか360度系で返す。
     * １２時の方向が0度で、反時計回りに角度を進ませる。
     * @param center
     * @param position
     * @return
     */
    public static float getAngleDegree(Vector2 center, Vector2 position) {
        float result = 0;

        Vector2 temp = new Vector2(position.x - center.x, position.y - center.y);
        if (temp.length() == 0) {
            return 0;
        }
        temp.normalize();

        result = (float) (Math.atan2(temp.y, temp.x) / Math.PI);
        result /= 2;
        result -= 0.25f;

        return normalizeDegree(result * 360.0f);
    }

    /**
     *
     * @param center
     * @param position
     * @return
     */
    public static float getAngleDegree2D(Vector2 center, Vector2 position) {
        return normalizeDegree(-getAngleDegree(center, position) - 180);
    }

    /**
     * ジョグダイアルを回す場合の補助
     * @param center ジョグダイアル中央
     * @param current 現在のクリック位置
     * @param scrollVector スクロール方向
     * @return
     */
    public static float getJogRotate2D(Vector2 center, Vector2 current, Vector2 scrollVector) {
        Vector2 before = new Vector2(current.x + scrollVector.x, current.y + scrollVector.y);

        final float currentRotate = EagleUtil.getAngleDegree2D(center, current);
        final float beforeRotate = EagleUtil.getAngleDegree2D(center, before);
        final float rotateDiff = EagleUtil.getAngleDiff(currentRotate, beforeRotate);
        return rotateDiff;
    }

    /**
     * 360度系の正規化を行う。
     *
     * @author eagle.sakura
     * @param now
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public static final float normalizeDegree(float now) {
        while (now < 0.0f) {
            now += 360.0f;
        }

        while (now >= 360.0f) {
            now -= 360.0f;
        }

        return now;
    }

    /**
     * フラグが立っていることを検証する。
     *
     * @author eagle.sakura
     * @param flg
     * @param check
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final boolean isFlagOn(int flg, int check) {
        return (flg & check) != 0;
    }

    /**
     * フラグが立っていることを検証する。
     *
     * @author eagle.sakura
     * @param flg
     * @param check
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final boolean isFlagOnAll(int flg, int check) {
        return (flg & check) == 0;
    }

    private static boolean output = true;

    /**
     * 出力の有無を指定する。
     * @param output
     */
    public static void setOutput(boolean output) {
        EagleUtil.output = output;
    }

    /**
     * ログメッセージを出力する。
     *
     * @author eagle.sakura
     * @param message
     * @version 2010/03/13 : 新規作成
     */
    public static final void logInfo(String message) {
        if (output && utilBridge != null) {
            utilBridge.log(IEagleUtilBridge.eLogTypeInfo, message);
        }
    }

    /**
     * ログメッセージを出力する。<BR>
     * 出力される場所は環境依存である。
     *
     * @author eagle.sakura
     * @param message
     * @version 2010/03/13 : 新規作成
     */
    public static final void log(String message) {
        if (output && utilBridge != null) {
            utilBridge.log(IEagleUtilBridge.eLogTypeInfo, message);
        }
    }

    /**
     * ログメッセージを出力する。
     *
     * @author eagle.sakura
     * @param message
     * @version 2010/03/13 : 新規作成
     */
    public static final void logDebug(String message) {
        if (output && utilBridge != null) {
            utilBridge.log(IEagleUtilBridge.eLogTypeDebug, message);
        }
    }

    /**
     * 例外情報を出力。
     *
     * @author eagle.sakura
     * @param e
     * @version 2010/03/26 : 新規作成
     */
    public static final void log(Exception e) {
        if (output && utilBridge != null && e != null) {
            utilBridge.log(IEagleUtilBridge.eLogTypeDebug, "" + e);
            utilBridge.log(IEagleUtilBridge.eLogTypeDebug, "" + e.getMessage());
            StackTraceElement steArray[] = e.getStackTrace();

            for (StackTraceElement ste : steArray) {
                utilBridge.log(IEagleUtilBridge.eLogTypeDebug, ste.getClassName() + " ( " + ste.getLineNumber() + " )");
            }
            e.printStackTrace();
        }
    }

    /**
     * フラグ情報を設定する。
     *
     * @author eagle.sakura
     * @param flg
     * @param check
     * @param is
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final int setFlag(int flg, int check, boolean is) {
        if (is)
            return flg | check;
        else
            return flg & (~check);
    }

    /**
     * ストリームをbyte配列に直す。
     * isはcloseされる。
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] decodeStream(InputStream is) throws IOException {
        byte[] result = null;

        //! 1kbずつ読み込む。
        byte[] tempBuffer = new byte[1024 * 5];
        //! 元ストリームを読み取り
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int n = 0;
            while ((n = is.read(tempBuffer)) > 0) {
                baos.write(tempBuffer, 0, n);
            }
            result = baos.toByteArray();
            is.close();
        }

        return result;
    }

    /**
     * 指定URLからデータを取得する。
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] getURLData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection
                .setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Linux; U; Android 1.6; ja-jp; SonyEricssonSO-01B Build/R1EA018)AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1");
        //        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
        if (basicAutorization != null) {
            connection.setRequestProperty("Authorization", "Basic " + basicAutorization);
        }

        connection.setConnectTimeout(1000 * 10);
        connection.connect();
        connection.getResponseCode();

        //        EagleUtil.log(connection.getHeaderFields().toString());
        byte[] datas = null;
        datas = decodeStream(connection.getInputStream());

        return datas;
    }

    /**
     * 指定URLからデータを取得する。
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] getURLData(String url, String userAgent) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setRequestMethod("GET");

        if (userAgent != null) {
            connection.setRequestProperty("User-Agent", userAgent);
        }
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.connect();
        connection.getResponseCode();

        String encode = null;

        List<String> listStrings = connection.getHeaderFields().get("Content-Encoding");
        if (listStrings != null && listStrings.size() > 0) {
            encode = listStrings.get(0);
            log("encoding : " + listStrings);
        }
        byte[] datas = null;

        if ("gzip".equals(encode)) {
            datas = decodeStream(new GZIPInputStream(connection.getInputStream()));
        } else {
            datas = decodeStream(connection.getInputStream());
        }

        return datas;
    }

    /**
     * POST/PUTの状態を取得する。
     * @author SAKURA
     *
     */
    public static class HttpResult {
        public int status;
        public byte[] datas;
    }

    static String basicAutorization = null;

    /**
     * ベーシック認証の認証文字列を設定する。
     * user:passをbase64したものを渡すこと。
     * @param basicAutorization
     */
    public static void setBasicAutorization(String basicAutorizationBase64) {
        EagleUtil.basicAutorization = basicAutorizationBase64;
    }

    /**
     * 指定URLへデータを送信する。
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpResult postURLData(String url, byte[] data, String contentType) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", contentType); // ヘッダを設定
        connection.getOutputStream().write(data);
        connection.getOutputStream().close();
        byte[] datas = null;
        try {
            datas = decodeStream(connection.getInputStream());
        } catch (Exception e) {
            datas = decodeStream(connection.getErrorStream());
        }
        int resp = connection.getResponseCode();
        EagleUtil.log("Response : " + resp);

        HttpResult result = new HttpResult();
        result.status = resp;
        result.datas = datas;
        return result;
    }

    /**
     * 指定URLへデータを送信する。
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpResult putURLData(String url, byte[] data, String contentType) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", contentType); // ヘッダを設定
        connection.setRequestProperty("Content-Length", "" + data.length); //!<    データの長さを指定
        if (basicAutorization != null) {
            connection.setRequestProperty("Authorization", "Basic " + basicAutorization);
        }
        connection.connect();

        OutputStream os = connection.getOutputStream();
        os.write(data);
        os.close();
        int resp = connection.getResponseCode();
        byte[] datas = null;
        try {
            datas = decodeStream(connection.getInputStream());
        } catch (Exception e) {
            datas = decodeStream(connection.getErrorStream());
        }

        HttpResult result = new HttpResult();
        result.status = resp;
        result.datas = datas;
        return result;
    }

    /**
     * 指定したミリ秒、呼び出したスレッドを止める。
     * @param ms
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            log(e);
        }
    }

    /**
     * byte配列からMD5を求める
     * @param buffer
     * @return
     */
    public static String genMD5(byte[] buffer) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            byte[] digest = md.digest();

            StringBuffer sBuffer = new StringBuffer(digest.length * 2);
            for (byte b : digest) {
                String s = Integer.toHexString(((int) b) & 0xff);

                if (s.length() == 1) {
                    sBuffer.append('0');
                }
                sBuffer.append(s);
            }
            return sBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ファイルからMD5を求める。
     * @param file
     * @return
     */
    public static String genMD5(File file) {
        try {

            FileInputStream is = new FileInputStream(file);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            {
                byte[] buffer = new byte[128 * 1024];
                int readed = 0;

                while ((readed = is.read(buffer)) > 0) {
                    md.update(buffer, 0, readed);
                }
            }
            is.close();
            byte[] digest = md.digest();

            StringBuffer sBuffer = new StringBuffer(digest.length * 2);
            for (byte b : digest) {
                String s = Integer.toHexString(((int) b) & 0xff);

                if (s.length() == 1) {
                    sBuffer.append('0');
                }
                sBuffer.append(s);
            }
            return sBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String zenkakuHiraganaToZenkakuKatakana(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c >= 'ァ' && c <= 'ン') {
                sb.setCharAt(i, (char) (c - 'ァ' + 'ぁ'));
            } else if (c == 'ヵ') {
                sb.setCharAt(i, 'か');
            } else if (c == 'ヶ') {
                sb.setCharAt(i, 'け');
            } else if (c == 'ヴ') {
                sb.setCharAt(i, 'う');
                sb.insert(i + 1, '゛');
                i++;
            }
        }

        return sb.toString();
    }

    /**
     * 日本語を意識してJavaの辞書順に並び替える
     * @param a
     * @param b
     * @return
     */
    public static int compareString(String a, String b) {
        a = zenkakuHiraganaToZenkakuKatakana(a.toLowerCase());
        b = zenkakuHiraganaToZenkakuKatakana(b.toLowerCase());

        return a.compareTo(b);
    }

    /**
     * ZIPファイルを特定ディレクトリに解凍する。
     * @param root
     * @param zip
     */
    public static void unzip(File root, InputStream zip) throws IOException {

        {
            ZipInputStream zipStream = new ZipInputStream(zip);
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null) {
                String _filePath = "";
                {
                    String[] roots = entry.getName().split("/");
                    String _name = "";
                    for (int i = 0; i < (roots.length - 1); ++i) {
                        (new File(root + _name + "/" + roots[i])).mkdir();
                        _name += ("/" + roots[i]);
                    }

                    for (String str : roots) {
                        if (_filePath.length() > 0) {
                            _filePath += "/";
                        }
                        _filePath += str;
                    }
                }

                File unzipFile = new File(root, _filePath);

                if (entry.isDirectory()) {
                    unzipFile.mkdir();
                } else {
                    FileOutputStream os = new FileOutputStream(unzipFile);
                    {
                        int size = 0;
                        byte[] buffer = new byte[1024 * 128];
                        while ((size = zipStream.read(buffer)) > 0) {
                            os.write(buffer, 0, size);
                        }
                    }
                    os.close();
                }
            }
        }
        zip.close();

    }
}
