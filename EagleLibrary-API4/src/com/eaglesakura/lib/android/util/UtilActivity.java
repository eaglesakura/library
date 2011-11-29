/**
 *
 * @author eagle.sakura
 * @version 2010/05/31 : 新規作成
 */
package com.eaglesakura.lib.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.WindowManager;

import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/05/31 : 新規作成
 */
public class UtilActivity extends Activity {
    private static String sdcardPath = null;

    static {
        sdcardPath = Environment.getExternalStorageDirectory().getPath();

        // ! モデル情報指定
        EagleUtil.log(Build.MODEL);
        if (Build.MODEL.equals("SC-01C")) {
            sdcardPath += "/external_sd";
        } else if (Build.MODEL.startsWith("Adam")) {
            sdcardPath = "mnt/sdcard2";
        }
    }

    /**
     * SDカードへの出力ストリームを開く。<BR>
     * pathにはSDカード以下のパスを記述する。<BR>
     * android.permission.WRITE_EXTERNAL_STORAGEが設定されていない場合、
     * このメソッドは必ずExceptionを投げる。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @version 2010/06/06 : 新規作成
     */
    @Deprecated
    public static FileOutputStream openSDOutputStream(String path) throws IOException {
        if (path.charAt(0) != '/' && path.charAt(0) != '\\') {
            path = "/" + path;
        }

        EagleUtil.log("path : " + path);
        return new FileOutputStream(sdcardPath + path);
    }

    /**
     * SDカード以下のパスをSDカードまでを含めたフルパスに変換する。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @version 2010/07/17 : 新規作成
     */
    @Deprecated
    public static String toSDPath(String path) {
        if (path.charAt(0) != '/' && path.charAt(0) != '\\') {
            path = "/" + path;
        }
        return sdcardPath + path;
    }

    /**
     * SDカードからの出力ストリームを開く。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @throws IOException
     * @version 2010/06/06 : 新規作成
     */
    @Deprecated
    public static FileInputStream openSDInputStream(String path) throws IOException {
        if (path.charAt(0) != '/' && path.charAt(0) != '\\') {
            path = "/" + path;
        }

        EagleUtil.log("path : " + path);
        return new FileInputStream(getSDCardRootPath() + path);
    }

    /**
     * SDカードとして認識するパスを指定する。
     *
     * @param path
     */
    @Deprecated
    public static void setSDCardRootPath(String path) {
        sdcardPath = path;
    }

    /**
     * SDカードのROOTパスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/06 : 新規作成
     */
    @Deprecated
    public static String getSDCardRootPath() {
        return sdcardPath;
    }

    /**
     * SDカード以下のディレクトリを指定し、SDカード用のものに変換する。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @version 2010/06/12 : 新規作成
     */
    @Deprecated
    public static String convertSDPath(String path) {
        if (path.charAt(0) != '/' && path.charAt(0) != '\\') {
            path = "/" + path;
        }

        return getSDCardRootPath() + path;
    }

