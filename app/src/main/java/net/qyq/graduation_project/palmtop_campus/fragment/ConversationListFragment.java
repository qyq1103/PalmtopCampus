package net.qyq.graduation_project.palmtop_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.activity.ChatActivity;

/**
 * Author：钱
 * Date: 2019/3/15 0015 20:19
 * Des:聊天会话列表
 */
public class ConversationListFragment extends EaseConversationListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation_list, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener((parent, view, position, id) -> {
            EMConversation conversation = conversationListView.getItem(position);
            startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
        });
    }
}
