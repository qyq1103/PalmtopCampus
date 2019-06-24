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
 * Date: 2019/5/9 0009 16:06
 * Des:
 */
public class AdvisoryAdapter extends BaseAdapter {
    private ArrayList<String> aparments;
    private ArrayList<String> calls;
    private Context mContext;
    private LayoutInflater mInflater;

    public AdvisoryAdapter(Context context, ArrayList<String> aparments, ArrayList<String> calls) {
        mContext = context;
        this.aparments = aparments;
        this.calls = calls;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aparments.size();
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
            convertView = mInflater.inflate(R.layout.item_advisory, parent, false);
            holder.apartment = convertView.findViewById(R.id.department);
            holder.call = convertView.findViewById(R.id.department_call);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.apartment.setText(aparments.get(position));
        holder.call.setText(calls.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView apartment;
        TextView call;
    }
}
