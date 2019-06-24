package net.qyq.graduation_project.palmtop_campus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.qyq.graduation_project.palmtop_campus.R;

import java.util.ArrayList;

/**
 * Author：钱
 * Date: 2019/4/18 0018 21:27
 * Des:通知适配器
 */
public class NoticeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> titles;
    private ArrayList<String> descs;
    private ArrayList<String> times;

    public NoticeAdapter(Context context, ArrayList<String> titles, ArrayList<String> descs, ArrayList<String> times) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.titles = titles;
        this.descs = descs;
        this.times = times;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_notice, parent, false);
            holder.title = convertView.findViewById(R.id.notice_title);
            holder.desc = convertView.findViewById(R.id.notice_desc);
            holder.time = convertView.findViewById(R.id.notice_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(titles.get(position));
        holder.desc.setText(descs.get(position));
        holder.time.setText(times.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView desc;
        TextView time;
    }
}
