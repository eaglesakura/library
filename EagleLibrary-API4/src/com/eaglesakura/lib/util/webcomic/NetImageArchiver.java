package com.eaglesakura.lib.util.webcomic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.eaglesakura.lib.util.EagleUtil;

/**
 * ネットから画像を落として特定のファイルにZIPとして配置する。
 * @author SAKURA
 *
 */
public class NetImageArchiver {
    /**
     * 作者名
     */
    String author = null;

    /**
     * 作品名
     */
    String title = null;

    /**
     * 画像のURLリスト
     */
    List<String> imageUrls = new ArrayList<String>();

    public NetImageArchiver(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * 画像URLを追加する
     * @param url
     */
    public void addImageUrl(String url) {
        imageUrls.add(url);
    }

    /**
     * 
     * @param os
     * @throws IOException
     */
    public void build(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);

        {
            for (int i = 0; i < imageUrls.size(); ++i) {
                String fileName = String.format("img_%04d.jpg", i);
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                byte[] image = EagleUtil.getURLData(imageUrls.get(i));
                zos.write(image);
                zos.closeEntry();
            }
        }

        zos.close();
    }

    public static class Meta {

    }
}
