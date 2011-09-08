package com.eaglesakura.lib.net.google.docs;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.eaglesakura.lib.util.EagleUtil;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Key;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AtomParser;

/**
 * Google Docs内部のファイルを管理する。
 * @author SAKURA
 *
 */
public class GoogleDocsEntries {

    /**
     * アクセストークン
     */
    String token = null;

    /**
     * gmailアドレス
     */
    String gmail = null;
    /**
     * gmailパスワード
     */
    String passowrd = null;

    /**
     * 取得したアイテム一覧
     */
    List<Entry> entries = new ArrayList<Entry>();

    /**
     * 次のページを持っているか。
     */
    String nextURL = null;

    String applicationName = "Eagle/GoogleDocsDownloader";

    public GoogleDocsEntries(String token) {
        this.token = token;
    }

    public GoogleDocsEntries(String gmail, String password) {
        this.gmail = gmail;
        this.passowrd = password;
    }

    void loginEmail(HttpTransport transport) throws IOException {

        ClientLogin authenticator = new ClientLogin();
        authenticator.applicationName = applicationName;
        authenticator.authTokenType = "writely";
        authenticator.username = gmail;
        authenticator.password = passowrd;

        Response authenticate = authenticator.authenticate();
        authenticate.setAuthorizationHeader(transport);
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 次のAPI戻り値を持っている場合trueを返す。
     * @return
     */
    public boolean hasNextResult() {
        return nextURL != null;
    }

    /**
     * 次のページを読み込む。
     * @throws IOException
     */
    public void getNextPage() throws IOException {
        if (hasNextResult()) {
            accessURL(nextURL);
        }
    }

    /**
     * docsにアクセスし、アイテム一覧を取得する。
     * @param keyword 検索ワード。nullですべて取得。
     * @throws IOException
     */
    public void accessURL(String _url) throws IOException {
        HttpTransport transport = GoogleTransport.create();
        GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
        headers.setApplicationName(applicationName);
        headers.gdataVersion = "3";

        if (token != null) {
            headers.setGoogleLogin(token);
        } else {
            loginEmail(transport);
        }

        //! parser
        AtomParser parser = new AtomParser();
        parser.namespaceDictionary = new XmlNamespaceDictionary();
        transport.addParser(parser);

        {
            HttpRequest request = transport.buildGetRequest();
            if (_url.startsWith("?")) {
                _url = "https://docs.google.com/feeds/default/private/full" + _url;
            }
            String url = _url;
            EagleUtil.log("search url : " + url);
            request.url = new GoogleUrl(url);
            HttpResponse responce = request.execute();

            //            printResponce(responce);
            {
                // 送信
                Feed feed = responce.parseAs(Feed.class);

                try {
                    responce.getContent().close();
                } catch (Exception e) {
                    EagleUtil.log(e);
                }
                //                            final String key = "@src";
                if (feed.entries != null) {
                    for (EntryItem entry : feed.entries) {
                        entries.add(new Entry(entry));
                    }
                }

                nextURL = null;
                if (feed.links != null) {
                    for (Link link : feed.links) {
                        EagleUtil.log("rel : " + link.rel);
                        EagleUtil.log("href : " + link.href);

                        if ("next".equals(link.rel) && link.href != null) {
                            nextURL = link.href;
                        }
                    }
                } else {
                    EagleUtil.log("no-link");
                }
            }
        }
    }

    void printResponce(HttpResponse resp) throws IOException {
        String str = new String(EagleUtil.decodeStream(resp.getContent()));

        EagleUtil.log(str);

        resp.getContent().reset();
    }

    /**
     * docsにアクセスし、アイテム一覧を取得する。
     * @param keyword 検索ワード。nullですべて取得。
     * @throws IOException
     */
    public void search(String keyword, boolean isQuery) throws IOException {
        String url = "https://docs.google.com/feeds/default/private/full";
        if (keyword != null && keyword.length() > 0) {
            url += ((isQuery ? "?q=" : "?title=") + URLEncoder.encode(keyword, "UTF-8"));
        }
        EagleUtil.log("search url : " + url);

        accessURL(url);
    }

    /**
     * 全てのアイテムを一括で取得する。
     * @param keyword
     * @param isQuery
     * @throws IOException
     */
    public void searchFullPage(String keyword, boolean isQuery, boolean sort) throws IOException {
        search(keyword, isQuery);
        while (hasNextResult()) {
            getNextPage();
        }

        if (sort) {
            sortByName();
        }
    }

    /**
     * 同名のファイルを持っていたらtrue
     * @param name
     * @return
     */
    public int hasItem(String name) {
        int result = 0;
        for (Entry entry : entries) {
            if (entry.getTitle().equals(name)) {
                return result;
            }
            ++result;
        }
        return -1;
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public Entry getEntry(int index) {
        return entries.get(index);
    }

    public void sortByName() {
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry object1, Entry object2) {
                return object1.getTitle().compareTo(object2.getTitle());
            }
        });
    }

    /**
     * Google Docs内の１アイテム。
     * @author SAKURA
     *
     */
    public class Entry {
        String title = null;
        String contentUrl = null;
        long contentSize = 0;

        public Entry(EntryItem item) {
            title = item.title;
            if (item.content != null && item.content.get("@src") != null) {
                contentUrl = item.content.get("@src").toString();
            }
            contentSize = Long.parseLong(item.quotaBytesUsed);
        }

        /**
         * ファイルタイトル。
         * @return
         */
        public String getTitle() {
            return title;
        }

        /**
         * コンテンツの元URL。
         * @return
         */
        public String getContentUrl() {
            return contentUrl;
        }

        public long getContentSize() {
            return contentSize;
        }
    }

    /**
     * Feed タグ
     */
    public static class Feed {
        @Key("entry")
        public List<EntryItem> entries;

        @Key("link")
        public List<Link> links;
    }

    public static class Link {
        @Key("@href")
        public String href;

        @Key("@rel")
        public String rel;
    }

    /**
     * Entry タグ
     */
    public static class EntryItem {
        @Key
        public String summary;

        @Key
        public String title;

        @Key
        public String updated;

        /**
         * コンテンツサイズ
         */
        @Key("gd:quotaBytesUsed")
        public String quotaBytesUsed;

        @Key
        public Map<String, String> link;

        @Key
        public Map<String, String> content;

    }
}
