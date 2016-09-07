package com.zheteng.wsj.myzhihurb.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.bean.SplashBean;
import com.zheteng.wsj.myzhihurb.listener.SimpleAnimatorListener;
import com.zheteng.wsj.myzhihurb.net.HttpUtil;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForBytes;
import com.zheteng.wsj.myzhihurb.net.OkHttpCallBackForString;
import com.zheteng.wsj.myzhihurb.net.UrlConstants;
import com.zheteng.wsj.myzhihurb.util.GsonUtil;
import com.zheteng.wsj.myzhihurb.util.ImageUtil;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.StartSaveUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/***
 *
 */
public class SplashActivity extends AppCompatActivity {

    @InjectView(R.id.img_splash)
    ImageView imgSplash;
    @InjectView(R.id.ll_splash)
    LinearLayout llSplash;
    @InjectView(R.id.tv_splash)
    TextView tvSplash;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);


        llSplash.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llSplash.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    llSplash.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                llSplash.setVisibility(View.VISIBLE);
                startAnimation();
            }
        });

    }

    private void startAnimation() {

        animator = ObjectAnimator.ofFloat(llSplash, "translationY", llSplash.getHeight(), 0);
        animator.setDuration(1000);

        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                initView();
            }
        });

        animator.start();
    }

    private void initView() {

        String imageUrl = StartSaveUtil.getInstance().getImageDispalyFile();
        String des = StartSaveUtil.getInstance().getStartDes();

        // 如果存在本地图片则加载本地图片，第一次肯定不存在 所以不会走这里
        if (imageUrl != null ){
            ImageUtil.getInstance().displayImageDefault(imageUrl,imgSplash);
        }else {
            imgSplash.setImageResource(R.mipmap.start);
        }

        if (!TextUtils.isEmpty(des)){
            tvSplash.setText(des);
        }else{
            tvSplash.setText("'");
        }


        HttpUtil.getInstance().getJsonString(UrlConstants.SplashUrl, new OkHttpCallBackForString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, String result) {
                llSplash.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(result)) {

                    final SplashBean splashBean = GsonUtil.parseJsonToBean(result, SplashBean.class);

                    HttpUtil.getInstance().getResponseBytes(splashBean.getImg(), new OkHttpCallBackForBytes() {
                        @Override
                        public void onFailure(Call call, Exception e) {
                            LogUtil.e("获取图片数据失败");
                        }

                        @Override
                        public void onSuccess(Call call, byte[] bytes) {
                            //保存图片 子线程
                            StartSaveUtil.getInstance().saveStartData(splashBean.getText(), bytes,SplashActivity.this);
                        }
                    });

                }
            }
        });

        startMainActivity();
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },1000);
    }
}
