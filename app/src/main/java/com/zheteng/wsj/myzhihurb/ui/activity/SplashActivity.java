package com.zheteng.wsj.myzhihurb.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.SplashBean;
import com.zheteng.wsj.myzhihurb.global.Constants;
import com.zheteng.wsj.myzhihurb.listener.SimpleAnimatorListener;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.ImageUtil;
import com.zheteng.wsj.myzhihurb.net.NetConnectUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForBytes;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.net.StartSaveUtil;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;

import butterknife.InjectView;
import okhttp3.Call;

/***
 *
 */
public class SplashActivity extends BaseActivity {

    @InjectView(R.id.img_splash)
    ImageView mImgSplash;
    @InjectView(R.id.tv_splash)
    TextView mTvSplash;
    @InjectView(R.id.ll_splash)
    LinearLayout mLlSplash;
    private ObjectAnimator mAnimator;
    private static Handler mHandler;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected Activity setActivity() {
        return this;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mLlSplash.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
    }


    @Override
    public void onClick(View v) {

    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLlSplash.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                mLlSplash.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            mLlSplash.setVisibility(View.VISIBLE);
            startAnimation();
        }
    }

    private void startAnimation() {
        mAnimator = ObjectAnimator.ofFloat(mLlSplash, "translationY", mLlSplash.getHeight(), 0);
        mAnimator.setDuration(1000);

        mAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loadSplashImage();
            }
        });
        mAnimator.start();
    }

    /**
     * 加载启动页面的图片数据
     */
    private void loadSplashImage() {
        String imageUrl = StartSaveUtil.getInstance().getImageDispalyFile();
        String des = StartSaveUtil.getInstance().getStartDes();

        // 如果存在本地图片则加载本地图片，第一次肯定不存在 所以不会走这里
        if (imageUrl != null && !TextUtils.isEmpty(des)) {
            //LogUtil.e(imageUrl);
            ImageUtil.getInstance().displayImageDefault(imageUrl, mImgSplash);
            mTvSplash.setText(des);
            startMainActivity();
        } else {
            mImgSplash.setImageResource(R.drawable.start);
            mTvSplash.setText("");
        }
        //如果网络可用 将最新的数据保存在
        if (NetConnectUtil.isConnect()) {
            loadSplashData();
        }
    }


    /**
     * 加载启动页数据
     */
    private void loadSplashData() {
        HttpUtil.getInstance().getJsonString(Constants.SplashUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                final SplashBean splashBean = GsonUtil.parseJsonToBean(result, SplashBean.class);
                HttpUtil.getInstance().getResponseBytes(splashBean.getImg(), new OkHttpCallBackForBytes() {
                    @Override
                    public void onFailure(Call call, Exception e) {
                        LogUtil.e("获取图片数据失败");
                    }

                    @Override
                    public void onSuccess(Call call, byte[] bytes) {
                        //保存图片 子线程
                        StartSaveUtil.getInstance().saveStartData(splashBean.getText(), bytes, SplashActivity.this);
                    }
                });
            }

        },false);
    }

    /**
     * 启动主页面 3s开启
     */
    private void startMainActivity() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 启动界面禁用返回键
     */
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}
