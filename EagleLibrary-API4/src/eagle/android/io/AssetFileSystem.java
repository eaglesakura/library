/**
 *
 * @author eagle.sakura
 * @version 2010/06/24 : 新規作成
 */
package eagle.android.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import eagle.io.FileSystem;
import eagle.util.EagleException;

/**
 * apkのAssetから読み込むシステム。
 *
 * @author eagle.sakura
 * @version 2010/06/24 : 新規作成
 */
public class AssetFileSystem extends FileSystem {
    private Context context;

    /**
     *
     * @author eagle.sakura
     * @param context
     * @version 2010/06/24 : 新規作成
     */
    public AssetFileSystem(Context context) {
        this.context = context;
    }

    /**
     *
     * @author eagle.sakura
     * @param filePath
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/06/24 : 新規作成
     */
    @Override
    public InputStream createInputStream(String filePath) throws IOException, EagleException {
        return context.getAssets().open(filePath);
    }

    /**
     * Assetへは出力できないため、常に例外が投げられる。
     *
     * @author eagle.sakura
     * @param filePath
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/06/24 : 新規作成
     */
    @Override
    public OutputStream createOutputStream(String filePath) throws IOException, EagleException {
        throw new FileNotFoundException();
    }
}
