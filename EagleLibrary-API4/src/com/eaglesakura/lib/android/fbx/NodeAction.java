/**
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
package com.eaglesakura.lib.android.fbx;

import java.io.IOException;

import com.eaglesakura.lib.io.DataInputStream;
import com.eaglesakura.lib.math.Vector3;
import com.eaglesakura.lib.util.EagleException;


/**
 * １ノードごとのアニメーション情報を管理する。
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
public class NodeAction {
    /**
     * キーフレームの集合体。
     */
    private KeyFrame[] keys = null;

    /**
     * 関連付けられたノード番号。
     */
    private int nodeNumber = 0;

    /**
     * 関連付けられたノード名。
     */
    private String name = "";

    /**
     *
     * @author eagle.sakura
     * @param dis
     * @version 2010/07/11 : 新規作成
     */
    public NodeAction(DataInputStream dis) throws IOException, EagleException {
        int version = dis.readS32();
        if (version != 0x1) {
            throw new EagleException(EagleException.eStatusUnknownFileVersion);
        }

        // ! ノード番号
        nodeNumber = dis.readS32();

        // ! ノード名
        name = dis.readString();

        // ! キーフレーム数
        int keysize = dis.readS32();
        keys = new KeyFrame[keysize];

        for (int i = 0; i < keysize; ++i) {
            KeyFrame key = new KeyFrame((float) (i));
            keys[i] = key;

            {
                key.getScale().x = dis.readFloat();
                key.getScale().y = dis.readFloat();
                key.getScale().z = dis.readFloat();
            }
            {
                key.getRotate().x = dis.readFloat();
                key.getRotate().y = dis.readFloat();
                key.getRotate().z = dis.readFloat();
            }
            {
                key.getTranslate().x = dis.readFloat();
                key.getTranslate().y = dis.readFloat();
                key.getTranslate().z = dis.readFloat();
            }
        }
    }

    /**
     * 最終フレームを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public float getLastKeyFrame() {
        return keys[keys.length - 1].getFrame();
    }

    /**
     * キー情報を取得する。
     *
     * @author eagle.sakura
     * @param frame
     * @param result
     * @version 2010/07/11 : 新規作成
     */
    public void bind(float frame, KeyFrame result) {
        int firstFrame = (int) frame, secondFrame = (int) frame + 1;
        if (secondFrame >= keys.length) {
            result.set(keys[firstFrame]);
            return;
        }

        KeyFrame first = keys[firstFrame], second = keys[secondFrame];

        float leap = (frame - firstFrame);

        result.setFrame(frame);
        Vector3.leap(first.getScale(), second.getScale(), leap, result.getScale());
        Vector3.leap(first.getRotate(), second.getRotate(), leap, result.getRotate());
        Vector3.leap(first.getTranslate(), second.getTranslate(), leap, result.getTranslate());
    }

    /**
     * 接続先のノード番号を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public int getNodeNumber() {
        return nodeNumber;
    }

    /**
     * 接続先のノード名を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public String getName() {
        return name;
    }

}
