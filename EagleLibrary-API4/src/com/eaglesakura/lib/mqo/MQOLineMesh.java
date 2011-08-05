package com.eaglesakura.lib.mqo;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.lib.math.Vector3;

/**
 * MQOのラインを生成する。
 */
public class MQOLineMesh {
    List<Vector3> sortedVertices = new ArrayList<Vector3>();

    public MQOLineMesh(MQOChunk obj) {
        List<Vector3> vertices = genVertices(obj.getChild("vertex"));
        List<MQOLineFace> faces = genFaces(obj.getChild("face"));

        {
            List<Integer> tempIndices = new ArrayList<Integer>();

            MQOLineFace face = faces.get(0);
            faces.remove(0);

            tempIndices.add(face.v0);
            tempIndices.add(face.v1);

            int temp = face.v1;
            while ((temp = nextFace(faces, temp)) >= 0) {
                tempIndices.add(temp);
            }

            for (Integer i : tempIndices) {
                sortedVertices.add(vertices.get(i));
            }
        }
    }

    /**
     * ソート済みの位置情報リストを取得する。
     * @return
     */
    public List<Vector3> getSortedVertices() {
        return sortedVertices;
    }

    /**
     * 次の頂点を取得する。
     * @param faces
     * @param current
     * @return
     */
    int nextFace(List<MQOLineFace> faces, int current) {
        MQOLineFace result = null;
        for (MQOLineFace face : faces) {
            if (face.v0 == current || face.v1 == current) {
                result = face;
                break;
            }
        }

        if (result == null) {
            return -1;
        }

        faces.remove(result);

        if (result.v0 != current) {
            return result.v0;
        } else {
            return result.v1;
        }
    }

    List<Vector3> genVertices(MQOChunk vertex) {
        List<Vector3> vertices = new ArrayList<Vector3>();
        for (int i = 0; i < vertex.getLineCount(); ++i) {
            MQOLine line = vertex.getLine(i);
            Vector3 v = new Vector3();

            v.x = Float.parseFloat(line.get(0));
            v.y = Float.parseFloat(line.get(1));
            v.z = Float.parseFloat(line.get(2));

            vertices.add(v);
        }
        return vertices;
    }

    /**
     * 頂点情報を取り出す。
     * @param face
     * @return
     */
    List<MQOLineFace> genFaces(MQOChunk face) {
        List<MQOLineFace> result = new ArrayList<MQOLineFace>();
        for (int i = 0; i < face.getLineCount(); ++i) {
            result.add(new MQOLineFace(face.getLine(i)));
        }
        return result;
    }

    static class MQOLineFace {
        int v0 = -1;
        int v1 = -1;

        public MQOLineFace(MQOLine line) {
            for (int k = 0; k < line.getWordCount(); ++k) {
                String word = line.get(k);
                if (word.startsWith("V(")) {

                    //! 頂点番号を解析する
                    {
                        String n = word.substring("V(".length());
                        v0 = Integer.parseInt(n);
                    }
                    ++k;
                    word = line.get(k);
                    v1 = Integer.parseInt(word.substring(0, word.length() - 1));
                }
            }
        }
    }
}
