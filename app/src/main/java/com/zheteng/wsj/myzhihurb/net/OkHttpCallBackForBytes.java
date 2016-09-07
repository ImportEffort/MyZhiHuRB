package com.zheteng.wsj.myzhihurb.net;

import okhttp3.Call;

/**
 * Created by wsj20 on 2016/9/7.
 */
public interface OkHttpCallBackForBytes {

    void onFailure(Call call, Exception e);

    void onSuccess(Call call, byte[] bytes);

}
