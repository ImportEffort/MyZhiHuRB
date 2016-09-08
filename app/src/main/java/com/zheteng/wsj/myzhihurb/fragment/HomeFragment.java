package com.zheteng.wsj.myzhihurb.fragment;

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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.HeaderRecycleViewAdapter;
import com.zheteng.wsj.myzhihurb.adapter.LoopViewpagerIndater;
import com.zheteng.wsj.myzhihurb.bean.LastNewBean;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.net.UrlConstants;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final int LOOPER_MESSAGE = 1000;
    private static final long TOP_NEWS_CHANGE_TIME = 2000;
    @InjectView(R.id.viewPager_main)
    ViewPager mViewPagerMain;
    @InjectView(R.id.indicator)
    CircleIndicator mIndicator;
    private HeaderRecycleViewAdapter adapter;
    private View mHeaderView;
    private boolean isLooper = false;
    private LoopViewpagerIndater mIndaterAdapter;
    private RecyclerView mRecyclerView;
    private final static int REFRESH = 0;
    private final static int LOADMORE = 1;
    private int loadAction = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mViewPagerMain.getCurrentItem();
            if (currentItem >= 4) {
                currentItem = -1;
            }
            mViewPagerMain.setCurrentItem(currentItem + 1);
            sendEmptyMessageDelayed(LOOPER_MESSAGE, 2000);
        }
    };
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initSwipeRefreshLayout(view);

        initHeaderView(inflater, container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HeaderRecycleViewAdapter();
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android
                .R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color
                .holo_red_dark);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initHeaderView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mHeaderView = inflater.inflate(R.layout.fragment_home_header, container, false);
        ButterKnife.inject(this, mHeaderView);
        mIndaterAdapter = new LoopViewpagerIndater();

    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initlooper();
    }

    private void loadData() {

        HttpUtil.getInstance().getJsonString(UrlConstants.LastUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                LastNewBean lastNewBean = GsonUtil.parseJsonToBean(result, LastNewBean.class);
                //轮播图需要数据
                List<LastNewBean.TopStoriesBean> top_stories = lastNewBean.getTop_stories();
                initHeaderData(top_stories);
                //RecycleView需要的数据
                List<LastNewBean.StoriesBean> stories = lastNewBean.getStories();
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
    private void initRecycleData(List<LastNewBean.StoriesBean> stories) {

        if (loadAction == 0) {
            adapter.setDatas(stories);
        } else if (loadAction == 1) {
            adapter.addDatas(stories);
        }
        adapter.setHeaderView(mHeaderView);
        adapter.setOnItemClickListener(new HeaderRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                ToastUtil.show("" + position);
                LogUtil.e(data);
            }
        });

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
                            mHandler.sendEmptyMessageDelayed(LOOPER_MESSAGE, 2000);
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
        mHandler.sendEmptyMessageDelayed(LOOPER_MESSAGE, 2000);
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
        loadAction = 0;
        loadData();
//        mRecyclerView.set
    }
}
