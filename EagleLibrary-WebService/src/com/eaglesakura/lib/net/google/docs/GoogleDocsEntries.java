package com.eaglesakura.lib.net.google.docs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eaglesakura.lib.net.google.docs.DocsAPIException.Type;
import com.eaglesakura.lib.util.EagleUtil;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
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

    void loginEmail(HttpTransport transport) throws DocsAPIException {

        ClientLogin authenticator = new ClientLogin();
        authenticator.applicationName = applicationName;
        authenticator.authTokenType = "writely";
        authenticator.username = gmail;
        authenticator.password = passowrd;

        try {
            Response authenticate = authenticator.authenticate();
            authenticate.setAuthorizationHeader(transport);
        } catch (IOException ioe) {
            throw new DocsAPIException(Type.AuthError, ioe);
        }
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
    public void getNextPage() throws DocsAPIException {
        if (hasNextResult()) {
            accessURL(nextURL);
        }
    }

    /**
     * Google Docs APIの指定URLから情報を取得する。
     * @param _url
     * @return
     * @throws IOException
     */
    public byte[] getResult(String _url) throws DocsAPIException {
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
            final String url = _url;
            EagleUtil.log("search url : " + url);
            request.url = new GoogleUrl(url);
            HttpResponse responce = null;
            try {
                responce = request.execute();
                byte[] buffer = EagleUtil.decodeStream(responce.getContent());
                return buffer;
            } catch (IOException ioe) {
                throw new DocsAPIException(Type.APIResponseError, ioe);
            } finally {
                try {
                    responce.ignore();
                } catch (Exception e) {

                }

            }
        }
    }

    /**
     * docsにアクセスし、アイテム一覧を取得する。
     * @param keyword 検索ワード。nullですべて取得。
     * @throws IOException
     */
    public void accessURL(String _url) throws DocsAPIException {
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

        try {
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
                    responce.ignore();
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
                        //                        EagleUtil.log("rel : " + link.rel);
                        //                        EagleUtil.log("href : " + link.href);

                        if ("next".equals(link.rel) && link.href != null) {
                            nextURL = link.href;
                        }
                    }
                } else {
                    EagleUtil.log("no-link");
                }
            }
        } catch (HttpResponseException hre) {
            switch (hre.response.statusCode) {
            case 401:
            case 403:
                throw new DocsAPIException(Type.AuthError, hre);
            }
            throw new DocsAPIException(Type.APIResponseError, hre);
        } catch (IOException ioe) {
            throw new DocsAPIException(Type.APIResponseError, ioe);
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
    public void search(String keyword, boolean isQuery) throws DocsAPIException {
        String url = "https://docs.google.com/feeds/default/private/full";

        try {
            if (keyword != null && keyword.length() > 0) {
                url += ((isQuery ? "?q=" : "?title=") + URLEncoder.encode(keyword, "UTF-8"));
            }
            EagleUtil.log("search url : " + url);

            accessURL(url);
        } catch (UnsupportedEncodingException uee) {
            throw new DocsAPIException(Type.Unknown, uee);
        }
    }

    /**
     * 全てのアイテムを一括で取得する。
     * @param keyword
     * @param isQuery
     * @throws IOException
     */
    public void searchFullPage(String keyword, boolean isQuery, boolean sort) throws DocsAPIException {
        search(keyword, isQuery);
        while (hasNextResult()) {
            getNextPage();
        }

        if (sort) {
            sortByName();
        }
    }

    /**
     * Google Docsフォルダを取得する。
     * @return
     */
    public Directory getDocsDirectory() throws DocsAPIException {

        //! まずはデータを取得する。
        accessURL("https://docs.google.com/feeds/default/private/full/-/folder");
        while (hasNextResult()) {
            getNextPage();
        }

        Directory root = new Directory();

        //! 属性URLとEntryのマップを作成する
        Map<String, Entry> entryMap = new HashMap<String, Entry>();
        Map<String, Directory> directoryMap = new HashMap<String, Directory>();
        {
            for (Entry entry : this.entries) {
                entryMap.put(entry.self.href, entry);
                directoryMap.put(entry.self.href, new Directory(entry));
            }
        }

        //! 全部のマップの親属性を設定する。
        {
            for (Entry entry : this.entries) {
                Directory self = directoryMap.get(entry.self.href);
                if (entry.parent != null) {
                    //! 親を検索する
                    Directory parent = directoryMap.get(entry.parent.href);
                    parent.addDirectory(self);
                } else {
                    //! 親がいないからRoot属性にする
                    root.addDirectory(self);
                }
            }
        }

        return root;
    }

    /**
     * Docsのアイテムをディレクトリを所属させる。
     * どこにも所属していないアイテムはRootに所属させる。
     * @param root
     */
    public void bindDirectory(Directory root) {
        for (Entry entry : entries) {
            Directory parent = null;
            String parentHref = null;
            if (entry.getParentLink() != null) {
                parentHref = entry.getParentLink().href;
            }

            if (parentHref != null) {
                parent = root.searchHref(parentHref);
            }

            if (parent == null) {
                parent = root;
            }

            parent.addEntry(entry);
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
                return EagleUtil.compareString(object1.getTitle(), object2.getTitle());
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

        Link self = null;
        Link parent = null;

        String md5 = null;

        String id = null;

        String resourceId = null;

        public Entry(EntryItem item) {
            title = item.title;
            if (item.content != null) {

                if (item.content.get("@src") != null) {
                    contentUrl = item.content.get("@src").toString();
                }

            }

            if (item.links != null) {
                for (Link link : item.links) {
                    if ("http://schemas.google.com/docs/2007#parent".equals(link.rel) && link.title != null) {
                        parent = link;
                    } else if ("self".equals(link.rel)) {
                        self = link;
                    }
                }
            }
            contentSize = Long.parseLong(item.quotaBytesUsed);
            md5 = item.md5;
            id = item.id;
            resourceId = item.resourceId;
        }

        public String getId() {
            return id;
        }

        public String getResourceId() {
            return resourceId;
        }

        /**
         * ファイルタイトル。
         * @return
         */
        public String getTitle() {
            return title;
        }

        /**
         * ファイルのMD5情報を取得する。
         * MD5を持たない（ディレクトリとか）はnullを返す。
         * @return
         */
        public String getMd5() {
            return md5;
        }

        /**
         * MD5情報を持っていたらtrueを返す。
         * @return
         */
        public boolean hasMD5() {
            return md5 != null;
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

        /**
         * 親属性を取得する。
         * @return
         */
        public Link getParentLink() {
            return parent;
        }

        /**
         * 自分の属性を取得する。
         * @return
         */
        public Link getSelfLink() {
            return self;
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

        @Key("@title")
        public String title;
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

        @Key("link")
        public List<Link> links;

        @Key
        public Map<String, String> content;

        @Key("docs:md5Checksum")
        public String md5;

        @Key("gd:etag")
        public String etag;

        @Key
        public String id;

        @Key("gd:resourceId")
        public String resourceId;
    }

    /**
     * Docs内部のフォルダを示す。
     * @author SAKURA
     *
     */
    public static class Directory {
        /**
         * フォルダ名
         */
        String title = null;

        /**
         * 一意なURL
         */
        String href = "";

        /**
         * 親となるフォルダURL
         */
        String parent = null;

        /**
         * サブディレクトリを示す。
         */
        List<Directory> childs = new ArrayList<Directory>();

        /**
         * 含まれているアイテムを示す。
         */
        List<Entry> items = new ArrayList<Entry>();

        /**
         * フォルダを構成する。
         * @param item
         */
        public Directory(Entry item) {
            title = item.title;
            href = item.self.href;
            if (item.parent != null) {
                parent = item.parent.href;
            }
        }

        /**
         * メインフォルダを構築する。
         */
        public Directory() {
            title = "root";
        }

        /**
         * ディレクトリを追加する。
         * @param dir
         */
        public void addDirectory(Directory dir) {
            if (childs.indexOf(dir) < 0) {
                childs.add(dir);
            }
        }

        /**
         * リンクするアイテムを追加する。
         * @param item
         */
        public void addEntry(Entry item) {
            if (items.indexOf(item) < 0) {
                items.add(item);
            }
        }

        public String getTitle() {
            return title;
        }

        public String getHref() {
            return href;
        }

        public int getChildCount() {
            return childs.size();
        }

        public Directory getChild(int index) {
            return childs.get(index);
        }

        public Directory getChild(String name) {
            for (Directory dir : childs) {
                if (dir.getTitle().equals(name)) {
                    return dir;
                }
            }
            return null;
        }

        /**
         * URLからディレクトリを検索する。
         * サブディレクトリも検索対象となる。
         * @param href
         * @return
         */
        public Directory searchHref(String href) {
            if (getHref().equals(href)) {
                return this;
            }

            for (Directory dir : childs) {
                if (dir.getHref().equals(href)) {
                    return dir;
                }
                Directory result = dir.searchHref(href);
                if (result != null) {
                    return result;
                }
            }

            return null;
        }

        public int getEntryCount() {
            return items.size();
        }

        public Entry getEntry(int index) {
            return items.get(index);
        }

        public Entry getEntry(String name) {
            for (Entry entry : items) {
                if (entry.getTitle().equals(name)) {
                    return entry;
                }
            }
            return null;
        }

        public void sort() {
            Collections.sort(items, new Comparator<Entry>() {
                @Override
                public int compare(Entry object1, Entry object2) {
                    return EagleUtil.compareString(object1.getTitle(), object2.getTitle());
                }
            });
            Collections.sort(childs, new Comparator<Directory>() {
                @Override
                public int compare(Directory object1, Directory object2) {
                    return EagleUtil.compareString(object1.getTitle(), object2.getTitle());
                }
            });

            for (Directory dir : childs) {
                dir.sort();
            }
        }
    }
}
