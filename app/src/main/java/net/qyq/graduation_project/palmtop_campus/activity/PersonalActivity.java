package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/15 0015 20:47
 * Des:
 */
public class PersonalActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.login_out)
    Button loginOut;

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        Intent intent = getIntent();
        boolean isLogin = intent.getBooleanExtra("isLogin", false);

        if (isLogin) {
            loginOut.setText(R.string.login_out);
        } else {
            loginOut.setText(R.string.login);
        }
        loginOut.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    public void onClick(View v) {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                MainActivity.mainActivity.finish();
                Log.i("Tag", "success");
                SharedPreferences sp = getSharedPreferences("palmtop_saveUser",Context.MODE_PRIVATE);
                sp.edit().clear().commit();
                finish();
            }

            @Override
            public void onError(int code, String error) {
                Log.i("Tag", "error");
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
