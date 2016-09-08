package com.zheteng.wsj.myzhihurb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.adapter.NavAdapter;
import com.zheteng.wsj.myzhihurb.bean.NavBean;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.net.UrlConstants;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;

import java.util.List;

import okhttp3.Call;

public class NavFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private View logHeaderView;
    private ListView listView;
    private View homeHeaderView;
    private NavAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //logHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.list_nav_log_header,container,false);
        //logHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.list_nav_home_header,container,false);
        logHeaderView = View.inflate(getContext(),R.layout.list_nav_log_header,null);
        homeHeaderView = View.inflate(getContext(),R.layout.list_nav_home_header,null);

        ImageView iv_userIcon = (ImageView) logHeaderView.findViewById(R.id.icon_user);
        return inflater.inflate(R.layout.fragment_nav,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeHeaderView.setBackgroundColor(getResources().getColor(R.color.light_gray));
                adapter.changeSelected(-1);
            }
        });


        listView = getListView();

        listView.addHeaderView(logHeaderView);
        listView.addHeaderView(homeHeaderView);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initNavData();
    }


    private void initNavData() {
        HttpUtil.getInstance().getJsonString(UrlConstants.ThemeUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                NavBean navBean = GsonUtil.parseJsonToBean(result, NavBean.class);
                List<NavBean.OthersBean> listData = navBean.getOthers();

                adapter = new NavAdapter(getActivity());
                adapter.setData(listData);
                listView.setAdapter(adapter);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.changeSelected(position);
        homeHeaderView.setBackgroundColor(getResources().getColor(R.color.write));
    }
}
