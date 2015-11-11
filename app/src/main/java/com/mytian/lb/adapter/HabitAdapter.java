package com.mytian.lb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.util.CommonUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.R;
import com.mytian.lb.bean.habitUser.HabitResult;
import com.mytian.lb.enums.ItemButton;
import com.mytian.lb.helper.AnimationHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HabitAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mInflater;

    private ArrayList<HabitResult> list;

    private Context mContext;

    public HabitAdapter(Context context, ArrayList<HabitResult> _list) {
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

    public void refresh(ArrayList<HabitResult> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_habit, null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.like.setOnClickListener(this);
            viewHolder.dislike.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HabitResult bean = list.get(position);
        Glide.with(mContext).load(bean.getHead()).placeholder(R.mipmap.icon_contact).centerCrop().crossFade().into(viewHolder.head);
        viewHolder.name.setText(bean.getName());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.like) {
            AnimationHelper.getInstance().viewAnimationScal(v);
            CommonUtil.showToast("记录成功");
            setIconInfo((ImageView) v, ItemButton.RECORD_LIKE);
        } else if (id == R.id.dislike) {
            AnimationHelper.getInstance().viewAnimationScal(v);
            CommonUtil.showToast("已取消");
            setIconInfo((ImageView) v, ItemButton.RECORD_DISLIKE);
        }
    }

    private void setIconInfo(ImageView imageView, ItemButton menu) {
        boolean isClick;
        isClick = (boolean) imageView.getTag();
        if (!isClick) {
            imageView.setImageResource(menu.getResid_press());
            imageView.setTag(true);
        } else {
            imageView.setImageResource(menu.getResid_normal());
            imageView.setTag(false);
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
        @Bind(R.id.like)
        ImageView like;
        @Bind(R.id.dislike)
        ImageView dislike;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}