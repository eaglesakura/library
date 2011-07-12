/**
 * EagleScriptFormat形式のスクリプトコマンドを管理する。
 * @author eagle.sakura
 * @version 2010/03/21 : 新規作成
 */
package com.eaglesakura.lib.script.esf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.lib.io.FileSystem;
import com.eaglesakura.lib.util.EagleUtil;


/**
 * EagleScriptFormat形式のスクリプトコマンドを管理する。<BR>
 * コマンドは階層構造をもち、入れ子を表現出来る。<BR>
 * <BR>
 * 設定出来る値は次の通り。<BR>
 * コマンド名 : 1つのみ<BR>
 * 引数 : 0以上無制限<BR>
 * 子コマンド : 0以上無制限
 *
 * @author eagle.sakura
 * @version 2010/03/21 : 新規作成
 */
public class Command {
    private List<String> params = new ArrayList<String>();
    private List<Command> childs = null;
    private Command parent = null;

    /**
     * コマンド名を指定して作成する。
     *
     * @author eagle.sakura
     * @param name
     * @version 2010/03/21 : 新規作成
     */
    public Command(String name) {
        if (name == null) {
            name = "";
        }

        params.add(name);
    }

    /**
     * コマンド名を指定せずに作成する。
     *
     * @author eagle.sakura
     * @version 2010/03/21 : 新規作成
     */
    public Command() {

    }

    /**
     * 子を追加する。
     *
     * @author eagle.sakura
     * @param child
     * @version 2010/04/03 : 新規作成
     */
    public void addChild(Command child) {
        if (childs == null) {
            childs = new ArrayList<Command>();
        }

        child.parent = this;
        childs.add(child);
    }

    /**
     * コマンドの末尾にパラメータを追加する。
     *
     * @author eagle.sakura
     * @param param
     * @version 2010/03/21 : 新規作成
     */
    public void addParam(String param) {
        params.add(param);
    }

    /**
     * コマンド名を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/03/21 : 新規作成
     */
    public String getName() {
        return params.get(0);
    }

    /**
     * パラメータを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/03/21 : 新規作成
     */
    public String getParam(int index) {
        return params.get(index + 1);
    }

    /**
     * パラメータを数値として取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/03/21 : 新規作成
     */
    public int getParamInt(int index) {
        return Integer.parseInt(getParam(index));
    }

    /**
     * マスコットカプセル標準の固定小数として取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/04/03 : 新規作成
     */
    public float getParamMCFixed(int index) {
        return ((float) getParamInt(index)) / ((float) EagleUtil.eMCFixed1_0);
    }

    /**
     * GL標準の固定小数として取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/04/03 : 新規作成
     */
    public float getParamGLFixed(int index) {
        return ((float) getParamInt(index)) / ((float) EagleUtil.eGLFixed1_0);
    }

    /**
     * パラメータを浮動小数として取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/03/21 : 新規作成
     */
    public float getParamFloat(int index) {
        return Float.parseFloat(getParam(index));
    }

    /**
     * パラメータを論理値として取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/03/21 : 新規作成
     */
    public boolean getParamBool(int index) {
        return getParam(index).equals("true");
    }

    /**
     * パラメータ数を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public int getParamCount() {
        return params.size() - 1;
    }

    /**
     * サブコマンド数を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public int getChildCount() {
        if (childs == null) {
            return 0;
        }
        return childs.size();
    }

    /**
     * 子コマンドを取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public Command getChild(int index) {
        return childs.get(index);
    }

    /**
     * コマンドの階層を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public int getDepth() {
        int n = 0;
        Command cmd = parent;
        while (cmd != null) {
            cmd = cmd.parent;
            ++n;
        }
        return n;
    }

    /**
     * コマンド階層を追加する。
     *
     * @author eagle.sakura
     * @param parent
     * @param reader
     * @version 2010/04/04 : 新規作成
     */
    private static void addCommand(Command parent, BufferedReader reader) throws IOException {
        while (true) {
            String textLine = reader.readLine();
            // ! チャンクのフッダである場合、追加を終了する
            if (textLine == null) {
                return;
            }

            EsfLine line = EsfLine.create(textLine);

            // ! ラインの取得に失敗した場合は次の行を読み取る。
            if (line == null) {
                continue;
            }

            // ! フッダだったら上の階層に戻る。
            if (line.isChunkFooter()) {
                return;
            }

            Command current = new Command();

            // ! コマンドをひとまず追加する
            int cmdSize = line.getWordCount();
            for (int i = 0; i < cmdSize; ++i) {
                // ! コマンドの追加
                current.addParam(line.get(i));
            }

            // ! 親に追加する
            parent.addChild(current);

            // ! チャンクの開始の場合
            if (line.isChunkHead()) {
                // ! 子階層を追加する。
                addCommand(current, reader);
            }
        }
    }

    /**
     * ファイルから階層コマンドを作成する。<BR>
     * コマンドはすべて"ROOT"の名を持つ親コマンドの子となる。
     *
     * @author eagle.sakura
     * @param file
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public static Command create(BufferedReader file) {
        try {
            Command root = new Command("ROOT");
            addCommand(root, file);
            return root;
        } catch (Exception e) {
            EagleUtil.log(e);
            return null;
        }
    }

    /**
     * ファイルから階層コマンドを作成する。<BR>
     * パスを直接指定する。
     *
     * @author eagle.sakura
     * @param filePath
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public static Command create(String filePath, FileSystem fs) {
        try {
            BufferedReader br = fs.createFileReaderSJIS(filePath);

            Command result = create(br);

            br.close();

            return result;
        } catch (Exception e) {
            EagleUtil.log(e);
            return null;
        }
    }
}
