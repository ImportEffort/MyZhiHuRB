package com.zheteng.wsj.myzhihurb.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.fragment.HomeFragment;
import com.zheteng.wsj.myzhihurb.fragment.NavFragment;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final String NAV_FRAGMENT = "nav_fragment";
    private static final String HOME_FRGMENT = "home_frgment";//ctrl shif U
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fl_content)
    FrameLayout flContent;
    @InjectView(R.id.navigation_fragment)
    FrameLayout navigationFragment;
    @InjectView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setToolBar();

        initNavFragment();

    }

    private void setToolBar() {

        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(getResources().getColor(R.color.write));
        drawerToggle = new ActionBarDrawerToggle(this,drawerlayout,toolbar,0,0);

        drawerToggle.syncState();
        drawerlayout.addDrawerListener(drawerToggle);

        setSupportActionBar(toolbar);
    }

    private void initNavFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NavFragment fragment = new NavFragment();
        fragmentTransaction.replace(R.id.navigation_fragment, fragment, NAV_FRAGMENT);
        fragmentTransaction.commit();

        FragmentTransaction mainFragmentTransaction = fragmentManager.beginTransaction();
        mainFragmentTransaction.replace(R.id.fl_content,new HomeFragment(),HOME_FRGMENT);
        mainFragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerToggle.onOptionsItemSelected(item);
                return true;
            case R.id.toolbar_logIn:
                ToastUtil.show("login");
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
        drawerlayout.closeDrawer(Gravity.LEFT);
    }

    public void setToolBarTitle(String name) {

        toolbar.setTitle(name);
    }
}
