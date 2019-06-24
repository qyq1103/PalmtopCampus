package net.qyq.graduation_project.palmtop_campus.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;

import butterknife.BindView;

public class DetailedActivity extends Activity {
    @BindView(R.id.txt_title)
    TextView titlebar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.news_img)
    ImageView newImg;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.time)
    TextView time;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_detailed;
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        int code = intent.getIntExtra("code", Constant.CODE_SUCCESSFUL);
        title.setText(intent.getStringExtra("title"));
        content.setText("\t\t" + intent.getStringExtra("content"));
        time.setText(intent.getStringExtra("time"));
        if (code == Constant.CODE_SUCCESSFUL) {
            titlebar.setText("新闻详情");
            byte buff[] = intent.getByteArrayExtra("image");
            if (buff != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
                newImg.setImageBitmap(bitmap);
            } else {
                newImg.setImageResource(R.drawable.ic_default);
            }
        } else {
            titlebar.setText("通知详情");
            department.setText(intent.getStringExtra("department"));
        }
    }
}
