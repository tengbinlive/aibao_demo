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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论
 */
public class CommentPagerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<CommentResult> list;

    private Context mContext;

    public CommentPagerAdapter(Context context, ArrayList<CommentResult> _list) {
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
        final CommentResult bean = list.get(position);
        Glide.with(mContext).load(bean.getHeadUrl()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
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
        @BindView(R.id.head_comment)
        RoundedImageView head;
        @BindView(R.id.name_comment)
        TextView name;
        @BindView(R.id.content_comment)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}