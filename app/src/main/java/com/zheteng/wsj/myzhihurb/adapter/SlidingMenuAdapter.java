package com.zheteng.wsj.myzhihurb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.SlidingMenuBean;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class SlidingMenuAdapter extends BaseAdapter {

    private  int selectItem = -1;

    private Context context;
//    private List<SlidingMenuBean.OthersBean> data;
    private ArrayList<SlidingMenuBean.OthersBean> data = new ArrayList<>();

    public SlidingMenuAdapter(Context context) {
        this.context = context;
    }

    public void changeSelected(int positon){ //刷新方法
        if(positon != selectItem){
            selectItem = positon - 2;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置侧边栏数据显示
     * @param data
     */
    public void setData(List<SlidingMenuBean.OthersBean> data) {
        this.data = (ArrayList<SlidingMenuBean.OthersBean>) data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_nav_items, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //改变listview选中条目的颜色
        if (selectItem == position) {
            viewHolder.rlItem.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        }else {
            viewHolder.rlItem.setBackgroundColor(context.getResources().getColor(R.color.write));
        }
        viewHolder.tvTitle.setText(data.get(position).getName());
        viewHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("关注成功，关注内容以后会在主页呈现");
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_follow)
        TextView tvFollow;
        @InjectView(R.id.rl_item)
        RelativeLayout rlItem;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }
    }
}
