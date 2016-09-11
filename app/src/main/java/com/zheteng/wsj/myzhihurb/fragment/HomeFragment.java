package com.zheteng.wsj.myzhihurb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.HeaderRecycleViewAdapter;
import com.zheteng.wsj.myzhihurb.adapter.LoopViewpagerIndater;
import com.zheteng.wsj.myzhihurb.bean.BaseBean;
import com.zheteng.wsj.myzhihurb.bean.LastNewBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.ui.activity.DetailActivity;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.view.PullUpRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PullUpRecyclerView.OnLoadMoreListener, HeaderRecycleViewAdapter.OnItemClickListener {


    private static final int LOOPER_MESSAGE = 1000;
    private static final long TOP_NEWS_CHANGE_TIME = 4000;
    @InjectView(R.id.viewPager_main)
    ViewPager mViewPagerMain;
    @InjectView(R.id.indicator)
    CircleIndicator mIndicator;
    private HeaderRecycleViewAdapter adapter;
    private View mHeaderView;
    private boolean isLooper = false;
    private LoopViewpagerIndater mIndaterAdapter;
    private PullUpRecyclerView mRecyclerView;
    private final static int REFRESH = 0;
    private final static int LOADMORE = 1;
    private int loadAction = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mLastData;//最新消息的日期


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mViewPagerMain.getCurrentItem();
            if (currentItem >= 4) {
                currentItem = -1;
            }
            mViewPagerMain.setCurrentItem(currentItem + 1);
            sendEmptyMessageDelayed(LOOPER_MESSAGE, TOP_NEWS_CHANGE_TIME);
        }
    };
    private LastNewBean mLastNewBean;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (PullUpRecyclerView) view.findViewById(R.id.recyclerView);
        //初始化下拉刷新
        initSwipeRefreshLayout(view);
        //初始化头布局
        initHeaderView(inflater, container);
        //初始化mRecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HeaderRecycleViewAdapter();
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreListener(this);

        return view;
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

    private void initHeaderView(final LayoutInflater inflater, @Nullable ViewGroup container) {
        mHeaderView = inflater.inflate(R.layout.fragment_home_header, container, false);
        ButterKnife.inject(this, mHeaderView);
        mIndaterAdapter = new LoopViewpagerIndater();
        //点击跳转到详情界面
        mIndaterAdapter.setOnPagerClickLisener(new LoopViewpagerIndater.OnPagerClickLisener() {
            @Override
            public void onPagerClick(int id) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(Constants.NEWS_ID,id+"");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initlooper();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private void loadData() {

        HttpUtil.getInstance().getJsonString(Constants.LastUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onSuccess(Call call, String result) {

             //LogUtil.e("当前线程" + Thread.currentThread().getName());

                LastNewBean lastNewBean = GsonUtil.parseJsonToBean(result, LastNewBean.class);
                mLastData = lastNewBean.getDate();
                //LogUtil.e("mData" + lastNewBean.getDate());

                //轮播图需要数据
                List<LastNewBean.TopStoriesBean> top_stories = lastNewBean.getTop_stories();
                initHeaderData(top_stories);
                //RecycleView需要的数据
                List<BaseBean> stories = lastNewBean.getStories();
                initRecycleData(stories);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 初始化首页内容
     *
     * @param stories
     */
    private void initRecycleData(List<BaseBean> stories) {

        if (loadAction == 0) {
            adapter.setDatas(stories);
        } else if (loadAction == 1) {
            adapter.addDatas(stories);
        }
        adapter.setHeaderView(mHeaderView);

    }

    private void initHeaderData(List<LastNewBean.TopStoriesBean> top_stories) {

        if (top_stories == null || top_stories.size() == 0) {
            return;
        }
        //初始化头布局的数据
        if (loadAction == 0) {
            mIndaterAdapter.setData(top_stories);
        } else if (loadAction == 1) {
            mIndaterAdapter.addData(top_stories);
        }
        mViewPagerMain.setAdapter(mIndaterAdapter);

        mIndicator.setViewPager(mViewPagerMain);

    }

    /**
     * 开启轮播
     */
    private void initlooper() {

        mViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE://0
                        if (!isLooper) {
                            mHandler.sendEmptyMessageDelayed(LOOPER_MESSAGE, TOP_NEWS_CHANGE_TIME);
                            isLooper = true;
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING://1
                        mHandler.removeCallbacksAndMessages(null);
                        isLooper = false;
                        break;
                }
            }
        });
        mHandler.sendEmptyMessageDelayed(LOOPER_MESSAGE, TOP_NEWS_CHANGE_TIME);
        isLooper = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        HttpUtil.getInstance().cancelAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);


    }

    @Override
    public void onRefresh() {
        loadAction = REFRESH;
        loadData();
    }

    @Override
    public void pullToLoadData() {

        HttpUtil.getInstance().getJsonString(Constants.BeforeUrl + mLastData, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onSuccess(Call call, String result) {

                loadAction = LOADMORE;

                mLastNewBean = GsonUtil.parseJsonToBean(result, LastNewBean.class);
                mLastData = mLastNewBean.getDate();
                //LogUtil.e("mData" + lastNewBean.getDate());

                //RecycleView需要的数据
                List<BaseBean> stories = mLastNewBean.getStories();
                initRecycleData(stories);

                mRecyclerView.setLoadMoreComplite(true);
            }
        });
    }

    @Override
    public void onItemClick(int position, String data) {
        //BaseBean baseBean = mLastNewBean.getStories().get(position);

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.NEWS_ID, data);
        startActivity(intent);
    }
}