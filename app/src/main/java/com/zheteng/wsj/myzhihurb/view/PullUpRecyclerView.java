package com.zheteng.wsj.myzhihurb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by wsj20 on 2016/9/9.
 */
public class PullUpRecyclerView extends RecyclerView {
    private int mLastVisiblePosition;//这里的mLastVisiblePosition 不包括以显示的最后一个条目
    private boolean isLoadMore;//是否正在加载更多

    public PullUpRecyclerView(Context context) {
        super(context);
    }

    public PullUpRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullUpRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * Called when the scroll position of this RecyclerView changes. Subclasses should use
     * this method to respond to scrolling within the adapter's data set instead of an explicit
     * listener.
     * <p/>
     * 这个方法在RecycleView中是空实现，但是当滑动的时候Recycleview会调用该方法
     */
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        //瀑布流拿到最后一个可见条目
        if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] position = manager.findLastCompletelyVisibleItemPositions(new
                    int[manager
                    .getSpanCount()]);
            mLastVisiblePosition = getMaxPosition(position);
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            //线性布局拿到最后一个条目 但是这里的mLastVisiblePosition 不包括以显示的最后一个条目
            LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
            mLastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
        }

        //判断条件 监听器不为空，最后一个可见条目是否为当前最后一条 是否为向上滑动 是否正在加载更多
        if (mListener != null && mLastVisiblePosition + 1 == getAdapter().getItemCount() && dy > 0 && !isLoadMore) {
            isLoadMore = true;
            mListener.pullToLoadData();
        }


    }

    private int getMaxPosition(int[] positions) {

        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 设置是否在完成
     *
     * @param isComplite true 加载完成，false未加载完成
     */
    public void setLoadMoreComplite(boolean isComplite) {
        //如果加载完成 设置正在加载标志位为false
        isLoadMore = !isComplite;
    }

    /**
     * 设置加载更多监听器
     *
     * @param listener
     */
    public void setLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }

    private OnLoadMoreListener mListener;

    /**
     * 加载更多的回调接口
     */
    public interface OnLoadMoreListener {
        void pullToLoadData();
    }

}
