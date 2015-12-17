package com.mytian.lb.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.adapter.DynamicAdapter;
import com.mytian.lb.bean.DynamicResult;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class DtnameicFragment extends AbsFragment {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    View llListEmpty;

    private ListView mActualListView;
    private DynamicAdapter mAdapter;

    private ArrayList<DynamicResult> arrayList;

    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                arrayList = null;
                getListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new DynamicAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);

    }

    private void getListData() {
        int startIndex = arrayList == null || arrayList.size() <= 0 ? 0 : arrayList.size();
        activityHandler.sendEmptyMessageDelayed(INIT_LIST, 1000);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_dtnameic;
    }

    @Override
    public void EInit() {
        initListView();
        getListData();
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj, what);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne, int what) {
        dialogDismiss();
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                DynamicResult result = new DynamicResult();
                result.setAlias("韩韩");
                result.setDate("五分钟前");
                result.setDesc("麦宝客户端");
                result.setContent("[英语]：家长您好！您的宝宝已经学习英语第一课fruits，通过视频讲解和游戏的方式学习了4个水果单词、1个基本句式以及2个对话句式。获得了6个麦币，非常棒！英语的学习需要通过多说多练来提高，希望您能多多鼓励孩子跟着我们一起大声念出来吧。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setType(DynamicResult.TYPE_SYS);
                result.setAlias("麦田官方");
                result.setDate("刚刚");
                result.setDesc("系统");
                result.setContent("幼儿时期是孩子学习语言的黄金期！孩子的语言模仿能力极强，这一时期孩子会更容易形成正确的英语表达方式。年纪增长过程中孩子学习的灵性与语言敏感性越来越弱！在英语学习时需要以兴趣入手，锻炼他们的探索能力与求知欲，而语言是一种长期积累的过程，切不可操之过急。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("五分钟前");
                result.setDesc("系统");
                result.setContent("正确的鼓励可以使孩子拥有爱心且充满自信，在鼓励方面家长要传达出对孩子能力的信任，这种被人信任的信息，对孩子是一种极大的上推力。使得孩子有了力量来克服他每时每刻都会遇到的困难。在鼓励的基础上如果加上身体的拥抱等亲密动作会让这个鼓励更多的拥有爱的感受，而拥有更多爱的孩子会表现的更加有自信与勇气，所以，不要吝啬你的夸赞，同时也要注意方法哟。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("小明");
                result.setDate("刚刚");
                result.setDesc("麦宝客户端");
                result.setContent("[数与运算]：家长您好！您的宝宝今天学习了数与运算第一课1-10的认识，测试获得60分，还需要继续努力呢。希望您能帮助您的宝宝共同完成课程学习，加油哦！");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("小红");
                result.setDate("刚刚");
                result.setDesc("麦宝客户端");
                result.setContent("[逻辑思维]：家长您好！您的宝宝已经学习了逻辑思维第一课观察找不同，该课程适合3-4岁幼儿，开发适龄儿童的认知以及思维能力，您的宝宝表现非常出色，可以进一步学习我们其他的课程，再接再厉哦！");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("五分钟前");
                result.setDesc("系统");
                result.setContent("打造一个爱“社交”的宝宝需要家长有意识的安排孩子的玩伴需求，在交往中要注意孩子的沟通方式与情绪从而加以正确的引导，父母的态度和行为对宝宝社交能力的培养也非常重要。在日常生活里，家长应该言传身教，潜移默化中，宝宝也可以学习一些待人接物、交流合作的交际技能。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("五分钟前");
                result.setDesc("系统");
                result.setContent("幼儿时期的亲子阅读陪伴不但可以培养孩子的早期阅读习惯，同时也是增进亲子感情与学习能力模仿的有效方法，注重幼儿阅读的家庭的孩子在今后的独立阅读与书写能力方面将有大的提升，同时在选择早期书籍时也要参考孩子的兴趣点，以达到事半功倍的效果。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("小红");
                result.setDate("刚刚");
                result.setDesc("麦宝客户端");
                result.setContent("[国学礼仪]：家长您好！您的宝宝今天学习了国学礼仪第一课孔融让梨，教育小朋友凡事应该懂得谦让的礼仪，希望我们能陪伴您的宝宝健康快乐成长。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("小明");
                result.setDate("刚刚");
                result.setDesc("麦宝客户端");
                result.setContent("[科学探索]：家长您好！您的宝宝已经学习了科学探索第十课--蜡烛为什会熄灭，学习时间18分钟，爱探索的宝宝是个聪明的宝宝。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("五分钟前");
                result.setDesc("系统");
                result.setContent("孩子的艺术天分是熏陶出来的！在孩子小的时候多让他欣赏一些优美的画作可以奠定孩子的艺术品味。在孩子进行画画“创作”时家长要注意引导孩子对自己的画作进行描述，这样不但可以增强孩子的信心，同时也可以培养孩子想象力与沟通能力。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("五分钟前");
                result.setDesc("系统");
                result.setContent("惩罚是最无效的改变宝宝行为的方式，然而一些比较温和的处理方式往往更有成效。在与宝宝相处时家庭的规则首先要明确，其实要提前告知宝宝规则如果遭到违犯的话将遭到的“惩罚”或后果是怎样的，这样在宝宝每次违犯的时候家长只需实施与宝宝之前约定的后果即可。这样不但使孩子心灵得到保护，最主要的是使得孩子拥有承担责任的能力。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("韩梅梅");
                result.setDate("刚刚");
                result.setDesc("麦宝客户端");
                result.setContent("[认字]：家长您好！您的宝宝已经学习了认字的第一课，从一到十的汉字认识以及笔画笔顺的书写。测试获得80分，还不错哦。汉字的学习需要循序渐进，幼儿识字从认识图片开始，通过图片来认识汉字，希望您能对宝宝的努力给予肯定和鼓励。");
                arrayList.add(result);
                result = new DynamicResult();
                result.setAlias("麦田官方");
                result.setType(DynamicResult.TYPE_SYS);
                result.setDate("刚刚");
                result.setDesc("系统");
                result.setContent("当你和孩子共处一室却没有互动与交流时这顶多算是“看护”，而真正陪伴孩子的过程是积极的沟通与协作，与孩子相处时更要注重的是“质”。研究表明亲子互动积极家庭的孩子，更容易拥有同理心，自控力也更强。");
                arrayList.add(result);
            }
            mAdapter.refresh(arrayList);
        listview.onRefreshComplete();
    }

}
