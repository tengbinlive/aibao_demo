package com.mytian.lb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.util.StringUtil;
import com.mytian.lb.R;
import com.mytian.lb.bean.DynamicResult;
import com.mytian.lb.helper.GlideRoundTransform;
import com.mytian.lb.manager.ShareManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;

public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<DynamicResult> list;

    private Context mContext;

    private GlideRoundTransform transform;

    public DynamicAdapter(Context context, ArrayList<DynamicResult> _list) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        transform = new GlideRoundTransform(context);
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
        final DynamicResult bean = list.get(position);
        String heandurl = bean.getHead();
        if (StringUtil.isBlank(heandurl)) {
            viewHolder.head.setImageDrawable(bean.getDrawable());
        } else {
            Glide.with(mContext).load(heandurl).asBitmap()
                    .transform(transform)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon_contact).into(viewHolder.head);
        }
        viewHolder.name.setText(bean.getAlias());
        viewHolder.date.setText(bean.getDate());
        viewHolder.desc.setText(bean.getDesc());
        viewHolder.content.setText(bean.getContent());
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Platform.ShareParams shareParams = ShareManager.getInstance().getParams("test_title","test_content",null,"http://img0.pcgames.com.cn/pcgames/1106/16/2239524_00018.gif",bean.getDrawable());
                ShareManager.getInstance().share(shareParams);
            }
        });
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
        @Bind(R.id.share)
        ImageView share;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}