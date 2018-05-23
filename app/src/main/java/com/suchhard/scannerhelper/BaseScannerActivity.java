package com.suchhard.scannerhelper;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

/**
 * @author Mr.Ye
 * @description 扫码设备的数据获取基类
 * @datetime 2018/05/23 16:22
 * @email superrhye@163.com
 */

public abstract class BaseScannerActivity extends AppCompatActivity {
    /**
     * 你的设备名字，可以通过日志查看
     */
    private static final String BARCODE_DEVICES = "";

    /**
     * 最终扫到条码
     */
    private StringBuilder scanResult = new StringBuilder();

    /**
     * 该判断也可忽略，主要是为了防止input设备进行操作
     *
     * @return
     */
    public boolean hasBarcodeInputDeviceExist() {
        for (int deviceId : InputDevice.getDeviceIds()) {
            String name = InputDevice.getDevice(deviceId).getName().trim();
            Log.e("--------------input设备：", name);
            if (BARCODE_DEVICES.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扫码枪或者仪器，其实是基于键盘输入的，那事件会先分发到获取焦点的界面中的 dispatchKeyEvent(KeyEvent event)
     * 但是没办法统一为应用设置监听，只能在每个Activity作监听。这里可以基类（BaseScannerActivity）处理扫码器的输入事件
     * 也可以通过AccessibilityService（不懂的可以百度，我也没深入了解） 的 onKeyEvent(KeyEvent event) 事件去处理，前提是辅助功能要手动开启。
     * KeyEvent继承了InputEvent，可以通过getDevice获取设备，来加准判断
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (hasBarcodeInputDeviceExist()) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                int keyCode = event.getKeyCode();
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                    scanResult.append(keyCode - KeyEvent.KEYCODE_0);
                    Log.e("--------------识别中的付款码", scanResult + "");
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.e("--------------识别到的付款码", scanResult + "");
                    //自行回调，eventbus 接口 广播，随便吧
                    if (mCodeCallBack != null) {
                        mCodeCallBack.callback(scanResult.toString().trim());
                    }

                    //清空result
                    scanResult = new StringBuilder();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private CodeCallBack mCodeCallBack;

    public void setCodeCallBack(CodeCallBack codeCallBack) {
        this.mCodeCallBack = codeCallBack;
    }

    public interface CodeCallBack {
        void callback(String code);
    }
}
