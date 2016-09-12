package com.zheteng.wsj.myzhihurb.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zheteng.wsj.myzhihurb.ui.UiOpration;

import butterknife.ButterKnife;

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

        initView();
        setToolBar();

        initListener();
        initData(savedInstanceState);
    }

    protected abstract Activity setActivity();

    @Override
    public void initView() {

    }

    /**
     * 设置该页面的toolbar显示内容
     */
    public  void setToolBar(){

    };


}
