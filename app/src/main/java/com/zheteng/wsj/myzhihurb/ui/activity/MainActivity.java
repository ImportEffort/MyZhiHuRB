package com.zheteng.wsj.myzhihurb.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.fragment.HomeFragment;
import com.zheteng.wsj.myzhihurb.fragment.NavFragment;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    private static final String NAV_FRAGMENT = "nav_fragment";
    private static final String HOME_FRGMENT = "home_frgment";//ctrl shif U
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.fl_content)
    FrameLayout mFlContent;
    @InjectView(R.id.navigation_fragment)
    FrameLayout mNavigationFragment;
    @InjectView(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected Activity setActivity() {
        return this;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initListener() {
        
    }

    @Override
    public void initData() {
        intNavFragment();
    }


   

    @Override
    public void setToolBar() {
        super.setToolBar();

        mToolbar.setTitle("首页");
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, 0, 0);

        mDrawerToggle.syncState();
        mDrawerlayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(mToolbar);
    }

    @Override
    public void onClick(View v) {

    }

    private void intNavFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NavFragment fragment = new NavFragment();

        fragmentTransaction.replace(R.id.navigation_fragment, fragment, NAV_FRAGMENT);
        fragmentTransaction.replace(R.id.fl_content,new HomeFragment(),HOME_FRGMENT);
        fragmentTransaction.commit();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                return true;
            case R.id.toolbar_logIn:
                startActivity(new Intent(this,SplashLogActivity.class));
                return true;
            case R.id.toolbar_dark_theme:
                ToastUtil.show("dark_theme");
                return true;
            case R.id.toolbar_setting:
                ToastUtil.show("setting");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * 关闭侧边栏
     */
    public void closeMenu(){
        mDrawerlayout.closeDrawer(Gravity.LEFT);
    }

    public void setToolBarTitle(String name) {

        mToolbar.setTitle(name);
    }
}
