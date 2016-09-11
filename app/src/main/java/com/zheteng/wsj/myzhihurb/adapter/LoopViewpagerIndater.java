package com.zheteng.wsj.myzhihurb.adapter;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.LastNewBean;
import com.zheteng.wsj.myzhihurb.global.GlobalApplication;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class LoopViewpagerIndater extends PagerAdapter {

    ImageView mIvViewpagerLooper;
    TextView mTvViewpagerTitle;
    private ArrayList<LastNewBean.TopStoriesBean> mDatas = new ArrayList<>();
    private Context mContext = GlobalApplication.context;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private FrameLayout mFlTopContent;

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
        View view = LayoutInflater.from(GlobalApplication.context).inflate(R.layout.looper_viewpager_item, container, false);
        mIvViewpagerLooper = (ImageView) view.findViewById(R.id.iv_viewpager_top);
        mTvViewpagerTitle = (TextView) view.findViewById(R.id.tv_viewpager_title);
        mFlTopContent = (FrameLayout) view.findViewById(R.id.fl_top_cotent);


        final LastNewBean.TopStoriesBean topStoriesBean = mDatas.get(position);
        mIvViewpagerLooper.setColorFilter((Integer) argbEvaluator.evaluate(0.8f, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
        ImageUtil.getInstance().displayImageDefault(topStoriesBean.getImage(), mIvViewpagerLooper);
        mTvViewpagerTitle.setText(topStoriesBean.getTitle());
        mFlTopContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLisener != null && topStoriesBean.getId() > 0) {
                    mLisener.onPagerClick(topStoriesBean.getId());
                }
            }
        });
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public void setOnPagerClickLisener(OnPagerClickLisener lisener) {
        mLisener = lisener;
    }

    private OnPagerClickLisener mLisener;


    public interface OnPagerClickLisener {
        void onPagerClick(int id);
    }
}
