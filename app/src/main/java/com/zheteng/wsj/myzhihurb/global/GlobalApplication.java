package com.zheteng.wsj.myzhihurb.global;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.zheteng.wsj.myzhihurb.util.ChannelUtil;

/**
 * Created by wsj20 on 2016/9/6.
 */
public class GlobalApplication  extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //内存泄漏检测工具
        LeakCanary.install(this);
        //初始化ImageLoader
        initImageLoader();
        initUmengAnalytics();
    }

    /**
     * 初始化友盟统计
     */
    private void initUmengAnalytics() {

        String defaultChannel = "GooglePlay";
        String channel = ChannelUtil.getChannel(this, defaultChannel);
        String appkey = "57df704f67e58e201100432a";
        MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,appkey,channel));
    }

    /**
     * 初始化U—I-L
     */
    private void initImageLoader() {
        //初始化Imageloader的配置，包括缓存目录的初始化，线程池的初始化，缓存上限等的初始化
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }
}
