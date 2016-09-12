package com.zheteng.wsj.myzhihurb.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.BaseBean;
import com.zheteng.wsj.myzhihurb.bean.BeforeNewsBean;
import com.zheteng.wsj.myzhihurb.bean.NewsBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;
import com.zheteng.wsj.myzhihurb.net.NetConnectUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.List;

import okhttp3.Call;

/**
 * 侧边栏新闻的Fragment
 * Created by wsj20 on 2016/9/12.
 */
public class NewsFragment extends BaseNewsFragemnt{

    private View mHeaderView;
    private TextView mTv_tile;
    private ImageView mIv_header;
    private int mUrlId;//页面内数据url后缀id
    private int mBeforeId;
    private final static int REFRESH = 0;
    private final static int LOADMORE = 1;
    private int loadAction = REFRESH;
    private boolean isRefresh;


    public NewsFragment(int urlId) {
        mUrlId = urlId;
    }

    /**
     * 初始化了头布局
     * @param inflater inflater
     * @param container container
     */
    @Override
    protected void initHeaderView(LayoutInflater inflater, ViewGroup container) {
        mHeaderView = inflater.inflate(R.layout.news_layout_header, container, false);
        mTv_tile = (TextView) mHeaderView.findViewById(R.id.tv_news_header);
        mIv_header = (ImageView) mHeaderView.findViewById(R.id.iv_news_header);
    }

    /**
     * 在onViewCreated中调用
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        //启动页跳转过来的时候先展示缓存数据
        loadData(false);
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
     * 请求网络数据
     * @param isForeFromNet true 强制从网络请求，false 优先从缓存取
     */
    private void loadData(boolean isForeFromNet) {

        HttpUtil.getInstance().getJsonString(Constants.NewsPrefixUrl + mUrlId, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                // LogUtil.e(result);
                NewsBean newsBean = GsonUtil.parseJsonToBean(result, NewsBean.class);
                showData(newsBean);
                //
                isRefresh = false;
            }
        },isForeFromNet);

    }

    private void showData(NewsBean newsBean) {
        String description = newsBean.getDescription();
        String imageUrl = newsBean.getImage();
        initHeaderData(description, imageUrl);
        //RecycleView需要的数据
        List<BaseBean> stories = newsBean.getStories();

        mBeforeId = stories.get(stories.size() - 1).getId();//请求之前数据的id

        initRecycleData(stories);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void initRecycleData(List<BaseBean> stories) {

        if (loadAction == REFRESH) {
            mAdapter.setDatas(stories);
        } else if (loadAction == LOADMORE) {
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
    public void pullToLoadData() {
        //如果正在刷新更多应该不让其上拉
        if (isRefresh){
            return;
        }
        String url = String.format(Constants.NewsBeforeUrl,mUrlId,mBeforeId);
        HttpUtil.getInstance().getJsonString(url , new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onSuccess(Call call, String result) {
                loadAction = LOADMORE;
                BeforeNewsBean beforeNewsBean = GsonUtil.parseJsonToBean(result, BeforeNewsBean.class);
                showBeforeData(result, beforeNewsBean);
            }
        },false);
    }

    /**
     * 由于新闻栏目的往日数据有所不同，所以展示方法也不同
     * @param result 网络请求结果
     * @param beforeNewsBean 往日数据的封装类
     */
    private void showBeforeData(String result, BeforeNewsBean beforeNewsBean) {
        List<BaseBean> stories = beforeNewsBean.getStories();
        if (stories.size()>0) {
            mBeforeId = stories.get(stories.size() - 1).getId();
            initRecycleData(stories);
            mRecyclerView.setLoadMoreComplite(true);
        }
    }

    @Override
    public void onRefresh() {
        //应该禁用loadmore
        loadAction = REFRESH;
        isRefresh = true;
        loadData(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {

    }
}
