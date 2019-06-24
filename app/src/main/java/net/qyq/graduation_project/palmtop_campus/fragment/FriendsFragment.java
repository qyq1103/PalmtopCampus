package net.qyq.graduation_project.palmtop_campus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;

import net.qyq.graduation_project.common.app.Fragment;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.activity.ChatActivity;
import net.qyq.graduation_project.palmtop_campus.activity.MainActivity;
import net.qyq.graduation_project.palmtop_campus.adapter.FriendsListAdapter;
import net.qyq.graduation_project.palmtop_campus.helper.FriendsChildInfo;
import net.qyq.graduation_project.palmtop_campus.helper.FriendsParentInfo;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/15 0015 10:15
 * Des:好友列表
 */
public class FriendsFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    @BindView(R.id.friends_list)
    ExpandableListView friendsList;

    private List<FriendsParentInfo> mList = new ArrayList<>();
    /**
     * 完善时，从数据库中获取数据
     */
    public String[] groupStrings = {"老师", "学生"};
    private String username;
    private String[][] mChildName;
    private String[][] mChildId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        username = ((MainActivity) activity).getUserName();
    }

    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initWidget(View root, Bundle savedInstanceState) {
        super.initWidget(root, savedInstanceState);
        initList();
        FriendsListAdapter adapter = new FriendsListAdapter(mList, getContext());
        friendsList.setAdapter(adapter);
        friendsList.setOnChildClickListener(this);
    }

    private void initList() {
        RequestParams params = new RequestParams(Constant.S_T_FRIENDS_URL);
        params.addQueryStringParameter("username", username);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == Constant.CODE_SUCCESSFUL) {
                        JSONArray teachers = jsonObject.getJSONArray("teachers");
                        JSONArray students = jsonObject.getJSONArray("students");
                        String[] teacherName = new String[teachers.length()];
                        String[] teacherId = new String[teachers.length()];
                        for (int i = 0; i < teachers.length(); i++) {
                            teacherName[i] = teachers.getJSONObject(i).getString("name");
                            teacherId[i] = teachers.getJSONObject(i).getString("id");
                        }
                        String[] studentName = new String[students.length()];
                        String[] studentId = new String[students.length()];
                        for (int i = 0; i < students.length(); i++) {
                            studentName[i] = students.getJSONObject(i).getString("name");
                            studentId[i] = students.getJSONObject(i).getString("id");
                        }
                        mChildName = new String[][]{teacherName, studentName};
                        mChildId = new String[][]{teacherId, studentId};
                        for (int i = 0; i < groupStrings.length; i++) {
                            FriendsParentInfo parentInfo = new FriendsParentInfo();
                            parentInfo.setTitle(groupStrings[i]);
                            List<FriendsChildInfo> list = new ArrayList<>();
                            for (int j = 0; j < mChildName[i].length; j++) {
                                FriendsChildInfo childInfo = new FriendsChildInfo();
                                childInfo.setRemark(mChildName[i][j]);
                                childInfo.setUserID(mChildId[i][j]);
                                list.add(childInfo);
                            }
                            parentInfo.setInfo(list);
                            mList.add(parentInfo);
                        }
                    } else {
                        Toast.makeText(getContext(), "请求好友列表错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        startActivity(new Intent(getContext(),
                ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,
                mChildId[groupPosition][childPosition]));
        return false;
    }
}
