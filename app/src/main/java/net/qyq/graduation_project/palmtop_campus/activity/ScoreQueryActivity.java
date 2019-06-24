package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.adapter.ScoreAdapter;
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
 * Date: 2019/4/19 0019 8:51
 * Des:成绩查询
 */
public class ScoreQueryActivity extends Activity {
    @BindView(R.id.txt_title)
    TextView title;
    @BindView(R.id.score_list)
    ListView scoreList;


    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<String> scores = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_score_query;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        title.setText("成绩查询");

    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        RequestParams params = new RequestParams(Constant.SEARCH_SCORE_URL);
        params.addQueryStringParameter("username", username);
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
                            courses.add(object.getString("course_name"));
                            scores.add(object.getString("score"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ScoreAdapter adapter = new ScoreAdapter(getApplicationContext(), courses, scores);
                scoreList.setAdapter(adapter);
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
}
