package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
 * Author：钱
 * Date: 2019/3/14 0014 21:00
 * Des:登录注册
 */
public class LoginActivity extends Activity
        implements TextWatcher { //监听userName、password输入框是否合法及焦点是否改变
    //注解绑定的方式进行控件的绑定使用
    @BindView(R.id.et_username)
    EditText userName;
    @BindView(R.id.et_password)
    EditText pwd;
    @BindView(R.id.txt_user)
    TextView userWarn;
    @BindView(R.id.txt_pwd)
    TextView pwdWarn;
    @BindView(R.id.remember_pwd)
    CheckBox re_pwd;

    private static boolean userInput = false, pwdInput = false;
    private boolean autoLogin = false;
    SharedPreferences sp;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        if (EMClient.getInstance().isLoggedInBefore()) {
            autoLogin = true;
            sp = getSharedPreferences("palmtop_saveUser", Context.MODE_PRIVATE);
            String username = sp.getString("username", null);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", username);
            intent.putExtra("if_login", Constant.LOGIN);
            startActivity(intent);
            return;
        }
    }

    /**
     * 初始化控件
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        userName.addTextChangedListener(this);
        pwd.addTextChangedListener(this);
    }


    /**
     * @param s     输入框中改变前的字符串信息
     * @param start 输入框中改变前的字符串的起始位置
     * @param count 输入框中改变前后的字符串改变数量一般为0
     * @param after 输入框中改变后的字符串与起始位置的偏移量
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * @param s      输入框中改变前的字符串信息
     * @param start  输入框中改变后的字符串的起始位置
     * @param before 输入框中改变前的字符串的位置 默认为0
     * @param count  输入框中改变后的一共输入字符串的数量
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * @param s 输入结束呈现在输入框中的信息
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (userName.getText().toString().length() == 0) {
            userInput = false;
            userWarn.setText("");
        } else if (userName.getText().toString().length() == 6 ||  //老师工号
                userName.getText().toString().length() == 11 || //手机号
                userName.getText().toString().length() == 12) {  //学号
            userInput = true;
            userWarn.setText("");
        } else {
            userInput = false;
            userWarn.setText(getResources().getString(R.string.num_or_phone));
        }
        if (pwd.getText().toString().length() == 0) {
            pwdInput = false;
            pwdWarn.setText("");
        } else if (pwd.getText().toString().length() < 6) {
            pwdInput = false;
            pwdWarn.setText(getResources().getString(R.string.input_password));
        } else {
            pwdInput = true;
            pwdWarn.setText("");
        }
    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     * 选择使用方式（登陆或是稍后登陆）
     * 跳转不完整，还需改进
     * 改进如下：
     * 1、添加返回码，或Button的Id用于区分是哪个按钮点击
     * 2、嵌入环信EaseUI的登陆方式或其他平台的登陆方式
     */
    public void Login(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (userInput && pwdInput) {
                    String username = userName.getText().toString().trim();
                    String password = pwd.getText().toString();
                    RequestParams params = new RequestParams(Constant.USER_LOGIN_URL);
                    params.addQueryStringParameter("username", username);
                    params.addQueryStringParameter("password", password);
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int code = jsonObject.getInt("code");
                                if (code == Constant.CODE_SUCCESSFUL) {
                                    enterMain(username, password, jsonObject.getString("data").equals("null"));
                                } else if (code == Constant.CODE_TEACHER) {
                                    String username = jsonObject.getString("data");
                                    enterMain(username, password, jsonObject.getString("data").equals("null"));
                                } else if (code == Constant.CODE_STUDENT) {
                                    String username = jsonObject.getString("data");
                                    enterMain(username, password, jsonObject.getString("data").equals("null"));
                                } else if (code == Constant.CODE_NO_USER) {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "请先输入用户名和密码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next_login:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("if_login", Constant.NO_LOGIN);
                intent.putExtra("userName", "");
                startActivity(intent);
                finish();
                break;
        }
    }

    private void enterMain(String username, String password, boolean isFirst) {
        if (isFirst) {
            Intent intent = new Intent(this, ActivationActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            easeLogin(username, password);
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
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show());
                Log.i("Tag", "登录成功");
                finish();
            }

            @Override
            public void onError(int code, String error) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败" + error, Toast.LENGTH_SHORT).show());
                Log.i("Tag", "登录失败" + error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }
}
