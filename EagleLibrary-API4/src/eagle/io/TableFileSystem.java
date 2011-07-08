/**
 *
 * @author eagle.sakura
 * @version 2010/06/30 : 新規作成
 */
package eagle.io;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import eagle.util.EagleException;

/**
 * @author eagle.sakura
 * @version 2010/06/30 : 新規作成
 */
public class TableFileSystem extends FileSystem {
    private Map<String, byte[]> fileTable = new HashMap<String, byte[]>();

    /**
     * オンメモリのbyte[]をファイルとして扱い、それを管理する。
     *
     * @author eagle.sakura
     * @version 2010/06/30 : 新規作成
     */
    public TableFileSystem() {

    }

    /**
     *
     * @author eagle.sakura
     * @param key
     * @param file
     * @version 2010/06/30 : 新規作成
     */
    public void addFile(String key, byte[] file) {
        fileTable.put(key, file);
    }

    /**
     *
     * @author eagle.sakura
     * @param key
     * @return
     * @version 2010/07/30 : 新規作成
     */
    public byte[] getFile(String key) {
        return fileTable.get(key);
    }

    /**
     * ファイルテーブルの内容をzipへ圧縮する。
     *
     * @author eagle.sakura
     * @param os
     * @throws IOException
     * @version 2010/06/30 : 新規作成
     */
    public void complessZip(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);

        Iterator<String> itr = fileTable.keySet().iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            byte[] value = fileTable.get(key);

            zos.putNextEntry(new ZipEntry(key));
            zos.write(value);
            zos.closeEntry();
        }

        zos.close();
    }

    /**
     * 入力ストリームを生成する。<BR>
     * ここで生成されるのは必ず{@link ByteArrayInputStream}である。
     *
     * @author eagle.sakura
     * @param filePath
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/06/30 : 新規作成
     */
    @Override
    public InputStream createInputStream(String filePath) throws IOException, EagleException {
        byte[] file = fileTable.get(filePath);
        if (file != null) {
            return new ByteArrayInputStream(file);
        } else {
            return null;
        }
    }

    /**
     * 出力ストリームの生成は必ず例外を吐き出す。
     *
     * @author eagle.sakura
     * @param filePath
     * @return
     * @throws IOException
     * @throws EagleException
     * @version 2010/06/30 : 新規作成
     */
    @Override
    public OutputStream createOutputStream(String filePath) throws IOException, EagleException {
        throw new FileNotFoundException();
    }

    /**
     * Zipファイルの内部を解凍し、ファイルテーブルを生成する。
     *
     * @author eagle.sakura
     * @param zis
     * @return
     * @version 2010/06/30 : 新規作成
     */
    public static TableFileSystem createInstance(ZipInputStream zis) throws IOException {
        ZipEntry entry = null;
        TableFileSystem result = new TableFileSystem();

        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            byte[] buffer = new byte[(int) entry.getSize()];
            int header = 0;
            int read = 0;
            while ((read = zis.read(buffer, header, (buffer.length - header) >= 1024 ? 1024 : (buffer.length - header))) > 0) {
                header += read;
            }

            // ! ファイルを追加する。
            result.addFile(name, buffer);

            zis.closeEntry();
        }

        return result;
    }
}
