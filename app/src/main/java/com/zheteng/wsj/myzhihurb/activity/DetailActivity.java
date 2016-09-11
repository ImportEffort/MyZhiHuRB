package com.zheteng.wsj.myzhihurb.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.ContentBean;
import com.zheteng.wsj.myzhihurb.bean.NewsExtra;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.net.UrlConstants;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by wsj20 on 2016/9/10.
 */
public class DetailActivity extends AppCompatActivity {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.webview)
    WebView mWebview;
    @InjectView(R.id.coordeinatorlayout)
    CoordinatorLayout mCoordeinatorlayout;
    @InjectView(R.id.iv_top)
    ImageView mIvTop;
    @InjectView(R.id.tv_title_top)
    TextView mTvTitleTop;
    @InjectView(R.id.tv_content_top)
    TextView mTvContentTop;
    @InjectView(R.id.rl_top)
    RelativeLayout mRlTop;
    @InjectView(R.id.iv_share)
    ImageView mIvShare;
    @InjectView(R.id.iv_star)
    ImageView mIvStar;
    @InjectView(R.id.iv_comment)
    ImageView mIvComment;
    @InjectView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @InjectView(R.id.iv_thumb)
    ImageView mIvThumb;
    @InjectView(R.id.tv_thumb_num)
    TextView mTvThumbNum;
    private boolean isThumb;
    private String mNews_Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        initView();
        initLisener();
        initData();
    }


    /**
     * 初始化View
     */
    private void initView() {
        setToolBar();
        setWebView();

    }

    /**
     * 用于界面展示的展示的时候初始化展示数据
     */
    private void initData() {
        mNews_Id = getIntent().getStringExtra(UrlConstants.NEWS_ID);
        loadData();
        upateToolBar();
    }

    /**
     * 设置点击事件的监听
     */
    public void initLisener() {
        // 返回按键监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                SystemClock.currentThreadTimeMillis();
                System.currentTimeMillis();
            }
        });
        mIvThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isThumb) {
                    mIvThumb.setImageResource(R.drawable.thumb_red);
                    ToastUtil.show("点赞成功");
                    mTvCommentNum.setText(Integer.parseInt(mTvCommentNum.getText().toString()) + 1 + "");
                    isThumb = true;
                } else {
                    mIvThumb.setImageResource(R.drawable.thumb);
                    ToastUtil.show("取消点赞");
                    mTvCommentNum.setText(Integer.parseInt(mTvCommentNum.getText().toString()) - 1 + "");
                    isThumb = false;
                }

            }
        });
    }


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.write));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));//设置返回键的图标
    }

    /**
     * 获取新闻详情页内容
     */
    private void loadData() {
        LogUtil.e(UrlConstants.NewsDetailUrl.toString() + mNews_Id);
        HttpUtil.getInstance().getJsonString(UrlConstants.NewsDetailUrl + mNews_Id, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                ContentBean contentBean = GsonUtil.parseJsonToBean(result, ContentBean.class);
                List<String> images = contentBean.getImages();
                String imageUrl = contentBean.getImage();
                if (images != null && !images.isEmpty()) {
                    mRlTop.setVisibility(View.VISIBLE);
                    mTvTitleTop.setText(contentBean.getTitle());
                    mTvContentTop.setText(contentBean.getImage_source());
                    ImageUtil.getInstance().displayImage(imageUrl, mIvTop);
                } else {
                    mRlTop.setVisibility(View.GONE);
                }

                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news\" " +
                        "type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + contentBean.getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                mWebview.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
            }
        });


    }

    /**
     * 该方法在获取完数据后调用，更新点赞评论的数量
     */
    private void upateToolBar() {
        LogUtil.e(UrlConstants.NewExtraUrl + mNews_Id);
        HttpUtil.getInstance().getJsonString(UrlConstants.NewExtraUrl + mNews_Id, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
//                LogUtil.e(result);
                NewsExtra newsExtra = GsonUtil.parseJsonToBean(result, NewsExtra.class);
                mTvCommentNum.setText(String.valueOf(newsExtra.getComments()));
                mTvThumbNum.setText(String.valueOf(newsExtra.getPopularity()));
            }
        });

    }

    private void setWebView() {

        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebview.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebview.getSettings().setAppCacheEnabled(true);

        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setSupportZoom(true);

    }
}
