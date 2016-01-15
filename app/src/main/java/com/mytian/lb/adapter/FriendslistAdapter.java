package com.mytian.lb.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.manager.FollowManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendslistAdapter extends BaseSwipeAdapter {

    private LayoutInflater mInflater;

    private ArrayMap<String, FollowUser> list;

    private AbsActivity mContext;

    private FollowManager manager = new FollowManager();

    private int pinkColor;
    private int textMatchColor;

    private int deleteIndex;

    public FriendslistAdapter(AbsActivity context, ArrayMap<String, FollowUser> _list) {
        this.list = _list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        pinkColor = mContext.getResources().getColor(R.color.pink);
        textMatchColor = mContext.getResources().getColor(R.color.textcolor_match);
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

    public void refresh(ArrayMap<String, FollowUser> _list) {
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
        convertView = mInflater.inflate(R.layout.item_friends_list, viewGroup, false);
        final ViewHolder holder = new ViewHolder(convertView);
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAllItems();
                deleteIndex = (Integer) view.getTag();
                FollowUser user = list.valueAt(deleteIndex);
                mContext.dialogShow(R.string.cancel_ing);
                manager.followcancel(mContext, user.getUid(), handler, CANCEL);
            }
        });
        holder.deleteLayout.setClickable(false);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
                holder.deleteLayout.setClickable(true);
            }

            @Override
            public void onClose(SwipeLayout layout) {
                super.onClose(layout);
                holder.deleteLayout.setClickable(false);
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        FollowUser bean = list.valueAt(position);
        Glide.with(mContext).load(bean.getHead_thumb()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_head)
                .centerCrop()
                .into(viewHolder.head);
        viewHolder.name.setText(bean.getAlias());
        boolean isOnline = FollowUser.ONLINE.equals(bean.getIs_online());
        setState(viewHolder.stateTv, viewHolder.stateIv, bean.getAppointing(), isOnline);
        viewHolder.deleteLayout.setTag(position);
    }

    /**
     * 用户状态显示设置
     *
     * @param textView
     * @param imageView
     * @param ag_state
     * @param isOnline
     */
    private void setState(TextView textView, ImageView imageView, String ag_state, boolean isOnline) {
        if (isOnline) {
            if (AgreementStateEventType.AGREEMENT_ING.equals(ag_state)) {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setText(R.string.agreementing);
                textView.setTextColor(pinkColor);
                return;
            } else {
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.offline);
        }
        textView.setTextColor(textMatchColor);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_friends_list.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.head)
        RoundedImageView head;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.state_tv)
        TextView stateTv;
        @Bind(R.id.state_iv)
        ImageView stateIv;
        @Bind(R.id.delete_layout)
        RelativeLayout deleteLayout;
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;

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
                    list.removeAt(deleteIndex);
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}