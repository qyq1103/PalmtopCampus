package net.qyq.graduation_project.palmtop_campus.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import net.qyq.graduation_project.common.app.Fragment;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.adapter.NavigationViewPagerAdapter;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/15 0015 10:12
 * Des:新的消息
 */
public class NewsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.news_navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.vp_news)
    ViewPager mViewPager;

    private NavigationViewPagerAdapter mAdapter;

    @Override
    protected void initWidget(View root, Bundle savedInstanceState) {
        super.initWidget(root, savedInstanceState);
        mAdapter = new NavigationViewPagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(new ConversationListFragment());
        mAdapter.addFragment(new NoticeFragment());
        mViewPager.setAdapter(mAdapter);
        //NavigationView点击监听
        mNavigation.setOnNavigationItemSelectedListener(this);
        //ViewPager滑动监听
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.information:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.notice:
                mViewPager.setCurrentItem(1);
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        //当 ViewPager 滑动后设置BottomNavigationView 选中相应选项
        mNavigation.getMenu().getItem(i).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
