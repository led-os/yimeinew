package com.chengzi.app.im.session.action;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.chengzi.app.im.session.extension.OnlineCaseAttachment;
import com.chengzi.app.ui.message.viewmodel.DoctorSelectorViewModel;
import com.chengzi.app.ui.mine.activity.onlinecases.OnlineCasesActivity;

import java.util.Map;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class FileAction extends BaseAction {

    public FileAction() {
        super(R.drawable.message_plus_file_selector, R.string.input_panel_file);
    }

    /**
     * **********************文件************************
     */
    private void chooseFile() {
        OnlineCasesActivity.startForResult(getActivity(), makeRequestCode(RequestCode.GET_LOCAL_FILE));
//        FileBrowserActivity.startActivityForResult(getActivity(), makeRequestCode(RequestCode.GET_LOCAL_FILE));
    }

    @Override
    public void onClick() {
        chooseFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.GET_LOCAL_FILE) {
            if (data == null) {
                return;
            }
//            String path = data.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
//            String path = "";
//            File file = new File(path);
//            IMMessage message = MessageBuilder.createFileMessage(getAccount(), getSessionType(), file, file.getName());
//            sendMessage(message);

            String case_uid = data.getStringExtra("case_uid");
            String avatar = data.getStringExtra("avatar");
            String nickname = data.getStringExtra("nickname");

            DoctorSelectorViewModel viewModel = ViewModelProviders.of((FragmentActivity) getActivity())
                    .get(DoctorSelectorViewModel.class);
            viewModel.caseForwardLive.observe((LifecycleOwner) getActivity(), new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    viewModel.caseForwardLive.removeObserver(this);
                    OnlineCaseAttachment onlineCaseAttachment = new OnlineCaseAttachment();
                    onlineCaseAttachment.setCaseUserAvatar(avatar);
                    onlineCaseAttachment.setCaseUserId(case_uid);
                    onlineCaseAttachment.setCaseUserName(nickname);
                    IMMessage customMessage = MessageBuilder.createCustomMessage(getContainer().account, getContainer().sessionType, nickname + "的病例", onlineCaseAttachment);
                    sendMessage(customMessage);
                }
            });

            NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(getAccount());
            if (userInfo == null) {
                return;
            }
            Map<String, Object> extensionMap = userInfo.getExtensionMap();
            if (extensionMap == null) {
                return;
            }
            String userId = extensionMap.get("userId").toString();
            viewModel.setCaseUserId(case_uid);
            viewModel.caseForward(userId);

        }
    }
}
