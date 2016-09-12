package com.zheteng.wsj.myzhihurb.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.zheteng.wsj.myzhihurb.ui.fragment.HomeFragment;
import com.zheteng.wsj.myzhihurb.ui.fragment.NavFragment;
import com.zheteng.wsj.myzhihurb.ui.fragment.NavFragment.OnSildingItemClickListener;
import com.zheteng.wsj.myzhihurb.ui.fragment.NewsFragment;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements OnSildingItemClickListener {

    private static final String NAV_FRAGMENT = "nav_fragment";
    private static final String HOME_FRGMENT = "home_frgment";//ctrl shif U
    private NavFragment meunFragment;
    private HomeFragment homeFragment;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.fl_content)
    FrameLayout mFlContent;
    @InjectView(R.id.navigation_fragment)
    FrameLayout mNavigationFragment;
    @InjectView(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String[] FRAGMENTNAME = new String[]{
            "日常心理学", "用户推荐日报", "电影日报", "不许无聊",
            "设计日报", "大公司日报", "财经日报", "互联网安全",
            "开始游戏", "音乐日报", "动漫日报", "体育日报"};
    private Fragment iFragment;
    private Fragment isFragment;
    private NewsFragment mFragment1;
    private NewsFragment mFragment2;
    private NewsFragment mFragment3;
    private NewsFragment mFragment4;
    private NewsFragment mFragment5;
    private NewsFragment mFragment6;
    private NewsFragment mFragment7;
    private NewsFragment mFragment8;
    private NewsFragment mFragment9;
    private NewsFragment mFragment10;
    private NewsFragment mFragment11;
    private NewsFragment mFragment12;
    private long firstTime;

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
    public void initData(Bundle savedInstanceState) {
        initFragment(savedInstanceState);
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

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (meunFragment == null) {
                meunFragment = new NavFragment();
            }
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                isFragment = homeFragment;
            }
            fragmentTransaction.replace(R.id.navigation_fragment, meunFragment, NAV_FRAGMENT);
            fragmentTransaction.replace(R.id.fl_content, homeFragment, HOME_FRGMENT);
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                return true;
            case R.id.toolbar_logIn:
                startActivity(new Intent(this, SplashLogActivity.class));
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSildingItemClick(int id, String name) {
        /*LogUtil.e(name);
        LogUtil.e(id + "");*/

        mDrawerlayout.closeDrawer(Gravity.LEFT);
        mToolbar.setTitle(name);

        switch (name) {

            case "首页":
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                switchContent(isFragment,homeFragment);
                break;
            case "日常心理学":
                if (mFragment1 == null) {
                    mFragment1 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment1);
                break;
            case "用户推荐日报":
                if (mFragment2 == null) {
                    mFragment2 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment2);
                break;
            case "电影日报":
                if (mFragment3 == null) {
                    mFragment3 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment3);
                break;
            case "不许无聊":
                if (mFragment4 == null) {
                    mFragment4 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment4);
                break;
            case "设计日报":
                if (mFragment5 == null) {
                    mFragment5 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment5);
                break;
            case "大公司日报":
                if (mFragment6 == null) {
                    mFragment6 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment6);
                break;
            case "财经日报":
                if (mFragment7== null) {
                    mFragment7 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment7);
                break;
            case "互联网安全":
                if (mFragment8 == null) {
                    mFragment8 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment8);
                break;
            case "开始游戏":
                if (mFragment9 == null) {
                    mFragment9 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment9);
                break;
            case "音乐日报":
                if (mFragment10 == null) {
                    mFragment10 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment10);
                break;
            case "动漫日报":
                if (mFragment11 == null) {
                    mFragment11 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment11);
                break;
            case "体育日报":
                if (mFragment12 == null) {
                    mFragment12 = new NewsFragment(id);
                }
                switchContent(isFragment,mFragment12);
                break;

        }


        //根据穿过来的id生成fragment 并替换原来的fragment

    }

    /**
     * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {

        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.fl_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerlayout.closeDrawer(Gravity.LEFT);
        } else {
            long secondTime = System.currentTimeMillis();

            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(mFlContent, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }

    }
}
