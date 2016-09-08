package com.zheteng.wsj.myzhihurb.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.fragment.NavFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final String NAV_FRAGMENT = "nav_fragment";
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

        toolbar.setTitle("知乎日报");
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

}
