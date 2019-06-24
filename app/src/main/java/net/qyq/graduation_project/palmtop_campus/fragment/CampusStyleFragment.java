package net.qyq.graduation_project.palmtop_campus.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.qyq.graduation_project.common.app.Fragment;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.activity.DetailedActivity;
import net.qyq.graduation_project.palmtop_campus.adapter.NewsListAdapter;
import net.qyq.graduation_project.palmtop_campus.adapter.ViewPagerAdapter;
import net.qyq.graduation_project.palmtop_campus.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/14 0014 22:59
 * Des:校园风采
 * 1、轮播最新的新闻或活动
 */
public class CampusStyleFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.news_title)
    TextView news_title;
    @BindView(R.id.lineLayout_dot)
    LinearLayout dot;
    @BindView(R.id.news_list)
    ListView newsList;

    private ArrayList<String> titles = new ArrayList<>();//标题
    private ArrayList<String> descs = new ArrayList<>();//内容
    private ArrayList<Bitmap> images = new ArrayList<>();//图片
    private ArrayList<String> updateAt = new ArrayList<>();//新闻发布的时间
    private List<ImageView> mDots = new ArrayList<>();
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4, R.id.pager_image5};
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIME = 5000;//间隔时间

    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_campus_style;
    }

    @Override
    protected void initWidget(View root, Bundle savedInstanceState) {
        super.initWidget(root, savedInstanceState);
        newsList.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        RequestParams params = new RequestParams(Constant.NEWS_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == Constant.CODE_SUCCESSFUL) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        System.out.println(array);
                        titles.clear();
                        descs.clear();
                        images.clear();
                        updateAt.clear();
                        for (int i = array.length() - 1; i >= 0; i--) {
                            JSONObject object = array.getJSONObject(i);
                            titles.add(object.getString("title"));
                            descs.add(object.getString("rep"));
                            updateAt.add(object.getString("update_at"));
                        }
                    } else {
                        Toast.makeText(getContext(), "获取数据异常，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), titles, descs, images, updateAt);
                newsList.setAdapter(newsListAdapter);
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
        /**
         * 测试用的轮播图
         */

        int[] imageId = new int[]{
                R.drawable.vpt, R.drawable.vpt2, R.drawable.vpt1,
                R.drawable.vpt4, R.drawable.vpt5
        };
        ImageView iv;
        ImageView dot_iv;
        for (int i = 0; i < imageId.length; i++) {
            iv = new ImageView(getContext());
            dot_iv = new ImageView(getContext());

            iv.setBackgroundResource(imageId[i]);
            iv.setId(imgae_ids[i]);
            iv.setOnClickListener(new pagerImageOnClick());
            mImageViews.add(iv);

            dot_iv.setImageResource(R.drawable.viewpager_dot_normal);
            dot_iv.setPadding(5, 0, 5, 0);
            mDots.add(dot_iv);
        }
        for (int i = 0; i < mDots.size(); i++) {
            dot.addView(mDots.get(i));
        }
        initAdapter();
        autoPlayView();
    }

    private void initAdapter() {
        mViewPager.setAdapter(new ViewPagerAdapter(mImageViews, mViewPager));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mDots.size();
                int count = dot.getChildCount();
                for (int i = 0; i < count; i++) {
                    ImageView iv = (ImageView) dot.getChildAt(i);
                    if (i == newPosition) {
                        iv.setImageResource(R.drawable.viewpager_dot_select);
                    } else {
                        iv.setImageResource(R.drawable.viewpager_dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    private void setFirstLocation() {
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageViews.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), DetailedActivity.class);
        intent.putExtra("code", Constant.CODE_SUCCESSFUL);
        intent.putExtra("title", titles.get(position));
        intent.putExtra("content", descs.get(position));
        byte[] buff = new byte[1024];
        if (images.size() != 0) {
            Bitmap bitmap = images.get(position);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            buff = out.toByteArray();
        }
        intent.putExtra("images", buff);
        intent.putExtra("time", updateAt.get(position));
        startActivity(intent);
    }

    //图片点击事件
    private class pagerImageOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(getContext(), "图片1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(getContext(), "图片2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(getContext(), "图片3被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(getContext(), "图片4被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image5:
                    Toast.makeText(getContext(), "图片5被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIME);
                }
            }
        }).start();
    }
}
