package net.qyq.graduation_project.palmtop_campus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.helper.FriendsParentInfo;

import java.util.List;

/**
 * Author：钱
 * Date: 2019/4/18 0018 13:31
 * Des:好友列表适配器
 */
public class FriendsListAdapter extends BaseExpandableListAdapter {
    private List<FriendsParentInfo> mParentInfos;
    private Context mContext;
    private LayoutInflater mInflater;

    public FriendsListAdapter(List<FriendsParentInfo> parentInfos, Context context) {
        mParentInfos = parentInfos;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //分组数
    @Override
    public int getGroupCount() {
        return mParentInfos.size();
    }

    //组子数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mParentInfos.get(groupPosition).getInfo().size();
    }

    //组的对象
    @Override
    public Object getGroup(int groupPosition) {
        return mParentInfos.get(groupPosition);
    }

    //子的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mParentInfos.get(groupPosition).getInfo().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //当子条目ID相同时是否复用
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //组展开列表
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_friends_parent, null);
            TextView groupName = convertView.findViewById(R.id.friend_group_name);
            groupName.setText(mParentInfos.get(groupPosition).getTitle());
        }
        return convertView;
    }

    //子条目内容
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_friends_child, null);
            holder.friendPortrait = convertView.findViewById(R.id.im_portrait);
            holder.remark = convertView.findViewById(R.id.remark);
            holder.sign = convertView.findViewById(R.id.user_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.remark.setText(mParentInfos.get(groupPosition).getInfo().get(childPosition).getRemark());
        holder.sign.setText(mParentInfos.get(groupPosition).getInfo().get(childPosition).getUserID());
        return convertView;
    }

    //子条目是否可以被点击/选中/选择
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        ImageView friendPortrait;
        TextView remark;
        TextView sign;
    }
}
