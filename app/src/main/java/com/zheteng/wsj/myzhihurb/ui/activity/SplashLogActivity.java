package com.zheteng.wsj.myzhihurb.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SplashLogActivity extends AppCompatActivity {


    @InjectView(R.id.toolbar_splash_login)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_log);
        ButterKnife.inject(this);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("登陆");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 返回按键监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.btn_splash_sina, R.id.btn_splash_tencent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_splash_sina:
                ToastUtil.show("小本生意没有服务器，暂时无法登陆");
                break;
            case R.id.btn_splash_tencent:
                ToastUtil.show("小本生意没有服务器，暂时无法登陆");
                break;
        }
    }
}
