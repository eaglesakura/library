/**
 *
 */
package com.eaglesakura.lib.rakuten;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import com.eaglesakura.lib.util.EagleUtil;

/**
 *
 */
public class SearchAPIResult {
    private SearchAPIResult() {

    }

    public static class Item {
        public String itemUrl = "";
        public String smallImageUrl = "";
        public String mediumImageUrl = "";
        public String itemName = "";
        public String catchcopy = "";
        public String itemPrice = "";

        // ! 画像の有無
        public boolean imageFlag = false;
    };

    public static class SearchKey {
        public String developerID = "";
        public String keyword = "";
        public String NGKeyword = "";
        public String genreId = "";
        public String sort = eSortTypeStandard;
        public String orFlag = "0";
    };

    /**
     * 書籍ジャンル
     */
    public static final String eGenreIDBooks = "200162";

    /**
     * 書籍ジャンル
     */
    public static final String eGenreIDToyHobbyGame = "101164";

    /**
     * コミックジャンル
     */
    public static final String eGenreIDComic = "101299";

    /**
     * ソート順 更新日時昇順
     */
    public static final String eSortTypeUpdateTimestampPlus = "+updateTimestamp";
    /**
     * ソート順 更新日時昇順
     */
    public static final String eSortTypeUpdateTimestampMinus = "-updateTimestamp";

    /**
     * ソート順 標準
     */
    public static final String eSortTypeStandard = "standard";

    /**
     *
     */
    private List<Item> items = new ArrayList<Item>();

    /**
     * 検索結果を取得する。
     *
     * @param num
     * @return
     */
    public Item getItem(int num) {
        return items.get(num);
    }

    /**
     * 検索できたアイテム数を取得する。
     *
     * @return
     */
    public int getItemCount() {
        return items.size();
    }

    @SuppressWarnings("all")
    public static SearchAPIResult createInstance(SearchKey key) {
        try {
            SearchAPIResult result = new SearchAPIResult();

            URL url = new URL("http://api.rakuten.co.jp/rws/3.0/json?" + "developerId=" + key.developerID + "&operation=ItemSearch" + "&version=2010-09-15"
                    + "&keyword=" + URLEncoder.encode(key.keyword, "UTF-8") + "&sort=" + URLEncoder.encode(key.sort, "UTF-8")
                    + (key.genreId.length() > 0 ? ("&genreId=" + key.genreId) : "") + "&imageFlag=1" + "&orFlag=" + key.orFlag
                    + (key.NGKeyword.length() > 0 ? "&NGKeyword=" + URLEncoder.encode(key.NGKeyword, "UTF-8") : ""));
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.connect();

            InputStream is = http.getInputStream();

            Map<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> map = (Map<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>>) JSON
                    .decode(is);

            EagleUtil.log("" + map);
            // ! 検索結果を取り出す
            List<Map<String, Object>> items = map.get("Body").get("ItemSearch").get("Items").get("Item");

            for (Map<String, Object> item : items) {
                Item add = new Item();
                add.catchcopy = item.get("catchcopy").toString();
                add.itemName = item.get("itemName").toString();
                add.itemUrl = item.get("itemUrl").toString();
                add.mediumImageUrl = item.get("mediumImageUrl").toString();
                add.smallImageUrl = item.get("smallImageUrl").toString();
                add.itemPrice = item.get("itemPrice").toString();
                add.imageFlag = "1".equals(item.get("imageFlag").toString());
                result.items.add(add);
            }
            return result;
        } catch (Exception e) {
            EagleUtil.log(e);
            return new SearchAPIResult();
        }
    }
}
