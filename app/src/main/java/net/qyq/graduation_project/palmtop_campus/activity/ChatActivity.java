package net.qyq.graduation_project.palmtop_campus.activity;

import com.hyphenate.easeui.ui.EaseChatFragment;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;

/**
 * Author：钱
 * Date: 2019/3/19 0019 20:37
 * Des:聊天
 */
public class ChatActivity extends Activity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initData() {
        super.initData();
        //EaseUI封装好的聊天界面
        EaseChatFragment easeChatFragment = new EaseChatFragment();
        //将参数传给聊天界面
        easeChatFragment.setArguments(getIntent().getExtras());
        //加载EaseUI封装的的聊天界面Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.chat_container, easeChatFragment).commit();
    }
}
