/**
 *
 * @author eagle.sakura
 * @version 2009/12/04 : 新規作成
 */
package com.eaglesakura.lib.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 単一ディレクトリおよびそのサブディレクトリを管理する。
 *
 * @author eagle.sakura
 * @version 2009/12/04 : 新規作成
 */
public class Directory {
    private List<String> fileNames = new ArrayList<String>();
    private List<File> files = new ArrayList<File>();
    private String rootPath = "";

    /**
     * 特定ディレクトリ内の特定拡張子のファイルを列挙する。
     *
     * @author eagle.sakura
     * @param root
     * @param ext
     * @param isSub
     * @version 2009/12/04 : 新規作成
     */
    public Directory(String root, String ext, boolean isSub) {
        // ! slashで終わっていなかったら付け加える
        if (root.charAt(root.length() - 1) != '/') {
            root += "/";
        }

        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }

        enumlateFiles(root, ext, isSub);
    }

    /**
     *
     * @author eagle.sakura
     * @param root
     * @version 2010/06/17 : 新規作成
     */
    public Directory(String root) {
        // ! slashで終わっていなかったら付け加える
        if (root.charAt(root.length() - 1) != '/') {
            root += "/";
        }

        rootPath = root;
    }

    /**
     * 指定したファイルが存在する場合、そのファイル名を登録する。
     *
     * @author eagle.sakura
     * @param fileName
     * @version 2010/06/17 : 新規作成
     */
    public void registFileName(String fileName) {
        File file = new File(rootPath + fileName);
        if (file.exists()) {
            fileNames.add(fileName);
            files.add(file);
        }
    }

    /**
     * 指定した拡張子を含むファイル名を列挙する。
     *
     * @author eagle.sakura
     * @param root
     * @param ext
     * @param isSubDirectory
     * @version 2010/06/17 : 新規作成
     */
    public void enumlateFiles(String root, String ext, boolean isSub) {
        rootPath = root;
        File file = new File(root);
        if (file.isDirectory() && file.exists()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    if (files[i].isFile()) {
                        if (ext != null && ext.length() > 0) {
                            // ! 拡張子が一致した
                            if (getFileExt(files[i].getName()).toUpperCase().equals(ext.toUpperCase())) {
                                fileNames.add(files[i].getName());
                                this.files.add(files[i]);
                            }
                        }
                        // ! dbファイルは取り除く
                        else if (!getFileExt(files[i].getName()).equals("db")) {
                            fileNames.add(files[i].getName());
                            this.files.add(files[i]);
                        }
                    }
                }
            }
        }
    }

    /**
     * ファイル名の数を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/12/04 : 新規作成
     */
    public int getFileNameCount() {
        return fileNames.size();
    }

    /**
     * ファイルへのフルパスを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2009/12/04 : 新規作成
     */
    public String getFileFullPath(int index) {
        return rootPath + getFileName(index);
    }

    /**
     * 生情報を取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/06/13 : 新規作成
     */
    public File getFile(int index) {
        return files.get(index);
    }

    /**
     * ファイル名を取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2009/12/04 : 新規作成
     */
    public String getFileName(int index) {
        return fileNames.get(index);
    }

    /**
     * ファイル拡張子を取得する。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @version 2009/12/04 : 新規作成
     */
    public static String getFileExt(String fileName) {
        if (fileName == null)
            return "";
        int point = fileName.lastIndexOf('.');
        if (point != -1) {
            return fileName.substring(point + 1);
        } else {
            // fileName = "";
            return "";
        }
    }

    /**
     * 拡張子付のファイル名からファイル名のみを抜き出す
     *
     * @author eagle.sakura
     * @param fileName
     *            ファイル名
     * @return 拡張子を取り除いたファイル名
     * @version 2009/05/27 : 新規作成
     */
    public static String getFileName(final String fileName) {
        if (fileName == null)
            return "";
        int point = fileName.lastIndexOf('.');
        if (point != -1) {
            return fileName.substring(0, point);
        } else {
            // fileName = "";
            return "";
        }
    }

    /**
     * カレントディレクトリのパスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/31 : 新規作成
     */
    public static String getCurrentDirectoryPath() {
        return (new File(".").getAbsoluteFile().getParent());
    }

    /**
     * カレントディレクトリの管理パスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/31 : 新規作成
     */
    public static File getCurrentDirectory() {
        return (new File(".").getAbsoluteFile().getParentFile());
    }

    /**
     * ファイルを名前順にソートする。
     * @param files
     * @return
     */
    public static File[] sort(File[] files) {
        List<File> temp = new ArrayList<File>();
        for (File f : files) {
            temp.add(f);
        }

        Collections.sort(temp, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
                String f0 = object1.getAbsolutePath();
                String f1 = object2.getAbsolutePath();
                return f0.compareTo(f1);
            }
        });

        for (int i = 0; i < files.length; ++i) {
            files[i] = temp.get(i);
        }

        return files;
    }

    /**
     * 下の階層も含めてすべて削除する。
     * @param file
     */
    public static void deleteAll(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteAll(f);
                }
            }
            file.delete();
        }
    }
}
