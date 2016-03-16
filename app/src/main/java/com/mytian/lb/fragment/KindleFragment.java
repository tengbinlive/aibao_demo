package com.mytian.lb.fragment;

import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;

public class KindleFragment extends AbsFragment {

    @Bind(R.id.photoview)
    PhotoView photoview;

    @Bind(R.id.progress)
    ProgressWheel progress;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

    @Override
    public void EInit() {
        super.EInit();
        photoview.enable();
        progress.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(R.drawable.test).listener(new RequestListener<Object, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(photoview);
    }
}
