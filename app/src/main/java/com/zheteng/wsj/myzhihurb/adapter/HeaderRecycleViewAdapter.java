package com.zheteng.wsj.myzhihurb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.BaseBean;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class HeaderRecycleViewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;//Header标志
    public static final int TYPE_NORMAL = 1;//正常的条目标志

    private View mHeaderView;//头布局
    private ArrayList<BaseBean> mDatas = new ArrayList<>();

    private OnItemClickListener mListener;

    /**
     * 设置条目点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 添加HeaderView
     *
     * @param headerView
     */

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;

        notifyItemInserted(0);//添加一个View在第0个位置，并通知recyclerView条目位置的改变
    }

    public View getmHeaderView() {
        return mHeaderView;
    }

    /**
     * 返回添加头布局后真实的条目数据
     *
     * @return
     */

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    /**
     * 设置数据 每次调用addDatas RecyclerView的内容就会重新加载
     *
     * @param datas 集合
     */
    public void addDatas(List<BaseBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setDatas(List<BaseBean> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获取条目类型，如果mHeaderView为空，表示用户并没有添加头布局，RecyclerView的条目内容为正常布局
     * <p/>
     * 如果添加了头部，则在则在Postion == 0 的时候返回头布局View，注意一旦调用了addHeaderView postion位置将会变化，头布局的位置为0
     * 其他位置返回 正常布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (mHeaderView == null) {
            return TYPE_NORMAL;
        }

        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    /**
     * RecycleView内部实现是通过onCreateViewHolder为不同条目类型返回不同的Holder对象
     *
     * @param parent
     * @param viewType 条目类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new Holder(mHeaderView);
        }
        //正常条目布局类型
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_items, parent, false);
        return new Holder(layout);
    }

    /**
     * 为条目设置内容显示
     *
     * @param holder   此参数表示每个条目的holder对象 如果有不同的条目类型，则holder的类型可能不同
     * @param position 此处的position从0开始，如果添加的头部0处就是headerView 这里是RecycleView内部真实的条目位置，包括头布局
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果当前条目是headerView的话则不需要设置数据类型，直接返回
        if (getItemViewType(position) == TYPE_HEADER)
            return;

        //如果没有设置头布局则从这里还是显示 我们需要拿到真正的位置然后从集合取数据 如果有头布局则position位置总是比没有添加头布局前大1

        final int pos = getRealPostion(holder);
        final BaseBean storiesBean = mDatas.get(pos);


        //如果这里不进行判断，下步强转的时候有可能导致类型转换失败
        if (holder instanceof Holder) {
            //由此可以看出实际上holder会向强转为对应的holder类型。
            //这里设置条目布局的显示的内容
            ((Holder) holder).mTv_des.setText(storiesBean.getTitle());

            if (storiesBean.getImages() != null) {
                if (storiesBean.getImages().size() > 0) {
                    ImageUtil.getInstance().displayImageDefault(storiesBean.getImages().get(0), ((Holder) holder).mIv_image);
                }
            }else {
                ((Holder) holder).mIv_image.setVisibility(View.GONE);
            }
            //条目点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(pos, storiesBean.getId()+"");
                    }
                }
            });
        }


    }

    /**
     * @param holder
     * @return 返回减去头布局个数的Postion
     */
    private int getRealPostion(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;

    }


    /**
     * 为adapter提供view的Holder对象
     * 构造方法 需要fbc 找到控件对象
     */
    private class Holder extends RecyclerView.ViewHolder {

        private ImageView mIv_image;
        private TextView mTv_des;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView)
                return;

            //这里返回正常条目的布局类型
            mTv_des = (TextView) itemView.findViewById(R.id.tv_items_Des);
            mIv_image = (ImageView) itemView.findViewById(R.id.iv_items_image);

        }
    }

    /**
     * HeaderRecyclerView的条目点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
}
