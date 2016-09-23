package com.zheteng.wsj.myzhihurb.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.zheteng.wsj.myzhihurb.ui.UiOpration;

import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by wsj20 on 2016/9/12.
 */
public abstract class BaseActivity extends AppCompatActivity implements UiOpration {
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.inject(setActivity());
        context = this;
        ShareSDK.initSDK(context, "171265ec57988");
        initView();
        setToolBar();

        initListener();
        initData(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
    // 统计页面使用时间
        MobclickAgent.onPageStart(getClass().getSimpleName());
    // 开始统计
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    // 统计页面使用时间
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    // 关闭统计
        MobclickAgent.onPause(this);
    }

    protected abstract Activity setActivity();

    @Override
    public void initView() {

    }

    /**
     * 设置该页面的toolbar显示内容
     */
    public void setToolBar() {

    }

    ;


}
