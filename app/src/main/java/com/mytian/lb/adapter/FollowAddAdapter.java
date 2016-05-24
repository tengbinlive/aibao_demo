package com.mytian.lb.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.activity.ShowPictureActivity;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.imp.EItemCallOnClick;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowAddAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayMap<String, FollowUser> list;

    private Context mContext;

    private EItemCallOnClick itemCallOnClick;

    public void setItemCallOnClick(EItemCallOnClick itemCallOnClick) {
        this.itemCallOnClick = itemCallOnClick;
    }

    public FollowAddAdapter(AbsActivity context, ArrayMap<String, FollowUser> _list) {
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
        return list.valueAt(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void refresh(ArrayMap<String, FollowUser> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    public void refresh(String key, FollowUser value) {
        if (list != null && list.containsKey(key)) {
            list.put(key, value);
            notifyDataSetChanged();
        }
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

        final FollowUser bean = list.valueAt(position);
        Glide.with(mContext.getApplicationContext()).load(bean.getHead_thumb())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.default_head)
                .into(viewHolder.head);

        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> imgs = new ArrayList<>();
                imgs.add(bean.getHead_thumb());
                ShowPictureActivity.toShowPicture(mContext, imgs, bean.getAlias());
            }
        });

        String is_online = FollowUser.ONLINE.equals(bean.getIs_online()) ? mContext.getString(R.string.online_brackets) : mContext.getString(R.string.offline_brackets);
        viewHolder.name.setText(bean.getAlias() + is_online);
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
                        FollowUser user = list.valueAt(index);
                        ((Button) view).setText(R.string.already_attention);
                        list.valueAt(index).setFocus(true);
                        setAccepatView((Button) view, false);
                        view.setEnabled(false);
                        if (null != itemCallOnClick) {
                            itemCallOnClick.callOnClick(user.getUid());
                        }
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
            button.setBackgroundResource(R.drawable.button_bg_theme);
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
        @BindView(R.id.head)
        RoundedImageView head;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.accept_bt)
        Button accept_bt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}