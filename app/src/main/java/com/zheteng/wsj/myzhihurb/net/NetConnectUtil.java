package com.zheteng.wsj.myzhihurb.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zheteng.wsj.myzhihurb.global.GlobalApplication;

/**
 * 检查网络是否可用的工具类
 * Created by wsj20 on 2016/9/12.
 */
public class NetConnectUtil {

    /**
     *
     * @return true 网络可用 false网络连接不可用
     */
    public static boolean isConnect() {

        ConnectivityManager service = (ConnectivityManager) GlobalApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = service.getActiveNetworkInfo();

        return info != null && info.isAvailable();
    }
}
