package net.qyq.graduation_project.common.widget;

import android.app.AlertDialog;
import android.content.DialogInterface;

import net.qyq.graduation_project.common.app.Activity;


/**
 * Author：钱
 * Date: 2019/3/14 0014 13:14
 * Des:提示弹窗
 */
public class DialogView {
    private Activity mActivity;
    private String title;
    private String msg;
    private String positive;
    private String negative;

    private DialogButtonClick mButtonClick;

    /**
     * @param activity Activity
     * @param title    提示的标题
     * @param msg      提示信息
     * @param positive 确定按钮
     * @param negative 否定按钮
     *                 两个按钮的提示框
     */
    public DialogView(Activity activity, String title, String msg, String positive, String negative) {
        mActivity = activity;
        this.title = title;
        this.msg = msg;
        this.positive = positive;
        this.negative = negative;
    }

    public void showDialog(DialogButtonClick dialogButtonClick) {
        this.mButtonClick = dialogButtonClick;
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确定时处理的方法
                mButtonClick.onPositiveClick();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击否定时处理的方法
                mButtonClick.onNegativeClick();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //用户取消弹窗监听，比如点击返回键时的处理方法
                mButtonClick.onCancelClick();
            }
        });
        builder.show();
    }

    /**
     * 自定义的事件处理方法
     */
    public abstract interface DialogButtonClick {
        void onPositiveClick();

        void onNegativeClick();

        void onCancelClick();
    }

}
