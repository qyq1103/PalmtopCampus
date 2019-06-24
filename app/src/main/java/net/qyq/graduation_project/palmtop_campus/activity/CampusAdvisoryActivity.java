package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.adapter.AdvisoryAdapter;
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
 * Date: 2019/4/18 0018 22:30
 * Des:校园咨询
 */
public class CampusAdvisoryActivity extends Activity implements AdapterView.OnItemClickListener {
    @BindView(R.id.txt_title)
    TextView title;
    @BindView(R.id.advisory_list)
    ListView advisory;

    private ArrayList<String> apartments = new ArrayList<>();
    private ArrayList<String> calls = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_campus_advisory;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        title.setText("校园咨询");
        advisory.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        RequestParams params = new RequestParams(Constant.ADVISORY_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == Constant.CODE_SUCCESSFUL) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            apartments.add(object.getString("apartment"));
                            calls.add(object.getString("call"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdvisoryAdapter adapter = new AdvisoryAdapter(getApplicationContext(), apartments, calls);
                advisory.setAdapter(adapter);
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
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + calls.get(position)));
        startActivity(intent);
    }
}
