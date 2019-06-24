package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.fragment.ApplicationFragment;
import net.qyq.graduation_project.palmtop_campus.fragment.CampusStyleFragment;
import net.qyq.graduation_project.palmtop_campus.fragment.FriendsFragment;
import net.qyq.graduation_project.palmtop_campus.fragment.NewsFragment;
import net.qyq.graduation_project.palmtop_campus.helper.NavHelper;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/14 0014 21:06
 * Des:主要页面，显示菜单按钮，承载各个功能的Fragment
 */
public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {
    @BindView(R.id.appbar)
    View mLayAppBar;
    @BindView(R.id.txt_title)
    TextView mMainTitle;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private NavHelper<Integer> mNavHelper;
    private String userName;
    protected static MainActivity mainActivity;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        mainActivity = this;

        Intent intent = getIntent();
        String if_login = intent.getStringExtra("if_login");
        //通过用户名判断是学生还是老师或是未登录
        userName = intent.getStringExtra("userName");
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        if (if_login.equals("login")) {
            mNavigation.inflateMenu(R.menu.login_navigation_items);
            mNavHelper.add(R.id.news, new NavHelper.Tab<>(NewsFragment.class, R.string.news))
                    .add(R.id.friends, new NavHelper.Tab<>(FriendsFragment.class, R.string.friends))
                    .add(R.id.campus_style, new NavHelper.Tab<>(CampusStyleFragment.class, R.string.campus_style))
                    .add(R.id.application, new NavHelper.Tab<>(ApplicationFragment.class, R.string.application));
        } else {
            mNavigation.inflateMenu(R.menu.no_login_navigation_items);
            mNavHelper.add(R.id.campus_style, new NavHelper.Tab<>(CampusStyleFragment.class, R.string.campus_style))
                    .add(R.id.application, new NavHelper.Tab<>(ApplicationFragment.class, R.string.application));
        }
        //添加对底部按钮点击的监听
        mNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //从底部导航中接管我们的Menu，然后进行手动的触发第一次点击
        Menu menu = mNavigation.getMenu();
        //触发首次选中Home
        if (userName.length() == 0) {
            menu.performIdentifierAction(R.id.campus_style, 0);
        } else {
            menu.performIdentifierAction(R.id.news, 0);
        }
    }

    /**
     * 当我们的底部导航被点击的时候触发
     *
     * @param menuItem menuItem
     * @return True代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //转接事件流到工具类中
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

    /**
     * NAVHelper处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出我们的Title资源Id
        mMainTitle.setText(newTab.extra);
    }

    public String getUserName() {
        return userName;
    }
}
