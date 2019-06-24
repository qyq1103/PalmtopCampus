package net.qyq.graduation_project.palmtop_campus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.qyq.graduation_project.palmtop_campus.R;

import java.util.ArrayList;

/**
 * Author：钱
 * Date: 2019/5/8 0008 22:07
 * Des:
 */
public class NewsListAdapter extends BaseAdapter {
    private ArrayList<String> titles;//标题
    private ArrayList<String> descs;//内容
    private ArrayList<Bitmap> images;//图片
    private ArrayList<String> updateAt;//新闻发布的时间

    private Context mContext;
    private LayoutInflater mInflater;

    public NewsListAdapter(Context context, ArrayList<String> titles,
                           ArrayList<String> descs, ArrayList<Bitmap> images, ArrayList<String> updateAt) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.titles = titles;
        this.descs = descs;
        this.images = images;
        this.updateAt = updateAt;
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
            convertView = mInflater.inflate(R.layout.item_campus_style, parent, false);
            holder.title = convertView.findViewById(R.id.title);
            holder.desc = convertView.findViewById(R.id.news_desc);
            holder.image = convertView.findViewById(R.id.news_img);
            holder.update = convertView.findViewById(R.id.campus_style_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(titles.get(position));
        holder.desc.setText(descs.get(position));
        holder.update.setText(updateAt.get(position));
        if (images.size() > 0 && images.get(position) != null) {
            holder.image.setImageBitmap(images.get(position));
        } else {
            holder.image.setImageResource(R.drawable.ic_default);
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView desc;
        TextView update;
        ImageView image;
    }
}
