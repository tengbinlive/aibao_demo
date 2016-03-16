package com.mytian.lb.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class ShowPictureActivity extends AbsActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.dot)
    TextView dot;
    private ArrayList<Object> mImages;
    private ArrayList<ViewGroup> mViews;

    public final static String IMAGES = "DATA";
    public final static String TITLE = "TITLE";
    public final static String INDEX = "INDEX";

    private int item_size;

    @Override
    public void EInit() {
        super.EInit();
        initView();
        String title = getIntent().getStringExtra(TITLE);
        String actionbarTitlte = StringUtil.isBlank(title) ? getString(R.string.app_name) : title;
        setToolbarIntermediateStr(actionbarTitlte);
    }

    public static void toShowPicture(Activity activity, ArrayList<Object> imgs) {
        Intent intent = new Intent(activity, ShowPictureActivity.class);
        intent.putExtra(ShowPictureActivity.IMAGES, imgs);
        activity.startActivity(intent);
    }

    public static void toShowPicture(Activity activity, ArrayList<Object> imgs, String title) {
        Intent intent = new Intent(activity, ShowPictureActivity.class);
        intent.putExtra(ShowPictureActivity.IMAGES, imgs);
        intent.putExtra(ShowPictureActivity.TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_show_picture;
    }

    private void initView() {
        mImages = (ArrayList<Object>) getIntent().getExtras().getSerializable(IMAGES);
        int index = getIntent().getExtras().getInt(INDEX, 0);
        mViews = new ArrayList<>();
        if (mImages != null && mImages.size() > 0) {
            item_size = mImages.size();
            setDot(index, item_size);
            for (int i = 0; i < item_size; i++) {
                RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.layout_show_picture, null);
                PhotoView view = (PhotoView) layout.findViewById(R.id.photoview);
                view.enable();
                final ProgressWheel progressbar = (ProgressWheel) layout.findViewById(R.id.progress);
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                Object imageUrl = mImages.get(i);
                progressbar.setVisibility(View.VISIBLE);
                Glide.with(this.getBaseContext()).load(imageUrl).listener(new RequestListener<Object, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(view);
                mViews.add(layout);
            }
            viewPager.setAdapter(new PictureAdapter(mViews));
            viewPager.setOnPageChangeListener(mOnPageChangeListener);
            viewPager.setOffscreenPageLimit(0);
            viewPager.setCurrentItem(index);
        }
    }

    private void setDot(int index, int size) {
        if (size > 1) {
            dot.setVisibility(View.VISIBLE);
            // 从1开始
            dot.setText((index + 1) + "/" + size);
        } else {
            dot.setVisibility(View.GONE);
        }
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            setDot(position, item_size);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    static class PictureAdapter extends PagerAdapter {

        List<ViewGroup> mViews;

        public PictureAdapter(List<ViewGroup> mViews) {
            this.mViews = mViews;
        }

        @Override
        public int getCount() {
            return mViews == null ? 0 : mViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            View item = mViews.get(arg1);
            try {
                ((ViewPager) arg0).addView(item, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        }
    }

}
