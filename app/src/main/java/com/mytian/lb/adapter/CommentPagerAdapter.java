package com.mytian.lb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.R;
import com.mytian.lb.bean.user.CommentResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 评论
 */
public class CommentPagerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<CommentResult> list;

    private Context mContext;

    private int offset;

    public CommentPagerAdapter(Context context, ArrayList<CommentResult> _list,int _offset) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        offset = _offset;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size()-offset;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position+offset);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void refresh(ArrayList<CommentResult> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dynamic_comment_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        position = position+offset;
        final CommentResult bean = list.get(position);
        Glide.with(mContext).load(bean.getHeadUrl()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.default_head)
                .into(viewHolder.head);
        viewHolder.name.setText(bean.getName());
        viewHolder.content.setText(bean.getContent());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_dynamic_comment_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.head_comment)
        RoundedImageView head;
        @Bind(R.id.name_comment)
        TextView name;
        @Bind(R.id.content_comment)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}