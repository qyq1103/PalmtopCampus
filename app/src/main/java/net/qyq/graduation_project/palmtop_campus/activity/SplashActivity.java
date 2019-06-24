package net.qyq.graduation_project.palmtop_campus.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.common.widget.DialogView;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;

public class SplashActivity extends Activity {
    @BindView(R.id.splash)
    RelativeLayout splash;
    @BindView(R.id.versionName)
    TextView mVersionName;

    private static final int CODE_UPDATE_DIALOG = 1;
    private static final int CODE_ENTER_HOME = 2;
    private static final int CODE_NETWORK_ERROR = 3;
    private static final int CODE_JSON_ERROR = 4;

    private int mVersionCode;
    private String mDes;
    private String mUrl;
    private ProgressDialog mProgressDialog;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    //升级弹窗
                    showPlay();
                    break;
                case CODE_ENTER_HOME:
                    //进入登录页面
                    enterLogin();
                    break;
                case CODE_NETWORK_ERROR:
                    Toast.makeText(SplashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    enterLogin();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                    enterLogin();
                    break;
            }
        }
    };

    /**
     * 跳转到登录页面
     */
    private void enterLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    /**
     * 下载新的APP并进行安装
     */
    private void downloadAndInstall() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断sdcard是否存在
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/AppleCampus.apk";
            // Xutils
            RequestParams params = new RequestParams(mUrl);
            params.setAutoRename(false);//下载完成后自动为文件命名
            params.setAutoResume(true);//断点续传
            params.setSaveFilePath(path);//下载文件的保存路径
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    mProgressDialog = new ProgressDialog(SplashActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
                    mProgressDialog.setMessage("正在下载中...");
                    mProgressDialog.setProgress(0);
                    mProgressDialog.show();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    mProgressDialog.setMax((int) total);
                    mProgressDialog.setProgress((int) current);
                }
            });
        } else {
            Toast.makeText(this, "没有找到sdcard!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取版本信息，返回显示
     *
     * @return return versionName
     */
    private String getVersionName() {
        PackageManager manager = getPackageManager();//包管理器
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getVersionCode() {
        PackageManager manager = getPackageManager();//包管理器
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();
        mVersionName.setText(String.format("版本号：%s", getVersionName()));

        checkUpdate();

        AlphaAnimation animation = new AlphaAnimation(0.2f, 1);
        animation.setDuration(2000);
        splash.startAnimation(animation);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    private void checkUpdate() {
        Message msg = Message.obtain();
        long startTime = System.currentTimeMillis();
        RequestParams params = new RequestParams(Constant.CHECK_UPDATE_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    mVersionCode = jsonObject.getInt("versionCode");
                    mDes = jsonObject.getString("des");
                    mUrl = jsonObject.getString("url");
                    if (getVersionCode() < mVersionCode) {
                        msg.what = CODE_UPDATE_DIALOG;
                    } else {
                        msg.what = CODE_ENTER_HOME;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = CODE_JSON_ERROR;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                msg.what = CODE_NETWORK_ERROR;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        try {
            if (usedTime < 2000) {
                Thread.sleep(2000 - usedTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    @Override
    protected void showPlay() {
        super.showPlay();


        DialogView dialogView = new DialogView(SplashActivity.this, "提示", mDes, "现在更新", "稍后更新");
        dialogView.showDialog(new DialogView.DialogButtonClick() {
            @Override
            public void onPositiveClick() {
                downloadAndInstall();
            }

            @Override
            public void onNegativeClick() {
                enterLogin();
            }

            @Override
            public void onCancelClick() {
                enterLogin();
            }
        });
    }
}
