package com.mytian.lb.fragment;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.util.StringUtil;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.AgreementAdapter;
import com.mytian.lb.bean.AgreementBean;
import com.mytian.lb.mview.CircleNetworkImageView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class AgreementFragment extends AbsFragment {

    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_icon)
    CircleNetworkImageView user_icon;
    @Bind(R.id.user_name)
    TextView user_name;

    private AgreementAdapter mAdapter;

    private ArrayList<AgreementBean> arrayList = new ArrayList<>();

    private void initGridView() {

        initData();

        mAdapter = new AgreementAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(gridview);

        gridview.setAdapter(animationAdapter);

        gridview.setEmptyView(llListEmpty);
    }

    private String[] argeementTitle = new String[]{"写作业", "看书", "做家务", "吃饭", "出去玩", "睡觉"};

    private void initData() {
        for (int i = 0; i < 6; i++) {
            String iconStr = "icon_love_hb";
            int imgId = getResources().getIdentifier(iconStr + (i % 6 + 1), "mipmap", App.getInstance().getPackageName());
            AgreementBean bean = new AgreementBean(argeementTitle[i % 6], imgId);
            arrayList.add(bean);
        }
    }

    private void setUserInfo() {
        String name = "小明";
        name = StringUtil.isNotBlank(name) ? name : "你猜.";
        String head = "";
        head = StringUtil.isNotBlank(head) ? head : "";
        String phone = "...";
        phone = StringUtil.isNotBlank(phone) ? phone : "...";
        user_name.setText(name);
        user_phone.setText(phone);
        //        Glide.with(this).load(R.mipmap.head_default).placeholder(R.mipmap.default_head).centerCrop().crossFade().into(user_icon);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_agreement;
    }

    @Override
    public void EInit() {
        setUserInfo();
        initGridView();
    }

}
