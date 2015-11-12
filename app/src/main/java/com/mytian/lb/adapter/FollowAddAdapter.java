package com.mytian.lb.adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.manager.FollowManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FollowAddAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<FollowUser> list;

    private AbsActivity mContext;

    private FollowManager manager = new FollowManager();

    public FollowAddAdapter(AbsActivity context, ArrayList<FollowUser> _list) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void refresh(ArrayList<FollowUser> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_follow_add, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }

        FollowUser bean = list.get(position);
        Glide.with(mContext).load(bean.getHead_thumb()).placeholder(R.mipmap.icon_contact).into(viewHolder.head);
        viewHolder.name.setText(bean.getName());
        if (FollowUser.LB.equals(bean.getFocus_from())) {
            setAccepatView(viewHolder.accept_bt, false);
            viewHolder.accept_bt.setText(R.string.already_pull);
        } else {
            if (bean.isFocus()) {
                setAccepatView(viewHolder.accept_bt, false);
                viewHolder.accept_bt.setText(R.string.already_attention);
            } else {
                viewHolder.accept_bt.setTag(position);
                setAccepatView(viewHolder.accept_bt, true);
                viewHolder.accept_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (Integer) view.getTag();
                        FollowUser user = list.get(index);
                        mContext.dialogShow(R.string.accepat_ing);
                        ((Button) view).setText(R.string.already_attention);
                        list.get(index).setFocus(true);
                        setAccepatView((Button) view, false);
                        view.setEnabled(false);
                        manager.followAgree(mContext, user.getUid(), handler, ACCEPAT);
                    }
                });
            }
        }
        return convertView;
    }

    private void setAccepatView(Button button, boolean is) {
        if (!is) {
            button.setBackgroundResource(0);
            button.setTextColor(0xffbdbdbd);
        } else {
            button.setBackgroundResource(R.drawable.login_bg);
            button.setTextColor(0xffffffff);
        }

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_habit.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.head)
        RoundedImageView head;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.accept_bt)
        Button accept_bt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private static final int ACCEPAT = 1;//同意关注
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case ACCEPAT:
                    mContext.dialogDismiss();
                    CommonResponse resposne = (CommonResponse) msg.obj;
                    CommonUtil.showToast(resposne.getMsg());
                    break;
                default:
                    break;
            }
        }
    };
}