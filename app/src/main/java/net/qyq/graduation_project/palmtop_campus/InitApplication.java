package net.qyq.graduation_project.palmtop_campus;

import android.support.multidex.MultiDexApplication;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

import org.xutils.x;

/**
 * Author：钱
 * Date: 2019/3/14 0014 21:27
 * Des:
 */
public class InitApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        //初始化easeUI
        EaseUI.getInstance().init(getApplicationContext(), null);
        EMClient.getInstance().setDebugMode(true);
    }
}
