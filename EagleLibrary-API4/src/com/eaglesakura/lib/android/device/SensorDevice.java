/**
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
package com.eaglesakura.lib.android.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * センサー情報の管理を行う。
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
public class SensorDevice implements SensorEventListener {

    /**
     *
     */
    private SensorManager nativeSensor;
    /**
     *
     */
    private Sensor sensor = null;
    /**
     *
     */
    private int sensorType = 0;
    /**
     * 現在の値。
     */
    private float[] value = new float[3];

    /**
     * 前フレームの値。
     */
    private float[] before = new float[3];

    /**
     * 現在フレームの値。
     */
    private float[] current = new float[3];

    /**
     * インデックスの参照値。
     */
    private int[] valueIndex = { 0, 1, 2 };

    /**
     * 方位を示す。
     */
    public static final int eValueTypeAzimuth = 0;

    /**
     * ピッチを示す。
     */
    public static final int eValueTypePitch = 1;

    /**
     * ロールを示す。
     */
    public static final int eValueTypeRoll = 2;

    /**
     * X軸方向加速度。
     */
    public static final int eValueTypeXAccel = 0;

    /**
     * Y軸方向加速度。
     */
    public static final int eValueTypeYAccel = 1;

    /**
     * Z軸方向加速度。
     */
    public static final int eValueTypeZAccel = 2;

    /**
     * 傾きセンサー。
     */
    public static final int eSensorTypeOrientation = Sensor.TYPE_ORIENTATION;

    /**
     * 加速度センサー。
     */
    public static final int eSensorTypeAccel = Sensor.TYPE_ACCELEROMETER;

    /**
     * 最高速度でセンサー更新を行う。
     */
    public static final int eSensorDelayFastest = SensorManager.SENSOR_DELAY_FASTEST;

    /**
     * 通常速度。
     */
    public static final int eSensorDelayNormal = SensorManager.SENSOR_DELAY_NORMAL;

    /**
     * UIにあわせる。
     */
    public static final int eSensorDelayUI = SensorManager.SENSOR_DELAY_UI;

    /**
     * センサー管理。
     *
     * @author eagle.sakura
     * @version 2009/11/29 : 新規作成
     */
    public SensorDevice(Context context, int eSensorType, int eSensorDelay) {
        nativeSensor = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        sensor = nativeSensor.getSensorList(eSensorType).get(0);

        // ! 高速動作のセンサーとして登録
        nativeSensor.registerListener(this, sensor, eSensorDelay);
        sensorType = eSensorType;
    }

    /**
     * 毎フレームの更新を行う。
     *
     * @author eagle.sakura
     * @version 2010/05/22 : 新規作成
     */
    public void update() {
        for (int i = 0; i < 3; ++i) {
            before[i] = current[i];
            current[i] = value[i];
        }
    }

    /**
     * 重力加速度を取得する。<BR>
     * 現時点で地球以外の需要は無。
     *
     * @author eagle.sakura
     * @param eValueType
     * @return
     * @version 2010/05/22 : 新規作成
     */
    public float getGravity(int eValueType) {
        return getValue(eValueType) / SensorManager.GRAVITY_EARTH;
    }

    /**
     * 縦向きでデバイスを使用する。
     */
    public static final int eDirectionTypeVertical = 0;

    /**
     * 横向きでデバイスを使用する。
     */
    public static final int eDirectionTypeHorizontal = 1;

    /**
     * デバイスの向きを指定する。
     *
     * @author eagle.sakura
     * @param eDirectionType
     * @version 2009/11/29 : 新規作成
     */
    public void setDeviceDirection(int eDirectionType) {
        // ! 縦向きで使用する。
        if (eDirectionType == eDirectionTypeVertical) {
            for (int i = 0; i < 3; ++i) {
                valueIndex[i] = i;
            }
        }
        // ! 横向きで使用する。
        else {
            // ! 加速度センサー
            if (sensorType == eSensorTypeAccel) {
                valueIndex[0] = eValueTypeYAccel;
                valueIndex[1] = eValueTypeXAccel;
                valueIndex[2] = eValueTypeZAccel;
            }
            // ! 磁気センサー
            else {
                valueIndex[0] = 0;
                valueIndex[2] = 1;
                valueIndex[1] = 2;
            }
        }
    }

    /**
     * センサーの値を取得する。
     *
     * @author eagle.sakura
     * @param eValueType
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public float getValue(int eValueType) {
        int index = valueIndex[eValueType];
        return value[index];
    }

    /**
     * センサーの値が変更された。
     *
     * @author eagle.sakura
     * @param event
     * @version 2009/11/29 : 新規作成
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        for (int i = 0; i < 3; ++i) {
            value[i] = event.values[i];
        }
    }

    /**
     * 精度変更をされた。
     *
     * @author eagle.sakura
     * @param sensor
     * @param accuracy
     * @version 2009/11/29 : 新規作成
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
