package net.qyq.graduation_project.palmtop_campus.navigation_map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviPath;

import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.navigation_map.AMapUtil;

import java.util.List;

/**
 * Author：钱
 * Date: 2019/4/3 0003 9:43
 * Des:家车及步行的线路选择适配
 */
public class RouteLineAdapter extends BaseAdapter {
    private int selectedPosition;
    private Context mContext;
    private List<AMapNaviPath> mPathList;
    private LayoutInflater layoutInflater;

    public RouteLineAdapter(Context context, List<AMapNaviPath> pathList) {
        mContext = context;
        mPathList = pathList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.item_route_line, null);
            holder.routeTitle = convertView.findViewById(R.id.route_line_title);
            holder.routeLength = convertView.findViewById(R.id.route_line_length);
            holder.routeTime = convertView.findViewById(R.id.route_line_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (mPathList.size() > 1) {
            holder.routeTitle.setText(mPathList.get(position).getLabels());
        } else {
            holder.routeTitle.setText("仅一个方案");
        }
        holder.routeLength.setText(AMapUtil.getFriendlyLength(mPathList.get(position).getAllLength()));
        holder.routeTime.setText(AMapUtil.getFriendlyTime(mPathList.get(position).getAllTime()));
        if (selectedPosition == position) {
            holder.routeTitle.setTextColor(mContext.getResources().getColor(R.color.background_blue));
            holder.routeLength.setTextColor(mContext.getResources().getColor(R.color.background_blue));
            holder.routeTime.setTextColor(mContext.getResources().getColor(R.color.background_blue));
        } else {
            holder.routeTitle.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.routeLength.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.routeTime.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return convertView;
    }

    class Holder {
        TextView routeTitle;
        TextView routeLength;
        TextView routeTime;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
}
