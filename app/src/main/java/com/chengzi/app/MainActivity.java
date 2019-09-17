package com.chengzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMainBinding;
import com.chengzi.app.event.NormalCategoryEvent;
import com.chengzi.app.event.OnNewMessageEvent;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.event.VipEvent;
import com.chengzi.app.ui.discover.fragment.DiscoverFragment;
import com.chengzi.app.ui.home.fragment.HomeFragment;
import com.chengzi.app.ui.home.viewmodel.MainViewModel;
import com.chengzi.app.ui.message.fragment.MessageFragment;
import com.chengzi.app.ui.mine.fragment.MineCounselorFragment;
import com.chengzi.app.ui.mine.fragment.MineDoctorFragment;
import com.chengzi.app.ui.mine.fragment.MineFragment;
import com.chengzi.app.ui.mine.fragment.MineHospitalFragment;
import com.chengzi.app.ui.vip.fragment.VipFragment;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseActivity;
import com.handongkeji.autoupdata.CheckVersion;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    public static boolean isSetMine = false;
    public FragmentNavigator navigator;

    private int position = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        registerObservers(false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.bottomNavigation.setItemIconTintList(null);

        EventBus.getDefault().register(this);
        registerObservers(true);

        mBinding.setUnreadCount(mViewModel.unreadCount);
        mBinding.setImUnreadCount(mViewModel.imUnreadCount);

        navigator = new FragmentNavigator(getSupportFragmentManager(), new MainFragmentNavigatorAdapter(), R.id.container);
        navigator.onCreate(savedInstanceState);
        navigator.setDefaultPosition(0);
        navigator.showFragment(navigator.getCurrentPosition());
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.item_home:
                    position = 0;
                    navigator.showFragment(0);
                    break;
                case R.id.item_message:
                    position = 1;
                    navigator.showFragment(1);
                    break;
                case R.id.item_vip:
                    position = 2;
                    navigator.showFragment(2);
                    break;
                case R.id.item_find:
                    position = 3;
                    navigator.showFragment(3);
                    break;
                case R.id.item_wode:
                    position = 4;
                    navigator.showFragment(4);
                    break;
            }
            return true;
        });

        observe();

        CheckVersion.update(this, Api.getBaseUrl() + "android-version");
    }

    private void observe() {
        mViewModel.unreadMessageCountLive.observe(this, bean -> {
            if (bean == null) {
                return;
            }
            int unreadCount = 0;
            unreadCount += bean.getSystem();
            unreadCount += bean.getFindpostremind();
            unreadCount += bean.getLikescomment();
            unreadCount += bean.getFans();
            unreadCount += bean.getQuesitiononline();

            mViewModel.unreadCount.set(unreadCount);
        });

        mViewModel.userInfoLiveData.observe(this, userInfoBean -> {
            if (userInfoBean == null) {
                return;
            }
            String auth_status = userInfoBean.getAuth_status();
            AccountHelper.setAuthStatus(auth_status);
        });

        mViewModel.switchTabLive.observe(this, position -> {
            switch (position) {
                case 0:
                    mBinding.bottomNavigation.setSelectedItemId(R.id.item_home);
                    break;
                case 1:
                    mBinding.bottomNavigation.setSelectedItemId(R.id.item_message);
                    break;
                case 2:
                    mBinding.bottomNavigation.setSelectedItemId(R.id.item_vip);
                    break;
                case 3:
                    mBinding.bottomNavigation.setSelectedItemId(R.id.item_find);
                    break;
                case 4:
                    mBinding.bottomNavigation.setSelectedItemId(R.id.item_wode);
                    break;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigator.onSaveInstanceState(outState);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            mViewModel.imUnreadCount.set(NIMClient.getService(MsgService.class).getTotalUnreadCount());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getMessageUnreadCount();
        if (AccountHelper.isLogin()) {
            mViewModel.imUnreadCount.set(NIMClient.getService(MsgService.class).getTotalUnreadCount());
        } else {
            mViewModel.imUnreadCount.set(0);
        }
        if (position == 4 && isSetMine) {
            isSetMine = false;
            //设置item的选中状态
            mBinding.bottomNavigation.getMenu().getItem(position).setChecked(true);
            navigator.showFragment(position, true);
        }
        mViewModel.userInfo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int position = intent.getIntExtra(Sys.EXTRA, -1);

        if (position >= 0) {
            String categoryId = intent.getStringExtra("categoryId");
            if (position == 2) {
                EventBus.getDefault().postSticky(new VipEvent(categoryId));
                switchMainTab(new SwitchMainTabEvent(position));
            } else if (position == 0) {
                EventBus.getDefault().post(new NormalCategoryEvent(categoryId));
            }
        }
    }

    //  切换到发现页面
    @org.greenrobot.eventbus.Subscribe
    public void switchMainTab(SwitchMainTabEvent event) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        mViewModel.switchTabLive.postValue(event.getPosition());
    }

    @org.greenrobot.eventbus.Subscribe
    public void onNewMessageArrive(OnNewMessageEvent event) {
        mViewModel.getMessageUnreadCount();
    }

    class MainFragmentNavigatorAdapter implements FragmentNavigatorAdapter {

        @Override
        public Fragment onCreateFragment(int i) {
            switch (i) {
                case 0:
                    position = 0;
                    return HomeFragment.newInstance();
                case 1:
                    position = 1;
                    return MessageFragment.newInstance();
                case 2:
                    position = 2;
                    return VipFragment.newInstance();
                case 3:
                    position = 3;
                    return DiscoverFragment.newInstance();
                case 4:
                    position = 4;
                    if (AccountHelper.getIdentity() == 2)
                        return MineDoctorFragment.newInstance();
                    else if (AccountHelper.getIdentity() == 3)
                        return MineCounselorFragment.newInstance();
                    else if (AccountHelper.getIdentity() == 4)
                        return MineHospitalFragment.newInstance();
                    else
                        return MineFragment.newInstance();
            }
            return null;
        }

        @Override
        public String getTag(int i) {
            switch (i) {
                case 0:
                    return HomeFragment.class.getSimpleName();
                case 1:
                    return MessageFragment.class.getSimpleName();
                case 2:
                    return VipFragment.class.getSimpleName();
                case 3:
                    return DiscoverFragment.class.getSimpleName();
                case 4:
                    return MineFragment.class.getSimpleName();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}