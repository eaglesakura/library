/**
 *
 * @author eagle.sakura
 * @version 2010/03/03 : 新規作成
 */
package eagle.util;

/**
 * ライブラリ内で発生した特有エラーを示す。
 *
 * @author eagle.sakura
 * @version 2010/03/03 : 新規作成
 */
public class EagleException extends Exception {
    public static final long serialVersionUID = 1;

    private int status;
    /**
     * ファイルバージョンが不正であることを示す。
     */
    public static final int eStatusUnknownFileVersion = 1;
    /**
     * ファイルタイプが不正であることを示す。
     */
    public static final int eStatusUnknownFileType = 2;

    /**
     * エラーステータスを指定して生成する。
     *
     * @author eagle.sakura
     * @param status
     * @version 2010/03/03 : 新規作成
     */
    public EagleException(int status) {
        this.status = status;
    }

    /**
     * エラー内容を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/03 : 新規作成
     * @see #eStatusUnknownFileType
     * @see #eStatusUnknownFileVersion
     */
    public int getStatus() {
        return status;
    }
}
