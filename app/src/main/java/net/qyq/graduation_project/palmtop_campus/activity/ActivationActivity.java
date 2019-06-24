package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;
import net.qyq.graduation_project.palmtop_campus.utils.SharedSPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;

/**
 * 激活用户，重置初始密码
 */
public class ActivationActivity extends Activity implements TextWatcher {
    @BindView(R.id.et_birthday)
    EditText etBirthday;
    @BindView(R.id.et_national)
    EditText etNational;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_office)
    EditText etOffice;
    @BindView(R.id.et_title_position)
    EditText etTitpos;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_activation)
    Button btnActviation;

    /**
     * 按钮需改进
     */
    SharedPreferences sp;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_activation;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        sp = getSharedPreferences("remberPassword", Context.MODE_PRIVATE);
    }

    public void Activation(View view) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String birthday = etBirthday.getText().toString().trim();
        String national = etNational.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String office = etOffice.getText().toString().trim();
        String titpos = etTitpos.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (username.length() == 6) {
            RequestParams params = new RequestParams(Constant.UPDATE_TEACHER_URL);
            params.addQueryStringParameter("username", username);
            params.addQueryStringParameter("birthday", birthday);
            params.addQueryStringParameter("password", password);
            params.addQueryStringParameter("national", national);
            params.addQueryStringParameter("phone", phone);
            params.addQueryStringParameter("office", office);
            params.addQueryStringParameter("title", titpos);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultCode = jsonObject.getInt("code");
                        if (resultCode == Constant.CODE_SUCCESSFUL) {
                            Toast.makeText(ActivationActivity.this, username + "老师，欢迎您使用！", Toast.LENGTH_SHORT).show();
                            new Thread(() -> {
                                try {
                                    EMClient.getInstance().createAccount(username, password);
                                    //                                    easeLogin(username, password, remberPWD);
                                    easeLogin(username, password);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }

                            }).start();
                        } else {
                            Toast.makeText(ActivationActivity.this, "激活失败请稍后重试！", Toast.LENGTH_SHORT).show();
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
        } else {
            RequestParams params = new RequestParams(Constant.UPDATE_STUDENT_URL);
            params.addQueryStringParameter("username", username);
            params.addQueryStringParameter("birthday", birthday);
            params.addQueryStringParameter("password", password);
            params.addQueryStringParameter("national", national);
            params.addQueryStringParameter("phone", phone);
            params.addQueryStringParameter("dormitory", office);
            params.addQueryStringParameter("position", titpos);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int resultCode = jsonObject.getInt("code");
                        if (resultCode == Constant.CODE_SUCCESSFUL) {
                            Toast.makeText(ActivationActivity.this, username + "同学，欢迎你使用！", Toast.LENGTH_SHORT).show();
                            new Thread(() -> {
                                try {
                                    EMClient.getInstance().createAccount(username, password);
                                    easeLogin(username, password);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        } else {
                            Toast.makeText(ActivationActivity.this, "激活失败请稍后重试！", Toast.LENGTH_SHORT).show();
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
    }

    private void easeLogin(String username, String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                boolean saveusernameB = SharedSPUtils.saveUser(getApplicationContext(), username);
                System.out.println("保存成功：" + saveusernameB);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("if_login", Constant.LOGIN);
                intent.putExtra("userName", username);
                startActivity(intent);
                runOnUiThread(() -> Toast.makeText(ActivationActivity.this, "登录成功", Toast.LENGTH_SHORT).show());
                Log.i("Tag", "登录成功");
                finish();
            }

            @Override
            public void onError(int code, String error) {
                runOnUiThread(() -> Toast.makeText(ActivationActivity.this, "登录失败" + error, Toast.LENGTH_SHORT).show());
                Log.i("Tag", "登录失败" + error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (etPassword.getText().toString().length() == 0) {
            btnActviation.setEnabled(false);
        } else if (etPassword.getText().toString().length() < 6) {
            btnActviation.setEnabled(false);
            Toast.makeText(this, getResources().getString(R.string.input_password), Toast.LENGTH_SHORT).show();
        } else {
            btnActviation.setEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
