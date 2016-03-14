package com.mytian.lb.fragment;

import android.net.Uri;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.mview.CodeView;

import java.io.File;

import butterknife.Bind;

public class KindleFragment extends AbsFragment {

    @Bind(R.id.mcodeview)
    CodeView mcodeview;

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
        File dir = null;
        Uri fileUri = getActivity().getIntent().getData();
        if (fileUri != null) {
            dir = new File(fileUri.getPath());
        }
        if (dir != null) {
            mcodeview.setDirSource(dir);
        }
    }
}
