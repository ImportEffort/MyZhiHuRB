package com.zheteng.wsj.myzhihurb.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wsj20 on 2016/9/6.
 */
public class HttpUtil {

    private static HttpUtil instance = new HttpUtil();
    private OkHttpClient mOkHttpClient;
    private Handler mHanlder;
    private Call mCall;
    private HttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mHanlder = new Handler(Looper.getMainLooper());//获取主线程的Hander
    }

    public static HttpUtil getInstance() {
        return instance;
    }

    /**
     * 普通get请求
     *
     * @param url           请求的的目的地址
     * @param callInterface 网络请求的回调接口
     */
    public void getJsonString(final String url, final OkHttpCallBackForString callInterface) {
        String cahce = CacheMananger.getInstance().loadCahce(url);
        //如果该url地址内容存在缓存则返回缓存数据
        if (!TextUtils.isEmpty(cahce)){
            //LogUtil.e("返回的是缓存数据");
            callInterface.onSuccess(mCall,cahce);
            return;
        }

        //如果不存在该url地址的缓存 则请求网络数据

        Request request = new Request.Builder().url(url).build();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callInterface.onFailure(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                final String result = response.body().string();
                //请求成功保存缓存数据
                CacheMananger.getInstance().saveCache(url,result);
               // LogUtil.e("返回的是网络");

                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        callInterface.onSuccess(call, result);
                    }
                });
            }
        });
    }

    /**
     * 注意 该方法onSuccess方法在子线程返回
     *
     * @param url
     * @param callInterface
     */
    public void getResponseBytes(String url, final OkHttpCallBackForBytes callInterface) {

        Request request = new Request.Builder().url(url).build();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callInterface.onFailure(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                final byte[] result = response.body().bytes();

                callInterface.onSuccess(call, result);

            }
        });

    }

    /**
     * 取消正在进行的网络请求
     */
    public void cancelAll(){

        if (mOkHttpClient!= null && instance != null && mCall != null && !mCall.isCanceled())
            instance.mCall.cancel();
    }

}
