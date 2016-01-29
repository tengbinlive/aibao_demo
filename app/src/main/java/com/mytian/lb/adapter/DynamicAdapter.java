package com.mytian.lb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.util.StringUtil;
import com.mytian.lb.R;
import com.mytian.lb.bean.dymic.Dynamic;
import com.mytian.lb.bean.dymic.DynamicBaseInfo;
import com.mytian.lb.bean.dymic.DynamicContent;
import com.mytian.lb.helper.GlideRoundTransform;
import com.mytian.lb.manager.ShareManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;

public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<Dynamic> list;

    private Context mContext;

    private GlideRoundTransform transform;

    public DynamicAdapter(Context context, ArrayList<Dynamic> _list) {
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

    public void refresh(ArrayList<Dynamic> _list) {
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
        final Dynamic bean = list.get(position);
        final DynamicBaseInfo dynamicBaseInfo = bean.getBaseInfo();
        final DynamicContent dynamicContent = bean.getContent();
        String fromType = dynamicBaseInfo.getFromType();
        if (DynamicBaseInfo.TYPE_MB.equals(fromType)) {
            Glide.with(mContext).load(dynamicBaseInfo.getHead_thumb()).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.head_default)
                    .transform(transform)
                    .into(viewHolder.head);
        } else {
            int headResid = getHeadResid(dynamicBaseInfo.getSys_thumb_id());
            Glide.with(mContext).load(headResid).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.default_head)
                    .transform(transform)
                    .into(viewHolder.head);
        }
        viewHolder.name.setText(dynamicBaseInfo.getAlias());
        viewHolder.date.setText(dynamicBaseInfo.getTime());
        viewHolder.desc.setText(dynamicBaseInfo.getFromName());
        viewHolder.title.setText(dynamicContent.getTitle());
        String contentStr = dynamicContent.getText();
        String imageUrl = dynamicContent.getImageUrl();
        if(StringUtil.isBlank(contentStr)&&StringUtil.isBlank(imageUrl)){
            viewHolder.contentLayout.setVisibility(View.GONE);
        }else{
            viewHolder.contentLayout.setVisibility(View.VISIBLE);
            if(StringUtil.isBlank(imageUrl)){
                viewHolder.image.setVisibility(View.GONE);
            }else{
                viewHolder.image.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(imageUrl).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(transform)
                        .into(viewHolder.image);
            }
            viewHolder.content.setText(contentStr);
        }
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareManager.getInstance().share(dynamicContent);
            }
        });
        return convertView;
    }

    private int getHeadResid(String uid) {
        if ("100101".equals(uid)) return R.mipmap.head_default;
        if ("100102".equals(uid)) return R.mipmap.head_sys_1;
        if ("100103".equals(uid)) return R.mipmap.head_sys_2;
        if ("100104".equals(uid)) return R.mipmap.head_sys_3;
        if ("100105".equals(uid)) return R.mipmap.head_sys_4;
        if ("100106".equals(uid)) return R.mipmap.head_default;
        return -1;
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
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.share)
        ImageView share;
        @Bind(R.id.content_layout)
        LinearLayout contentLayout;
        @Bind(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}