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
 * 网络请求的帮助类，请求的同时保存了缓存数据
 * getStringJson() 表示从网络后去json字符串数据
 * getResponseBytes()表示从网络数据比特数据
 * 注意后者返回是在异步线程，而前者是在ui线程
 * Created by wsj20 on 2016/9/6.
 */
public class HttpUtil {

    private static HttpUtil instance = new HttpUtil();
    private OkHttpClient mOkHttpClient;
    private static Handler mHanlder;
    private Call mCall;

    private HttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mHanlder = new Handler(Looper.getMainLooper());//获取主线程的Hander
    }

    public static HttpUtil getInstance() {
        return instance;
    }

    /**
     * @param url               请求数据的url地址
     * @param callInterface     请求成功的回调接口
     * @param isForceRequestNet 是否强制从网络取数据
     */
    public void getJsonString(String url, OkHttpCallBackForString callInterface, boolean isForceRequestNet) {

        if (isForceRequestNet) {
            getJsonStringFromNet(url, callInterface);
        } else {
            String cahce = CacheMananger.getInstance().loadCahce(url);

            //如果该url地址内容存在缓存则返回缓存数据
            if (!TextUtils.isEmpty(cahce)) {
                callInterface.onSuccess(mCall, cahce);
            } else {
                //如果不存在该url地址的缓存（过期后被删除） 则请求网络数据
                getJsonStringFromNet(url, callInterface);
            }
        }
    }

    /**
     * 单纯从网络请求的数据，不从缓存取
     *
     * @param url           请求数据的url地址
     * @param callInterface 请求成功的回调接口
     */
    private void getJsonStringFromNet(final String url, final OkHttpCallBackForString callInterface) {
        Request request = new Request.Builder().url(url).build();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callInterface != null) {
                            callInterface.onFailure(call, e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String result = response.body().string();
                //请求成功保存缓存数据
                CacheMananger.getInstance().saveCache(url, result);
                // LogUtil.e("返回的是网络");

                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callInterface != null) {
                            callInterface.onSuccess(call, result);
                        }
                    }
                });
            }
        });
    }

    /**
     * 注意 该方法onSuccess方法在子线程返回
     *
     * @param url           请求数据的url地址
     * @param callInterface 请求成功的回调接口
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
    public void cancelAll() {

        if (mOkHttpClient != null && instance != null && mCall != null && !mCall.isCanceled())
            instance.mCall.cancel();
    }

}
