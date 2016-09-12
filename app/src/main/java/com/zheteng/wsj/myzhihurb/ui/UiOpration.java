package com.zheteng.wsj.myzhihurb.ui;

import android.os.Bundle;
import android.view.View;

/**
 * UI操作接口
 * Created by dzl on 2016/9/10.
 */
public interface UiOpration extends View.OnClickListener {
    /**
     * 返回Activity界面应显示的布局文件资源Id
     *
     * @return R.layout.*
     */
    int getLayoutResID();
    /**
     * 初始化该页面布局
     */
    void initView();



    /**
     * 初始化页面控件的监听器
     */
    void initListener();

    /**
     * 初始化页面数据
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);


}
