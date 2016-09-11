package com.zheteng.wsj.myzhihurb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.HeaderRecycleViewAdapter;
import com.zheteng.wsj.myzhihurb.bean.BaseBean;
import com.zheteng.wsj.myzhihurb.bean.BeforeNewsBean;
import com.zheteng.wsj.myzhihurb.bean.NewsBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.ui.activity.DetailActivity;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.view.PullUpRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by wsj20 on 2016/9/10.
 */
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PullUpRecyclerView.OnLoadMoreListener, HeaderRecycleViewAdapter.OnItemClickListener {


    private int mUrlId;//页面内数据url后缀id
    PullUpRecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View mHeaderView;
    private TextView mTv_tile;
    private ImageView mIv_header;
    private HeaderRecycleViewAdapter mAdapter;

    private final static int REFRESH = 0;
    private final static int LOADMORE = 1;
    private int loadAction = 0;
    private int mBeforeId;

    public NewsFragment(int id) {
        mUrlId = id;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (PullUpRecyclerView) view.findViewById(R.id.recyclerView);

        //初始化下拉刷新
        initSwipeRefreshLayout(view);
        //初始化头布局
        initHeaderView(inflater, container);
        //初始化mRecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HeaderRecycleViewAdapter();
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreListener(this);

        return view;
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {
        HttpUtil.getInstance().getJsonString(Constants.NewsPrefixUrl + mUrlId, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                // LogUtil.e(result);
                NewsBean newsBean = GsonUtil.parseJsonToBean(result, NewsBean.class);

                String description = newsBean.getDescription();
                String imageUrl = newsBean.getImage();
                initHeaderData(description, imageUrl);
                //RecycleView需要的数据
                List<BaseBean> stories = newsBean.getStories();

                mBeforeId = stories.get(stories.size() - 1).getId();//请求之前数据的id

                initRecycleData(stories);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecycleData(List<BaseBean> stories) {

        if (loadAction == 0) {
            mAdapter.setDatas(stories);
        } else if (loadAction == 1) {
            mAdapter.addDatas(stories);
        }

        mAdapter.setHeaderView(mHeaderView);

    }


    private void initHeaderData(String description, String imageUrl) {
        if (!TextUtils.isEmpty(description)) {
            mTv_tile.setText(description);
        }

        ImageUtil.getInstance().displayImage(imageUrl,mIv_header);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    /**
     * 初始化下拉刷新控件
     *
     * @param view
     */
    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android
                .R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color
                .holo_red_dark);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initHeaderView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mHeaderView = inflater.inflate(R.layout.news_layout_header, container, false);
        mTv_tile = (TextView) mHeaderView.findViewById(R.id.tv_news_header);
        mIv_header = (ImageView) mHeaderView.findViewById(R.id.iv_news_header);

    }

    @Override
    public void onRefresh() {
        loadAction = REFRESH;
        loadData();
    }

    @Override
    public void pullToLoadData() {

        String url = String.format(Constants.NewsBeforeUrl,mUrlId,mBeforeId);

        HttpUtil.getInstance().getJsonString(url , new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onSuccess(Call call, String result) {

                loadAction = LOADMORE;

                BeforeNewsBean beforeNewsBean = GsonUtil.parseJsonToBean(result, BeforeNewsBean.class);
                List<BaseBean> stories = beforeNewsBean.getStories();
                mBeforeId = stories.get(stories.size()-1).getId();

                initRecycleData(stories);

                mRecyclerView.setLoadMoreComplite(true);
              LogUtil.e(result);
            }
        });
    }

    @Override
    public void onItemClick(int position, String data) {
        LogUtil.e(Constants.NewsDetailUrl + data);

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.NEWS_ID, data);
        startActivity(intent);
    }
}
