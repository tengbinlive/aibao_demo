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
import com.core.util.CommonUtil;
import com.mytian.lb.R;
import com.mytian.lb.bean.user.UserAction;
import com.mytian.lb.enums.ItemButton;
import com.mytian.lb.helper.AnimationHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HabitAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<UserAction> list;

    private Context mContext;

    private boolean isOFFLINE;

    public void setIsOFFLINE(boolean isOFFLINE) {
        this.isOFFLINE = isOFFLINE;
    }

    public HabitAdapter(Context context, ArrayList<UserAction> _list) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = mInflater.inflate(R.layout.item_habit, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserAction bean = list.get(position);

        String iconUrl = isOFFLINE ? bean.getUrl() : bean.getUrl();

        Glide.with(mContext).load(iconUrl).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.head);
        viewHolder.name.setText(bean.getDes());

        int record = bean.getRecord();
        if (record == UserAction.GREAT) {
            setIconInfo(viewHolder.like, ItemButton.RECORD_LIKE, true);
            setIconInfo(viewHolder.dislike, ItemButton.RECORD_DISLIKE, false);
        } else if (record == UserAction.BAD) {
            setIconInfo(viewHolder.like, ItemButton.RECORD_LIKE, false);
            setIconInfo(viewHolder.dislike, ItemButton.RECORD_DISLIKE, true);
        } else {
            setIconInfo(viewHolder.like, ItemButton.RECORD_LIKE, false);
            setIconInfo(viewHolder.dislike, ItemButton.RECORD_DISLIKE, false);
        }
        viewHolder.like.setTag(R.id.item_habit_index, position);
        viewHolder.like.setTag(R.id.item_habit_view, viewHolder.dislike);
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.getInstance().viewAnimationScal(v);
                CommonUtil.showToast(R.string.success_record);
                int index = (int) v.getTag(R.id.item_habit_index);
                ImageView dislike = (ImageView) v.getTag(R.id.item_habit_view);
                list.get(index).setRecord(UserAction.GREAT);
                setIconInfo((ImageView) v, ItemButton.RECORD_LIKE, true);
                setIconInfo(dislike, ItemButton.RECORD_DISLIKE, false);
            }
        });
        viewHolder.dislike.setTag(R.id.item_habit_index, position);
        viewHolder.dislike.setTag(R.id.item_habit_view, viewHolder.like);
        viewHolder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.getInstance().viewAnimationScal(v);
                CommonUtil.showToast(R.string.success_record);
                int index = (int) v.getTag(R.id.item_habit_index);
                ImageView like = (ImageView) v.getTag(R.id.item_habit_view);
                list.get(index).setRecord(UserAction.BAD);
                setIconInfo((ImageView) v, ItemButton.RECORD_DISLIKE, true);
                setIconInfo(like, ItemButton.RECORD_LIKE, false);
            }
        });

        return convertView;
    }

    private void setIconInfo(ImageView imageView, ItemButton menu, boolean is) {
        if (is) {
            imageView.setImageResource(menu.getResid_press());
        } else {
            imageView.setImageResource(menu.getResid_normal());
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
        ImageView head;
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