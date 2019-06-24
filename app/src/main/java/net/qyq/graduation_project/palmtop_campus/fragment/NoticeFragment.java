package net.qyq.graduation_project.palmtop_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.qyq.graduation_project.common.app.Fragment;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.activity.DetailedActivity;
import net.qyq.graduation_project.palmtop_campus.adapter.NoticeAdapter;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/15 0015 20:20
 * Des: 通知
 */
public class NoticeFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.notice_list)
    ListView notices;

    private ArrayList<String> titles = new ArrayList<>();//通知标题
    private ArrayList<String> descs = new ArrayList<>();//通知内容
    private ArrayList<String> times = new ArrayList<>();//通知时间
    private ArrayList<String> departments = new ArrayList<>();//发布部门


    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initWidget(View root, Bundle savedInstanceState) {
        super.initWidget(root, savedInstanceState);
        notices.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        RequestParams params = new RequestParams(Constant.NOTICES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    titles.clear();
                    descs.clear();
                    departments.clear();
                    times.clear();
                    if (code == Constant.CODE_SUCCESSFUL) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = array.length() - 1; i >= 0; i--) {
                            JSONObject object = array.getJSONObject(i);
                            titles.add(object.getString("title"));
                            descs.add(object.getString("notice_body"));
                            departments.add(object.getString("department"));
                            times.add(object.getString("release_time"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NoticeAdapter adapter = new NoticeAdapter(getContext(), titles, descs, times);
                notices.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), DetailedActivity.class);
        intent.putExtra("code", 1);
        intent.putExtra("title", titles.get(position));
        intent.putExtra("content", descs.get(position));
        intent.putExtra("department", departments.get(position));
        intent.putExtra("time", times.get(position));
        startActivity(intent);
    }
}
