package com.zheteng.wsj.myzhihurb.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.LoopViewpagerIndater;
import com.zheteng.wsj.myzhihurb.bean.BaseBean;
import com.zheteng.wsj.myzhihurb.bean.LastNewBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.listener.MyPageChangeListener;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.NetConnectUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.ui.activity.DetailActivity;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

/**
 * 首页数据展示的fragment
 * Created by wsj20 on 2016/9/12.
 */
public class HomeFragment extends BaseNewsFragemnt implements LoopViewpagerIndater.OnPagerClickLisener {

    private static final int LOOPER_MESSAGE = 1000;
    private static final long TOP_NEWS_CHANGE_TIME = 4000;
    private final static int REFRESH = 0;
    private final static int LOADMORE = 1;
    private int loadAction = REFRESH;

    private static ViewPager mViewPagerMain;
    private CircleIndicator mIndicator;
    private View mHeaderView;
    private LoopViewpagerIndater mIndaterAdapter;
    private boolean isLooper;
    private String mLastData;

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
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


    @Override
    protected void initHeaderView(LayoutInflater inflater, ViewGroup container) {
        mHeaderView = inflater.inflate(R.layout.fragment_home_header, container, false);
        mViewPagerMain = (ViewPager) mHeaderView.findViewById(R.id.viewPager_main);
        mIndicator = (CircleIndicator) mHeaderView.findViewById(R.id.indicator);

        mAdapter.setHeaderView(mHeaderView);
        mIndaterAdapter = new LoopViewpagerIndater();
        //点击跳转到详情界面
        mIndaterAdapter.setOnPagerClickLisener(this);
       /* mViewPagerMain.setAdapter(mIndaterAdapter);
        mIndicator.setViewPager(mViewPagerMain);*/
    }

    @Override
    public void onClick(View v) {

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
                LastNewBean mLastNewBean = GsonUtil.parseJsonToBean(result, LastNewBean.class);
                sateBeforeData(mLastNewBean);
            }
        }, false);
    }

    /**
     * 显示上拉加载的数据
     * @param mLastNewBean 之前的新闻数据
     */
    private void sateBeforeData(LastNewBean mLastNewBean) {
        mLastData = mLastNewBean.getDate();
        //LogUtil.e("mData" + lastNewBean.getDate());

        //RecycleView需要的数据
        List<BaseBean> stories = mLastNewBean.getStories();
        initRecycleData(stories);

        mRecyclerView.setLoadMoreComplite(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        HttpUtil.getInstance().cancelAll();
    }

    @Override
    public void onRefresh() {
        loadAction = REFRESH;
        loadData(true);
    }

    /**
     * 第一次展示的数据
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        //启动页跳转过来的时候先展示缓存数据
        loadData(false);
        startlooper();
        mSwipeRefreshLayout.setRefreshing(true);
        //在网络良好的情况下，进入页面就刷新
        if (NetConnectUtil.isConnect()) {
            loadData(true);//强制从网络取数据
        } else {
            mSwipeRefreshLayout.setRefreshing(false);//网络有问题也要结束刷新
            ToastUtil.show("网络开了个小差，请稍受再试");
        }
    }

    /**
     * 开启轮播
     */
    private void startlooper() {
        mViewPagerMain.addOnPageChangeListener(looperListener);
        mHandler.sendEmptyMessageDelayed(LOOPER_MESSAGE, TOP_NEWS_CHANGE_TIME);
        isLooper = true;
    }


    /**
     * 从网络请求数据，如果存在缓存则先展示的缓存，缓存事件为7天
     */
    private void loadData(boolean isFroceNet) {


        HttpUtil.getInstance().getJsonString(Constants.LastUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);//网络有问题也要结束刷新
                ToastUtil.show("网络开了个小差，请稍受再试");
            }

            @Override
            public void onSuccess(Call call, String result) {
                //LogUtil.e("onSuccess" + result);
                showData(result);
            }
        }, isFroceNet);
    }

    /**
     * 根据请求的数据，展示数据
     *
     * @param result 要展示的新闻内容
     */
    private void showData(String result) {
        //LogUtil.e("当前线程" + Thread.currentThread().getName());
        LastNewBean lastNewBean = GsonUtil.parseJsonToBean(result, LastNewBean.class);
        mLastData = lastNewBean.getDate();
        //轮播图需要数据
        List<LastNewBean.TopStoriesBean> top_stories = lastNewBean.getTop_stories();
        initHeaderData(top_stories);
        mViewPagerMain.setAdapter(mIndaterAdapter);
        mIndicator.setViewPager(mViewPagerMain);
        //RecycleView需要的数据
        List<BaseBean> stories = lastNewBean.getStories();
        initRecycleData(stories);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity =null;

    }

    /**
     * 初始化首页内容
     *
     * @param stories 列表数据封装类
     */
    private void initRecycleData(List<BaseBean> stories) {

        if (loadAction == 0) {
            mAdapter.setDatas(stories);
        } else if (loadAction == 1) {
            mAdapter.addDatas(stories);
        }
    }

    /**
     * 初始化头部轮播图布局
     *
     * @param top_stories 轮播图
     */
    private void initHeaderData(List<LastNewBean.TopStoriesBean> top_stories) {

        if (top_stories == null || top_stories.size() == 0) {
            return;
        }
        //初始化头布局的数据
        if (loadAction == REFRESH) {
            mIndaterAdapter.setData(top_stories);
        } else if (loadAction == LOADMORE) {
            mIndaterAdapter.addData(top_stories);
        }


    }

    /**
     * 录播图点击事件
     *
     * @param id 被点击的轮播图id
     */
    @Override
    public void onPagerClick(int id) {
        new LoopViewpagerIndater.OnPagerClickLisener() {
            @Override
            public void onPagerClick(int id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.NEWS_ID, id + "");
                startActivity(intent);
            }
        };
    }

    /**
     * 轮播图监听器
     */
    private MyPageChangeListener looperListener = new MyPageChangeListener() {
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
    };

}
