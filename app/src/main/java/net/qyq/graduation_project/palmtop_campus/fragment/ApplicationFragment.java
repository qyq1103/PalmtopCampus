package net.qyq.graduation_project.palmtop_campus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.qyq.graduation_project.common.app.Fragment;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.activity.CampusAdvisoryActivity;
import net.qyq.graduation_project.palmtop_campus.activity.MainActivity;
import net.qyq.graduation_project.palmtop_campus.activity.PersonalActivity;
import net.qyq.graduation_project.palmtop_campus.activity.ScoreQueryActivity;
import net.qyq.graduation_project.palmtop_campus.navigation_map.MapActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/14 0014 23:05
 * Des:功能
 */
public class ApplicationFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.gr_application)
    GridView application;

    private String[] appName;
    private int[] icon;
    private List<Map<String, Object>> data;
    SimpleAdapter mAdapter;
    //身份验证
    private String identity;

    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_application;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        identity = ((MainActivity) activity).getUserName();
    }

    @Override
    protected void initData() {
        super.initData();
        if (identity.length() == 12) {//学生登录时显示的功能
            icon = new int[]{R.drawable.ic_score_query,
                    R.drawable.ic_campus_advisory, R.drawable.ic_go_out,
                    R.drawable.ic_personal, R.drawable.ic_expect};
            appName = new String[]{"成绩查询", "校园咨询", "出行导航", "个人中心", "更多"};
        } else if (identity.length() == 6) {//老师登录时显示的功能
            icon = new int[]{R.drawable.ic_campus_advisory, R.drawable.ic_go_out,
                    R.drawable.ic_personal, R.drawable.ic_expect};
            appName = new String[]{"校园咨询", "出行导航", "个人中心", "更多"};
        } else {//未登录时显示的功能
            icon = new int[]{R.drawable.ic_go_out, R.drawable.ic_personal,
                    R.drawable.ic_expect};
            appName = new String[]{"出行导航", "个人中心", "更多"};
            //            appName = new String[]{"成绩查询", "课表", "校园咨询", "出行导航", "个人中心", "更多"};
        }
        data = new ArrayList<>();
        mAdapter = new SimpleAdapter(getContext(), getData(),
                R.layout.item_application, new String[]{"icon", "appName"},
                new int[]{R.id.iV_application, R.id.application_name});
        application.setAdapter(mAdapter);
        application.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getData() {
        data.clear();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", icon[i]);
            map.put("appName", appName[i]);
            data.add(map);
        }
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (icon[position]) {
            case R.drawable.ic_score_query:
                Intent scoreIntent = new Intent(getContext(), ScoreQueryActivity.class);
                scoreIntent.putExtra("username", identity);
                startActivity(scoreIntent);
                break;
            case R.drawable.ic_campus_advisory:
                startActivity(new Intent(getContext(), CampusAdvisoryActivity.class));
                break;
            case R.drawable.ic_go_out:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.drawable.ic_personal:
                Intent intent = new Intent(getContext(), PersonalActivity.class);
                if (identity.length() != 0) {
                    intent.putExtra("isLogin", true);
                } else {
                    intent.putExtra("isLogin", false);
                }
                startActivity(intent);
                break;
            case R.drawable.ic_expect:
                Toast.makeText(getContext(), "等等吧，努力增加更多功能中", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
