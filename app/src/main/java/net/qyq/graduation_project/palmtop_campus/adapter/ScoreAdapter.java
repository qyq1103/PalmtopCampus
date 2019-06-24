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
 * Date: 2019/4/19 0019 10:21
 * Des:
 */
public class ScoreAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    private ArrayList<String> courses;
    private ArrayList<String> scores;

    public ScoreAdapter(Context context, ArrayList<String> courses, ArrayList<String> scores) {
        mContext = context;
        this.courses = courses;
        this.scores = scores;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return courses.size();
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_score_query, null);
            TextView courseName = convertView.findViewById(R.id.course_name);
            TextView score = convertView.findViewById(R.id.tv_score);
            courseName.setText(courses.get(position));
            score.setText(scores.get(position));
        }
        return convertView;
    }
}
