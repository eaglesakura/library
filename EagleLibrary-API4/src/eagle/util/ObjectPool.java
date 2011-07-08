/**
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
package eagle.util;

/**
 * 生成したオブジェクトをプールし、内部で保持しておく。<BR>
 * オブジェクトの使用後はこのクラスへ返却する必要がある。
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
public abstract class ObjectPool {
    private int add = 5;
    private Object[] objects = null;
    private int current = 0;

    /**
     *
     * @author eagle.sakura
     * @version 2009/11/29 : 新規作成
     */
    public ObjectPool() {
    }

    /**
     * 初期化を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/29 : 新規作成
     */
    private void init() {
        objects = new Object[add];
        current = 0;

        for (int i = 0; i < add; ++i) {
            objects[i] = createInstance();
        }
    }

    /**
     * 配列で使用するオブジェクトを生成する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public abstract Object createInstance();

    /**
     * テンポラリ配列に貯めてあるオブジェクトを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public Object pop() {
        if (objects == null) {
            init();
        }

        if (current >= objects.length) {
            // ! 配列の長さが足りなくなった。
            Object[] temp = new Object[objects.length + add];
            objects = temp;

            for (int i = current; i < objects.length; ++i) {
                objects[i] = createInstance();
            }
        }

        Object ret = objects[current];
        objects[current] = null;
        ++current;
        return ret;
    }

    /**
     * {@link #get()}したオブジェクトを返す。
     *
     * @author eagle.sakura
     * @param obj
     * @version 2009/11/29 : 新規作成
     */
    public void push(Object obj) {
        --current;
        objects[current] = obj;
    }
}
