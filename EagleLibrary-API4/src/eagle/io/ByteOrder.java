/**
 *
 * @author eagle.sakura
 * @version 2010/06/21 : 新規作成
 */
package eagle.io;

/**
 * @author eagle.sakura
 * @version 2010/06/21 : 新規作成
 */
public enum ByteOrder {
    /**
     * 入力をそのまま通す。
     */
    eThrough,
    /**
     * 入力を反転させる。
     */
    eReversing {
        /**
         * バイトオーダーの変更を行う。
         *
         * @author eagle.sakura
         * @param buffer
         *            元の配列
         * @param sizeof
         *            型の1要素サイズ（byte=1,short=2,int=4...)
         * @param elements
         *            要素数
         * @version 2010/06/21 : 新規作成
         */
        @Override
        public void encode(byte[] buffer, int sizeof, int elements) {
            int ptr = 0;
            byte[] temp = new byte[sizeof];
            while (ptr < (sizeof * elements)) {
                // ! bit反転
                for (int i = 0; i < sizeof; ++i) {
                    temp[i] = buffer[ptr + sizeof - i - 1];
                }

                // ! 書き戻し
                for (int i = 0; i < sizeof; ++i) {
                    buffer[ptr + i] = temp[i];
                }

                ptr += sizeof;
            }
        }
    };

    /**
     * バイトオーダーの変更を行う。<BR>
     * 標準ではスルーを行う。
     *
     * @author eagle.sakura
     * @param buffer
     * @param sizeof
     * @param length
     * @version 2010/06/21 : 新規作成
     */
    public void encode(byte[] buffer, int sizeof, int elements) {

    }
}
