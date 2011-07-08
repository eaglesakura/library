package eagle.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import eagle.util.EagleUtil;

/**
 *
 * @author eagle.sakura
 * @version 2010/05/24 : 新規作成
 */

/**
 * @author eagle.sakura
 * @version 2010/05/24 : 新規作成
 */
public class CameraView extends LooperSurfaceView implements PreviewCallback, AutoFocusCallback {
    /**
     * ネイティブのカメラ。
     */
    private Camera camera = null;
    /**
     * プレビューで受け取ったピクセル。
     */
    private byte[] currentPixels = null;

    /**
     * フォーカス合わせ中か。
     */
    private boolean isAutoFocus = false;

    /**
     *
     */
    private int previewWidth = 0;

    /**
     *
     */
    private int previewHeight = 0;

    /**
     * カメラ管理を行う。
     *
     * @author eagle.sakura
     * @param context
     * @param attr
     * @version 2010/05/24 : 新規作成
     */
    public CameraView(Context context, AttributeSet attr) {
        super(context, attr);

        SurfaceHolder holder = getHolder();
        // ! プッシュバッファの指定
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    /**
     * オートフォーカスで受け取る。
     *
     * @author eagle.sakura
     * @param arg0
     * @param arg1
     * @version 2010/05/26 : 新規作成
     */
    @Override
    public void onAutoFocus(boolean arg0, Camera arg1) {
        // Log.d( "Face", "autofocus : " + arg0 );
        isAutoFocus = false;
    }

    /**
     * オートフォーカスを開始する。
     *
     * @author eagle.sakura
     * @version 2010/05/26 : 新規作成
     */
    public void autoFocus() {
        if (!isAutoFocus) {
            camera.autoFocus(this);
            isAutoFocus = true;
        }
    }

    /**
     * カメラのピクセル情報を受け取る。
     *
     * @author eagle.sakura
     * @param arg0
     * @param arg1
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void onPreviewFrame(byte[] arg0, Camera arg1) {
        currentPixels = arg0;
    }

    /**
     * コールバックで取得された最新のYUVピクセル情報を返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/24 : 新規作成
     */
    public byte[] getCurrentPixels() {
        return currentPixels;
    }

    /**
     * ネイティブのカメラクラスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/24 : 新規作成
     */
    public Camera getNativeCamera() {
        return camera;
    }

    /**
     * カメラ情報を初期化する。
     *
     * @author eagle.sakura
     * @version 2010/05/24 : 新規作成
     */
    protected void initCamera() {
        try {
            camera = Camera.open();

            Parameters param = camera.getParameters();
            param.setPictureSize(4000, 3000);
            camera.setParameters(param);

            param = camera.getParameters();
            /*
             * Log.d( "Face", "PixelFormat : " + param.getPictureFormat() );
             * Log.d( "Face", "getPreviewFormat : " + param.getPreviewFormat()
             * ); Log.d( "Face", "getPictureSize : " +
             * param.getPictureSize().height ); Log.d( "Face",
             * "getPictureSize : " + param.getPictureSize().width ); Log.d(
             * "Face", "getPreviewSize : " + param.getPreviewSize().height );
             * Log.d( "Face", "getPreviewSize : " + param.getPreviewSize().width
             * );
             */

            camera.setPreviewDisplay(getHolder());
            camera.setPreviewCallback(this);

        } catch (Exception e) {
            EagleUtil.log(e);
        }
    }

    /**
     * サーフェイスが作成された。
     *
     * @author eagle.sakura
     * @param arg0
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        previewWidth = arg0.getSurfaceFrame().width();
        previewHeight = arg0.getSurfaceFrame().height();
        initCamera();
        super.surfaceCreated(arg0);
    }

    /**
     * サーフェイスが変更された。
     *
     * @author eagle.sakura
     * @param holder
     * @param format
     * @param width
     * @param height
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        previewWidth = width;
        previewHeight = height;
        super.surfaceChanged(holder, format, width, height);
        /*
         * Log.d( "Camera", "PixelFormat : " + param.getPictureFormat() );
         * Log.d( "Face", "getPreviewFormat : " + param.getPreviewFormat() );
         * Log.d( "Face", "getPictureSize : " + param.getPictureSize().height );
         * Log.d( "Face", "getPictureSize : " + param.getPictureSize().width );
         * Log.d( "Face", "getPreviewSize : " + param.getPreviewSize().height );
         * Log.d( "Face", "getPreviewSize : " + param.getPreviewSize().width );
         */
    }

    /**
     * プレビューの幅を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/25 : 新規作成
     */
    public int getPreviewWidth() {
        return previewWidth;
    }

    /**
     * プレビューの高さを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/25 : 新規作成
     */
    public int getPreviewHeight() {
        return previewHeight;
    }

    /**
     * サーフェイスが破棄された。
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            EagleUtil.log(e);
        }
    }

    public static final void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

    /**
     * YUV420データをBitmapに変換します
     *
     * @param rgb
     * @param yuv420sp
     * @param width
     * @param height
     */
    // YUV420 to BMP
    public static final void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height, int srcX, int srcY, int srcW, int srcH, int sample) {
        final int frameSize = srcW * srcH;

        int targetPixel = 0;
        for (int j = srcY; j < srcH; j += sample) {
            int uvp = frameSize + (j >> 1) * srcW;
            int u = 0, v = 0;
            for (int i = srcX; i < srcW; i += sample) {
                int y = (0xff & ((int) yuv420sp[(srcW * j) + i])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp + (i) + 0]) - 128;
                    u = (0xff & yuv420sp[uvp + (i) + 1]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb[targetPixel] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                ++targetPixel;
            }
        }
    }

    /**
     * YUV420データをBitmapに変換します
     *
     * @param rgb
     * @param yuv420sp
     * @param width
     * @param height
     */
    // YUV420 to BMP
    public static final void decodeYUV420SP(short[] rgb565, byte[] yuv420sp, int width, int height, int srcX, int srcY, int srcW, int srcH, int sample) {
        final int frameSize = width * height;

        int targetPixel = 0;
        int u = 0, v = 0;
        for (int j = srcY; j < srcH; j += sample) {
            int uvp = frameSize + (j >> 1) * width;

            for (int i = srcX; i < srcW; i += sample) {
                int y = (0xff & ((int) yuv420sp[(width * j) + i])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp + (i) + 0]) - 128;
                    u = (0xff & yuv420sp[uvp + (i) + 1]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb565[targetPixel] = (short) ((((r << 6) & 0xff0000) >> 3) | (((g >> 2) & 0xff00) >> 2) | (((b >> 10) & 0xff) >> 3));
                ++targetPixel;
            }
        }
    }

    /**
     * YUV420データをBitmapに変換します
     *
     * @param rgb
     * @param yuv420sp
     * @param width
     * @param height
     */
    // YUV420 to BMP
    public static final void decodeYUV420SP(Bitmap rgb, byte[] yuv420sp, int width, int height, int srcX, int srcY, int srcW, int srcH, int sample) {
        final int frameSize = width * height;

        int u = 0, v = 0;
        int pixY = 0;
        for (int j = srcY; j < (srcH + srcY); j += sample) {
            int uvp = frameSize + (j >> 1) * width;

            int pixX = 0;
            for (int i = srcX; i < (srcW + srcX); i += sample) {
                int y = (0xff & ((int) yuv420sp[(width * j) + i])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp + (i) + 0]) - 128;
                    u = (0xff & yuv420sp[uvp + (i) + 1]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb.setPixel(pixX, pixY, 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff));
                ++pixX;
            }

            ++pixY;
        }
    }

}
