package com.mytian.lb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mytian.lb.R;
import com.mytian.lb.bean.DynamicResult;
import com.mytian.lb.helper.GlideRoundTransform;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<DynamicResult> list;

    private Context mContext;

    private GlideRoundTransform transform;

    public DynamicAdapter(Context context, ArrayList<DynamicResult> _list) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        transform = new GlideRoundTransform(context, 4);
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

    public void refresh(ArrayList<DynamicResult> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dynamic, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DynamicResult bean = list.get(position);
        int type = bean.getType();
        int heandid = R.mipmap.head_0;
        bean.setAlias("韩韩");
        if (type == DynamicResult.TYPE_SYS) {
            heandid = R.mipmap.icon_sys;
            bean.setAlias("麦田官方");
        } else if (position % 4 == 0) {
            heandid = R.mipmap.head_1;
            bean.setAlias("小明");
        } else if (position % 4 == 1) {
            heandid = R.mipmap.head_2;
            bean.setAlias("小红");
        } else if (position % 4 == 2) {
            heandid = R.mipmap.head_3;
            bean.setAlias("韩梅梅");
        }
        Glide.with(mContext).load(heandid).asBitmap()
                .transform(transform)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_contact).into(viewHolder.head);
        viewHolder.name.setText(bean.getAlias());
        viewHolder.date.setText(bean.getDate());
        viewHolder.desc.setText(bean.getDesc());
        viewHolder.content.setText(bean.getContent());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_dynamic.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.head)
        ImageView head;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.desc)
        TextView desc;
        @Bind(R.id.content)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}