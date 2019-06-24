package net.qyq.graduation_project.palmtop_campus.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Author：钱
 * Date: 2019/5/9 0009 0:32
 * Des:
 */
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<ImageView> images;
    private ViewPager mViewPager;

    public ViewPagerAdapter(ArrayList<ImageView> images, ViewPager viewPager) {
        this.images = images;
        mViewPager = viewPager;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = images.get(position % images.size());
        mViewPager.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mViewPager.removeView(images.get(position % images.size()));
    }
}
