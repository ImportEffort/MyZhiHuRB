package com.zheteng.wsj.myzhihurb.util;

import android.widget.Toast;

import com.zheteng.wsj.myzhihurb.global.GlobalApplication;

/**
 * Created by wsj20 on 2016/9/6.
 * 一个弹Toast的利器，避免了Toast重复弹出
 */
public class ToastUtil {

    public static Toast mToast;
    public static void show(String text) {

        if (mToast == null){
            mToast = Toast.makeText(GlobalApplication.context, text, Toast.LENGTH_SHORT);
        }else {
            //如果当前的Toast没有消失就重新设置toast的内容而不是重新new一个Toast
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showLong(String text) {

        if (mToast == null){
            mToast = Toast.makeText(GlobalApplication.context, text, Toast.LENGTH_LONG);
        }else {
            //如果当前的Toast没有消失就重新设置toast的内容而不是重新new一个Toast
            mToast.setText(text);
        }
        mToast.show();
    }
}
