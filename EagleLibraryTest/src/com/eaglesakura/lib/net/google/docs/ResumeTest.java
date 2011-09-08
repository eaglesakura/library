package com.eaglesakura.lib.net.google.docs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.eaglesakura.lib.util.EagleUtil;

public class ResumeTest extends AndroidTestCase {
    static String mail = "";
    static String pass = "";

    @MediumTest
    public void docsSearchTest() throws IOException {
        GoogleDocsEntries entries = new GoogleDocsEntries(mail, pass);
        entries.accessURL("?start-index=50&max-results=10");

        EagleUtil.log("Entry : " + entries.getEntriesCount());
        for (int i = 0; i < entries.getEntriesCount(); ++i) {
            EagleUtil.log("Title : " + entries.getEntry(i).getTitle());
        }
    }

    @MediumTest
    public void docsDownloadTest() throws IOException {
        String keyword = "jdh";
        GoogleDocsEntries entries = new GoogleDocsEntries(mail, pass);
        entries.search(keyword, true);

        GoogleDocsEntries.Entry entry = entries.getEntry(0);
        EagleUtil.log("name : " + entry.getTitle());
        GoogleDocsDownloader downloader = new GoogleDocsDownloader(mail, pass);
        File file = new File(Environment.getExternalStorageDirectory(), entry.getTitle());
        file.delete();
        downloader.start(entry);

        OutputStream os = new FileOutputStream(file);
        while (!downloader.downloadBytes(1024 * 10, os)) {

        }
        os.close();
    }

    @MediumTest
    public void docsDownloadRangeTest() throws IOException {
        String keyword = "ぼく、オタリーマン";

        GoogleDocsEntries entries = new GoogleDocsEntries(mail, pass);

        entries.search(keyword, true);

        GoogleDocsEntries.Entry entry = entries.getEntry(0);
        EagleUtil.log("name : " + entry.getTitle());

        GoogleDocsDownloader downloader = new GoogleDocsDownloader(mail, pass);

        File file = new File(Environment.getExternalStorageDirectory(), entry.getTitle());
        file.delete();

        int counts = 0;
        while (file.length() != entry.getContentSize()) {
            OutputStream os = new FileOutputStream(file, true);
            downloader.setContentStartAndEnd(file.length(), entry.getContentSize());
            downloader.start(entry);
            downloader.downloadBytes(1024 * 10, os);
            os.close();
            ++counts;
        }

        if (file.length() != entry.getContentSize()) {
            throw new Error("not equal content size!!");
        }
        EagleUtil.log("down complete!! : " + counts + "step!");
    }

    @MediumTest
    public void networkTest() throws IOException {
        EagleUtil.getURLData("http://www.jisuidroid.com/");
    }
}
