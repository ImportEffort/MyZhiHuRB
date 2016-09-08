package com.zheteng.wsj.myzhihurb.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.LastNewBean;
import com.zheteng.wsj.myzhihurb.global.GlobalApplication;
import com.zheteng.wsj.myzhihurb.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class LoopViewpagerIndater extends PagerAdapter {

    @InjectView(R.id.iv_viewpager_looper)
    ImageView mIvViewpagerLooper;
    @InjectView(R.id.tv_viewpager_title)
    TextView mTvViewpagerTitle;
    private ArrayList<LastNewBean.TopStoriesBean> mDatas = new ArrayList<>();
    private Context mContext = GlobalApplication.context;

    public void setData(List<LastNewBean.TopStoriesBean> data) {
        mDatas = (ArrayList<LastNewBean.TopStoriesBean>) data;
        notifyDataSetChanged();
    }

    public void addData(List<LastNewBean.TopStoriesBean> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //View view = View.inflate(mContext, R.layout.looper_viewpager_item,null);
        View view = LayoutInflater.from(GlobalApplication.context).inflate(R.layout.looper_viewpager_item,container,false);
        ButterKnife.inject(this, view);

        LastNewBean.TopStoriesBean topStoriesBean = mDatas.get(position);

        ImageUtil.getInstance().displayImageDefault(topStoriesBean.getImage(),mIvViewpagerLooper);
        mTvViewpagerTitle.setText(topStoriesBean.getTitle());

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
