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
import com.dao.UserAction;
import com.mytian.lb.R;
import com.mytian.lb.helper.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 约定界面
 */
public class AgreementAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<UserAction> list;

    private Context mContext;

    private boolean isOFFLINE;

    private boolean isAGREEMENTING;

    public void setIsAGREEMENTING(boolean isAGREEMENTING) {
        this.isAGREEMENTING = isAGREEMENTING;
        notifyDataSetChanged();
    }

    public void setIsOFFLINE(boolean isOFFLINE) {
        this.isOFFLINE = isOFFLINE;
        notifyDataSetChanged();
    }

    public AgreementAdapter(Context context, ArrayList<UserAction> _list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_agreement, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserAction bean = list.get(position);

        int icon_id = Utils.getResource(mContext, (isOFFLINE || isAGREEMENTING) ? bean.getIcon_disabled() : bean.getIcon(), "mipmap");

        Glide.with(mContext).load(icon_id).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.icon);
        viewHolder.title.setText(bean.getTitle());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_agreement.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}