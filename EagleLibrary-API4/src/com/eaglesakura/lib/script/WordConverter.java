/**
 * ワードリストを読み込み、指定の単語と置き換える。<BR>
 * 入れ替え単語は大抵の場合構造体リストのインデックスである。
 * @author eagle.sakura
 * @version 2010/04/04 : 新規作成
 */
package com.eaglesakura.lib.script;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.eaglesakura.lib.io.DataInputStream;
import com.eaglesakura.lib.util.EagleUtil;


/**
 * ワードリストを読み込み、指定の単語と置き換える。<BR>
 * 入れ替え単語は大抵の場合構造体リストのインデックスである。<BR>
 * <BR>
 * Cプリプロセッサの#defineによる置換えに相当する。
 *
 * @author eagle.sakura
 * @version 2010/04/04 : 新規作成
 */
class WordConverter {
    private Map<String, String> wordMap = new HashMap<String, String>();

    /**
     *
     * @author eagle.sakura
     * @version 2010/04/04 : 新規作成
     */
    public WordConverter() {

    }

    /**
     * ワードリストを追加する。
     *
     * @author eagle.sakura
     * @param dis
     * @version 2010/04/04 : 新規作成
     */
    public void add(DataInputStream dis) {
        try {
            int length = dis.readS16();

            for (int i = 0; i < length; ++i) {
                String word = dis.readString();
                int value = dis.readS32();

                // ! 単語を追加する。
                wordMap.put(word, "" + value);
            }
        } catch (IOException e) {
            EagleUtil.log(e);
        }
    }

    /**
     * 単語をコンバートする。<BR>
     * ワードリストに登録されていなかった場合、keyの値をそのまま返す。
     *
     * @author eagle.sakura
     * @param key
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public String convert(String key) {
        String result = wordMap.get(key);

        if (result == null) {
            return key;
        } else {
            return result;
        }
    }
}
