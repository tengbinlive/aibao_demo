package com.mytian.lb.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.manager.FollowManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAdapter extends BaseSwipeAdapter {

    private LayoutInflater mInflater;

    private ArrayMap<String,FollowUser> list;

    private AbsActivity mContext;

    private FollowManager manager = new FollowManager();

    public UserAdapter(AbsActivity context, ArrayMap<String,FollowUser> _list) {
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

    public void refresh(ArrayMap<String,FollowUser> _list) {
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
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        View convertView;
        convertView = mInflater.inflate(R.layout.item_follow, viewGroup, false);
        ViewHolder holder = new ViewHolder(convertView);
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAllItems();
                int index = (Integer) view.getTag();
                FollowUser user = list.valueAt(index);
                mContext.dialogShow(R.string.cancel_ing);
                manager.followcancel(mContext, user.getUid(), handler, CANCEL);
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        FollowUser bean = list.valueAt(position);
        Glide.with(mContext).load(bean.getHead_thumb()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_contact).into(holder.head);
        String is_online = FollowUser.ONLINE.equals(bean.getIs_online())?"   [在线]":"   [离线]";
        holder.name.setText(bean.getAlias() + is_online);
        holder.phone.setText(bean.getPhone());
        holder.deleteLayout.setTag(position);
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
        @Bind(R.id.phone)
        TextView phone;
        @Bind(R.id.delete_layout)
        RelativeLayout deleteLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private static final int CANCEL = 1;//取消关注
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case CANCEL:
                    mContext.dialogDismiss();
                    CommonResponse resposne = (CommonResponse) msg.obj;
                    CommonUtil.showToast(resposne.getMsg());
                    break;
                default:
                    break;
            }
        }
    };

}