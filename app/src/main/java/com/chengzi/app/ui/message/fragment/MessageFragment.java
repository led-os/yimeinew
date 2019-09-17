package com.chengzi.app.ui.message.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentMessageBinding;
import com.chengzi.app.databinding.ItemMessageLayoutBinding;
import com.chengzi.app.event.OnNewMessageEvent;
import com.chengzi.app.ui.message.activity.AtMeActivity;
import com.chengzi.app.ui.message.activity.CommentAndThumbupActivity;
import com.chengzi.app.ui.message.activity.ConversationActivity;
import com.chengzi.app.ui.message.activity.FansActivity;
import com.chengzi.app.ui.message.activity.QuestionsAndAnswersActivity;
import com.chengzi.app.ui.message.activity.SystemMessageActivity;
import com.chengzi.app.ui.message.viewmodel.MessageViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息
 *
 * @ClassName:MessageFragment
 * @PackageName:com.yimei.app.ui.message.fragment
 * @Create On 2019/3/25 0025   16:09
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/25 0025 handongkeji All rights reserved.
 */
public class MessageFragment extends BaseFragment<FragmentMessageBinding, MessageViewModel> {

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        registerObservers(false);
        registerUserInfoObservable(false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        registerObservers(true);

        MessageAdapter adapter = new MessageAdapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.setNewData(generateData());

        observe();
        viewModel.getMessageUnreadCount();
        viewModel.getImMessageUnreadCount();

        registerUserInfoObservable(true);
    }

    private void registerUserInfoObservable(boolean register) {
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
    }

    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            List<RecentContact> list = viewModel.imUnreadMessageCountLive.getValue();
            if (list == null) {
                list = new ArrayList<>();
            }
            updateIMUnreadCount(list);
        }
    };

    private List<MessageBean> generateData() {
        List<MessageBean> list = new ArrayList<>();
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_xitongxiaoxi), getString(R.string.system_msg), SystemMessageActivity.class, viewModel.systemUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_wode), getString(R.string.at_me), AtMeActivity.class, viewModel.remindUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_zan), getString(R.string.comment_and_thumbup), CommentAndThumbupActivity.class, viewModel.likeAndCommentUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_fensi), getString(R.string.fans), FansActivity.class, viewModel.fansUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_wenda), getString(R.string.question_and_answer), QuestionsAndAnswersActivity.class, viewModel.questionUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_yonghuxiaoxi), "用户消息", ConversationActivity.class, viewModel.userUnreadCount));
//        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_jigouxiaoxi), "机构消息", ConversationActivity.class));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_yishengxiaoxi), "医生消息", ConversationActivity.class, viewModel.doctorUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_zixunshixiaoxi), "咨询师消息", ConversationActivity.class, viewModel.counselorUnreadCount));
        list.add(new MessageBean(covertResToDrawable(R.drawable.xiaoxi_kefu), "客服消息", ConversationActivity.class, viewModel.serviceUnreadCount));
        return list;
    }

    private void observe() {
        viewModel.unreadMessageCountLive.observe(this, bean -> {
            if (bean == null) {
                return;
            }
            viewModel.systemUnreadCount.set(bean.getSystem());
            viewModel.remindUnreadCount.set(bean.getFindpostremind());
            viewModel.likeAndCommentUnreadCount.set(bean.getLikescomment());
            viewModel.fansUnreadCount.set(bean.getFans());
            viewModel.questionUnreadCount.set(bean.getQuesitiononline());
        });

        viewModel.imUnreadMessageCountLive.observe(this, recentContacts -> {
            if (recentContacts == null) {
                recentContacts = new ArrayList<>();
            }
            updateIMUnreadCount(recentContacts);
        });
    }

    @Subscribe
    public void onNewMessageArrive(OnNewMessageEvent event) {
        viewModel.getMessageUnreadCount();
    }

    private Drawable covertResToDrawable(int res) {
        return getResources().getDrawable(res);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            viewModel.getImMessageUnreadCount();
            viewModel.getMessageUnreadCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            viewModel.getImMessageUnreadCount();
            viewModel.getMessageUnreadCount();
        }
    }

    private void updateIMUnreadCount(List<RecentContact> recentContacts) {
        int userUnreadCount = 0;
        int doctorUnreadCount = 0;
        int counselorUnreadCount = 0;
        int serviceUnreadCount = 0;
        for (RecentContact recentContact : recentContacts) {
            String contactId = recentContact.getContactId();
            NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(contactId);
            if (userInfo == null) {
                continue;
            }
            Map<String, Object> extensionMap = userInfo.getExtensionMap();
            if (extensionMap == null) {
                continue;
            }


            Object isWorker = extensionMap.get("isWorker");
            if (isWorker != null && Integer.valueOf(isWorker.toString()) == 1) {
                int unreadCount = recentContact.getUnreadCount();
                serviceUnreadCount += unreadCount;
            } else {
                Object t = extensionMap.get("t");
                if (t == null) {
                    continue;
                }
                int userType = Integer.valueOf(t.toString());
                if (userType == 1) {
                    int unreadCount = recentContact.getUnreadCount();
                    userUnreadCount += unreadCount;
                } else if (userType == 2) {
                    int unreadCount = recentContact.getUnreadCount();
                    doctorUnreadCount += unreadCount;
                } else if (userType == 3) {
                    int unreadCount = recentContact.getUnreadCount();
                    counselorUnreadCount += unreadCount;
                }
            }

        }
        viewModel.userUnreadCount.set(userUnreadCount);
        viewModel.doctorUnreadCount.set(doctorUnreadCount);
        viewModel.counselorUnreadCount.set(counselorUnreadCount);
        viewModel.serviceUnreadCount.set(serviceUnreadCount);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            viewModel.getImMessageUnreadCount();
        }
    };

    private class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> implements ClickEventHandler<MessageBean> {

        public MessageAdapter() {
            super(R.layout.item_message_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageBean item) {
            ItemMessageLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setListener(this);
            binding.setUnreadCount(item.observableInt);
            binding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, MessageBean bean) {

            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            if (AccountHelper.shouldLogin(view.getContext(),bean.clazz != SystemMessageActivity.class)) {
                return;
            }

            if (bean.clazz != null) {
                if (bean.clazz == ConversationActivity.class) {
                    int userType = 1;
                    if (TextUtils.equals(bean.text, "机构消息")) {
                        userType = 4;
                    } else if (TextUtils.equals(bean.text, "医生消息")) {
                        userType = 2;
                    } else if (TextUtils.equals(bean.text, "咨询师消息")) {
                        userType = 3;
                    } else if (TextUtils.equals(bean.text, "客服消息")) {
                        userType = 5;
                    } else {
                        userType = 1;
                    }
                    ConversationActivity.start(view.getContext(), userType);
                } else {
                    Context context = view.getContext();
                    context.startActivity(new Intent(context, bean.clazz));
                }
            }
        }
    }

    public static class MessageBean {
        private Drawable res;
        private String text;
        private Class clazz;
        private ObservableInt observableInt;

        public MessageBean(Drawable res, String text, Class clazz, ObservableInt observableInt) {
            this.res = res;
            this.text = text;
            this.clazz = clazz;
            this.observableInt = observableInt;
        }

        public Drawable getRes() {
            return res;
        }

        public void setRes(Drawable res) {
            this.res = res;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }
    }
}
