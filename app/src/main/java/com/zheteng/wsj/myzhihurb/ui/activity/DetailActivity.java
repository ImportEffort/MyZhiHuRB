package com.zheteng.wsj.myzhihurb.ui.activity;

import android.app.Activity;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
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
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 新新闻详情界面
 * Created by wsj20 on 2016/9/10.
 */
public class DetailActivity extends BaseActivity {


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
    protected Activity setActivity() {
        return this;
    }


    @Override
    public int getLayoutResID() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        super.initView();
        setWebView();
    }

    /**
     * 初始化webView属性
     */
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

    @Override
    public void initListener() {
        // 返回按键监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                SystemClock.currentThreadTimeMillis();
                System.currentTimeMillis();
            }
        });
        mIvThumb.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mNews_Id = getIntent().getStringExtra(Constants.NEWS_ID);
        loadData();
        upateToolBar();
    }


    @Override
    public void setToolBar() {
        super.setToolBar();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.write));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));//设置返回键的图标
    }

    /**
     * 获取新闻详情页内容
     */
    private void loadData() {
       // LogUtil.e(Constants.NewsDetailUrl.toString() + mNews_Id);
        HttpUtil.getInstance().getJsonString(Constants.NewsDetailUrl + mNews_Id, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                ContentBean contentBean = GsonUtil.parseJsonToBean(result, ContentBean.class);
                List<String> images = contentBean.getImages();
                String imageUrl = contentBean.getImage();
                initViewData(contentBean, images, imageUrl);
            }
        },false);


    }

    /**
     * 设置详情界面数据
     *
     * @param contentBean 新闻内容封装bean
     * @param images 详情的image数据，注意从这里获取的图片为压缩后的图片
     * @param imageUrl  详情的image数据 高清图片
     */
    private void initViewData(ContentBean contentBean, List<String> images, String imageUrl) {
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

    /**
     * 该方法在获取完数据后调用，更新点赞评论的数量
     */
    private void upateToolBar() {
        LogUtil.e(Constants.NewExtraUrl + mNews_Id);
        HttpUtil.getInstance().getJsonString(Constants.NewExtraUrl + mNews_Id, new OkHttpCallBackForString() {
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
        },false);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.iv_thumb):
                checkThumb();
                break;
        }
    }

    private void checkThumb() {
        if (!isThumb) {
            mIvThumb.setImageResource(R.drawable.thumb_red);
            ToastUtil.show("点赞成功");
            mTvCommentNum.setText((Integer.parseInt(mTvCommentNum.getText().toString()) + 1) + "");
            isThumb = true;
        } else {
            mIvThumb.setImageResource(R.drawable.thumb);
            ToastUtil.show("取消点赞");
            mTvCommentNum.setText((Integer.parseInt(mTvCommentNum.getText().toString()) - 1) + "");
            isThumb = false;
        }
    }
}
