package net.qyq.graduation_project.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Author：钱
 * Date: 2019/3/14 0014 20:23
 * Des:基类Activity，封装部分方法
 */
public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            //得到界面id并设置到Activity中
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initWidget(savedInstanceState);
            initData();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化控件
     *
     * @param savedInstanceState Bundle
     */

    protected void initWidget(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    /**
     * 得到当前界面的资源id
     *
     * @return 资源Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true，错误返回false
     */
    private boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 弹窗等的显示
     */
    protected void showPlay() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                //判断是否为我们能够处理的Fragment类型
                if (fragment instanceof net.qyq.graduation_project.common.app.Fragment) {
                    //判断是否拦截了返回按钮
                    if (((net.qyq.graduation_project.common.app.Fragment) fragment).onBackPressed()) {
                        //如果有直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }
}
