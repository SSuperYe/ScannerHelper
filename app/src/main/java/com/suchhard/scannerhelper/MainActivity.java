package com.suchhard.scannerhelper;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseScannerActivity implements BaseScannerActivity.CodeCallBack {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCodeCallBack(this);
    }

    @Override
    public void callback(String code) {
        TextView textView = findViewById(R.id.tv_code);
        textView.setText("scan code : " + code);
    }
}
