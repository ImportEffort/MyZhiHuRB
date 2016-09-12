package com.zheteng.wsj.myzhihurb.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.HeaderRecycleViewAdapter;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.ui.activity.DetailActivity;
import com.zheteng.wsj.myzhihurb.view.PullUpRecyclerView;

/**
 * 首页新闻，和其他栏目新闻的Fragment基类
 * Created by wsj20 on 2016/9/12.
 */
public abstract class BaseNewsFragemnt extends BaseFragment implements PullUpRecyclerView.OnLoadMoreListener, HeaderRecycleViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected PullUpRecyclerView mRecyclerView;
    protected HeaderRecycleViewAdapter mAdapter;

    @Override
    public int getLayoutResID() {
        return  R.layout.fragment_home;
    }

    @Override
    protected void initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (PullUpRecyclerView) rootView.findViewById(R.id.recyclerView);
        //初始化下拉刷新
        initSwipeRefreshLayout(rootView);
        //初始化mRecyclerView
        initRecyclerView();
        //初始化头布局
        initHeaderView(inflater, container);
    }

    /**
     * 初始化mRecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HeaderRecycleViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadMoreListener(this);
    }

    /**
     * 初始化RecycleView的头布局
     * @param inflater
     * @param container
     */
    protected abstract void initHeaderView(LayoutInflater inflater, ViewGroup container);


    private void initSwipeRefreshLayout(ViewGroup rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android
                .R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color
                .holo_red_dark);

    }

    @Override
    public void onItemClick(int position, String data) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(Constants.NEWS_ID, data);
        startActivity(intent);
    }
}
