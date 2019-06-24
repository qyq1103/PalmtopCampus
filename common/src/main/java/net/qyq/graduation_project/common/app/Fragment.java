package net.qyq.graduation_project.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author：钱
 * Date: 2019/3/14 0014 20:24
 * Des:基类Fragment，封装部分方法
 */
public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View rootView;
    protected Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            int layoutId = getContextLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layoutId, container, false);
            initWidget(root, savedInstanceState);
            rootView = root;
        } else {
            if (rootView.getParent() != null) {
                //把当前 Root从其父控件中移除
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当视图初始化完成后初始化数据
        initData();
    }

    /**
     * 初始化参数
     *
     * @param arguments Bundle
     */
    protected void initArgs(Bundle arguments) {
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContextLayoutId();

    /**
     * 初始化控件
     *
     * @param root               View
     * @param savedInstanceState Bundle
     */
    protected void initWidget(View root, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, root);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 返回按键触发是调用
     *
     * @return 返回true代表我已经处理返回逻辑，Activity不用自己finish。
     * 返回false代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return true;
    }
}
