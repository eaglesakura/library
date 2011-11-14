/**
 * 
 */
package com.eaglesakura.lib.cui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * コマンドラインのパースと実行を行う。
 * 
 * $実行ファイル init -a -fとかの形式を扱う
 * initとかのコマンドを省略する場合はgetCommand()でeCommandNullを返す。
 * @author SAKURA
 *
 */
public class CommandLineParser {
    /**
     * 何も示さないコマンド。
     */
    public static final String eCommandNull = "__NO_NAME_COMMAND__";

    /**
     * オプションと値のペアを保存する。
     * @author SAKURA
     *
     */
    public static class OptionPair {
        /**
         * -vで指定した場合、vが格納される。
         * -prvで指定した場合、p r vと分割設定される。
         * --prvで指定した場合、prvと連結状態で設定される。
         */
        String key = null;

        /**
         * -vの次に設定される値が格納される。
         * 次の値がなければ何も発生しない。
         */
        String value = null;

        /**
         * オプションのキーを取得する。
         * @return
         */
        public String getKey() {
            return key;
        }

        /**
         * オプションに実値が指定されてたら設定する
         * @return
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * 実行を行う。
     * @author SAKURA
     *
     */
    public interface ParseListener {
        /**
         * コマンド名(initとかupdateとか）を取得する
         * @return
         */
        public String getCommand();

        /**
         * オプションの設定を行う。
         * @param parser
         * @param option
         */
        public void onOptionEnable(CommandLineParser parser, OptionPair option);

        /**
        * 実行を行い、終了コードを返す。
        * @param parser
        * @return
        */
        public int run(CommandLineParser parser);
    }

    /**
     * リスナのコマンドと実行の対応付け。
     */
    Map<String, ParseListener> listeners = new HashMap<String, CommandLineParser.ParseListener>();

    /**
     * CUIのオプションを指定する。
     */
    Map<String, OptionPair> options = new HashMap<String, CommandLineParser.OptionPair>();

    /**
     * 実行対象のコマンド
     */
    String command = null;

    /**
     * リスナを登録する。
     * @param listener
     */
    public void addListener(ParseListener listener) {
        listeners.put(listener.getCommand(), listener);
    }

    class CommandParser {
        String[] args = null;
        int current = 0;

        CommandParser(String[] args) {
            this.args = args;
        }

        String getCurrentValue() {
            return current < args.length ? args[current] : null;
        }

        String getNextValue() {
            return (current + 1) < args.length ? args[current + 1] : null;
        }

        String getNextOptValue() {
            String value = getNextValue();
            if (value == null) {
                return null;
            }

            if (value.startsWith("-")) {
                return null;
            }

            return value;
        }

        void nextWord() {
            String value = args[current];

            //! オプション指定の場合
            if (value.startsWith("-")) {

                String key = value.substring(value.lastIndexOf('-') + 1);
                String optValue = getNextOptValue();

                //! --から始まったら
                if (value.startsWith("--")) {
                    OptionPair pair = new OptionPair();
                    pair.key = key;
                    pair.value = optValue;
                    options.put(pair.key, pair);

                } else {
                    char[] optionsChar = key.toCharArray();
                    for (char c : optionsChar) {
                        OptionPair pair = new OptionPair();
                        pair.key = "" + c;
                        pair.value = optValue;
                        options.put(pair.key, pair);
                    }
                }

                //! カーソルを進める。
                ++current;
                if (optValue != null) {
                    ++current;
                }
            }
            //! コマンドの場合
            else {
                command = value;
                ++current;
            }
        }

        void parse() {
            while (current < args.length) {
                nextWord();
            }
        }
    }

    /**
     * コマンドライン引数をパースする。
     * @param arsg
     */
    public void parse(String[] args) {
        CommandParser parser = new CommandParser(args);
        parser.parse();
    }

    /**
     * オプションが有効ならtrueを指定する。
     * @param opt
     * @return
     */
    public boolean isOptionEnable(String opt) {
        return options.get(opt) != null;
    }

    /**
     * オプションに指定された値を取得する。
     * 何も指定されていない、もしくはオプション自体が指定されていない場合はnullを返す。
     * @param opt
     * @return
     */
    public String getOptionValue(String opt) {
        OptionPair pair = options.get(opt);
        if (pair != null) {
            return pair.value;
        }
        return null;
    }

    /**
     * 実行を行わせる。
     * @return
     */
    public int run() {
        if (command == null) {
            command = eCommandNull;
        }

        ParseListener listener = listeners.get(command);

        {
            Iterator<Entry<String, OptionPair>> iterator = options.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, OptionPair> next = iterator.next();
                listener.onOptionEnable(this, next.getValue());
            }
        }

        return listener.run(this);
    }
}
