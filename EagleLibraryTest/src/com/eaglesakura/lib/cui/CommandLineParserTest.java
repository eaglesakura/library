package com.eaglesakura.lib.cui;

import com.eaglesakura.lib.BaseTestCase;
import com.eaglesakura.lib.cui.CommandLineParser.OptionPair;
import com.eaglesakura.lib.util.EagleUtil;

public class CommandLineParserTest extends BaseTestCase {

    public void parserTest() {
        String[] args = { "init", //!<
                "-v", //!<    普通のオプション
                "-o", "100", //!<    値付きオプション
                "-prf", //!<    複数オプション
                "-qwe", "200", //<複数オプション引数付き
                "--final", //!< 長文オプション
                "--dragon", "10", //!< 長文オプション引数付き
        };

        CommandLineParser parser = new CommandLineParser();
        parser.parse(args);

        //! 各オプションを検証する
        assertTrue(parser.isOptionEnable("v"));

        assertTrue(parser.isOptionEnable("o"));
        assertEquals(parser.getOptionValue("o"), "100");

        assertTrue(parser.isOptionEnable("p"));
        assertTrue(parser.isOptionEnable("r"));
        assertTrue(parser.isOptionEnable("f"));

        assertTrue(parser.isOptionEnable("q"));
        assertTrue(parser.isOptionEnable("w"));
        assertTrue(parser.isOptionEnable("e"));
        assertEquals(parser.getOptionValue("q"), "200");
        assertEquals(parser.getOptionValue("w"), "200");
        assertEquals(parser.getOptionValue("e"), "200");

        assertTrue(parser.isOptionEnable("final"));

        assertTrue(parser.isOptionEnable("dragon"));
        assertEquals(parser.getOptionValue("dragon"), "10");

        log("parse ok!");
    }

    public void runnerTest() {
        String[] args = { //!< 

        "init", //!<
                "-v", //!<    普通のオプション
                "-o", "100", //!<    値付きオプション
                "-prf", //!<    複数オプション
                "-qwe", "200", //<複数オプション引数付き
                "--final", //!< 長文オプション
                "--dragon", "10", //!< 長文オプション引数付き
        };

        CommandLineParser parser = new CommandLineParser();
        parser.parse(args);

        parser.addListener(new CommandLineParser.ParseListener() {

            @Override
            public int run(CommandLineParser parser) {
                //! 実行結果を返す
                return 100;
            }

            @Override
            public void onOptionEnable(CommandLineParser parser, OptionPair option) {
                EagleUtil.log("key=" + option.key + " :: value=" + option.value);
            }

            @Override
            public String getCommand() {
                return "init";
            }
        });

        assertTrue(parser.run() == 100);
    }
}
