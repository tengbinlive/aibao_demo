package com.mytian.lb.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.R;
import com.mytian.lb.bean.follow.FollowUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendslistAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayMap<String, FollowUser> list;

    private Context mContext;

    public FriendslistAdapter(Context context, ArrayMap<String, FollowUser> _list) {
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
            convertView = mInflater.inflate(R.layout.item_friendslist, null);
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

        FollowUser bean = list.valueAt(position);
        Glide.with(mContext).load(bean.getHead_id()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.head);
        String is_online = "0".equals(bean.getIs_online()) ? "   [在线]" : "   [离线]";
        viewHolder.name.setText(bean.getAlias() + is_online);
        viewHolder.phone.setText(bean.getPhone());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_friendslist.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.head)
        RoundedImageView head;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.phone)
        TextView phone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}