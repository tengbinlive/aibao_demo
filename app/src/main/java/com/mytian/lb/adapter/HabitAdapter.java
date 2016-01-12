package com.mytian.lb.adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.dao.UserAction;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.enums.ItemButton;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.Utils;
import com.mytian.lb.manager.UserActionManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HabitAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<UserAction> list;

    private AbsActivity mContext;

    private View habitView;

    private UserActionManager manager = new UserActionManager();

    private FollowUser followUser;

    public HabitAdapter(AbsActivity context, FollowUser cureentParent, ArrayList<UserAction> _list) {
        this.list = _list;
        mContext = context;
        followUser = cureentParent;
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

        int icon_id = Utils.getResource(mContext, bean.getIcon(), "mipmap");

        Glide.with(mContext).load(icon_id).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.head);
        viewHolder.name.setText(bean.getTitle());

        String record = bean.getRecord();
        if (UserAction.GREAT.equals(record)) {
            setIconInfo(viewHolder.like, ItemButton.RECORD_LIKE, true);
            setIconInfo(viewHolder.dislike, ItemButton.RECORD_DISLIKE, false);
        } else if (UserAction.BAD.equals(record)) {
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
                habitView = v;
                sendHabit(v, UserAction.GREAT);
            }
        });
        viewHolder.dislike.setTag(R.id.item_habit_index, position);
        viewHolder.dislike.setTag(R.id.item_habit_view, viewHolder.like);
        viewHolder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.getInstance().viewAnimationScal(v);
                habitView = v;
                sendHabit(v, UserAction.BAD);
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

    private static final int HABIT_GREAT = 0x01;//赞
    private static final int HABIT_BAD = 0x02;//贬
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case HABIT_GREAT:
                case HABIT_BAD:
                    loadData((CommonResponse) msg.obj, what);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 数据返回
     *
     * @param resposne
     */
    private void loadData(CommonResponse resposne, int what) {
        mContext.dialogDismiss();
        CommonUtil.showToast(resposne.getMsg());
        if (resposne.isSuccess()) {
            setHabitView(habitView, what);
        }
    }

    /**
     * 设置评价结果
     * @param v
     * @param type
     */
    private void setHabitView(View v, int type) {
        int index = (int) v.getTag(R.id.item_habit_index);
        ImageView imageView = (ImageView) v.getTag(R.id.item_habit_view);
        if (type == HABIT_GREAT) {
            list.get(index).setRecord(UserAction.GREAT);
            setIconInfo((ImageView) v, ItemButton.RECORD_LIKE, true);
            setIconInfo(imageView, ItemButton.RECORD_DISLIKE, false);
        }
        if (type == HABIT_BAD) {
            list.get(index).setRecord(UserAction.BAD);
            setIconInfo((ImageView) v, ItemButton.RECORD_DISLIKE, true);
            setIconInfo(imageView, ItemButton.RECORD_LIKE, false);
        }
    }

    /**
     * 发生评价
     * @param v
     * @param isPraise
     */
    private void sendHabit(View v, String isPraise) {
        int index = (int) v.getTag(R.id.item_habit_index);
        String record=list.get(index).getRecord();
        if(isPraise.equals(record)){
            return;
        }
        int state = UserAction.GREAT.equals(isPraise) ? HABIT_GREAT : HABIT_BAD;
        mContext.dialogShow(R.string.evaluate);
        manager.sendHabit(mContext, followUser.getUid(), list.get(index).getAppointId(), isPraise, activityHandler, state);
    }
}