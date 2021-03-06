package com.zheteng.wsj.myzhihurb.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.zheteng.wsj.myzhihurb.ui.UiOpration;

/**
 * 所有fragment的基类
 * Created by wsj20 on 2016/9/12.
 */
public abstract class BaseFragment extends Fragment implements UiOpration {
    protected Activity mActivity;
    protected ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        rootView = (ViewGroup) inflater.inflate(getLayoutResID(), container, false);
        initFragmentView(inflater, container, savedInstanceState);
        initListener();
        return rootView;
    }

    /**
     * 初始化跟布局中的控件内容
     *
     * @param inflater           inflater
     * @param container          父容器
     * @param savedInstanceState bundle
     */
    protected abstract void initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        initData(savedInstanceState);
        initData(savedInstanceState,bundle);
    }

    protected abstract void initData(Bundle savedInstanceState, Bundle bundle);

    /**
     * fragment请使用initFragmentView初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 统计页面使用时间
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        // 统计页面使用时间
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}