    /**
     * ディスプレイのXYサイズを取得する。
     *
     * @author eagle.sakura
     * @param context
     * @param result
     * @return
     * @version 2010/07/14 : 新規作成
     */
    public static Vector2 getDisplaySize(Context context, Vector2 result) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int displayW = wm.getDefaultDisplay().getWidth(), displayH = wm.getDefaultDisplay().getHeight();
        result.set((float) displayW, (float) displayH);
        return result;
    }

    /**
     * 現在の日付をYYYYMMDDHHMMSS形式で取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/06 : 新規作成
     */
    public static String getDateString() {
        String str = "";

        Calendar calender = Calendar.getInstance();
        int month = calender.get(Calendar.MONTH) + 1, date = calender.get(Calendar.DATE), hour = calender.get(Calendar.HOUR_OF_DAY), minute = calender
                .get(Calendar.MINUTE), seconds = calender.get(Calendar.SECOND);

        str += calender.get(Calendar.YEAR);

        if (month < 10) {
            str += "0";
        }
        str += month;

        if (date < 10) {
            str += "0";
        }
        str += date;

        if (hour < 10) {
            str += "0";
        }
        str += hour;

        if (minute < 10) {
            str += "0";
        }
        str += minute;

        if (seconds < 10) {
            str += "0";
        }
        str += seconds;

        return str;
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @return
     * @version 2010/06/05 : 新規作成
     */
    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @return
     * @version 2010/06/05 : 新規作成
     */
    public static boolean isOrientationVertical(Context context) {
        return getOrientation(context) == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @return
     * @version 2010/06/13 : 新規作成
     */
    public static boolean isOrientationAuto(Context context) {
        return getOrientation(context) == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    /**
     * SDカードの指定位置にディレクトリを作成する。
     *
     * @author eagle.sakura
     * @param path
     * @return
     * @version 2010/06/06 : 新規作成
     */
    @Deprecated
    public static boolean createSDDirectory(String path) {
        File file = new File(getSDCardRootPath() + path);
        if (file.isDirectory()) {
            return true;
        }

        // ! ディレクトリを作成する。
        return file.mkdir();
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @param input
     * @param output
     * @return
     * @version 2010/06/12 : 新規作成
     */
    public static boolean copyFile(Context context, InputStream input, Uri output) {
        OutputStream os = null;

        try {
            os = context.getContentResolver().openOutputStream(output);
            copyFile(context, input, os);
            os.close();
            return true;
        } catch (Exception e) {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {

            }
            return false;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @param input
     * @param output
     * @return
     * @version 2010/06/12 : 新規作成
     */
    public static boolean copyFile(Context context, Uri input, Uri output) {
        OutputStream os = null;

        try {
            os = context.getContentResolver().openOutputStream(output);
            copyFile(context, input, os);
            os.close();
            return true;
        } catch (Exception e) {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {

            }
            return false;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param origin
     * @param os
     * @version 2010/06/12 : 新規作成
     */
    public static void copyFile(Context context, InputStream is, OutputStream os) throws IOException {
        byte[] temp = new byte[1024 * 512];

        try {
            while (is.available() > 0) {
                if (is.available() > temp.length) {
                    is.read(temp);
                    os.write(temp);
                } else {
                    int size = is.available();
                    is.read(temp);
                    os.write(temp, 0, size);
                }
            }

            os.flush();
            is.close();
        } catch (IOException ioe) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
            throw ioe;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param origin
     * @param os
     * @version 2010/06/12 : 新規作成
     */
    public static void copyFile(Context context, Uri origin, OutputStream os) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(origin);

        byte[] temp = new byte[1024 * 512];

        try {
            while (is.available() > 0) {
                if (is.available() > temp.length) {
                    is.read(temp);
                    os.write(temp);
                } else {
                    int size = is.available();
                    is.read(temp);
                    os.write(temp, 0, size);
                }
            }

            os.flush();
            is.close();
        } catch (IOException ioe) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
            throw ioe;
        }
    }

    /**
     * 指定したURIのファイルをコピーする。
     *
     * @author eagle.sakura
     * @param context
     * @param origin
     * @param outPath
     * @version 2010/06/12 : 新規作成
     */
    public static void copyFile(Context context, Uri origin, String outPath) throws IOException {
        FileOutputStream os = new FileOutputStream(outPath);

        copyFile(context, origin, os);

        os.close();
    }

    /**
     * 指定方向に端末画面を固定する。
     *
     * @author eagle.sakura
     * @param context
     * @param isVertical
     * @version 2010/06/13 : 新規作成
     */
    public static void setOrientation(Context context, boolean isVertical) {
        try {
            Activity activity = (Activity) context;
            if (isVertical) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (ClassCastException cce) {
            return;
        }
    }

    /**
     * 設定を反転する。
     *
     * @author eagle.sakura
     * @param context
     * @version 2010/06/16 : 新規作成
     */
    public static void toggleOrientationFixed(Context context) {
        try {
            Activity activity = (Activity) context;
            int ori = context.getResources().getConfiguration().orientation;
            if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (ClassCastException cce) {
            return;
        }
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @param is
     * @version 2010/05/31 : 新規作成
     */
    public static void setOrientationFixed(Context context, boolean is) {
        try {
            Activity activity = (Activity) context;
            if (is) {
                int ori = context.getResources().getConfiguration().orientation;
                if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        } catch (ClassCastException cce) {
            return;
        }
    }

    /**
     * SDKバージョンが1.x系列だったらtrueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/13 : 新規作成
     */
    public static boolean isSdkVersion1_x() {
        // return ( new Build.VERSION() ).SDK_INT <= 4;
        return (EagleUtil.getBridge().getPlatformVersion()) <= 4;
    }

    /**
     * SDKバージョンが2.x系列だったらtrueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/13 : 新規作成
     */
    public static boolean isSdkVersion2_x() {
        // return ( new Build.VERSION() ).SDK_INT > 4;
        return (EagleUtil.getBridge().getPlatformVersion()) > 4;
    }

    /**
     * DP->Pixを変換して返す。
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPix(float dp, Context context) {
        float tmpDensity = context.getResources().getDisplayMetrics().density;
        // ピクセル値を求める
        int tmpPx = (int) (dp * tmpDensity + 0.5f);
        return tmpPx;
    }

    /**
     * デバッグモードならtrueを返す。
     * @return
     */
    public static boolean isDebug(Context context) {
        PackageManager manager = context.getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = manager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        if ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
            return true;
        }
        return false;
    }
}
