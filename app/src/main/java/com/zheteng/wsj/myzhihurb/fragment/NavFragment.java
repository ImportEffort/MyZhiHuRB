package com.zheteng.wsj.myzhihurb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.SlidingMenuAdapter;
import com.zheteng.wsj.myzhihurb.bean.SlidingMenuBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.ui.activity.MainActivity;
import com.zheteng.wsj.myzhihurb.ui.activity.SplashLogActivity;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;

import java.util.List;

import okhttp3.Call;

public class NavFragment extends ListFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View logHeaderView;
    private ListView listView;
    private View homeHeaderView;
    private SlidingMenuAdapter adapter;
    private FragmentActivity mActivity;
    private ImageView mIv_userIcon;
    private LinearLayout mll_homeHeader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHeaderView();
        return inflater.inflate(R.layout.fragment_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        initListener();
        initData();//当view初始化完成后，立刻设置数据
    }

    /**
     * 初始化策划菜单listView
     */
    private void initListView() {
        //为listView添加头布局
        listView = getListView();
        listView.addHeaderView(logHeaderView);
        listView.addHeaderView(homeHeaderView);
        adapter = new SlidingMenuAdapter(getActivity());
        listView.setAdapter(adapter);
    }
    /**
     * 初始化策划菜单，listview的头布局
     */
    private void initHeaderView() {
        logHeaderView = View.inflate(getContext(), R.layout.list_nav_log_header, null);
        homeHeaderView = View.inflate(getContext(), R.layout.list_nav_home_header, null);
        mIv_userIcon = (ImageView) logHeaderView.findViewById(R.id.icon_user);
        mll_homeHeader = (LinearLayout) homeHeaderView.findViewById(R.id.ll_homeheader);

    }

    /**
     * 初始化按钮的点击事件
     */
    private void initListener() {
        mIv_userIcon.setOnClickListener(this);
        mll_homeHeader.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }


    private void initData() {
        HttpUtil.getInstance().getJsonString(Constants.ThemeUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onSuccess(Call call, String result) {
                SlidingMenuBean navBean = GsonUtil.parseJsonToBean(result, SlidingMenuBean.class);
                List<SlidingMenuBean.OthersBean> listData = navBean.getOthers();
                adapter.setData(listData);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.changeSelected(position);
        homeHeaderView.setBackgroundColor(getResources().getColor(R.color.write));
        //adapter.getItem(position) 由于有头布局所以拿到位置不正确
        SlidingMenuBean.OthersBean item = (SlidingMenuBean.OthersBean) listView.getItemAtPosition(position);
        //替换Activity主界面fragment的内容显示
        getFragmentManager().beginTransaction().replace(
                R.id.fl_content, new NewsFragment(item.getId())//除了首页外数据需要使用由侧滑菜单的条目id组成url地址
        ).commit();

        ((MainActivity) mActivity).closeMenu();
        ((MainActivity) mActivity).setToolBarTitle(item.getName());
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_user://侧滑菜单登陆按钮点击事件
                startActivity(new Intent(getActivity(), SplashLogActivity.class));
                break;
            case R.id.ll_homeheader://侧滑菜单登陆按钮点击事件

                homeHeaderView.setBackgroundColor(getResources().getColor(R.color.light_gray));
                adapter.changeSelected(-1);

                getFragmentManager().beginTransaction().replace(

                        R.id.fl_content, new HomeFragment()

                ).commit();
                ((MainActivity) mActivity).closeMenu();
                ((MainActivity) mActivity).setToolBarTitle("首页");

                break;

        }
    }

}
